package gestor;

import basedatos.modelo.PartidaVO;

import javax.websocket.RemoteEndpoint;
import javax.websocket.Session;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Autor: Javier
 * Fecha: 01/04/2018
 */
public class Lobby {
    private HashMap<String, JugadorGestor> jugadores = new HashMap<>();
    private int ronda = 0;
    private PartidaVO partidaVO = null;
    private int maxID = 4; // Para asignarlo a los espectadores
    private int numJugadores = 0;

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
    }

    public void anyadir(JugadorGestor jug) {
        if (!jugadores.containsKey(jug.getNombre())) {
            jugadores.put(jug.getNombre(), jug);
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
        ArrayList<String> lista = new ArrayList<String>();
        for (JugadorGestor jug : jugadores.values()){
            lista.add(jug.getNombre());
        }
        /*
        for (int i = 0; i<numJugadores; i++) {
            lista.add(buscarId(i).getNombre());
        **/
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
            if (jug.getDesconectado() == null) {
                return true;
            }
        }
        return false;
    }

    public void setPartidaVO(PartidaVO p) {
        this.partidaVO = p;
    }

    public PartidaVO getPartidaVO() {return this.partidaVO;}
}
