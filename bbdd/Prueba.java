import java.beans.PropertyVetoException;
import java.io.IOException;
import java.sql.SQLException;

public class Prueba {
    public static void main(String[] args) throws PropertyVetoException, SQLException, IOException, ExceptionCampoInvalido {
        UsuarioVO u = new UsuarioVO("juliagv", "716185@unizar.es", "Julia", "Guerrero", true);
        InterfazDatos.instancia().crearUsuario(u);
        UsuarioVO nuevo = InterfazDatos.instancia().obtenerDatosUsuario("juliagv");
        nuevo.mostrar();
        nuevo.setCorreo("ahoraotrocorreo@unizar.es");
        nuevo.setApellidos("Viu");
        InterfazDatos.instancia().modificarDatosUsuario(nuevo);
        nuevo = InterfazDatos.instancia().obtenerDatosUsuario("juliagv");
        nuevo.mostrar();

        nuevo.setAdmin(false);
        InterfazDatos.instancia().modificarDatosUsuario(nuevo);
        nuevo = InterfazDatos.instancia().obtenerDatosUsuario("juliagv");
    
        InterfazDatos.instancia().eliminarUsuario("juliagv");
        
        StatsUsuarioVO su = new StatsUsuarioVO("juliagv");
        
        if(su.getPuntuacion()==-1){
            System.out.println("TODO CONTROLADO!!"); 
        }        

        su.setPuntuacion(3);

        if(su.getPuntuacion()==3){
            System.out.println("TODO CONTROLADO!!"); 
        }
    }
}
