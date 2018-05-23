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
    private boolean lleno;

    public TorneoMatch(TorneoVO t) {
        this.jugadores = new HashMap<>();
        this.vo = t;
        this.lleno = false;
        fase = t.getNumFases();
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
    }

    public BigInteger getIdTorneo() {
        return this.vo.getId();
    }

    public Session getSesion(String nombre) {
        return jugadores.get(nombre);
    }

    public void setLleno(boolean valor) {
        this.lleno = valor;
    }
}
