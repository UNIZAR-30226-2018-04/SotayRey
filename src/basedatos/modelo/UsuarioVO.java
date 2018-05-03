/*
 * Autor: Guerrero Viu, Julia
 * Fecha: 20-03-2018
 * Fichero: UsuarioVO.java
 * Descripción: Representa un usuario con todos sus datos de perfil de usuario
 */

package basedatos.modelo;

import java.util.Date;
import java.sql.Timestamp;

import basedatos.exceptions.*;

public class UsuarioVO {

    private String username;
    private String correo;
    private Timestamp timeCreacion;
    private String plaintextPassword;
    private String fb_auth;
    private String nombre;
    private String apellidos;
    private Date fechaNac;
    private boolean admin;

    public UsuarioVO() { admin = false;}

	/* Usar este constructor siempre que sea un usuario con contraseña
	 */ 
    public UsuarioVO(String username, String plaintextPassword, String correo, String nombre, String apellidos, boolean admin) throws ExceptionCampoInvalido {
        // Comprobar que la longitud de los campos no sea mayor que los limites de la base de datos
        if (username.length()>20 || nombre.length()>25 || apellidos.length()>50){
            // Lanzar excepción de campo no válido
            throw new ExceptionCampoInvalido("Campo invalido, longitud " + username.length() + " mayor que la máxima permitida");
        }
        this.username = username;
        this.correo = correo;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.admin = admin;
        this.plaintextPassword = plaintextPassword;
    }
	
	/* Usar este constructor siempre que sea un usuario registrado con Facebook
	 */
    public UsuarioVO(String username, String correo, String nombre, String apellidos, boolean admin, String fb_auth) throws ExceptionCampoInvalido {
        // Comprobar que la longitud de los campos no sea mayor que los limites de la base de datos
        if (username.length()>20 || nombre.length()>25 || apellidos.length()>50){
            // Lanzar excepción de campo no válido
            throw new ExceptionCampoInvalido("Campo invalido, longitud " + username.length() + " mayor que la máxima permitida");
        }
        this.username = username;
        this.correo = correo;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.admin = admin;
        this.fb_auth = fb_auth;
    }

    public String getPlaintextPassword() {
        return plaintextPassword;
    }

    public void setPlaintextPassword(String plaintextPassword) {
        this.plaintextPassword = plaintextPassword;
    }

    public UsuarioVO(String username, String correo, String nombre, String apellidos, boolean admin, Date fechaNac, String fb_auth) throws ExceptionCampoInvalido {
        // Comprobar que la longitud de los campos no sea mayor que los limites de la base de datos
        if (username.length()>20 || nombre.length()>25 || apellidos.length()>50){
            // Lanzar excepción de campo no válido 
            throw new ExceptionCampoInvalido("Campo invalido, longitud " + username.length() + " mayor que la máxima permitida");       
        }
        this.plaintextPassword = plaintextPassword;
        this.username = username;
        this.correo = correo;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.fechaNac = fechaNac;
        this.admin = admin;
        this.fb_auth = fb_auth;
    }

    public String getUsername(){
        return username;    
    }

    public String getCorreo(){
        return correo;    
    }

    public String getNombre(){
        return nombre;    
    }
    
    public String getApellidos(){
        return apellidos;    
    }
    
    public boolean getAdmin(){
        return admin;    
    }

    public String getFb_auth() {
        return fb_auth;
    }

    public void setFb_auth(String fb_auth) {
        this.fb_auth = fb_auth;
    }

    public Date getFechaNac(){
        return fechaNac;    
    }

	public Timestamp getTimeCreacion(){
        return timeCreacion;    
    }

    public void setUsername(String username){
        this.username = username;    
    }

    public void setCorreo(String correo){
        this.correo = correo;    
    }

    public void setNombre(String nombre){
        this.nombre = nombre;    
    }

    public void setApellidos(String apellidos){
        this.apellidos = apellidos;    
    }

    public void setAdmin(boolean admin){
        this.admin = admin;    
    }

    public void setFechaNac(Date fechaNac){
        this.fechaNac = fechaNac;
    }

    public void setTimeCreacion(Timestamp timeCreacion){
        this.timeCreacion = timeCreacion;
    }
  
}
