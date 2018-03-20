public class UsuarioVO {

    private String username;
    private String nombre;

    public UsuarioVO(String un, String n) {
        username = un;
        nombre = n;
    }
    
    public String get_username(){
        return username;
    }

    public String get_nombre(){
        return nombre;
    }
        
}
