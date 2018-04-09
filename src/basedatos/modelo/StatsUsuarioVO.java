/*
 * Autor: Guerrero Viu, Julia
 * Fecha: 21-03-2018
 * Fichero: StatsUsuarioVO.java
 * Descripción: Representa un usuario con sus stats principales (puntos, liga, puesto, monedas)
 */

package basedatos.modelo;

import com.mchange.v2.c3p0.ComboPooledDataSource;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import basedatos.exceptions.*;

public class StatsUsuarioVO {

    private String username;
    private int puntuacion;
    private int divisa;
    private String ligaActual;
    private int puesto;
    private String ligaMaxima;
    private int ganadas;
    private int perdidas;
    private int teAbandonaron;
    private int abandonaste;

    public StatsUsuarioVO(String username) throws ExceptionCampoInvalido {
        if (username.length() > 15) {
            // Lanzar excepción de campo no válido 
            throw new ExceptionCampoInvalido("Campo invalido, longitud " + username.length() + " mayor que la máxima permitida");
        }
        this.username = username;
        this.puntuacion = -1;
        this.divisa = -1;
        this.puesto = -1;
        this.ganadas = -1;
        this.perdidas = -1;
        this.teAbandonaron = -1;
        this.abandonaste = -1;
    }

    public StatsUsuarioVO(String username, int puntuacion, int divisa, String ligaActual, int puesto, String ligaMaxima, int ganadas, int perdidas, int teAbandonaron, int abandonaste) throws ExceptionCampoInvalido {
        // Comprobar que la longitud de los campos no sea mayor que los limites de la base de datos
        if (username.length() > 15 || ligaActual.length() > 50 || ligaMaxima.length() > 50) {
            // Lanzar excepción de campo no válido 
            throw new ExceptionCampoInvalido("Campo invalido, longitud " + username.length() + " mayor que la máxima permitida");
        }
        if (puntuacion < 0 || divisa < 0 || puesto < 0) {
            // Lanzar excepción de campo no válido 
            throw new ExceptionCampoInvalido("Campo negativo no permitido");
        }
        this.username = username;
        this.puntuacion = puntuacion;
        this.divisa = divisa;
        this.ligaActual = ligaActual;
        this.puesto = puesto;
        this.ligaMaxima = ligaMaxima;
        this.ganadas = ganadas;
        this.perdidas = perdidas;
        this.teAbandonaron = teAbandonaron;
        this.abandonaste = abandonaste;
    }

    public String getUsername() {
        return username;
    }

    public String getLigaActual() {
        return ligaActual;
    }

    public String getLigaMaxima() {
        return ligaMaxima;
    }

    public int getPuntuacion() {
        return puntuacion;
    }

    public int getDivisa() {
        return divisa;
    }

    public int getPuesto() {
        return puesto;
    }

    public int getGanadas() {
        return ganadas;
    }

    public int getPerdidas() {
        return perdidas;
    }

    public int getTeAbandonaron() {
        return teAbandonaron;
    }

    public int getAbandonaste() {
        return abandonaste;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setLigaActual(String ligaActual) {
        this.ligaActual = ligaActual;
    }

    public void setLigaMaxima(String ligaMaxima) {
        this.ligaMaxima = ligaMaxima;
    }

    public void setPuntuacion(int puntuacion) {
        this.puntuacion = puntuacion;
    }

    public void setDivisa(int divisa) {
        this.divisa = divisa;
    }

    public void setPuesto(int puesto) {
        this.puesto = puesto;
    }

    public void setGanadas(int ganadas) {
        this.ganadas = ganadas;
    }

    public void setPerdidas(int perdidas) {
        this.perdidas = perdidas;
    }

    public void setTeAbandonaron(int teAbandonaron) {
        this.teAbandonaron = teAbandonaron;
    }

    public void setAbandonaste(int abandonaste) {
        this.abandonaste = abandonaste;
    }
}