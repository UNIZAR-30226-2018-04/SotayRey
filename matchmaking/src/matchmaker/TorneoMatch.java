package matchmaker;

import basedatos.modelo.TorneoVO;

import javax.websocket.Session;
import java.math.BigInteger;
import java.util.HashMap;

/**
 * Autor: Javier
 * Fecha: 21/05/2018
 */
public class TorneoMatch {
    private HashMap<String, Session> jugadores;
    private TorneoVO vo;
    private int fase;
    private boolean empezadaUltRonda;

    public TorneoMatch(TorneoVO t) {
        this.jugadores = new HashMap<>();
        this.vo = t;
        this.fase = t.getNumFases();
    }

    public void setVO(TorneoVO t) {
        this.vo = t;
    }

    public TorneoVO getVO() {
        return this.vo;
    }

    public void anyadirJugador(String nombre, Session sesion) {
        if (!jugadores.containsKey(nombre)) {
            jugadores.put(nombre, sesion);
        }
    }

    public void eliminarJugador(String nombre) {
        if (jugadores.containsKey(nombre)) {
            jugadores.remove(nombre);
        }
    }

    public int getFase() {
        return this.fase;
    }

    public void setFase(int fase) { this.fase = fase; }

    public void decFase() {
        this.fase--;
        jugadores.clear();
    }

    public boolean todosConectados() {
        return jugadores.keySet().size() == Math.pow(2, this.fase);
    }

    public boolean getMaxFase() {
        return this.fase == this.vo.getNumFases();
    }

    public BigInteger getIdTorneo() {
        return this.vo.getId();
    }

    public Session getSesion(String nombre) {
        return jugadores.get(nombre);
    }

    public boolean getAcabado() {
        return this.fase == 1 && this.jugadores.size() == 2;
    }

    public String eliminarJugador(Session sesion) {
        String encontrado = null;
        for (String nombre : jugadores.keySet()) {
            if (jugadores.get(nombre) == sesion) {
                jugadores.remove(nombre);
                encontrado = nombre;
                break;
            }
        }
        return encontrado;
    }
}
