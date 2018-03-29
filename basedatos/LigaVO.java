/*
 * Autor: Guerrero Viu, Julia
 * Fecha: 29-03-2018
 * Fichero: LigaVO.java
 * Descripción: Representa una liga con sus datos 
 */

import java.util.Date;
import java.sql.Timestamp;

public class LigaVO {

    private String nombre;
    private String descripcion;
    // Los siguientes porcentajes representan qué grupo de usuarios pertenecen a la liga (entre un porcentajeMin% y un porcentajeMax%)
    private int porcentajeMin;  // la mejor liga tiene porcentajeMin = 0
    private int porcentajeMax;  // la peor liga tiene porcentajeMax = 100

    public LigaVO() {}

    public LigaVO(String nombre, int porcentajeMin, int porcentajeMax) throws ExceptionCampoInvalido {
        // Comprobar que la longitud de los campos no sea mayor que los limites de la base de datos
        if (nombre.length()>50){
            // Lanzar excepción de campo no válido
            throw new ExceptionCampoInvalido("Campo invalido, longitud " + nombre.length() + " mayor que la máxima permitida");
        }
        this.nombre = nombre;
        this.porcentajeMin = porcentajeMin;
        this.porcentajeMax = porcentajeMax;
    }

    public LigaVO(String nombre, String descripcion, int porcentajeMin, int porcentajeMax) throws ExceptionCampoInvalido {
        // Comprobar que la longitud de los campos no sea mayor que los limites de la base de datos
        if (nombre.length()>50){
            // Lanzar excepción de campo no válido
            throw new ExceptionCampoInvalido("Campo invalido, longitud " + nombre.length() + " mayor que la máxima permitida");
        }
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.porcentajeMin = porcentajeMin;
        this.porcentajeMax = porcentajeMax;
    }

    public String getNombre(){
        return nombre;    
    }

    public String getDescripcion(){
        return descripcion;    
    }

    public int getPorcentajeMin(){
        return porcentajeMin;    
    }
    
    public int getPorcentajeMax(){
        return porcentajeMax;    
    }

    public void setNombre(String nombre){
        this.nombre = nombre;    
    }

    public void setDescripcion(String descripcion){
        this.descripcion = descripcion;    
    }

    public void setPorcentajeMin(int porcentajeMin){
        this.porcentajeMin = porcentajeMin;    
    }

    public void setPorcentajeMax(int porcentajeMax){
        this.porcentajeMax = porcentajeMax;    
    }
       
}
