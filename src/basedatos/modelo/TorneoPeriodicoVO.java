package basedatos.modelo;

import java.sql.Timestamp;

// Representa el evento de creación de un Torneo Periodico
// Utilizarlo para la gestión de los torneos periódicos pero utilizar TorneoVO para
// todas las cosas relativas a un torneo en concreto (partidas, jugadores apuntados...)
public class TorneoPeriodicoVO {
    private String nombre;
    private String descripcion;
    private Timestamp timePrimero;
    // Periodicidad del torneo en dias de 24 horas
    private int dias;
    private int numFases;
    private int premioPuntuacionPrimera;
    private int premioDivisaPrimera;


    public TorneoPeriodicoVO(String nombre, Timestamp timePrimero, int dias) {
        this.nombre = nombre;
        this.timePrimero = timePrimero;
        this.dias = dias;
        this.numFases = -1;
        this.premioDivisaPrimera = -1;
        this.premioPuntuacionPrimera = -1;
    }

    public TorneoPeriodicoVO(String nombre, String descripcion, Timestamp timePrimero, int dias, int numFases, int premioPuntuacionPrimera, int premioDivisaPrimera) {

        this.nombre = nombre;
        this.descripcion = descripcion;
        this.timePrimero = timePrimero;
        this.dias = dias;
        this.numFases = numFases;
        this.premioPuntuacionPrimera = premioPuntuacionPrimera;
        this.premioDivisaPrimera = premioDivisaPrimera;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Timestamp getTimePrimero() {
        return timePrimero;
    }

    public void setTimePrimero(Timestamp timePrimero) {
        this.timePrimero = timePrimero;
    }

    public int getDias() {
        return dias;
    }

    public void setDias(int dias) {
        this.dias = dias;
    }

    public int getNumFases() {
        return numFases;
    }

    public void setNumFases(int numFases) {
        this.numFases = numFases;
    }

    public int getPremioPuntuacionPrimera() {
        return premioPuntuacionPrimera;
    }

    public void setPremioPuntuacionPrimera(int premioPuntuacionPrimera) {
        this.premioPuntuacionPrimera = premioPuntuacionPrimera;
    }

    public int getPremioDivisaPrimera() {
        return premioDivisaPrimera;
    }

    public void setPremioDivisaPrimera(int premioDivissaPrimera) {
        this.premioDivisaPrimera = premioDivissaPrimera;
    }
}
