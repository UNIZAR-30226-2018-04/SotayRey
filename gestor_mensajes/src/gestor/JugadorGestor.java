package gestor;

import javax.websocket.RemoteEndpoint;
import javax.websocket.Session;
import java.util.Date;

/**
 * Autor: Javier
 * Fecha: 01/04/2018
 */
public class JugadorGestor {
    private int id;
    private String nombre;
    private Date desconectado;
    private Session sesion;

    public JugadorGestor(int id, String nombre, Session sesion) {
        this.id = id;
        this.nombre = nombre;
        this.sesion = sesion;
        this.desconectado = null;
    }

    public JugadorGestor(int id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public RemoteEndpoint.Basic getRemoto() {
        if (this.sesion != null) {
            return this.sesion.getBasicRemote();
        } else {
            return null;
        }
    }

    public int getId() {
        return this.id;
    }

    public String getNombre() {
        return this.nombre;
    }

    public Date getDesconectado() { return this.desconectado; }

    public void reconectar(Session nuevaSesion) { this.desconectado = null; this.sesion = nuevaSesion; }

    public void desconectar() { this.desconectado = new Date(); this.sesion = null; }

    public Session getSesion() { return this.sesion; }
}
