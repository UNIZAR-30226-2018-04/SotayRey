package gestorMensajes;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.HashMap;

@ServerEndpoint("/endpoint")
public class GestorMensajes {
    private JSONParser parser = new JSONParser();

    @OnOpen
    public void onOpen(Session session) {

    }

    @OnMessage
    public void onMessage(Session session, String msg) {
        try {
            Object obj = parser.parse(msg);
        } catch (ParseException ex) {
            ex.printStackTrace();
        }
        System.out.println("Hola");
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

    private void recibirReady(Session session, String msg) {

    }
}
