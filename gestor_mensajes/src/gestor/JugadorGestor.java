package gestor;

import javax.websocket.RemoteEndpoint;
import javax.websocket.Session;

/**
 * Autor: Javier
 * Fecha: 01/04/2018
 */
public class JugadorGestor {
    private int id;
    private String nombre;
    private boolean conectado;
    // TODO: Mas datos privados del jugador
    private Session sesion;

    public JugadorGestor(int id, String nombre, Session sesion) {
        this.id = id;
        this.nombre = nombre;
        this.sesion = sesion;
        this.conectado = false;
    }

    public RemoteEndpoint.Basic getRemoto() {
        return this.sesion.getBasicRemote();
    }

    public int getId() {
        return this.id;
    }

    public String getNombre() {
        return this.nombre;
    }

    public boolean getConectado() { return this.conectado; }

    public void setConectado(boolean estado) { this.conectado = estado; }
}
