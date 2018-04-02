package gestor;

import javax.websocket.RemoteEndpoint;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Autor: Javier
 * Fecha: 01/04/2018
 */
public class Lobby {
    private static HashMap<String, JugadorGestor> jugadores = new HashMap<>();

    public Lobby() {}
    public Lobby(JugadorGestor jug) {
        anyadir(jug);
    }

    public void anyadir(JugadorGestor jug) {
        if (!jugadores.containsKey(jug.getNombre())) {
            jugadores.put(jug.getNombre(), jug);
        }
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
        if (jug != null) {
            return jug.getRemoto();
        }
        else {
            return null;
        }
    }

    public ArrayList<RemoteEndpoint.Basic> getTodosRemotos() {
        ArrayList<RemoteEndpoint.Basic> lista = new ArrayList<>();
        for (String nombre : jugadores.keySet()) {
            lista.add(getRemotoJug(nombre));
        }
        return lista;
    }

    public ArrayList<String> getTodosNombres() {
        ArrayList<String> lista = new ArrayList<String>();
        for (int i = 0; i<tam(); i++) {
            lista.add(buscarId(i).getNombre());
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
}
