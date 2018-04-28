package basedatos;

import basedatos.dao.*;
import basedatos.exceptions.ExceptionCampoInexistente;
import basedatos.exceptions.ExceptionCampoInvalido;
import basedatos.modelo.*;
import com.mchange.v2.c3p0.ComboPooledDataSource;

import java.beans.PropertyVetoException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class InterfazDatos {

    private static InterfazDatos ifd;
    private ComboPooledDataSource cpds;

    private InterfazDatos() throws IOException, PropertyVetoException {
        //Fichero properties
        Properties dbProps = new Properties();
        URL resource = this.getClass().getResource("./db.properties");
        File file = null;
        try {
            file = new File(resource.toURI());
        } catch(Exception e){
            System.out.println(resource.toString());
        }
        FileInputStream input = new FileInputStream(file);
        dbProps.load(input);
        cpds = new ComboPooledDataSource();

        cpds.setDriverClass(dbProps.getProperty("driver")); //loads the jdbc driver
        cpds.setJdbcUrl(dbProps.getProperty("url"));
        cpds.setUser(dbProps.getProperty("user"));
        cpds.setPassword(dbProps.getProperty("password"));

        // the settings below are optional -- c3p0 can work with defaults
        cpds.setMinPoolSize(3);
        cpds.setAcquireIncrement(5);
        cpds.setMaxPoolSize(20);
        cpds.setMaxStatements(180);

    }

    /*
     * Devuelve la instancia singleton de la clase InterfazDatos
     * Es necesario llamar a esta función primero para poder llamar al resto de métodos de esta interfaz:
     *      ejemplo de uso: InterfazDatos.instancia().crearUsuario(u);
     */
    public static InterfazDatos instancia() throws IOException, PropertyVetoException {
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
    public void crearUsuario(UsuarioVO u) throws ExceptionCampoInexistente, SQLException {
        UsuarioDAO.crearUsuario(u, this.cpds);
        ArticuloUsuarioDAO.crearArticuloUsuario(new ArticuloUsuarioVO("Tapete Verde",'T',true, u.getUsername()), this.cpds);
        ArticuloUsuarioDAO.crearArticuloUsuario(new ArticuloUsuarioVO("Una cara",'A',true, u.getUsername()), this.cpds);
        ArticuloUsuarioDAO.crearArticuloUsuario(new ArticuloUsuarioVO("Heracleto Furnier",'B', true, u.getUsername()), this.cpds);
    }

    /*
     * Devuelve cierto si y sólo si el usuario username posee la contraseña plaintextPassword. Si el usuario no posee
     * ninguna contraseña lanzará la excepción ExceptionCampoInexistente
     */
    public boolean autentificarUsuario(String username, String plaintextPassword) throws  SQLException, ExceptionCampoInexistente {
        return UsuarioDAO.autentificarUsuario(username,plaintextPassword,this.cpds);
    }

    /*
     * Devuelve un usuario con todos sus datos (datos de perfil de usuario), a partir de su username
     */
    public UsuarioVO obtenerDatosUsuario(String username) throws SQLException, ExceptionCampoInexistente{
        return UsuarioDAO.obtenerDatosUsuario(username, this.cpds);
    }

    /*
     * Elimina un usuario del sistema a partir de su username (no lo elimina por completo, solo le impide el acceso)
     */
    public void eliminarUsuario(String username) throws SQLException, ExceptionCampoInexistente {
        UsuarioDAO.eliminarUsuario(username, this.cpds);
    }

    /*
     * Modifica los datos de perfil del usuario u (solamente los atributos que no son null)
     */
    public void modificarDatosUsuario(UsuarioVO u) throws ExceptionCampoInexistente, SQLException{
        UsuarioDAO.modificarDatosUsuario(u, this.cpds);
    }

    /*
     * Devuelve true si el usuario identificado por username es un administrador, false en caso contrario
     */
    public boolean esAdministrador(String username) throws ExceptionCampoInexistente, SQLException {
        return UsuarioDAO.esAdministrador(username, this.cpds);
    }

    /*
     * Devuelve las stats principales (puntuacion y divisa) del usuario username
     */
    public StatsUsuarioVO obtenerStatsUsuario(String username) throws ExceptionCampoInexistente, ExceptionCampoInvalido, SQLException {
        return StatsUsuarioDAO.obtenerStatsUsuario(username, this.cpds);
    }

    /*
     * Devuelve TODAS las Stats del usuario username:
     *      puntuacion, divisa, ligaActual, puesto, ligaMaxima, número de
     *      partidas ganadas, perdidas, abandonadas y en las que fue abandonado
     */
    public StatsUsuarioVO obtenerTodasStatsUsuario(String username) throws ExceptionCampoInexistente, ExceptionCampoInvalido, SQLException {
        return StatsUsuarioDAO.obtenerTodasStatsUsuario(username, this.cpds);
    }

    /* Inserta una nueva partida empezada en la base de datos y modifica el objeto PartidaVO
     * de forma que contiene el id de la partida.
     */
    public void crearNuevaPartida(PartidaVO p) throws SQLException {
        PartidaDAO.insertarNuevaPartida(p, this.cpds);
    }

    /* Inserta al Usuario u al Torneo t en su fase inicial. Si u llena el numero de participantes de la fase
     * se produce el emparejamiento
     */
 //   public void apuntarTorneo(UsuarioVO u, TorneoVO t) throws  ExceptionCampoInvalido, SQLException {
 //       TorneoDAO.apuntarTorneo(u,t,this.cpds);
 //   }

    /* Rellena los campos de FaseVO f. f debe de poseer la id del torneo y el número de fase
     * Se le rellenan los datos con las partidas (no empezadas, pero ya incluidas en la base de datos) y la lista
     * de participantes.
     */
 //   public void  obtenerPartidasFaseTorneo(FaseVO f) throws SQLException {
 //       PartidaDAO.obtenerPartidasFaseTorneo(f,this.cpds);
 //   }

    /* Se modifican la partida p en la base de datos con los datos de finalización, p debe incluir
     * el id que se modificó en la función crearNuevaPartida(p).
     * Actualiza las puntuaciones y divisas de los usuarios implicados 
     *      (Ganada:3puntos / 5monedas, Perdida:0puntos / 1moneda, Abandonada:-1puntos/ 0monedas, Teabandonan:0puntos / 1moneda)
     * Si la partida era de Torneo, incluye al ganador en la siguiente fase y la recompensa depende de la fase del torneo
     */
    public void finalizarPartida(PartidaVO p) throws ExceptionCampoInvalido, ExceptionCampoInexistente, SQLException {
        // Finalizar partida
        PartidaDAO.finalizarPartida(p, this.cpds);

        int puntosGanador = 3;
        int divisaGanador = 5;

        //Jejeje
 //       if (p.getFaseNum()>0) {
 //           PartidaDAO.finalizarPartidaFaseTorneo(p,this.cpds);
 //           TorneoVO t = TorneoDAO.obtenerDatosTorneo(p.getTorneoId());
 //           puntosGanador = Math.pow(2,t.numFases()-p.getFaseNum())*2;
 //           divisaGanador = Math.pow(2,t.numFases()-p.getFaseNum())*2;
 //       }

        // Actualizar datos de los usuarios implicados
        char gan = p.getGanador();
        
        List<UsuarioVO> lista = p.getUsuarios();
        // Partida NO abandonada
        if (gan=='1' || gan=='2'){
            for(int i=0; i<lista.size(); i=i+2){
                if(gan =='1'){
                    //ganador
                    StatsUsuarioVO su1 = StatsUsuarioDAO.obtenerStatsUsuario(lista.get(i).getUsername(),this.cpds);
                    su1.setPuntuacion(su1.getPuntuacion()+3);
                    su1.setDivisa(su1.getDivisa()+5);
                    StatsUsuarioDAO.modificarStatsUsuario(su1,this.cpds);
                    //perdedor
                    StatsUsuarioVO su2 = StatsUsuarioDAO.obtenerStatsUsuario(lista.get(i+1).getUsername(),this.cpds); 
                    su2.setDivisa(su2.getDivisa()+1); 
                    StatsUsuarioDAO.modificarStatsUsuario(su2,this.cpds);         
                }
                else{
                    //perdedor
                    StatsUsuarioVO su1 = StatsUsuarioDAO.obtenerStatsUsuario(lista.get(i).getUsername(),this.cpds);
                    su1.setDivisa(su1.getDivisa()+1);
                    StatsUsuarioDAO.modificarStatsUsuario(su1,this.cpds);
                    //ganador
                    StatsUsuarioVO su2 = StatsUsuarioDAO.obtenerStatsUsuario(lista.get(i+1).getUsername(),this.cpds); 
                    su2.setPuntuacion(su2.getPuntuacion()+puntosGanador);
                    su2.setDivisa(su2.getDivisa()+divisaGanador);
                    StatsUsuarioDAO.modificarStatsUsuario(su2,this.cpds);  
                    
                }            
            }
        }
        // Partida abandonada
        else if (gan=='A'){
            for(int i=0; i<lista.size(); i++){
                StatsUsuarioVO su = StatsUsuarioDAO.obtenerStatsUsuario(lista.get(i).getUsername(),this.cpds);
                if(p.getAbandonador()==i){
                    su.setPuntuacion(su.getPuntuacion()-1);
                }
                else{
                    su.setDivisa(su.getDivisa()+divisaGanador);
                    su.setPuntuacion(su.getPuntuacion()+puntosGanador);
                } 
                StatsUsuarioDAO.modificarStatsUsuario(su,this.cpds);           
            }        
        }
        else{
            throw new ExceptionCampoInvalido("La partida debe estar finalizada (con ganador o abandonador)");
        } 
    }

    /* Devuelve un array con todas las partidas jugadas por el usuario identificado por username
     */
    public ArrayList<PartidaVO> obtenerHistorialPartidas(String username) throws SQLException {
        return PartidaDAO.obtenerHistorialPartidas(username, this.cpds);
    }

    /* Devuelve un array con todas las partidas públicas que todavía no han finalizado
     */
    public  ArrayList<PartidaVO> obtenerPartidasPublicasCurso() throws SQLException {
        return PartidaDAO.obtenerPartidasPublicasCurso(this.cpds); 
    }

    /* Crea una nueva liga en el sistema. Necesita un nombre y los porcentajes min y max que la definen
     */
    public void crearLiga(LigaVO l) throws SQLException, ExceptionCampoInvalido {
        LigaDAO.crearLiga(l, this.cpds);
    }
    
    /* Devuelve los datos básicos de la liga denominada nombre
     */
    public LigaVO obtenerDatosLiga(String nombre) throws SQLException, ExceptionCampoInexistente {
        return LigaDAO.obtenerDatosLiga(nombre, this.cpds);
    }

    /* Elimina la liga denominada nombre del sistema. Cuidado! Está operación no se permitirá si hay usuarios
     * que pertenezcan a esta liga
     */   
    public void eliminarLiga(String nombre) throws SQLException {
        LigaDAO.eliminarLiga(nombre, this.cpds);
    }

    /*
     * Modifica los datos de la liga l (solamente los atributos que no son null)
     */
    public void modificarDatosLiga(LigaVO l) throws SQLException, ExceptionCampoInexistente {
        LigaDAO.modificarDatosLiga(l,this.cpds);    
    }

    /*
     * Devuelve todas las ligas del sistema
     */
    public ArrayList<LigaVO> obtenerLigas() throws SQLException, ExceptionCampoInvalido {
        return LigaDAO.obtenerLigas(this.cpds);
    }
    
    /* Devuelve la clasificación actual completa de la liga denominada nombre, formada por los nombres de los 
     * usuarios y sus puntuaciones (el resto de atributos de de los StatsUsuario tienen valor null)
     */
    public ArrayList<StatsUsuarioVO> obtenerClasificacionLiga(String nombre) throws SQLException, ExceptionCampoInvalido {
        return LigaDAO.obtenerClasificacionLiga(nombre, this.cpds);
    }

    /* Añade un nuevo artículo al sistema. El atributo requiere de a puede ser nulo si no se requiere ninguna
     * liga para desbloquear el artículo
     */
    public void crearArticulo(ArticuloVO a) throws SQLException, ExceptionCampoInvalido {
        ArticuloDAO.crearArticulo(a, this.cpds); 
    }

    /* Elimina un artículo del sistema basándose en el nombre del artículo a
     */
    public void eliminarArticulo(ArticuloVO a) throws SQLException {
        ArticuloDAO.eliminarArticulo(a, this.cpds); 
    }

    /* Modifica el articulo del sistema con el nombre de a, deja todos sus atributos como los atributos de a
     */
    public void modificarArticulo(ArticuloVO a) throws SQLException, ExceptionCampoInexistente{
        ArticuloDAO.modificarArticulo(a, this.cpds); 
    }

    /* Devuelve el articulo con el nombre art
     */
    public ArticuloVO obtenerArticulo(String art) throws SQLException, ExceptionCampoInexistente {
        return ArticuloDAO.obtenerArticulo(art, this.cpds); 
    }

    /* Devuelve todos los articulos comprados por el usuario username
     */
    public ArrayList<ArticuloUsuarioVO> obtenerArticulosUsuario(String username) throws SQLException {
        return ArticuloUsuarioDAO.obtenerArticulosUsuario(username, this.cpds);
    }

    /* Marca el articulo de a como comprado para el usuario especificado en a.
     * a debe contener el tipo, favorito, nombre de usuario y nombre de artículo
     */
    public void comprarArticuloUsuario(ArticuloUsuarioVO a) throws SQLException, ExceptionCampoInvalido, ExceptionCampoInexistente {
        StatsUsuarioVO stats = StatsUsuarioDAO.obtenerStatsUsuario(a.getUsername(), this.cpds);
        System.out.println("Dinero: " + stats.getDivisa());
        boolean encontrado = false;
        for (ArticuloUsuarioVO art: ArticuloUsuarioDAO.obtenerArticulosTienda(a.getUsername(),this.cpds)) {
            if (art.getNombre().equals(a.getNombre())) {
                encontrado = true;
                System.out.println("Cuesta: " + art.getPrecio());
                if (art.isComprado()) { ArticuloUsuarioDAO.crearArticuloUsuario(a,this.cpds);}
                else if (!art.isDisponible()) { throw new ExceptionCampoInvalido("El Usuario: "+a.getUsername()+" no tiene la liga necesaria para el Articulo:"+a.getNombre());}
                else if (stats.getDivisa()<art.getPrecio()) { throw new ExceptionCampoInvalido("El Usuario: "+a.getUsername()+" no tiene el dinero necesario para el Articulo: "+a.getNombre());}
                else {
                    stats.setDivisa(stats.getDivisa()-art.getPrecio());
                    StatsUsuarioDAO.modificarStatsUsuario(stats, this.cpds);
                    ArticuloUsuarioDAO.crearArticuloUsuario(a, this.cpds);
                }
            }
        }
        if (!encontrado) { throw new ExceptionCampoInexistente("Error de acceso a la base de datos: Articulo: " + a.getNombre() + " no existente");}
    }

    /* Devuelve todos los artículos de la tienda para un usuario
     */
    public ArrayList<ArticuloUsuarioVO> obtenerArticulosTienda(String username) throws SQLException, ExceptionCampoInexistente {
        return ArticuloUsuarioDAO.obtenerArticulosTienda(username, this.cpds);
    }

}
