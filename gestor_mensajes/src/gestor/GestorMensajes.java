package gestor;


import main.java.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

@ServerEndpoint("/endpoint")
public class GestorMensajes {
    private static HashMap<Integer, LogicaPartida> listaPartidas = new HashMap<>();
    private static HashMap<Integer, Lobby> lobbies = new HashMap<>();

    @OnOpen
    public void onOpen(Session session) {
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
            case "listo_jugador":
                System.out.println(tipo + " recibido");
                recibirListo(session, msg);
                break;
            default:
                System.out.println("Mierda");
        }
        // TODO: Recibir ready
        // TODO: Mandar estado inicial
        // TODO: Broadcast de turno
        // TODO: Recibir tirar carta
        // TODO: Recibir cambio triunfo
        // TODO: Broadcast de jugador tira carta
        // TODO: Broadcast de nuevoTurno
        // TODO: Informar quien gana ronda
        // TODO: Informar tipo de ronda
    }

    private void recibirListo(Session session, JSONObject msg) {
        // Obtener datos del remitente
        int idPartida = (int) (long) ( (JSONObject) msg.get("remitente") ).get("id_partida");
        int idJugador = (int) (long) ( (JSONObject) msg.get("remitente") ).get("id_jugador");
        String nombre = (String) msg.get("nombre_participante");

        JugadorGestor jugador = new JugadorGestor(idJugador, nombre, session);

        int totalJugadores = (int) (long) msg.get("total_jugadores");
        Lobby lobby = new Lobby();
        if (!lobbies.containsKey(idPartida)) {
            // Si la partida aún no existe, la crea
            lobby.anyadir(jugador);
        } else {
            lobby = lobbies.get(idPartida);
            lobby.anyadir(jugador);
        }
        lobbies.put(idPartida, lobby);

        if (lobby.tam() == totalJugadores && !listaPartidas.containsKey(idPartida)) {
            try {
                LogicaPartida partida = new LogicaPartida(lobby.getTodosNombres());
                partida.crearPartida();
                listaPartidas.put(idPartida, partida);
                broadcastEstado(idPartida);
                lobby.incRonda();
                broadcastTurno(idPartida);
            } catch (ExceptionEquipoIncompleto exceptionEquipoIncompleto) {
                exceptionEquipoIncompleto.printStackTrace();
            } catch (ExceptionNumeroMaximoCartas exceptionNumeroMaximoCartas) {
                exceptionNumeroMaximoCartas.printStackTrace();
            } catch (ExceptionMazoVacio exceptionMazoVacio) {
                exceptionMazoVacio.printStackTrace();
            } catch (ExceptionJugadorIncorrecto exceptionJugadorIncorrecto) {
                exceptionJugadorIncorrecto.printStackTrace();
            } catch (ExceptionCartaYaExiste exceptionCartaYaExiste) {
                exceptionCartaYaExiste.printStackTrace();
            }
            System.out.println("Nueva partida añadida");
        }
    }

    private void broadcastTurno(int idPartida) {
        // Obtiene el estado de la partida
        LogicaPartida partida = listaPartidas.get(idPartida);
        EstadoPartida estado = partida.getEstado();
        Lobby lobby = lobbies.get(idPartida);
        JSONObject objTurno = new JSONObject();
        objTurno.put("tipo_mensaje", "broadcast_accion");
        objTurno.put("tipo_accion", "turno");
        objTurno.put("id_jugador", estado.getTurno());
        objTurno.put("ronda", lobby.getRonda());
        broadcastMensaje(lobby, objTurno);
    }

    private void broadcastMensaje(Lobby lobby, JSONObject obj) {
        for (RemoteEndpoint.Basic remoto : lobby.getTodosRemotos()) {
            try {
                remoto.sendText(obj.toJSONString());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void broadcastEstado(int idPartida) {
        EstadoPartida estado = listaPartidas.get(idPartida).getEstado();
        // Obtiene el lobby de la partida actual
        Lobby lobby = lobbies.get(idPartida);
        // Construye el estado en formato JSON
        JSONObject objEstado = new JSONObject();
        objEstado.put("tipo_mensaje", "estado_inicial");
        // Objeto "partida"
        JSONObject partida = new JSONObject();
        partida.put("ronda", 0);
        partida.put("tipo_ronda", "idas");
        partida.put("restantes_mazo", estado.getMazo().size());
        objEstado.put("partida", partida);
        // Array jugadores
        JSONArray jugadores = new JSONArray();
        ArrayList<Carta> cartasTapete = estado.getCartasEnTapete();
        int i = 0;
        for (String nombre : lobby.getTodosNombres()) {
            JugadorGestor jugGes = lobby.buscarNombre(nombre);
            JSONObject jugador = new JSONObject();
            jugador.put("id", jugGes.getId());
            jugador.put("nombre", nombre);
            jugador.put("avatar", "ruta_avatar");
            jugador.put("tipo", "jugador");
            try {
                jugador.put("puntos", estado.getPuntosJugador(nombre));
            } catch (ExceptionJugadorIncorrecto exceptionJugadorIncorrecto) {
                exceptionJugadorIncorrecto.printStackTrace();
            }
            try {
                jugador.put("num_cartas", estado.getCartasEnMano(nombre).size());
            } catch (ExceptionJugadorIncorrecto exceptionJugadorIncorrecto) {
                exceptionJugadorIncorrecto.printStackTrace();
            }
            // Carta mesa
            JSONObject cartaMesa = new JSONObject();
            if (cartasTapete.size() == 0) {
                cartaMesa.put("numero", 0);
                cartaMesa.put("palo", "X");
            } else {
                Carta carta = cartasTapete.get(i);
                cartaMesa.put("numero", carta.getValor());
                cartaMesa.put("palo", carta.getPalo());
            }

            jugador.put("carta_mesa", cartaMesa);
            jugadores.add(jugador);
            i++;
        }
        objEstado.put("jugadores", jugadores);
        // Objeto triunfo
        JSONObject triunfo = new JSONObject();
        Carta triunfoCarta = estado.getTriunfo();
        triunfo.put("numero", triunfoCarta.getValor());
        triunfo.put("palo", triunfoCarta.getPalo());
        objEstado.put("triunfo", triunfo);
        String stringEstado = objEstado.toJSONString();
        for (String nombre : lobby.getTodosNombres()) {
            JugadorGestor jug = lobby.buscarNombre(nombre);
            // Array mano personalizado para cada jugador
            JSONArray mano = new JSONArray();
            try {
                for (Carta carta : estado.getCartasEnMano(nombre)) {
                    JSONObject objCarta = new JSONObject();
                    objCarta.put("numero", carta.getValor());
                    objCarta.put("palo", carta.getPalo());
                    mano.add(objCarta);
                }
                JSONObject objEstadoPers = objEstado;
                objEstadoPers.put("mano", mano);
            } catch (ExceptionJugadorIncorrecto exceptionJugadorIncorrecto) {
                exceptionJugadorIncorrecto.printStackTrace();
            }
            try {
                jug.getRemoto().sendText(objEstado.toJSONString());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
