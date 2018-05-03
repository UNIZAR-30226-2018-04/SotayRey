/*
 * Autor: Guerrero Viu, Julia
 * Fecha: 03-05-2018
 * Fichero: SesionDAO.java
 */

package basedatos.dao;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;
import basedatos.modelo.SesionVO;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;

public class SesionDAO {

    public static void crearSesionAbierta (SesionVO s, ComboPooledDataSource pool) throws SQLException {
        Connection connection = null;
        Statement statement = null;
        connection = pool.getConnection();
        statement = connection.createStatement();

        String insert = "INSERT INTO sesion_abierta (usuario, url) VALUES ('" + s.getUsername() + "', '" + s.getUrl() + "')";
        statement.executeUpdate(insert);
        
        if (statement != null) statement.close();
        if (connection != null) connection.close();
    }

    public static String obtenerUrlSesion (String username, ComboPooledDataSource pool) throws  SQLException {
        Connection connection = null;
        Statement statement = null;
        connection = pool.getConnection();
        statement = connection.createStatement();
		connection.setAutoCommit(false);

		String url;

        ResultSet resultSet = statement.executeQuery("SELECT url FROM sesion_abierta WHERE usuario = '" + username + "'");
		if(!resultSet.isBeforeFirst()){
            url = null;
        }
        else {
			resultSet.next();
        	url = resultSet.getString("url");
		}

		String delete = "DELETE FROM sesion_abierta WHERE usuario = '" + username + "'";
        statement.executeUpdate(delete);

		connection.commit();

        if (statement != null) statement.close();
        if (connection != null) {connection.setAutoCommit(true); connection.close();}

        return url;
    }
}
