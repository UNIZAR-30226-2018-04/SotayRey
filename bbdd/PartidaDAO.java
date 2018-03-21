/*
 * Autor: Sergio Izquierdo Barranco
 * Fecha: 20-03-18
 * Fichero: Objeto PartidaDAO con las funciones para interaccionar con la base de datos
 */

import java.math.BigInteger;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.Statement;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import java.text.SimpleDateFormat;

public class PartidaDAO {
    public static void insertarNuevaPartida(PartidaVO p, ComboPooledDataSource pool) {
        Connection connection = null;
        Statement statement = null;
        try {
            connection = pool.getConnection();
            statement = connection.createStatement();
            connection.setAutoCommit(false);

            //Comienza transacción
            SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String insertPartida = "INSERT INTO partida (timeInicio, publica) VALUES ('" +
                    sd.format(p.getTimeInicio()) + "'," + p.isPublica() + ")";

            statement.executeUpdate(insertPartida);

            //Conseguir id partida
            ResultSet res = statement.executeQuery("SELECT LAST_INSERT_ID()");
            res.next();
            String p_id = res.getString("LAST_INSERT_ID()");
            p.setId(new BigInteger(p_id));

            // Recorremos los usuarios e insertamos la relacion juega
            for (int i = 0; i<p.getUsuarios().size(); i++) {
                String insertJuega = "INSERT INTO juega (usuario, partida, equipo) VALUES ('" +
                                     p.getUsuarios().get(i).getNombre() + "'," + p_id + ",'" + (i%2)+1 +"')";
                statement.executeUpdate(insertJuega);
            }
            connection.commit();


        } catch (SQLException e ) {
            e.printStackTrace();
            if (connection != null) {
                try {
                    System.err.print("Transaction is being rolled back");
                    connection.rollback();
                } catch(SQLException excep) {
                    excep.printStackTrace();
                }
            }
        } finally {
            if (statement != null) try { statement.close(); } catch (SQLException e) {e.printStackTrace();}
            if (connection != null) try { connection.close(); connection.setAutoCommit(true);} catch (SQLException e) {e.printStackTrace();}
        }
    }

    public static void finalizarPartida(PartidaVO p, ComboPooledDataSource pool) {
        Connection connection = null;
        Statement statement = null;
        try {
            connection = pool.getConnection();
            statement = connection.createStatement();
            connection.setAutoCommit(false);

            //Comienza transacción
            SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String updatePartida = "UPDATE partida SET timeFin = '" +
                    sd.format(p.getTimeFin()) + "', SET ganador = '" + p.getGanador() + "' WHERE id == " +
                    p.getId().toString();

            statement.executeUpdate(updatePartida);

            // Recorremos los usuarios e insertamos la relacion juega
            for (int i = 0; i<p.getUsuarios().size(); i++) {
                int puntos = 0;
                if (i%2 == 0) { puntos = p.getPuntos1(); }
                String updateJuega = "UPDATE juega SET puntos = " + puntos +
                        ", veintes = " + p.getVeintes().get(i) +
                        ", cuarentas = " + p.getCuarentas().get(i) +
                        ", abandonador = " + (p.getAbandonador()==i+1) +
                        " WHERE partida == " + p.getId() +
                        " AND jugador == '" + p.getUsuarios().get(i).getUsername() + "'";
                statement.executeUpdate(updateJuega);
            }
            connection.commit();


        } catch (SQLException e ) {
            e.printStackTrace();
            if (connection != null) {
                try {
                    System.err.print("Transaction is being rolled back");
                    connection.rollback();
                } catch(SQLException excep) {
                    excep.printStackTrace();
                }
            }
        } finally {
            if (statement != null) try { statement.close(); } catch (SQLException e) {e.printStackTrace();}
            if (connection != null) try { connection.close(); connection.setAutoCommit(true);} catch (SQLException e) {e.printStackTrace();}
        }
    }
}
