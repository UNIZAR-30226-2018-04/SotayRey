import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint("/endpoint")
public class GestorMensajes {
    @OnOpen
    public void onOpen(Session session) {

    }

    @OnMessage
    public void onMessage(Session session, String message) {

    }

    @OnClose
    public void onClose(Session session, CloseReason closeReason) {

    }

    @OnError
    public void onError(Session session, Throwable ex) {

    }

    private void messageHandler(String message) {
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
}
