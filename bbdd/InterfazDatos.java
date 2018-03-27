import com.mchange.v2.c3p0.ComboPooledDataSource;
import java.beans.PropertyVetoException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Properties;
import java.io.FileInputStream;

public class InterfazDatos {

    private static InterfazDatos ifd;
    private ComboPooledDataSource cpds;

    private InterfazDatos() throws IOException, SQLException, PropertyVetoException {
        //Fichero properties
        Properties dbProps = new Properties();
        dbProps.load(new FileInputStream("bbdd/db.properties"));
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

    /*
     * Devuelve la instancia singleton de la clase InterfazDatos
     * Es necesario llamar a esta función primero para poder llamar al resto de métodos de esta interfaz:
     *      ejemplo: InterfazDatos.instancia().crearUsuario(u);
     */
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
     * Devuelve cierto si y sólo si el usuario username posee la contraseña plaintextPassword. Si el usuario no posee
     * ninguna contraseña lanzará la excepción ExceptionCampoInexistente
     */
    public boolean autentificarUsuario(String username, String plaintextPassword) throws  ExceptionCampoInexistente {
        return UsuarioDAO.autentificarUsuario(username,plaintextPassword,this.cpds);
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
     * Modifica los datos de perfil del usuario u (solamente los atributos que no son null)
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

    /*
     * Devuelve las stats principales (puntuacion y divisa) del usuario username
     */
    public StatsUsuarioVO obtenerStatsUsuario(String username) throws ExceptionCampoInexistente, ExceptionCampoInvalido{
        return StatsUsuarioDAO.obtenerStatsUsuario(username, this.cpds);
    }

    /*
     * Devuelve TODAS las Stats del usuario username:
     *      puntuacion, divisa, ligaActual, puesto, ligaMaxima, número de
     *      partidas ganadas, perdidas, abandonadas y en las que fue abandonado
     */
    public StatsUsuarioVO obtenerTodasStatsUsuario(String username) throws ExceptionCampoInexistente, ExceptionCampoInvalido{
        return StatsUsuarioDAO.obtenerTodasStatsUsuario(username, this.cpds);
    }

    /* Inserta una nueva partida empezada en la base de datos y modifica el objeto PartidaVO
     * de forma que contiene el id de la partida.
     */
    public void crearNuevaPartida(PartidaVO p) {
        PartidaDAO.insertarNuevaPartida(p, this.cpds);
    }

    /* Se modifican la partida p en la base de datos con los datos de finalización, p debe incluir
     * el id que se modificó en la función crearNuevaPartida(p).
     */
    public void finalizarPartida(PartidaVO p) {
        PartidaDAO.finalizarPartida(p, this.cpds);
    }

    /* Devuelve un array con todas las partidas jugadas por el usuario identificado por username
     */
    public ArrayList<PartidaVO> obtenerHistorialPartidas(String username) {
        return PartidaDAO.obtenerHistorialPartidas(username, this.cpds);
    }

    /* Devuelve un array con todas las partidas públicas que todavía no han finalizado
     */
    public  ArrayList<PartidaVO> obtenerPartidasPublicasCurso() { return PartidaDAO.obtenerPartidasPublicasCurso(this.cpds); }

    /* Añade un nuevo artículo al sistema. El atributo requiere de a puede ser nulo si no se requiere ningun
     * liga para desbloquear el artículo
     */
    public void crearArticulo(ArticuloVO a) { ArticuloDAO.crearArticulo(a, this.cpds); }

    /* Elimina un artículo del sistema basándose en el nombre del artículo a
     */
    public void eliminarArticulo(ArticuloVO a) { ArticuloDAO.eliminarArticulo(a, this.cpds); }

    /* Modifica el articulo del sistema con el nombre de a, deja todos sus atributos como los atributos de a
     */
    public void modificarArticulo(ArticuloVO a) { ArticuloDAO.modificarArticulo(a, this.cpds); }

    /* Devuelve el articulo con el nombre art
     */
    public ArticuloVO obtenerArticulo(String art) { return ArticuloDAO.obtenerArticulo(art, this.cpds); }


    // TODO: SOLO PARA PRUEBAS, BORRAR EN EL ENTREGABLE
    public void modificarStatsUsuario(StatsUsuarioVO s){
        StatsUsuarioDAO.modificarStatsUsuario(s,this.cpds);
    }
}
