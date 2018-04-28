package matchmaker;

import javax.websocket.RemoteEndpoint;
import javax.websocket.Session;

/**
 * Autor: Javier
 * Fecha: 28/04/2018
 */
public class JugadorMatch {
    private String tipo;
    private String nombre;
    private int jugadores;
    private String liga;
    private int sigos;
    private Session sesion;

    public JugadorMatch(String nombre, String tipo, int jugadores, String liga, Session sesion) {
            this.nombre = nombre;
            this.tipo = tipo;
            this.jugadores = jugadores;
            this.liga = liga;
            this.sigos = 0;
            this.sesion = sesion;
    }

    public RemoteEndpoint.Basic getRemoto() {
            return this.sesion.getBasicRemote();
        }

    public Session getSesion() {
        return this.sesion;
    }

    public String getTipo() {
            return this.tipo;
        }

    public String getNombre() {
            return this.nombre;
        }

    public int getJugadores() {
        return jugadores;
    }

    public String getLiga() {
        return liga;
    }

    public int getSigos() {
        return sigos;
    }

    public void incSigos() {
        this.sigos++;
    }
}
