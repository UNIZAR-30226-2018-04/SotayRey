/*
 * Autor: Guerrero Viu, Julia
 * Fecha: 29-03-2018
 * Fichero: LigaDAO.java
 */

package basedatos.dao;

import java.text.SimpleDateFormat;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import java.beans.PropertyVetoException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;
import java.io.FileInputStream;
import java.util.ArrayList;

import basedatos.exceptions.*;
import basedatos.modelo.*;
import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;

public class LigaDAO {

    public static void crearLiga(LigaVO l, ComboPooledDataSource pool) throws SQLException, ExceptionCampoInvalido {
        Connection connection = null;
        Statement statement = null;
        try {
            connection = pool.getConnection();
            statement = connection.createStatement();

            String prevalues = "INSERT INTO liga (nombre, porcentaje_min, porcentaje_max";
            String postvalues = " VALUES ('" + l.getNombre() + "', " + l.getPorcentajeMin() + ", " + l.getPorcentajeMax();
            if (l.getDescripcion() != null) {
                prevalues = prevalues + ", descripcion";
                postvalues = postvalues + ", '" + l.getDescripcion() + "'";
            }
            prevalues = prevalues + ")";
            postvalues = postvalues + ")";
            statement.executeUpdate(prevalues+postvalues);
        } catch (MySQLIntegrityConstraintViolationException e) {
           throw new ExceptionCampoInvalido("Error de acceso a la base de datos: Liga: " + l.getNombre() + " ya existente");
        } finally {
            if (statement != null) statement.close();
            if (connection != null) connection.close();
        }
    }

    public static LigaVO obtenerDatosLiga(String nombre, ComboPooledDataSource pool) throws SQLException, ExceptionCampoInexistente {
        Connection connection = null;
        Statement statement = null;
        connection = pool.getConnection();
        statement = connection.createStatement();
        String query = "SELECT * FROM liga WHERE nombre = '" + nombre + "'";
        ResultSet resultSet = statement.executeQuery(query);

        if(!resultSet.isBeforeFirst()){
            throw new ExceptionCampoInexistente("Error de acceso a la base de datos: Liga: " + nombre + "  no existente");
        }

        LigaVO l = new LigaVO();
        resultSet.next();
        l.setNombre(resultSet.getString("nombre"));
        l.setDescripcion(resultSet.getString("descripcion"));
        l.setPorcentajeMin(resultSet.getInt("porcentaje_min"));
        l.setPorcentajeMax(resultSet.getInt("porcentaje_max"));

        if (statement != null)  statement.close();
        if (connection != null) connection.close();

        return l;
    }

    public static void eliminarLiga(String nombre, ComboPooledDataSource pool) throws  SQLException {
        Connection connection = null;
        Statement statement = null;
        connection = pool.getConnection();
        statement = connection.createStatement();
        String delete = "DELETE FROM liga WHERE nombre = '" + nombre + "'";
        statement.executeUpdate(delete);
        if (statement != null)  statement.close();
        if (connection != null) connection.close();
    }

    public static void modificarDatosLiga(LigaVO l, ComboPooledDataSource pool) throws SQLException, ExceptionCampoInexistente {
        Connection connection = null;
        Statement statement = null;
        connection = pool.getConnection();

        statement = connection.createStatement();
        String updatepre = "UPDATE liga SET nombre = '" + l.getNombre() + "'";
        String updatepost = " WHERE nombre = " + "'" + l.getNombre() + "'";
        if(l.getDescripcion()!=null) updatepre = updatepre + ", descripcion = '" + l.getDescripcion() + "'";
        if(l.getPorcentajeMin()!=-1) updatepre = updatepre + ", porcentaje_min = '" + l.getPorcentajeMin() + "'";
        if(l.getPorcentajeMax()!=-1) updatepre = updatepre + ", porcentaje_max = '" + l.getPorcentajeMax() + "'";

        if (statement.executeUpdate(updatepre+updatepost) == 0) {
            throw new ExceptionCampoInexistente("Error de acceso a la base de datos: Liga: " + l.getNombre() + "  no existente");
        }

        if (statement != null)  statement.close();
        if (connection != null) connection.close();
    }

    public static ArrayList<LigaVO> obtenerLigas(ComboPooledDataSource pool) throws SQLException, ExceptionCampoInvalido {
        Connection connection = null;
        Statement statement = null;
        connection = pool.getConnection();
        statement = connection.createStatement();
        String query = "SELECT * FROM liga";
        ResultSet resultSet = statement.executeQuery(query);

        ArrayList<LigaVO> lista_ligas = new ArrayList<>();

        while (resultSet.next()) {
            String nombre = resultSet.getString("nombre");
            String descripcion = resultSet.getString("descripcion");
            int porcentajeMin = resultSet.getInt("porcentaje_min");
            int porcentajeMax = resultSet.getInt("porcentaje_max");
            LigaVO l = new LigaVO(nombre, descripcion, porcentajeMin, porcentajeMax);
            lista_ligas.add(l);
        }

        if (statement != null)  statement.close();
        if (connection != null) connection.close();

        return lista_ligas;
    
    }

    public static ArrayList<StatsUsuarioVO> obtenerClasificacionLiga(String nombre, ComboPooledDataSource pool) throws SQLException, ExceptionCampoInvalido{
        Connection connection = null;
        Statement statement = null;
        connection = pool.getConnection();
        statement = connection.createStatement();
        String query = "SELECT u1.username usuario, u1.puntuacion punt FROM usuario u1, pertenece_liga p1" +
                " WHERE u1.username = p1.usuario AND p1.liga = '"+ nombre + "' AND NOT EXISTS " +
                "(SELECT * FROM pertenece_liga p2 WHERE p2.usuario = p1.usuario and p2.timeEntrada > p1.timeEntrada)" +
                " ORDER BY punt DESC";
        ResultSet resultSet = statement.executeQuery(query);

        ArrayList<StatsUsuarioVO> clasificacion = new ArrayList<>();

        while (resultSet.next()) {
            String username = resultSet.getString("usuario");
            StatsUsuarioVO su = new StatsUsuarioVO(username);
            su.setPuntuacion(resultSet.getInt("punt"));
            clasificacion.add(su);
        }

        if (statement != null)  statement.close();
        if (connection != null) connection.close();
        return clasificacion;

    }
}
