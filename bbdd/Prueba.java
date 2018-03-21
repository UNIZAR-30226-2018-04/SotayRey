import java.beans.PropertyVetoException;
import java.io.IOException;
import java.sql.SQLException;

public class Prueba {
    public static void main(String[] args) throws PropertyVetoException, SQLException, IOException, ExceptionCampoInvalido {
        UsuarioVO u = new UsuarioVO("juliagviu3", "prueba@unizar.es", "Julia", "Guerrero", true);
        InterfazDatos.instancia().crearUsuario(u);
        UsuarioVO nuevo = InterfazDatos.instancia().obtenerDatosUsuario("juliagviu");
        nuevo.mostrar();
    }
}
