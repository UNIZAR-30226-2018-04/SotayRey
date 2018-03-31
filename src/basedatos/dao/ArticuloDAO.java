/*
 * Autor: Sergio Izquierdo Barranco
 * Fecha: 22-03-18
 * Fichero: Acceso a Datos de Artículo
 */

package basedatos.dao;

import com.mchange.v2.c3p0.ComboPooledDataSource;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import basedatos.modelo.*;



public class ArticuloDAO {
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

