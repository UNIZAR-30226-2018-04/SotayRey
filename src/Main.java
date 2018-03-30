import java.beans.PropertyVetoException;
import java.io.IOException;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws ExceptionCampoInvalido, PropertyVetoException, SQLException, IOException, ExceptionCampoInexistente {
        UsuarioVO julia = new UsuarioVO("juliagviu", "caca123","prueba@unizar.es", "Julia", "Guerrero", true);
        UsuarioVO sergio = new UsuarioVO("serizba", null, "serizba@unizar.es", "Sergio", "Izquierdo", true);
        UsuarioVO manolo = new UsuarioVO("manolo", "1234", "manolo@unizar.es", "Julia", "Guerrero", true);
        UsuarioVO julian = new UsuarioVO("eljuli", "0000","juli@unizar.es", "Sergio", "Izquierdo", true);
        InterfazDatos.instancia().crearUsuario(julia);
        InterfazDatos.instancia().crearUsuario(sergio);
        InterfazDatos.instancia().crearUsuario(manolo);
        InterfazDatos.instancia().crearUsuario(julian);
    }
}
