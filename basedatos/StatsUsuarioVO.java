/*
 * Autor: Guerrero Viu, Julia
 * Fecha: 21-03-2018
 * Fichero: StatsUsuarioVO.java
 * Descripción: Representa un usuario con sus stats principales (puntos, liga, puesto, monedas)
 */

import com.mchange.v2.c3p0.ComboPooledDataSource;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class StatsUsuarioVO {

    private String username;
    private int puntuacion;
    private int divisa;
    private String ligaActual;
    private int puesto; 
    private String ligaMaxima;
    private int ganadas;
    private int perdidas;
    private int teAbandonaron;
    private int abandonaste;

    public StatsUsuarioVO(String username) throws ExceptionCampoInvalido { 
        if (username.length()>15){
            // Lanzar excepción de campo no válido 
            throw new ExceptionCampoInvalido("Campo invalido, longitud " + username.length() + " mayor que la máxima permitida");       
        }
        this.username = username;
        this.puntuacion = -1;
        this.divisa = -1;
        this.puesto = -1;
        this.ganadas = -1;
        this.perdidas = -1;
        this.teAbandonaron = -1;
        this.abandonaste = -1;
    }

    public StatsUsuarioVO(String username, int puntuacion, int divisa, String ligaActual, int puesto, String ligaMaxima, int ganadas, int perdidas, int teAbandonaron, int abandonaste) throws ExceptionCampoInvalido {
        // Comprobar que la longitud de los campos no sea mayor que los limites de la base de datos
        if (username.length()>15 || ligaActual.length()>50 || ligaMaxima.length()>50){
            // Lanzar excepción de campo no válido 
            throw new ExceptionCampoInvalido("Campo invalido, longitud " + username.length() + " mayor que la máxima permitida");       
        }
        if (puntuacion < 0 || divisa < 0 || puesto < 0){
            // Lanzar excepción de campo no válido 
            throw new ExceptionCampoInvalido("Campo negativo no permitido");
        }
        this.username = username;
        this.puntuacion = puntuacion;
        this.divisa = divisa;
        this.ligaActual = ligaActual;
        this.puesto = puesto;
        this.ligaMaxima = ligaMaxima;
        this.ganadas = ganadas;
        this.perdidas = perdidas;
        this.teAbandonaron = teAbandonaron;
        this.abandonaste = abandonaste;
    }

    public String getUsername(){
        return username;    
    }

    public String getLigaActual(){
        return ligaActual;    
    }

    public String getLigaMaxima(){
        return ligaMaxima;    
    }
    
    public int getPuntuacion(){
        return puntuacion;    
    }
    
    public int getDivisa(){
        return divisa;    
    }
    
    public int getPuesto(){
        return puesto;    
    }
    
    public int getGanadas(){
        return ganadas;    
    }

    public int getPerdidas(){
        return perdidas;    
    }

    public int getTeAbandonaron(){
        return teAbandonaron;    
    }
    
    public int getAbandonaste(){
        return abandonaste;    
    }

    public void setUsername(String username){
        this.username = username;    
    }

    public void setLigaActual(String ligaActual){
        this.ligaActual = ligaActual;    
    }

    public void setLigaMaxima(String ligaMaxima){
        this.ligaMaxima = ligaMaxima;    
    }

    public void setPuntuacion(int puntuacion){
        this.puntuacion = puntuacion;    
    }

    public void setDivisa(int divisa){
        this.divisa = divisa;    
    }

    public void setPuesto(int puesto){
        this.puesto = puesto;    
    }

    public void setGanadas(int ganadas){
        this.ganadas = ganadas;    
    }
    
    public void setPerdidas(int perdidas){
        this.perdidas = perdidas;    
    }

    public void setTeAbandonaron(int teAbandonaron){
        this.teAbandonaron = teAbandonaron;    
    }
    
    public void setAbandonaste(int abandonaste){
        this.abandonaste = abandonaste;
    }

    public static class ArticuloDAO {
        public static void crearArticulo(ArticuloVO a, ComboPooledDataSource pool) {
            Connection connection = null;
            Statement statement = null;
            try {
                connection = pool.getConnection();
                statement = connection.createStatement();
                connection.setAutoCommit(false);
                String insertArticulo;

                //if (a.getRequiere() == null) {
                insertArticulo = "INSERT INTO articulo (nombre,precio,descripcion,rutaImagen,tipo) VALUES ('" +
                        a.getNombre() + "'," + a.getPrecio() + ",'" + a.getDescripcion() + "','" + a.getRutaImagen() + "','" + a.getTipo() + "')";
    //            } else {
    //                insertArticulo = "INSERT INTO articulo (nombre,precio,descripcion,rutaImagen,tipo,requiere_liga) VALUES ('" +
    //                        a.getNombre() + "'," + a.getPrecio() + ",'" + a.getDescripcion() + "','" + a.getRutaImagen() + "','" + a.getTipo() + "','" +  a.getRequiere() + "')";
    //            }
                //Comienza transacción

                statement.executeUpdate(insertArticulo);

                connection.commit();


            } catch (SQLException e) {
                e.printStackTrace();
                if (connection != null) {
                    try {
                        System.err.print("Transaction is being rolled back");
                        connection.rollback();
                    } catch (SQLException excep) {
                        excep.printStackTrace();
                    }
                }
            } finally {
                if (statement != null) try {
                    statement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                if (connection != null) try {
                    connection.setAutoCommit(true);
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        public static void eliminarArticulo(ArticuloVO a, ComboPooledDataSource pool) {
            Connection connection = null;
            Statement statement = null;
            try {
                connection = pool.getConnection();
                statement = connection.createStatement();
                connection.setAutoCommit(false);
                String deleteArticulo = "DELETE FROM articulo WHERE nombre = '"+a.getNombre() + "'";

                statement.executeUpdate(deleteArticulo);

                connection.commit();


            } catch (SQLException e) {
                e.printStackTrace();
                if (connection != null) {
                    try {
                        System.err.print("Transaction is being rolled back");
                        connection.rollback();
                    } catch (SQLException excep) {
                        excep.printStackTrace();
                    }
                }
            } finally {
                if (statement != null) try {
                    statement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                if (connection != null) try {
                    connection.setAutoCommit(true);
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        public static void modificarArticulo(ArticuloVO a, ComboPooledDataSource pool) {
            Connection connection = null;
            Statement statement = null;
            try {
                connection = pool.getConnection();
                statement = connection.createStatement();
                connection.setAutoCommit(false);
                String modificaArticulo;

                //if (a.getRequiere() == null) {
                modificaArticulo = "UPDATE articulo SET " +
                        "precio = " + a.getPrecio() + ", descripcion = '" + a.getDescripcion() + "', rutaImagen = '" + a.getRutaImagen() + "', tipo = '" + a.getTipo() + "' WHERE nombre = '" + a.getNombre() + "'";
    //            } else {
    //            modificaArticulo = "UPDATE articulo SET " +
    //                    "requiere_liga = '" + a.getRequiere() + "', precio = " + a.getPrecio() + ", descripcion = '" + a.getDescripcion() + "', rutaImagen = '" + a.getRutaImagen() + "', tipo = '" + a.getTipo() + "' WHERE nombre = '" + a.getNombre() + "'";
    //            }
                //Comienza transacción

                statement.executeUpdate(modificaArticulo);

                connection.commit();


            } catch (SQLException e) {
                e.printStackTrace();
                if (connection != null) {
                    try {
                        System.err.print("Transaction is being rolled back");
                        connection.rollback();
                    } catch (SQLException excep) {
                        excep.printStackTrace();
                    }
                }
            } finally {
                if (statement != null) try {
                    statement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                if (connection != null) try {
                    connection.setAutoCommit(true);
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        public static ArticuloVO obtenerArticulo(String artic, ComboPooledDataSource pool) {
            Connection connection = null;
            Statement statement = null;
            ResultSet res;
            ArticuloVO a = null;
            try {
                connection = pool.getConnection();
                statement = connection.createStatement();

                String obtener = "SELECT * FROM articulo WHERE nombre = '" + artic + "'";
                res = statement.executeQuery(obtener);

                res.next();

                a = new ArticuloVO(artic,res.getInt("precio"), res.getString("descripcion"), res.getString("rutaImagen"),res.getString("tipo").charAt(0));
    //            String liga = res.getString("requiere_liga");
    //            if (!res.wasNull()) {
    //                a.setLiga(new Liga(liga));
    //            }

            } catch (SQLException e ) {
                e.printStackTrace();
            } finally {
                if (statement != null) try { statement.close(); } catch (SQLException e) {e.printStackTrace();}
                if (connection != null) try { connection.close();} catch (SQLException e) {e.printStackTrace();}
            }
            return a;
        }
    }
}
