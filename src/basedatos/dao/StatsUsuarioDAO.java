/*
 * Autor: Guerrero Viu, Julia
 * Fecha: 21-03-2018
 * Fichero: StatsUsuarioDAO.java
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

import basedatos.modelo.*;
import basedatos.exceptions.*;

public class StatsUsuarioDAO {
    
    /*
     * Modifica las stats principales (puntuacion y divisa) del usuario stats.getUsername()
     */
    public static void modificarStatsUsuario(StatsUsuarioVO stats, ComboPooledDataSource pool) throws SQLException, ExceptionCampoInexistente {
        Connection connection = null;
        Statement statement = null;
        connection = pool.getConnection();
        statement = connection.createStatement();
        String updatepre = "UPDATE usuario SET username = '" + stats.getUsername() + "'";
        String updatepost = " WHERE username = " + "'" + stats.getUsername() + "'";
        if(stats.getPuntuacion()!=-1) updatepre = updatepre + ", puntuacion = " + stats.getPuntuacion();
        if(stats.getDivisa()!=-1) updatepre = updatepre + ", divisa = " + stats.getDivisa();

        if(statement.executeUpdate(updatepre+updatepost) == 0){
            throw new ExceptionCampoInexistente("Error de acceso a la base de datos: Usuario: " + stats.getUsername() + "  inexistente");
        }

        if (statement != null) statement.close();
        if (connection != null) connection.close();
    }    

    /* 
     * Devuelve las stats principales (puntuacion y divisa) del usuario username
     */
    public static StatsUsuarioVO obtenerStatsUsuario(String username, ComboPooledDataSource pool) throws SQLException, ExceptionCampoInvalido, ExceptionCampoInexistente {
        Connection connection = null;
        Statement statement = null;
        connection = pool.getConnection();
        statement = connection.createStatement();
        String query = "SELECT * FROM usuario WHERE username = '" + username + "'";
        ResultSet resultSet = statement.executeQuery(query);

        if(!resultSet.isBeforeFirst()){
            throw new ExceptionCampoInexistente("Error de acceso a la base de datos: Usuario: " + username + "  inexistente");
        }

        StatsUsuarioVO su = new StatsUsuarioVO(username);
        resultSet.next();
        su.setPuntuacion(resultSet.getInt("puntuacion"));
        su.setDivisa(resultSet.getInt("divisa"));

        if (statement != null) statement.close();
        if (connection != null) connection.close();

        return su;
    }

    /*
     * Devuelve TODAS las Stats del usuario username
     */
    public static StatsUsuarioVO obtenerTodasStatsUsuario(String username, ComboPooledDataSource pool) throws SQLException, ExceptionCampoInvalido, ExceptionCampoInexistente {
        Connection connection = null;
        Statement statement = null;
        connection = pool.getConnection();
        statement = connection.createStatement();
        connection.setAutoCommit(false);
        String query = "SELECT * FROM usuario WHERE username = '" + username + "'";
        ResultSet resultSet = statement.executeQuery(query);

        if(!resultSet.isBeforeFirst()){
            throw new ExceptionCampoInexistente("Error de acceso a la base de datos: Usuario: " + username + "  inexistente");
        }

        StatsUsuarioVO su = new StatsUsuarioVO(username);
        resultSet.next();
        su.setPuntuacion(resultSet.getInt("puntuacion"));
        su.setDivisa(resultSet.getInt("divisa"));

        // Obtener la liga actual, la última a la que ha entrado
        query = "SELECT p1.liga ligaActual FROM pertenece_liga p1 WHERE usuario = '"+ username + "'" +
                " AND NOT EXISTS (SELECT * FROM pertenece_liga p2 WHERE usuario = '" + username + "' AND p2.timeEntrada > p1.timeEntrada)";
        resultSet = statement.executeQuery(query);

        if(!resultSet.isBeforeFirst()){
            throw new ExceptionCampoInexistente("Error de acceso a la base de datos: Usuario: " + username + "  no registrado en ninguna liga");
        }
        resultSet.next();
        su.setLigaActual(resultSet.getString("ligaActual"));

        // Obtener el puesto
        query = " SELECT rank puesto FROM (SELECT @rank:=@rank+1 AS rank, user FROM" +
                "(SELECT u1.username user, u1.puntuacion FROM pertenece_liga p1, usuario u1" +
                " WHERE liga = '"+ su.getLigaActual() + "' AND p1.usuario = u1.username" +
                " AND NOT EXISTS (SELECT * FROM pertenece_liga p2 WHERE p2.usuario = p1.usuario AND p2.timeEntrada > p1.timeEntrada)" +
                " ORDER BY puntuacion DESC) t, (SELECT @rank:=0) r) a" +
                " WHERE user = '" + su.getUsername() + "'";
        resultSet = statement.executeQuery(query);

        if(!resultSet.isBeforeFirst()){
            throw new ExceptionCampoInexistente("Error de acceso a la base de datos: Usuario: " + username + "  no registrado en ninguna liga");
        }
        resultSet.next();
        su.setPuesto(resultSet.getInt("puesto"));

        // Obtener la liga máxima en la que ha estado (puede ser que salga más de una vez la misma)
        query = "SELECT l.nombre ligaMax FROM pertenece_liga p, liga l WHERE p.usuario = '" + username + "'" +
                " AND p.liga = l.nombre AND NOT EXISTS (SELECT * FROM pertenece_liga p2, liga l2 WHERE " +
                "p2.usuario = '" + username + "' AND p2.liga = l2.nombre AND l2.porcentaje_min < l.porcentaje_min)";
        resultSet = statement.executeQuery(query);
        if(!resultSet.isBeforeFirst()){
            throw new ExceptionCampoInexistente("Error de acceso a la base de datos: Usuario: " + username + "  no registrado en ninguna liga");
        }
        resultSet.next();
        su.setLigaMaxima(resultSet.getString("ligaMax"));

        // Obtener partidas ganadas por el usuario username
        query = "SELECT COUNT(*) ganadas FROM juega j, partida p WHERE j.usuario = '" + username +
                "' AND j.partida = p.id AND j.equipo = p.ganador";
        resultSet = statement.executeQuery(query);
        if(!resultSet.isBeforeFirst())
            su.setGanadas(0);
        else{
            resultSet.next();
            su.setGanadas(resultSet.getInt("ganadas"));
        }

        // Obtener partidas perdidas por el usuario username
        query = "SELECT COUNT(*) perdidas FROM juega j, partida p WHERE j.usuario = '" + username +
                "' AND j.partida = p.id AND j.equipo != p.ganador AND p.ganador != 'A' AND p.ganador is not null";
        resultSet = statement.executeQuery(query);
        if(!resultSet.isBeforeFirst())
            su.setPerdidas(0);
        else{
            resultSet.next();
            su.setPerdidas(resultSet.getInt("perdidas"));
        }

        // Obtener partidas en las que abandonaron al usuario username
        query = "SELECT COUNT(*) teAbandonaron FROM juega j, partida p WHERE j.usuario = '" + username +
                "' AND j.partida = p.id AND p.ganador = 'A' AND j.abandonador = FALSE";
        resultSet = statement.executeQuery(query);
        if(!resultSet.isBeforeFirst())
            su.setTeAbandonaron(0);
        else{
            resultSet.next();
            su.setTeAbandonaron(resultSet.getInt("teAbandonaron"));
        }

        // Obtener partidas abandonadas por el usuario username
        query = "SELECT COUNT(*) abandonadas FROM juega j WHERE j.usuario = '" + username +
                "' AND j.abandonador = TRUE";
        resultSet = statement.executeQuery(query);
        if(!resultSet.isBeforeFirst())
            su.setAbandonaste(0);
        else{
            resultSet.next();
            su.setAbandonaste(resultSet.getInt("abandonadas"));
        }

        connection.commit();
        if (statement != null) statement.close();
        if (connection != null) {connection.setAutoCommit(true); connection.close();}

        return su;
    }
        
}
