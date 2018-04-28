/*
 * Autor: Sergio Izquierdo Barranco
 * Fecha: 20-03-18
 * Fichero: Objeto Partida
 */

package basedatos.modelo;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.List;

public class PartidaVO {
    private BigInteger id;
    private Timestamp timeInicio;
    private Timestamp timeFin;
    private boolean publica;
    private char ganador; // '1' si ganador equipo1, '2' si ganador equipo2, 'A' si abandonada
    private List<UsuarioVO> usuarios; // miembros del equipo 1 en las posiciones pares del vector, miembros del equipo 2 en las impares
    private List<Integer> cuarentas;
    private List<Integer> veintes;
    private int puntos1; // puntos obtenidos por el equipo1
    private int puntos2; // puntos obtenidos por el equipo2
    private int abandonador; // Jugador que abandono la partida (1,2,3 o 4), si nadie abandona 0

    private int faseNum; // Fase del torneo
    private BigInteger torneoId; //Id del torneo de la partida

    /* Constructor para crear una nueva partida en curso sin especificar el time de Inicio,
     * (se actualizará automáticamente en la base de datos) los miembros del equipo
     * uno deben ir en las posiciones pares del vector, los miembros del equipo dos
     * en las impares.
     */

    public PartidaVO(boolean publica, List<UsuarioVO> usuarios) {
        this.publica = publica;
        this.usuarios = usuarios;
    }
    /* Constructor para crear una nueva partida en curso, los miembros del equipo
     * uno deben ir en las posiciones pares del vector, los miembros del equipo dos
     * en las impares
     */

    public PartidaVO(Timestamp timeInicio, boolean publica, List<UsuarioVO> usuarios) {
        this.timeInicio = timeInicio;
        this.publica = publica;
        this.usuarios = usuarios;
    }

    public PartidaVO(int faseNum, BigInteger torneoId, boolean publica, List<UsuarioVO> usuarios) {
        this.publica = publica;
        this.usuarios = usuarios;
        this.faseNum = faseNum;
        this.torneoId = torneoId;
    }

    public PartidaVO(BigInteger id, int faseNum, BigInteger torneoId, boolean publica, List<UsuarioVO> usuarios) {
        this.id = id;
        this.publica = publica;
        this.usuarios = usuarios;
        this.faseNum = faseNum;
        this.torneoId = torneoId;
    }

    /* Constructor para crear una partida con todos los datos, los miembros del equipo
     * uno deben ir en las posiciones pares del vector, los miembros del equipo dos
     * en las impares
     */

    public PartidaVO(BigInteger id, Timestamp timeInicio, Timestamp timeFin, boolean publica, char ganador, List<UsuarioVO> usuarios, List<Integer> cuarentas, List<Integer> veintes, int puntos1, int puntos2, int abandonador) {
        this.id = id;
        this.timeInicio = timeInicio;
        this.timeFin = timeFin;
        this.publica = publica;
        this.ganador = ganador;
        this.usuarios = usuarios;
        this.cuarentas = cuarentas;
        this.veintes = veintes;
        this.puntos1 = puntos1;
        this.puntos2 = puntos2;
        this.abandonador = abandonador;
    }
    public int getFaseNum() {
        return faseNum;
    }

    public void setFaseNum(int faseNum) {
        this.faseNum = faseNum;
    }

    public BigInteger getTorneoId() {
        return torneoId;
    }

    public void setTorneoId(BigInteger torneoId) {
        this.torneoId = torneoId;
    }

    public BigInteger getId() {
        return id;
    }

    public void setId(BigInteger id) {
        this.id = id;
    }

    public Timestamp getTimeInicio() {
        return timeInicio;
    }

    public void setTimeInicio(Timestamp timeInicio) {
        this.timeInicio = timeInicio;
    }

    public Timestamp getTimeFin() {
        return timeFin;
    }

    public void setTimeFin(Timestamp timeFin) {
        this.timeFin = timeFin;
    }

    public boolean isPublica() {
        return publica;
    }

    public void setPublica(boolean publica) {
        this.publica = publica;
    }

    public char getGanador() {
        return ganador;
    }

    public void setGanador(char ganador) {
        this.ganador = ganador;
    }

    public List<UsuarioVO> getUsuarios() {
        return usuarios;
    }

    public void setUsuarios(List<UsuarioVO> usuarios) {
        this.usuarios = usuarios;
    }

    public List<Integer> getCuarentas() {
        return cuarentas;
    }

    public void setCuarentas(List<Integer> cuarentas) {
        this.cuarentas = cuarentas;
    }

    public List<Integer> getVeintes() {
        return veintes;
    }

    public void setVeintes(List<Integer> veintes) {
        this.veintes = veintes;
    }

    public int getPuntos1() {
        return puntos1;
    }

    public void setPuntos1(int puntos1) {
        this.puntos1 = puntos1;
    }

    public int getPuntos2() {
        return puntos2;
    }

    public void setPuntos2(int puntos2) {
        this.puntos2 = puntos2;
    }

    public int getAbandonador() {
        return abandonador;
    }

    public void setAbandonador(int abandonador) {
        this.abandonador = abandonador;
    }
}
