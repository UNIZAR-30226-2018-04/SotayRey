import java.beans.PropertyVetoException;
import java.io.IOException;
import java.sql.SQLException;

public class Prueba {
    public static void main(String[] args) throws PropertyVetoException, SQLException, IOException, ExceptionCampoInvalido {
        UsuarioVO u = new UsuarioVO("juliagviu", "716185@unizar.es", "Julia", "Guerrero", true);
        InterfazDatos.instancia().crearUsuario(u);
        UsuarioVO nuevo = InterfazDatos.instancia().obtenerDatosUsuario("juliagviu");
        nuevo.mostrar();
        nuevo.setCorreo("ahoraotrocorreo@unizar.es");
        nuevo.setApellidos("Viu");
        InterfazDatos.instancia().modificarDatosUsuario(nuevo);
        nuevo = InterfazDatos.instancia().obtenerDatosUsuario("juliagviu");
        nuevo.mostrar();

        if (InterfazDatos.instancia().esAdministrador("juliagviu")){
            System.out.println("TODO CONTROLADO!!");        
        }

        nuevo.setAdmin(false);
        InterfazDatos.instancia().modificarDatosUsuario(nuevo);
        nuevo = InterfazDatos.instancia().obtenerDatosUsuario("juliagviu");
        
        if(!InterfazDatos.instancia().esAdministrador("juliagviu")){
            System.out.println("TODO SIGUE CONTROLADO!!");
        }
        InterfazDatos.instancia().eliminarUsuario("juliagviu");
        
        
    }
}
