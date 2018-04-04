/*
 * Autor: Sergio Izquierdo Barranco
 * Fecha: 22-03-18
 * Fichero: Acceso a Datos de Artículo
 */

package basedatos.dao;

import basedatos.exceptions.ExceptionCampoInexistente;
import basedatos.exceptions.ExceptionCampoInvalido;
import com.mchange.v2.c3p0.ComboPooledDataSource;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import basedatos.modelo.*;
import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;


public class ArticuloDAO {
    public static void crearArticulo(ArticuloVO a, ComboPooledDataSource pool) throws SQLException, ExceptionCampoInvalido {
        Connection connection = null;
        Statement statement = null;
        try {
            connection = pool.getConnection();
            statement = connection.createStatement();

            String insertArticulo;

            if (a.getRequiere() == null) {
                insertArticulo = "INSERT INTO articulo (nombre,precio,descripcion,rutaImagen,tipo) VALUES ('" + a.getNombre() + "'," + a.getPrecio() + ",'" + a.getDescripcion() + "','" + a.getRutaImagen() + "','" + a.getTipo() + "')";
            } else {
                insertArticulo = "INSERT INTO articulo (nombre,precio,descripcion,rutaImagen,tipo,requiere_liga) VALUES ('" + a.getNombre() + "'," + a.getPrecio() + ",'" + a.getDescripcion() + "','" + a.getRutaImagen() + "','" + a.getTipo() + "','" + a.getRequiere().getNombre() + "')";
            }
            //Comienza transacción
            statement.executeUpdate(insertArticulo);
        } catch (MySQLIntegrityConstraintViolationException e) {
            throw new ExceptionCampoInvalido("Ya existe el articulo(" + a.getNombre() + ")");
        }

        if (statement != null) statement.close();
        if (connection != null) connection.close();
    }

    public static void eliminarArticulo(ArticuloVO a, ComboPooledDataSource pool) throws SQLException {
        Connection connection = null;
        Statement statement = null;

        connection = pool.getConnection();
        statement = connection.createStatement();

        String deleteArticulo = "DELETE FROM articulo WHERE nombre = '"+a.getNombre() + "'";
        statement.executeUpdate(deleteArticulo);

        if (statement != null) statement.close();
        if (connection != null) connection.close();
    }

    public static void modificarArticulo(ArticuloVO a, ComboPooledDataSource pool) throws SQLException, ExceptionCampoInexistente {
        Connection connection = null;
        Statement statement = null;
            connection = pool.getConnection();
            statement = connection.createStatement();;
            String modificaArticulo;

            if (a.getRequiere() == null) {
            modificaArticulo = "UPDATE articulo SET " + "precio = " + a.getPrecio() + ", descripcion = '" + a.getDescripcion() + "', rutaImagen = '" + a.getRutaImagen() + "', tipo = '" + a.getTipo() + "' WHERE nombre = '" + a.getNombre() + "'";
            } else {
            modificaArticulo = "UPDATE articulo SET " + "requiere_liga = '" + a.getRequiere().getNombre() + "', precio = " + a.getPrecio() + ", descripcion = '" + a.getDescripcion() + "', rutaImagen = '" + a.getRutaImagen() + "', tipo = '" + a.getTipo() + "' WHERE nombre = '" + a.getNombre() + "'";
            }

            //Comienza transacción
            if (statement.executeUpdate(modificaArticulo) == 0) {
                throw new ExceptionCampoInexistente("El articulo(" + a.getNombre() + ") no existe");
            }

            if (statement != null) statement.close();
            if (connection != null) connection.close();
    }

    public static ArticuloVO obtenerArticulo(String artic, ComboPooledDataSource pool) throws ExceptionCampoInexistente, SQLException {
        Connection connection = null;
        Statement statement = null;
        ResultSet res;
        ArticuloVO a = null;

        connection = pool.getConnection();
        statement = connection.createStatement();

        String obtener = "SELECT * FROM articulo WHERE nombre = '" + artic + "'";
        res = statement.executeQuery(obtener);

        if (res.next()) {
            a = new ArticuloVO(artic, res.getInt("precio"), res.getString("descripcion"), res.getString("rutaImagen"), res.getString("tipo").charAt(0));
            String liga = res.getString("requiere_liga");
            if (!res.wasNull()) {
                LigaVO l = new LigaVO();
                l.setNombre(liga);
                a.setRequiere(l);
            }
        } else {
            throw new ExceptionCampoInexistente("El artículo (" + artic + ") no existe");
        }
        if (statement != null) statement.close();
        if (connection != null) connection.close();
        return a;
    }
}

