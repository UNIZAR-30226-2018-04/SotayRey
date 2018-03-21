import java.util.Date;
import java.sql.Timestamp;

public class UsuarioVO {

    private String username;
    //private String password o pw_hash; ??
    private String correo;
    //private String fb_token;
    private Timestamp timeCreacion;
    private String nombre;
    private String apellidos;
    private Date fechaNac;
    private boolean admin;

    public UsuarioVO() {}

    public UsuarioVO(String username, String correo, String nombre, String apellidos, boolean admin) throws ExceptionCampoInvalido {
        // Comprobar que la longitud de los campos no sea mayor que los limites de la base de datos
        if (username.length()>15){
            // Lanzar excepción de campo no válido 
            throw new ExceptionCampoInvalido("Campo username invalido, longitud: " + username.length() + " (longitud máxima 15)");       
        }
        this.username = username;
        this.correo = correo;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.admin = admin;
    }

    public UsuarioVO(String username, String correo, String nombre, String apellidos, boolean admin, Date fechaNac) throws ExceptionCampoInvalido {
        // Comprobar que la longitud de los campos no sea mayor que los limites de la base de datos
        if (username.length()>15){
            // Lanzar excepción de campo no válido
            throw new ExceptionCampoInvalido("Campo username invalido, longitud: " + username.length() + " (longitud máxima 15)");         
        }
        this.username = username;
        this.correo = correo;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.fechaNac = fechaNac;
        this.admin = admin;
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

    public Date getFechaNac(){
        return fechaNac;    
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
  
    // funcion solo de prueba: BORRAR EN VERSION ENTREGABLE
    public void mostrar(){
        System.out.println(username + " " + correo + " " + nombre + " "+ apellidos);
    }

       
}
