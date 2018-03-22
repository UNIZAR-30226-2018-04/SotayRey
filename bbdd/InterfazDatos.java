import com.mchange.v2.c3p0.ComboPooledDataSource;
import java.beans.PropertyVetoException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;
import java.io.FileInputStream;

public class InterfazDatos {

    private static InterfazDatos ifd;
    private ComboPooledDataSource cpds;

    private InterfazDatos() throws IOException, SQLException, PropertyVetoException {
        //Fichero properties
        Properties dbProps = new Properties();
        dbProps.load(new FileInputStream("db.properties"));
        cpds = new ComboPooledDataSource();

        cpds.setDriverClass(dbProps.getProperty("driver")); //loads the jdbc driver
        cpds.setJdbcUrl(dbProps.getProperty("url"));
        cpds.setUser(dbProps.getProperty("user"));
        cpds.setPassword(dbProps.getProperty("password"));

        // the settings below are optional -- c3p0 can work with defaults
        cpds.setMinPoolSize(5);
        cpds.setAcquireIncrement(5);
        cpds.setMaxPoolSize(20);
        cpds.setMaxStatements(180);

    }

    public static InterfazDatos instancia() throws IOException, SQLException, PropertyVetoException {
        if (ifd == null) {
            ifd = new InterfazDatos();
            return ifd;
        } else {
            return ifd;
        }
    }

    /*
     * Crea un usuario nuevo en el sistema. U debe contener (not null) como mínimo: username, correo, nombre, apellidos y admin
     */
    public void crearUsuario(UsuarioVO u) {
        UsuarioDAO.crearUsuario(u, this.cpds);
    }

    /*
     * Devuelve un usuario con todos sus datos (datos de perfil de usuario), a partir de su username
     */
    public UsuarioVO obtenerDatosUsuario(String username) {
        return UsuarioDAO.obtenerDatosUsuario(username, this.cpds);
    }

    /*
     * Elimina un usuario del sistema a partir de su username (no lo elimina por completo, solo le impide el acceso)
     */
    public void eliminarUsuario(String username){
        UsuarioDAO.eliminarUsuario(username, this.cpds);
    }

    /*
     * Modifica los datos de perfil del usuario u
     */
    public void modificarDatosUsuario(UsuarioVO u){
        UsuarioDAO.modificarDatosUsuario(u, this.cpds);
    }

    /*
     * Devuelve true si el usuario identificado por username es un administrador, false en caso contrario
     */
    public boolean esAdministrador(String username){
        return UsuarioDAO.esAdministrador(username, this.cpds);
    }

    /* La función inserta una nueva partida empezada en la base de datos y modifica el objeto PartidaVO
     * de forma que contiene el id de la partida.
     */
    public void crearNuevaPartida(PartidaVO p) { PartidaDAO.insertarNuevaPartida(p, this.cpds); }

    /* Se modifican la partida p en la base de datos con los datos de finalización, p debe incluir
     * el id que se modificó en la función crearNuevaPartida(p).
     */
    public void finalizarPartida(PartidaVO p) { PartidaDAO.finalizarPartida(p, this.cpds); }

    /* Devuelve un array con todas las partidas jugadas por el usuario con username username
     */
    public ArrayList<PartidaVO> obtenerHistorialPartidas(String username) { return PartidaDAO.obtenerHistorialPartidas(username, this.cpds); }

    /* Devuelve un array con todas las partidas públicas que todavía no han finalizado
     */
    public  ArrayList<PartidaVO> obtenerPartidasPublicasCurso() { return PartidaDAO.obtenerPartidasPublicasCurso(this.cpds); }
}
