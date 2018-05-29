package gestor;

import basedatos.modelo.PartidaVO;
import main.java.EstadoPartida;
import main.java.Jugador;
import sophia.Sophia;

import javax.websocket.RemoteEndpoint;
import javax.websocket.Session;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * Autor: Javier
 * Fecha: 01/04/2018
 */
public class Lobby {
    private HashMap<String, JugadorGestor> jugadores = new HashMap<>();
    private int ronda = 0;
    private int maxID = 4; // Para asignarlo a los espectadores
    private int numJugadores = 0;
    private Sophia ia = null;
    private boolean contraIA = false;
    private boolean finalizada = false;

    public void setNumJugadores(int numJugadores) {
        this.numJugadores = numJugadores;
    }

    public int getNumJugadores(){
        return numJugadores;
    }

    public Lobby() {
        ronda = 0;
    }
    public Lobby(JugadorGestor jug) {
        ronda = 0;
        anyadir(jug);
        contraIA = false;
    }

    public void anyadir(JugadorGestor jug) {
        if (!jugadores.containsKey(jug.getNombre())) {
            jugadores.put(jug.getNombre(), jug);
        }
    }

    // Unicamente se elimina si era un espectador
    public void eliminar(JugadorGestor jug) {
        if(jugadores.containsKey(jug.getNombre())) {
            jugadores.remove(jug.getNombre());
        }
    }

    public int getMaxID(){
        return maxID;
    }

    public void incrementarMaxID(){
        maxID++;
    }

    public int getRonda() {
        return ronda;
    }

    public void incRonda() {
        ronda += 1;
    }

    public JugadorGestor buscarId(int id) {
        for (JugadorGestor jug : jugadores.values()) {
            if (jug.getId() == id) {
                return jug;
            }
        }
        return null;
    }

    public JugadorGestor buscarNombre(String nombre) {
        if (jugadores.containsKey(nombre)) {
            return jugadores.get(nombre);
        } else {
            return null;
        }
    }

    public boolean isConectadoJug(String nombre) {
        return jugadores.get(nombre).getDesconectado() == null;
    }

    public JugadorGestor buscarSesion(Session sesion) {
        for (JugadorGestor jug : jugadores.values()) {
            if (sesion == jug.getSesion()) {
                return jug;
            }
        }
        return null;
    }

    public RemoteEndpoint.Basic getRemotoJug(int id) {
        JugadorGestor jug = buscarId(id);
        if (jug != null) {
            return jug.getRemoto();
        }
        else {
            return null;
        }
    }

    public RemoteEndpoint.Basic getRemotoJug(String nombre) {
        JugadorGestor jug = buscarNombre(nombre);
        if (jug != null && jug.getDesconectado() == null) {
            return jug.getRemoto();
        }
        else {
            return null;
        }
    }

    public ArrayList<RemoteEndpoint.Basic> getTodosRemotos() {
        ArrayList<RemoteEndpoint.Basic> lista = new ArrayList<>();
        for (String nombre : jugadores.keySet()) {
            RemoteEndpoint.Basic remoto = this.getRemotoJug(nombre);
            if (remoto != null) {
                lista.add(remoto);
            }
        }
        return lista;
    }

    public ArrayList<String> getTodosNombres() {
        boolean conIA = false;
        ArrayList<String> lista = new ArrayList<String>();
        for (JugadorGestor jug : jugadores.values()) {
            if (jug.getNombre().equals("SophIA")) {
                lista.add(jug.getNombre());
                conIA = true;
                break;
            }
        }
        int i;
        if (conIA) {
            for (i=1; i<jugadores.size(); i++){
                lista.add(this.buscarId(i).getNombre());
            }
        } else {
            for (i=0; i<jugadores.size(); i++){
                lista.add(this.buscarId(i).getNombre());
            }
        }
        return lista;
    }

    public ArrayList<Integer> getTodosIds() {
        ArrayList<Integer> ids = new ArrayList<>();
        for (int i = 0; i<tam(); i++) {
            ids.add(i);
        }
        return ids;
    }

    public int tam() {
        return jugadores.size();
    }

    public boolean algunConectado() {
        for (JugadorGestor jug : jugadores.values()) {
            if (jug.getSesion() != null) {
                return true;
            }
        }
        return false;
    }

    public void inicializarIA(EstadoPartida ep) {
        this.ia = new Sophia(ep, false);
    }

    public void vueltasIA(EstadoPartida ep) {
        this.ia = new Sophia(ep, true);
    }

    public Sophia getIA() {
        return this.ia;
    }

    public boolean getContraIA() {
        return this.contraIA;
    }

    public void setContraIA(boolean valor) {
        this.contraIA = valor;
    }

    public int getPrimerAbandonador() {
        Date primer = new Date();
        int primerIdx = -1;
        int i = 0;
        for (JugadorGestor j : jugadores.values()) {
            if (!j.getNombre().equals("SophIA") && j.getDesconectado() != null && j.getDesconectado().before(primer)) {
                primer = j.getDesconectado();
                primerIdx = i;
            }
            i++;
        }
        return primerIdx;
    }

    public void finalizar() {
        this.finalizada = true;
    }

    public boolean getFinalizada() {
        return this.finalizada;
    }
}
