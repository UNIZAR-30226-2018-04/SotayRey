import java.beans.PropertyVetoException;
import java.io.IOException;
import java.sql.SQLException;

public class Prueba {
    public static void main(String[] args) throws PropertyVetoException, SQLException, IOException, ExceptionCampoInvalido, ExceptionCampoInexistente {
        try{
        UsuarioVO u = new UsuarioVO("juliagv", "716185@unizar.es", "Julia", "Guerrero", true);
        InterfazDatos.instancia().crearUsuario(u);
//        UsuarioVO nuevo = InterfazDatos.instancia().obtenerDatosUsuario("juliagv");
//        nuevo.mostrar();
//        nuevo.setCorreo("ahoraotrocorreo@unizar.es");
//        nuevo.setApellidos("Viu");
//        InterfazDatos.instancia().modificarDatosUsuario(nuevo);
//        nuevo = InterfazDatos.instancia().obtenerDatosUsuario("juliagv");
//        nuevo.mostrar();

//        nuevo.setAdmin(false);
//        InterfazDatos.instancia().modificarDatosUsuario(nuevo);
//        nuevo = InterfazDatos.instancia().obtenerDatosUsuario("juliagv");
//    
//        InterfazDatos.instancia().eliminarUsuario("juliagv");
//        
        StatsUsuarioVO su = new StatsUsuarioVO("juliagv");
        su.setPuntuacion(7);
        su.setDivisa(10);
        InterfazDatos.instancia().modificarStatsUsuario(su);
        
        StatsUsuarioVO su2 = new StatsUsuarioVO("juliagv");
        su2 = InterfazDatos.instancia().obtenerStatsUsuario(su.getUsername());
        System.out.println("Stats del usuario: puntos " + su2.getPuntuacion() + ", divisa " + su2.getDivisa());
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
}
