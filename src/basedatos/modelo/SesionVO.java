/*
 * Autor: Guerrero Viu, Julia
 * Fecha: 03-05-2018
 * Fichero: SesionVO.java
 * Descripción: Representa una sesión abierta por un usuario que se desconecta
 */

package basedatos.modelo;

import java.sql.Timestamp;

public class SesionVO {

    private String username;
	private String url;
    private Timestamp timeCreacion;

    public SesionVO() { }

    public SesionVO(String username, String url) {
        this.username = username;
        this.url = url;
    }

    public SesionVO(String username, String url, Timestamp timeCreacion) {
        this.username = username;
        this.url = url;
        this.timeCreacion = timeCreacion;
    }

    public String getUsername(){
        return username;    
    }

    public String getUrl(){
        return url;    
    }

    public Timestamp getTimeCreacion(){
        return timeCreacion;    
    }

    public void setUsername(String username){
        this.username = username;    
    }

    public void setUrl(String url){
        this.url = url;    
    }

    public void setTimeCreacion(Timestamp timeCreacion){
        this.timeCreacion = timeCreacion;
    }
  
}
