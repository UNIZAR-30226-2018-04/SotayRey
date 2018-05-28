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
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import basedatos.InterfazDatos;
import basedatos.exceptions.ExceptionCampoInexistente;
import basedatos.exceptions.ExceptionCampoInvalido;
import basedatos.modelo.*;
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
    private static HashMap<BigInteger, TorneoMatch> torneos = new HashMap<>();
    private static InterfazDatos bd = null;
    private int limiteSigos = 3;
    private int delayTorneos = 120;  // Segundos tras los cuales se añaden las IAs

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
        System.out.println(tipo + " recibido");
        switch(tipo) {
            case "busco_partida":
                recibirBusco(session, msg);
                break;
            case "sigo_buscando":
                recibirSigo(msg);
                break;
            case "busco_torneo":
                recibirBuscoTorneo(session, msg);
                break;
            case "empezar_torneo":
                recibirEmpiezaTorneo(msg);
                break;
            default:
                System.out.println("Tipo de mensaje no reconocido");
        }
    }

    private void recibirEmpiezaTorneo(JSONObject msg) {
        BigInteger id = BigInteger.valueOf((long) msg.get("id_torneo"));
        empezarTorneo(id);
    }

    private void empezarTorneo(BigInteger id) {
        TorneoMatch torneoMatch = torneos.get(id);
        boolean lleno = anyadirJugadorTorneo(torneoMatch, "SophIA", null);
        while (!lleno) {
            lleno = anyadirJugadorTorneo(torneoMatch, "SophIA", null);
        }
        TorneoVO torneoVO = torneoMatch.getVO();
        // Obtener máxima fase
        torneoMatch.setFase(torneoVO.getNumFases());
        // Emparejar
        FaseVO fase = new FaseVO(id, torneoMatch.getFase());
        try {
            // Se llena el objeto fase
            bd.obtenerPartidasFaseTorneo(fase);
            // Notificar a los jugadores de la partida
            for (PartidaVO p : fase.getParejas()) {
                iniciarPartidaTorneo(torneoMatch, p);
            }
            if (torneoMatch.getFase() > 0) {
                torneoMatch.decFase();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void recibirBuscoTorneo(Session sesion, JSONObject msg) {
        // Obtener info del mensaje
        String nombre = (String) msg.get("nombre_participante");
        BigInteger id = BigInteger.valueOf((long) msg.get("id_torneo"));
        // Obtener datos del torneo
        TorneoVO t = null;
        try {
            t = bd.obtenerDatosTorneo(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (t != null) {
            // Si no existe en la lista lo añade
            if (!torneos.containsKey(id)) {
                // Crear nuevo torneo
                torneos.put(id, new TorneoMatch(t));
            }
            TorneoMatch torneo = torneos.get(id);
            if (torneo.getMaxFase()) {
                // Está preparando la primera fase
                if (anyadirJugadorTorneo(torneo, nombre, sesion)) {
                    // Ya están todos los jugadores
                    empezarTorneo(id);
                } else {
                    // Informa del tiempo hasta la hora de inicio
                    enviarRestante(t, sesion);
                }
            } else if (torneo.getAcabado()) {
                // Ya ha acabado la fase final
                // Notifica al ganador
                enviarGanador(sesion);
                // Eliminar el torneo de memoria
                torneos.remove(id);
            } else {
                // Está preparando una fase intermedia
                anyadirJugadorFase(torneo, nombre, sesion); // Añade al jugador a la lista de esa fase
                if (torneo.todosConectados()) {
                    // Si ya están conectados todos los de esa ronda, comienza
                    continuarTorneo(id);
                }
            }
        }
    }

    private void anyadirJugadorFase(TorneoMatch torneo, String nombre, Session sesion) {
        torneo.anyadirJugador(nombre, sesion);
    }

    private void continuarTorneo(BigInteger id) {
        TorneoMatch torneoMatch = torneos.get(id);
        TorneoVO torneoVO = torneoMatch.getVO();
        // Emparejar
        FaseVO fase = new FaseVO(id, torneoMatch.getFase());
        try {
            // Se llena el objeto fase
            bd.obtenerPartidasFaseTorneo(fase);
            // Notificar a los jugadores de la partida
            for (PartidaVO p : fase.getParejas()) {
                iniciarPartidaTorneo(torneoMatch, p);
            }
            if (torneoMatch.getFase() > 1) {
                torneoMatch.decFase();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void enviarGanador(Session sesion) {
        JSONObject obj = new JSONObject();
        obj.put("tipo_mensaje", "ganador_torneo");
        if (sesion != null) {
            try {
                sesion.getBasicRemote().sendText(obj.toJSONString());
            } catch (IOException e) {
                System.out.println("No se pudo enviar el mensaje de ganador de torneo");
            }
        }
    }

    private void iniciarPartidaTorneo(TorneoMatch t, PartidaVO p) {
        // Detectar si juega contra la IA
        boolean conIA = false;
        for (UsuarioVO u : p.getUsuarios()) {
            if (u.getUsername().equals("SophIA")) {
                conIA = true;
            }
        }
        // Obtener info de la partida
        BigInteger id = p.getId();
        int numJugadores = p.getUsuarios().size();
        // Convertir lista de UsuariosVO en JugadoresMatch
        ArrayList<JugadorMatch> jugadoresMatch = new ArrayList<>();
        for (UsuarioVO u : p.getUsuarios()) {
            String nombre = u.getUsername();
            if (!nombre.equals("SophIA")) {
                jugadoresMatch.add(new JugadorMatch(nombre, t.getSesion(nombre)));
            }
        }
        // Enviar mensajes de listo para cada jugador
        broadcastListo(jugadoresMatch, id, conIA, true);
    }

    private boolean anyadirJugadorTorneo(TorneoMatch t, String nombre, Session sesion) {
        boolean emparejamiento = false;
        TorneoVO torneoVO = t.getVO();
        try {
            // Apunta al usuario en la BD
            UsuarioVO jugador = bd.obtenerDatosUsuario(nombre);
            emparejamiento = bd.apuntarTorneo(jugador, torneoVO);
            // Almacena la sesión del usuario para notificarlo
            if (!nombre.equals("SophIA")) {
                t.anyadirJugador(nombre, sesion);
            }
        } catch (Exception e) {
            System.out.println("No se pudo apuntar al jugador al torneo");
            return true;
        }
        return emparejamiento;
    }

    private void recibirBusco(Session sesion, JSONObject msg) {
        boolean conIA = (Boolean) msg.get("con_ia");
        if (conIA) {
            ArrayList<JugadorMatch> jugsMatch = new ArrayList<>();
            // Obtener datos del jugador
            String nombre = (String) msg.get("nombre_participante");
            StatsUsuarioVO stats = null;
            try {
                stats = bd.obtenerTodasStatsUsuario(nombre);
            } catch (Exception e) {
                e.printStackTrace();
            }
            String liga = stats.getLigaActual();
            int totalJugs = 2;
            String tipo = (String) msg.get("tipo_partida");
            JugadorMatch jugador = new JugadorMatch(nombre, tipo, totalJugs, liga, sesion);
            jugsMatch.add(jugador);

            // Se convierten los jugadores a usuarios
            ArrayList<UsuarioVO> usuarios = convAUsuarios(jugsMatch);
            PartidaVO nuevaPartida = null;
            try {
                // Se añade la IA a la partida
                usuarios.add(0, bd.obtenerDatosUsuario("SophIA"));
                // Se intenta crear la nueva partida
                nuevaPartida = new PartidaVO(tipo.equals("publica"), usuarios);
                bd.crearNuevaPartida(nuevaPartida);
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (ExceptionCampoInexistente exceptionCampoInexistente) {
                exceptionCampoInexistente.printStackTrace();
            }
            // Se obtiene el id de la nueva partida
            BigInteger idPartida = nuevaPartida.getId();
            System.out.println("Nueva partida creada con id " + idPartida.toString());
            broadcastListo(jugsMatch, idPartida, true, false); // Notificar al jugador en espera
        } else {
            // Obtener datos del jugador
            String nombre = (String) msg.get("nombre_participante");
            StatsUsuarioVO stats = null;
            try {
                stats = bd.obtenerTodasStatsUsuario(nombre);
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
        if (jugador != null) {
            jugador.incSigos();
        }
        // Revisar condiciones
        emparejar(lista);
    }

    private void emparejar(HashMap<String, JugadorMatch> lista) {
        // Obtener info del tipo de lista
        JugadorMatch aux = null;
        if (lista.size() >= 1) {
            JugadorMatch[] auxList = (JugadorMatch[]) lista.values().toArray();
            if (auxList.length >= 1) {
                aux = auxList[0];
            }
            if (aux != null) {
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
                        System.out.println("Nueva partida creada con id " + idPartida.toString());
                        broadcastListo(liga, idPartida, false, false); // Las partidas con IA se gestionan en otra parte
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
                        System.out.println("Nueva partida creada con id " + idPartida.toString());
                        broadcastListo(lobby, idPartida, false, false);    // Las partidas con IA se gestionan en otra parte
                    }
                }
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

    private void broadcastListo(ArrayList<JugadorMatch> lobby, BigInteger idPartida, boolean conIA, boolean torneo) {
        int i = 0;
        if (conIA) {
            i++;
        }
        for (JugadorMatch jug : lobby) {
            if (jug.getSesion() != null) {
                enviarListo(jug, conIA, torneo, idPartida, i);
            }
            i++;
        }
    }

    private void enviarListo(JugadorMatch jug, boolean conIA, boolean torneo, BigInteger idPartida, int idJugador) {
        JSONObject obj = new JSONObject();
        obj.put("tipo_mensaje", "partida_lista");
        if (torneo) {
            obj.put("total_jugadores", 2);
        } else {
            obj.put("total_jugadores", jug.getJugadores());
        }
        obj.put("id_partida", idPartida);
        obj.put("nombre_jugador", jug.getNombre());
        obj.put("con_ia", conIA);   // Las partidas con IA se gestionan en otra parte
        obj.put("torneo", torneo);
        obj.put("id_jugador", idJugador);
        try {
            if (jug.getRemoto() != null) {
                jug.getRemoto().sendText(obj.toJSONString());
            }
        } catch (IOException e) {
            System.out.println("No se pudo enviar partida_lista al jugador " + jug.getNombre());
        }
    }

    private void enviarRestante(TorneoVO t, Session sesion) {
        if (t != null) {
            long restante = System.currentTimeMillis() - (t.getTimeInicio().getTime() + delayTorneos); // TODO: Definir bien esta resta
            if (restante < 0) {
                restante = 1;
            }
            JSONObject obj = new JSONObject();
            obj.put("tipo_mensaje", "restante_torneo");
            obj.put("tiempo", restante);
            try {
                sesion.getBasicRemote().sendText(obj.toJSONString());
            } catch (IOException e) {
                System.out.println("No se pudo enviar restante_torneo");
            }
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
        for (BigInteger idTorneo : torneos.keySet()) {
            TorneoMatch torneo = torneos.get(idTorneo);
            String encontrado = torneo.eliminarJugador(sesion);
            if (encontrado != null) {
                try {
                    bd.abandonarTorneo(bd.obtenerDatosUsuario(encontrado), bd.obtenerDatosTorneo(idTorneo));
                } catch (Exception e) {
                    System.out.println("No se pudo eliminar al jugador " + encontrado);
                }
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
