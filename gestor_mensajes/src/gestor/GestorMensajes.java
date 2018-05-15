package gestor;

import basedatos.InterfazDatos;
import basedatos.exceptions.ExceptionCampoInexistente;
import basedatos.exceptions.ExceptionCampoInvalido;
import basedatos.modelo.ArticuloUsuarioVO;
import basedatos.modelo.PartidaVO;
import basedatos.modelo.UsuarioVO;
import main.java.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;

import java.beans.PropertyVetoException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

@ServerEndpoint("/endpoint")
public class GestorMensajes {
    private static HashMap<Integer, LogicaPartida> listaPartidas = new HashMap<>();
    private static ArrayList<Integer> partidasPausadas = new ArrayList<Integer>();
    private static HashMap<Integer, Lobby> lobbies = new HashMap<>(); // TODO: En 4 jugadores, orden (eq1, eq2, eq1, eq2)
    private static InterfazDatos bd = null;
    private static int timeoutDesconectado = 30000;

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
        desconectar(session);
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
                intentarReconectar(session, msg);
                recibirListo(session, msg);
                break;
            case "accion":
                System.out.println(tipo + " recibida");
                recibirAccion(msg);
                break;
            case "timeout":
                System.out.println(tipo + " recibido");
                recibirTimeout(msg);
                break;
            default:
                System.out.println("Tipo de mensaje no reconocido");
        }
    }

    private void intentarReconectar(Session sesion, JSONObject msg) {
        // Obtener datos del remitente
        int idPartida = getIdPartidaMsg(msg);
        int idJugador = getIdJugadorMsg(msg);
        if (partidasPausadas.contains(idPartida)) {
            // La partida estaba pausada
            Lobby lobby = lobbies.get(idPartida);
            JugadorGestor jug = lobby.buscarId(idJugador);
            if (jug.getDesconectado() != null) {
                jug.reconectar(sesion);
            }
            // Eliminar la partida de pausadas
            partidasPausadas.remove((Integer) idPartida);
        }
    }

    private void recibirTimeout(JSONObject msg) {
        // Obtener datos del remitente
        int idPartida = getIdPartidaMsg(msg);
        System.out.println("Partida con id " + idPartida + " finalizada por timeout");
        finalizarPartida(idPartida);
    }

    private void recibirAccion(JSONObject msg) {
        // Obtener datos del remitente
        int idPartida = getIdPartidaMsg(msg);
        int idJugador = getIdJugadorMsg(msg);
        String tipoAccion = getTipoAccionMsg(msg);
        Lobby lobby = lobbies.get(idPartida);
        LogicaPartida partida = listaPartidas.get(idPartida);
        EstadoPartida estado = partida.getEstado();

        if (idJugador == estado.getTurno()) {
            // El jugador tiene turno, se elige qué acción se realiza
            switch (tipoAccion) {
                case "lanzar_carta":
                    // Lectura del mensaje
                    JSONObject objCarta = (JSONObject) msg.get("carta");
                    int numero = (int) (long) objCarta.get("numero");
                    String palo = (String) objCarta.get("palo");
                    Carta carta = null;
                    try {
                        carta = new Carta(numero, palo);
                    } catch (ExceptionCartaIncorrecta exceptionCartaIncorrecta) {
                        exceptionCartaIncorrecta.printStackTrace();
                    }
                    // Ejecución de la acción
                    try {
                        partida.lanzarCarta(estado.getJugadoresId().get(idJugador), carta);
                        broadcastLanzarCarta(idPartida, idJugador, carta);
                        System.out.println("El jugador " + idJugador + " lanza la carta "
                                + carta.getValor() + carta.getPalo());
                        broadcastTurno(idPartida);
                    } catch (ExceptionJugadorIncorrecto exceptionJugadorIncorrecto) {
                        exceptionJugadorIncorrecto.printStackTrace();
                    } catch (ExceptionCartaIncorrecta exceptionCartaIncorrecta) {
                        System.out.println("Carta incorrecta");
                    } catch (ExceptionTurnoIncorrecto exceptionTurnoIncorrecto) {
                        exceptionTurnoIncorrecto.printStackTrace();
                    } catch (ExceptionJugadorSinCarta exceptionJugadorSinCarta) {
                        exceptionJugadorSinCarta.printStackTrace();
                    }
                    try {
                        // LÓGICA DE FINALIZACIÓN DE RONDA
                        partida.siguienteRonda();
                        broadcastGanaRonda(idPartida, false);
                        // Se intenta que todos los jugadores vuelvan a tener 6 cartas
                        broadcastRobarCarta(idPartida);
                        // Asigna el turno al jugador correspondiente
                        broadcastTurno(idPartida);
                    } catch (ExceptionRondaNoAcabada exceptionRondaNoAcabada) {
                        System.out.println("La ronda aún no ha acabado, ESTA EXCEPCION ES NORMAL, PUEDE SER IGNORADA");
                    } catch (ExceptionCartaYaExiste exceptionCartaYaExiste) {
                        exceptionCartaYaExiste.printStackTrace();
                    } catch (ExceptionNumeroMaximoCartas exceptionNumeroMaximoCartas) {
                        exceptionNumeroMaximoCartas.printStackTrace();
                    } catch (ExceptionMazoVacio exceptionMazoVacio) {
                        exceptionMazoVacio.printStackTrace();
                    } catch (ExceptionJugadorIncorrecto exceptionJugadorIncorrecto) {
                        exceptionJugadorIncorrecto.printStackTrace();
                    } catch (ExceptionPartidaFinalizada exceptionPartidaFinalizada) {
                        System.out.println("Partida finalizada: " + idPartida);
                        broadcastGanaRonda(idPartida, false);
                        // Elimina la partida de la lista de partidas activas
                        finalizarPartida(idPartida);
                    } catch (ExceptionDeVueltas exceptionDeVueltas) {
                        System.out.println("De vueltas: " + idPartida);
                        broadcastEstado(idPartida, true, -1);
                        broadcastGanaRonda(idPartida, false);
                        broadcastRobarCarta(idPartida);
                        // Manda el turno a todos los clientes
                        broadcastTurno(idPartida);
                    }
                    break;
                case "cantar":
                    try {
                        partida.cantar(partida.getEstado().getTurnoId());
                        broadcastCantar(idPartida);
                    } catch (ExceptionJugadorIncorrecto exceptionJugadorIncorrecto) {
                        exceptionJugadorIncorrecto.printStackTrace();
                    } catch (ExceptionRondaNoAcabada exceptionRondaNoAcabada) {
                        exceptionRondaNoAcabada.printStackTrace();
                    } catch (ExceptionNoPuedesCantar exceptionNoPuedesCantar) {
                        exceptionNoPuedesCantar.printStackTrace();
                    }
                    break;
                case "cambiar_triunfo":
                    // Lectura del mensaje
                    JSONObject objTriunfo = (JSONObject) msg.get("nuevo_triunfo");
                    int numeroTriunfo = (int) (long) objTriunfo.get("numero");
                    String paloTriunfo = (String) objTriunfo.get("palo");
                    Carta cartaTriunfo = null;
                    try {
                        cartaTriunfo = new Carta(numeroTriunfo, paloTriunfo);
                    } catch (ExceptionCartaIncorrecta exceptionCartaIncorrecta) {
                        exceptionCartaIncorrecta.printStackTrace();
                    }
                    // Ejecución de la acción
                    try {
                        partida.cambiarCartaPorTriunfo(partida.getEstado().getTurnoId(), cartaTriunfo);
                        // Si se ejecuta correctamente la acción
                        broadcastCambiarTriunfo(idPartida, cartaTriunfo);
                    } catch (ExceptionJugadorIncorrecto exceptionJugadorIncorrecto) {
                        exceptionJugadorIncorrecto.printStackTrace();
                    } catch (ExceptionJugadorSinCarta exceptionJugadorSinCarta) {
                        exceptionJugadorSinCarta.printStackTrace();
                    } catch (ExceptionCartaIncorrecta exceptionCartaIncorrecta) {
                        exceptionCartaIncorrecta.printStackTrace();
                    } catch (ExceptionCartaYaExiste exceptionCartaYaExiste) {
                        exceptionCartaYaExiste.printStackTrace();
                    } catch (ExceptionNumeroMaximoCartas exceptionNumeroMaximoCartas) {
                        exceptionNumeroMaximoCartas.printStackTrace();
                    } catch (ExceptionRondaNoAcabada exceptionRondaNoAcabada) {
                        exceptionRondaNoAcabada.printStackTrace();
                    } catch(NullPointerException ex) {
                        System.out.println("Carta no aportada");
                    }
                    break;
                default:
                    System.out.println("Accion no reconocida");
                    break;
            }
        } else {
            // El jugador ha enviado una accion sin recibir su turno
            System.out.println(idJugador + " quiere accion pero no tiene turno");
        }
    }

    private void broadcastCantar(int idPartida) {
        Lobby lobby = lobbies.get(idPartida);
        LogicaPartida partida = listaPartidas.get(idPartida);
        ArrayList<Cante> cantadas = partida.getRecienCantadas();

        for (Cante c : cantadas){
            JSONObject objC = new JSONObject();
            objC.put("tipo_mensaje", "broadcast_accion");
            objC.put("tipo_accion", "cantar");
            objC.put("id_jugador", partida.getEstado().getTurno());
            objC.put("palo", c.getPalo());
            switch(c.getTipo()) {
                case LAS20:
                    objC.put("cantidad", 20);
                    break;
                case LAS40:
                    objC.put("cantidad", 40);
                    break;
            }
            broadcastMensaje(lobby, objC);
        }
    }

    private void broadcastCambiarTriunfo(int idPartida, Carta nuevoTriunfo) {
        Lobby lobby = lobbies.get(idPartida);
        LogicaPartida partida = listaPartidas.get(idPartida);
        JSONObject objCT = new JSONObject();
        objCT.put("tipo_mensaje", "broadcast_accion");
        objCT.put("tipo_accion", "cambiar_triunfo");
        objCT.put("id_jugador", partida.getEstado().getTurno());
        JSONObject objNT = new JSONObject();
        objNT.put("numero", nuevoTriunfo.getValor());
        objNT.put("palo", nuevoTriunfo.getPalo());
        objCT.put("nuevo_triunfo", objNT);
        broadcastMensaje(lobby, objCT);
    }

    private void broadcastRobarCarta(int idPartida) {
        Lobby lobby = lobbies.get(idPartida);
        LogicaPartida partida = listaPartidas.get(idPartida);
        ArrayList<String> todosNombres = lobby.getTodosNombres();
        for (String jugador : todosNombres) {
            // Si consigue robar carta para ese jugador, hacer broadcast a todos y sólo a él de esa carta
            Carta cartaRobada = robarCarta(partida, jugador);
            if (cartaRobada == null) {
                continue;
            }
            JSONObject objRob = new JSONObject();
            objRob.put("tipo_mensaje", "broadcast_accion");
            objRob.put("tipo_accion", "robar_carta");
            objRob.put("id_jugador", partida.getEstado().getJugadoresId().indexOf(jugador));
            for (String nombreReceptor : todosNombres) {
                if (nombreReceptor.equals(jugador)) {
                    // Debe de recibir la carta robada
                    JSONObject objCarta = new JSONObject();
                    objCarta.put("numero", cartaRobada.getValor());
                    objCarta.put("palo", cartaRobada.getPalo());
                    objRob.put("carta", objCarta);
                    mandarMensaje(lobby.getRemotoJug(nombreReceptor), objRob);
                } else {
                    // NO debe de recibir la carta robada
                    if (objRob.containsKey("carta")) {
                        objRob.remove("carta");
                    }
                    mandarMensaje(lobby.getRemotoJug(nombreReceptor), objRob);
                }
            }
        }
    }

    private Carta robarCarta(LogicaPartida partida, String jugador) {
        Carta carta = null;
        try {
            ArrayList<Carta> estAntes = partida.getEstado().getCartasEnMano(jugador);
            partida.repartirCarta(jugador);
            // Busca cuál es la nueva carta
            ArrayList<Carta> estDespues = partida.getEstado().getCartasEnMano(jugador);
            for (Carta nueva : estDespues) {
                if (!estAntes.contains(nueva)) {
                    carta = nueva;
                    break;
                }
            }
        } catch (ExceptionJugadorIncorrecto exceptionJugadorIncorrecto) {
            exceptionJugadorIncorrecto.printStackTrace();
        } catch (ExceptionNumeroMaximoCartas exceptionNumeroMaximoCartas) {
            System.out.println("El jugador ya tiene todas las cartas");
        } catch (ExceptionMazoVacio exceptionMazoVacio) {
            System.out.println("El mazo esta vacio");
        } catch (ExceptionCartaYaExiste exceptionCartaYaExiste) {
            exceptionCartaYaExiste.printStackTrace();
        }
        return carta;
    }


    private void broadcastGanaRonda(int idPartida, boolean timeout) {
        Lobby lobby = lobbies.get(idPartida);
        LogicaPartida partida = listaPartidas.get(idPartida);
        // Incrementa la ronda
        lobby.incRonda();
        // Crea el mensaje de nueva ronda
        JSONObject objNR = new JSONObject();
        objNR.put("tipo_mensaje", "gana_ronda");
        objNR.put("nueva_ronda", lobby.getRonda());
        objNR.put("restantes_mazo", partida.getEstado().getMazo().size());
        objNR.put("id_jugador", partida.getEstado().getGanadorUltimaRonda());
        // Obtener puntuaciones de cada jugador
        JSONArray punts = new JSONArray();
        int i = 0;
        for (String nombre : lobby.getTodosNombres()) {
            JSONObject jug = new JSONObject();
            jug.put("id_jugador", i);
            if (!timeout) {
                try {
                    jug.put("puntuacion", partida.consultarPuntos(nombre));
                } catch (ExceptionJugadorIncorrecto exceptionJugadorIncorrecto) {
                    exceptionJugadorIncorrecto.printStackTrace();
                } catch (ExceptionRondaNoAcabada exceptionRondaNoAcabada) {
                    // Caso de que no haya acabado la ronda
                    if (lobby.buscarNombre(nombre).getDesconectado() != null) {
                        jug.put("puntuacion", 0);
                    } else {
                        jug.put("puntuacion", 101);
                    }
                }
            } else {
                if (lobby.buscarNombre(nombre).getDesconectado() != null) {
                    jug.put("puntuacion", 0);
                } else {
                    jug.put("puntuacion", 101);
                }
            }
            punts.add(jug);
            i++;
        }
        objNR.put("puntuaciones", punts);
        broadcastMensaje(lobby, objNR);
    }

    private void broadcastLanzarCarta(int idPartida, int idJugador, Carta carta) {
        Lobby lobby = lobbies.get(idPartida);
        LogicaPartida partida = listaPartidas.get(idPartida);
        EstadoPartida estado = partida.getEstado();
        // TIPO DE MENSAJE
        JSONObject objLC = new JSONObject();
        objLC.put("tipo_mensaje", "broadcast_accion");
        objLC.put("id_jugador", idJugador);
        objLC.put("tipo_accion", "lanzar_carta");
        JSONObject objCarta = new JSONObject();
        objCarta.put("numero", carta.getValor());
        objCarta.put("palo", carta.getPalo());
        objLC.put("carta", objCarta);
        broadcastMensaje(lobby, objLC);
    }

    private int getIdPartidaMsg(JSONObject msg) {
        return (int) (long) ( (JSONObject) msg.get("remitente") ).get("id_partida");
    }

    private int getIdJugadorMsg(JSONObject msg) {
        return (int) (long) ( (JSONObject) msg.get("remitente") ).get("id_jugador");
    }

    private String getTipoAccionMsg(JSONObject msg) {
        return (String) msg.get("tipo_accion");
    }

    private void recibirListo(Session session, JSONObject msg) {
        // Obtener datos del remitente
        int idPartida = getIdPartidaMsg(msg);
        int idJugador = getIdJugadorMsg(msg);
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
        /*
        System.out.println(lobby.tam());
        System.out.println(totalJugadores);
        System.out.println(listaPartidas.containsKey(idPartida));
        */
        if (lobby.tam() == totalJugadores && !listaPartidas.containsKey(idPartida)) {
            try {
                LogicaPartida partida = new LogicaPartida(lobby.getTodosNombres());
                partida.crearPartida();
                // TODO: Obtener partidaVO para luego poder eliminarla
                //lobby.setPartidaVO(nuevaPartida);
                listaPartidas.put(idPartida, partida);
                // Manda el estado a todos los clientes
                broadcastEstado(idPartida, false, -1); // TODO: Detectar cuando se vaya de vueltas
                // Incrementa el numero de ronda a 1
                lobby.incRonda();
                // Manda el turno a todos los clientes
                broadcastTurno(idPartida);
                System.out.println("Nueva partida añadida con identificador de lobby: " + idPartida);
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
            //} catch (SQLException e) {
            //    e.printStackTrace();
            }
        }
        else if(lobby.tam() > totalJugadores && listaPartidas.containsKey(idPartida)){
            broadcastEstado(idPartida, false, idJugador); // TODO: Detectar cuando se vaya de vueltas
            // Manda el turno a todos los clientes
            broadcastTurno(idPartida);
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

    private void mandarMensaje(RemoteEndpoint.Basic remoto, JSONObject obj) {
        if (remoto != null) {
            try {
                remoto.sendText(obj.toJSONString());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // Si id < 0, se envia a todos, sino al correspondiente
    // Si es mayor que el numero de jugadores, se toma como espectador
    private void broadcastEstado(int idPartida, boolean vueltas, int id) {
        EstadoPartida estado = listaPartidas.get(idPartida).getEstado();
        // Obtiene el lobby de la partida actual
        Lobby lobby = lobbies.get(idPartida);
        // Construye el estado en formato JSON
        JSONObject objEstado = new JSONObject();
        objEstado.put("tipo_mensaje", "estado_inicial");
        // Objeto "partida"
        JSONObject partida = new JSONObject();
        partida.put("ronda", 0);
        if (vueltas) {
            if (estado.getMazo().size() == 0) {
                partida.put("tipo_ronda", "arrastre_vueltas");
            } else {
                partida.put("tipo_ronda", "vueltas");
            }
        } else {
            if (estado.getMazo().size() == 0) {
                partida.put("tipo_ronda", "arrastre");
            } else {
                partida.put("tipo_ronda", "idas");
            }
        }
        partida.put("restantes_mazo", estado.getMazo().size());
        objEstado.put("partida", partida);
        // Array jugadores
        JSONArray jugadores = new JSONArray();
        ArrayList<Carta> cartasTapete = estado.getCartasEnTapete();
        ArrayList<String> no_espectadores = estado.getJugadoresId(); // Los que estan realmente jugando
        int i = 0;
        for (String nombre : lobby.getTodosNombres()) {
            if (no_espectadores.contains(nombre)){
                JugadorGestor jugGes = lobby.buscarNombre(nombre);
                UsuarioVO jugadorVO = null;
                try {
                    jugadorVO = bd.obtenerDatosUsuario(nombre);
                } catch (ExceptionCampoInexistente exceptionCampoInexistente) {
                    exceptionCampoInexistente.printStackTrace();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                JSONObject jugador = new JSONObject();
                jugador.put("id", jugGes.getId());
                jugador.put("nombre", jugadorVO.getUsername());
                // TODO: obtener avatar y gestionar usuario no existente.

                // Esta ha modificado Carlos
                ArticuloUsuarioVO dorso = null;
                ArticuloUsuarioVO avatar = null;
                try {
                    dorso = bd.obtenerDorsoFavorito(nombre);
                    avatar = bd.obtenerAvatarFavorito(nombre);
                } catch (ExceptionCampoInexistente exceptionCampoInexistente) {
                    exceptionCampoInexistente.printStackTrace();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                // TODO: Añadir personalización
                jugador.put("dorso" , dorso.getRutaImagen());
                jugador.put("avatar", avatar.getRutaImagen());



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
                if (cartasTapete.size() == 0 || i >= cartasTapete.size()) {
                    cartaMesa.put("numero", 0);
                    cartaMesa.put("palo", "X");
                } else if(i < cartasTapete.size()) {
                    Carta carta = cartasTapete.get(i);
                    cartaMesa.put("numero", carta.getValor());
                    cartaMesa.put("palo", carta.getPalo());
                }

                jugador.put("carta_mesa", cartaMesa);
                jugadores.add(jugador);
                i++;
            }
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
            if (no_espectadores.contains(nombre)){ // Es un jugador
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
            }
            try {
                if(id > 0 && lobby.buscarId(id).getNombre() == jug.getNombre()) {
                    jug.getRemoto().sendText(objEstado.toJSONString());
                }
                else if (id < 0){
                    jug.getRemoto().sendText(objEstado.toJSONString());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void broadcastDesconectado(int idPartida, int idJugador) {
        // Obtiene el estado de la partida
        Lobby lobby = lobbies.get(idPartida);
        JSONObject objDesc = new JSONObject();
        objDesc.put("tipo_mensaje", "broadcast_desconectado");
        objDesc.put("id_jugador", idJugador);
        objDesc.put("timeout", timeoutDesconectado);
        broadcastMensaje(lobby, objDesc);
    }

    private void finalizarPartida(int idPartida) {
        System.out.println("Partida finalizada: " + idPartida);
        broadcastGanaRonda(idPartida, true);
        partidasPausadas.remove((Integer) idPartida);
        // Elimina la partida de la lista de partidas activas
        listaPartidas.remove(idPartida);
        /*try {
            //bd.finalizarPartida(lobbies.get(idPartida).getPartidaVO()); // TODO: Obtener partida para poder eliminarla
        } catch (ExceptionCampoInvalido exceptionCampoInvalido) {
            exceptionCampoInvalido.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }*/
    }

    private void desconectar(Session sesion) {
        for (int id : lobbies.keySet()) {
            Lobby lobby = lobbies.get(id);
            JugadorGestor jugador = lobby.buscarSesion(sesion);
            if (jugador != null) {
                // Si se encuentra al jugador desconectado
                jugador.desconectar();  // Desconectar al jugador
                if (!lobby.algunConectado()) {
                    // TODO: Eliminar partida
                    finalizarPartida(id);
                }
                else {
                    // Pausar la partida
                    if (!partidasPausadas.contains(id)) {
                        partidasPausadas.add(id);   // Si la partida no estaba ya pausada, se marca como pausada
                    }
                    // Notificar al resto de jugadores
                    broadcastDesconectado(id, jugador.getId());
                    break;
                }
            }
        }
    }
}
