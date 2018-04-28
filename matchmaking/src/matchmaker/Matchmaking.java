package matchmaker;

/**
 * Autor: Javier
 * Fecha: 27/04/2018
 */

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;

import java.beans.PropertyVetoException;
import java.io.IOException;
import java.math.BigInteger;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import basedatos.InterfazDatos;
import basedatos.exceptions.ExceptionCampoInexistente;
import basedatos.exceptions.ExceptionCampoInvalido;
import basedatos.modelo.PartidaVO;
import basedatos.modelo.StatsUsuarioVO;
import basedatos.modelo.UsuarioVO;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

@ServerEndpoint("/matchmaking")
public class Matchmaking {
    private static HashMap<String, JugadorMatch> individual = new HashMap<>();
    private static HashMap<String, JugadorMatch> parejas = new HashMap<>();
    private static HashMap<String, JugadorMatch> privIndividual = new HashMap<>();
    private static HashMap<String, JugadorMatch> privParejas = new HashMap<>();
    private static InterfazDatos bd = null;
    private int limiteSigos = 3;

    @OnOpen
    public void onOpen(Session session) {
        if (bd == null) {
            try {
                bd = InterfazDatos.instancia();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        System.out.println("Conexion abierta");
    }

    @OnMessage
    public void onMessage(Session session, String msg) {
        System.out.println("Recibiendo mensaje...");
        JSONObject msgJSON = (JSONObject) JSONValue.parse(msg);
        messageHandler(session, msgJSON);
    }

    @OnClose
    public void onClose(Session session, CloseReason closeReason) {
        // TODO: Buscar al jugador que ha cerrado la sesion a traves de su sesion
        System.out.println("Conexion cerrada");
        eliminarJugador(session);
    }

    @OnError
    public void onError(Session session, Throwable ex) {
        System.out.println("Error");
        ex.printStackTrace();
    }

    private void messageHandler(Session session, JSONObject msg) {
        // Revisar el tipo del mensaje
        String tipo = (String) msg.get("tipo_mensaje");
        switch(tipo) {
            case "busco_partida":
                System.out.println(tipo + " recibido");
                recibirBusco(session, msg);
                break;
            case "sigo_buscando":
                System.out.println(tipo + " recibida");
                recibirSigo(msg);
                break;
            default:
                System.out.println("Tipo de mensaje no reconocido");
        }
    }

    private void recibirBusco(Session sesion, JSONObject msg) {
        boolean conIA = (Boolean) msg.get("con_ia");
        if (conIA) {
            // TODO: Iniciar partida con IA inmediatamente
        } else {
            // Obtener datos del jugador
            String nombre = (String) msg.get("nombre_participante");
            StatsUsuarioVO stats = null;
            try {
                stats = bd.obtenerStatsUsuario(nombre);
            } catch (Exception e) {
                e.printStackTrace();
            }
            String liga = stats.getLigaActual();
            int totalJugs = ((Long) msg.get("total_jugadores")).intValue();
            String tipo = (String) msg.get("tipo_partida");
            JugadorMatch jugador = new JugadorMatch(nombre, tipo, totalJugs, liga, sesion);
            HashMap<String, JugadorMatch> lista = null;
            if (totalJugs == 2) {
                if (tipo.equals("publica")) {
                    individual.put(nombre, jugador);
                    lista = individual;
                } else {
                    privIndividual.put(nombre, jugador);
                    lista = privIndividual;
                }
            } else if (totalJugs == 4) {
                if (tipo.equals("publica")) {
                    parejas.put(nombre, jugador);
                    lista = parejas;
                } else {
                    privParejas.put(nombre, jugador);
                    lista = privParejas;
                }
            }
            emparejar(lista);
        }
    }

    private void recibirSigo(JSONObject msg) {
        // Obtener datos del jugador
        String nombre = (String) msg.get("nombre_participante");
        int totalJugs = ((Long) msg.get("total_jugadores")).intValue();
        String tipo = (String) msg.get("tipo_partida");
        JugadorMatch jugador = null;
        HashMap<String, JugadorMatch> lista = null;
        if (totalJugs == 2) {
            if (tipo.equals("privada")) {
                jugador = privIndividual.get(nombre);
                lista = privIndividual;
            } else if (tipo.equals("publica")) {
                jugador = individual.get(nombre);
                lista = individual;
            }
        } else if (totalJugs == 4) {
            if (tipo.equals("privada")) {
                jugador = privParejas.get(nombre);
                lista = privParejas;
            } else if (tipo.equals("publica")) {
                jugador = parejas.get(nombre);
                lista = parejas;
            }
        }
        jugador.incSigos();
        // Revisar condiciones
        emparejar(lista);
    }

    private void emparejar(HashMap<String, JugadorMatch> lista) {
        // Obtener info del tipo de lista
        JugadorMatch aux = (JugadorMatch) lista.values().toArray()[0];
        String tipo = aux.getTipo();
        int jugsPartida = aux.getJugadores();

        // Revisar si hay suficientes jugadores para emparejar
        if (lista.size() >= jugsPartida) {
            // Revisar si hay suficientes jugadores de la misma liga
            ArrayList<ArrayList<JugadorMatch>> jugsMismaLiga = jugadoresMismaLiga(lista, jugsPartida);
            // Para cada liga que tiene minimo jugsPartida jugadores
            for (ArrayList<JugadorMatch> liga : jugsMismaLiga) {
                // Elimina a los jugadores de la lista de espera
                for (JugadorMatch jug : liga) {
                    lista.remove(jug.getNombre());
                }
                // Se convierten los jugadores a usuarios
                ArrayList<UsuarioVO> usuarios = convAUsuarios(liga);
                PartidaVO nuevaPartida = new PartidaVO(tipo.equals("publica"), usuarios);
                try {
                    bd.crearNuevaPartida(nuevaPartida);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                // Se obtiene el id de la nueva partida
                BigInteger idPartida = nuevaPartida.getId();
                broadcastListo(liga, idPartida, false); // Las partidas con IA se gestionan en otra parte
            }
            // Revisar si el número de sigos supera el límite (peor caso posible)
            if (getMaxSigos(lista) >= limiteSigos) {
                // Emparejar de la forma más sencilla posible
                ArrayList<JugadorMatch> lobby = quitarNJugadores(lista.values(), jugsPartida);
                ArrayList<UsuarioVO> usuarios = convAUsuarios(lobby);
                // Se crea la nueva partida con los jugadores convertidos a usuarios
                PartidaVO nuevaPartida = new PartidaVO(tipo.equals("publica"), usuarios);
                try {
                    bd.crearNuevaPartida(nuevaPartida);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                // Se obtiene el id de la nueva partida
                BigInteger idPartida = nuevaPartida.getId();
                broadcastListo(lobby, idPartida, false);    // Las partidas con IA se gestionan en otra parte
            }
        }
    }

    private int getMaxSigos(HashMap<String, JugadorMatch> lista) {
        int maxSigos = 0;
        for (JugadorMatch jug : lista.values()) {
            if (jug.getSigos() > maxSigos) {
                maxSigos = jug.getSigos();
            }
        }
        return maxSigos;
    }

    private void broadcastListo(ArrayList<JugadorMatch> lobby, BigInteger idPartida, boolean conIA) {
        int i = 0;
        for (JugadorMatch jug : lobby) {
            enviarListo(jug, conIA, idPartida, i);
            i++;
        }
    }

    private void enviarListo(JugadorMatch jug, boolean conIA, BigInteger idPartida, int idJugador) {
        JSONObject obj = new JSONObject();
        obj.put("tipo_mensaje", "partida_lista");
        obj.put("total_jugadores", jug.getJugadores());
        obj.put("id_partida", idPartida);
        obj.put("nombre_jugador", jug.getNombre());
        obj.put("con_ia", conIA);   // Las partidas con IA se gestionan en otra parte
        obj.put("id_jugador", idJugador);
        try {
            jug.getRemoto().sendText(obj.toJSONString());
        } catch (IOException e) {
            System.out.println("No se pudo enviar partida_lista al jugador " + jug.getNombre());
            e.printStackTrace();
        }
    }

    private ArrayList<ArrayList<JugadorMatch>> jugadoresMismaLiga(HashMap<String, JugadorMatch> lista, int objetivo) {
        HashMap<String, ArrayList<JugadorMatch>> jugsPorLiga = new HashMap<>();
        ArrayList<ArrayList<JugadorMatch>> resultado = new ArrayList<>();
        // Añadir a los usuarios a un diccionario mapeados por nombre de liga
        for (JugadorMatch jug : lista.values()) {
            String liga = jug.getLiga();
            if (!jugsPorLiga.containsKey(liga)) {
                jugsPorLiga.put(liga, new ArrayList<>());
            }
            ArrayList<JugadorMatch> listaLiga = jugsPorLiga.get(liga);
            // Añade jugadores hasta llegar al número objetivo
            if (listaLiga.size() < objetivo) {
                listaLiga.add(jug);
            }
        }
        // Elimina las ligas sin suficientes jugadores
        for (ArrayList<JugadorMatch> listaLiga : jugsPorLiga.values()) {
            if (listaLiga.size() == objetivo) {
                resultado.add(listaLiga);
            }
        }
        return resultado;
    }

    private ArrayList<JugadorMatch> quitarNJugadores(Collection<JugadorMatch> jugadores, int n) {
        ArrayList<JugadorMatch> lobby = new ArrayList<>();
        int i = 0;
        for (JugadorMatch jug : jugadores) {
            if (i >= n) {
                break;
            }
            lobby.add(jug);
            i++;
        }
        jugadores.removeAll(lobby);
        return lobby;
    }

    private ArrayList<UsuarioVO> convAUsuarios(ArrayList<JugadorMatch> lobby) {
        ArrayList<UsuarioVO> usuarios = new ArrayList<>();
        for (JugadorMatch jug : lobby) {
            try {
                usuarios.add(bd.obtenerDatosUsuario(jug.getNombre()));
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (ExceptionCampoInexistente exceptionCampoInexistente) {
                exceptionCampoInexistente.printStackTrace();
            }
        }
        return usuarios;
    }

    private void eliminarJugador(Session sesion) {
        ArrayList<HashMap<String, JugadorMatch>> listas = new ArrayList<>();
        listas.add(individual);
        listas.add(privIndividual);
        listas.add(parejas);
        listas.add(privParejas);
        for (HashMap<String, JugadorMatch> lista : listas ) {
            if (eliminarJugLista(sesion, lista)) {
                System.out.println("Jugador correctamente eliminado.");
                break;
            }
        }
    }

    private boolean eliminarJugLista(Session sesion, HashMap<String, JugadorMatch> lista) {
        for (String nombre : lista.keySet()) {
            JugadorMatch jugador = lista.get(nombre);
            if (jugador.getSesion().equals(sesion)) {
                lista.remove(nombre);
                return true;
            }
        }
        return false;
    }
}
