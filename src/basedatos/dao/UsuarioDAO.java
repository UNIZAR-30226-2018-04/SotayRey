/*
 * Autor: Guerrero Viu, Julia
 * Fecha: 20-03-2018
 * Fichero: UsuarioDAO.java
 */

package basedatos.dao;

import basedatos.BCrypt;
import basedatos.exceptions.ExceptionCampoInexistente;
import basedatos.modelo.UsuarioVO;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;

public class UsuarioDAO {

    private static String hashPassword(String password_plaintext) {
        String salt = BCrypt.gensalt(15);

        return(BCrypt.hashpw(password_plaintext, salt));
    }

    public static void crearUsuario(UsuarioVO u, ComboPooledDataSource pool) throws SQLException, ExceptionCampoInexistente {
        Connection connection = null;
        Statement statement = null;
        try {
            connection = pool.getConnection();
            statement = connection.createStatement();
            connection.setAutoCommit(false);

            String prevalues = "INSERT INTO usuario (username, correo, nombre, apellidos, admin";
            String postvalues = " VALUES ('" + u.getUsername() + "', '" + u.getCorreo() + "', '" + u.getNombre() + "', '" + u.getApellidos() + "'";
            if (u.getAdmin()) postvalues = postvalues + ", TRUE";
            else postvalues = postvalues + ", FALSE";
            if (u.getFechaNac() != null) {
                prevalues = prevalues + ", fechaNac";
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                postvalues = postvalues + ", '" + sdf.format(u.getFechaNac()) + "'";
            }
            prevalues = prevalues + ", fb_auth, pw_hash ";
            if (u.getPlaintextPassword() == null) {
                postvalues = postvalues + ",'" + u.getFb_auth() + "',NULL";
            } else {
                postvalues = postvalues + ", NULL,'" + hashPassword(u.getPlaintextPassword()) + "'";
            }
            prevalues = prevalues + ")";
            postvalues = postvalues + ")";
            statement.executeUpdate(prevalues + postvalues);

            //Buscar la liga más baja
            String query = "SELECT nombre FROM liga WHERE porcentaje_max = 100";
            ResultSet resultSet = statement.executeQuery(query);

            if (!resultSet.isBeforeFirst()) {
                throw new ExceptionCampoInexistente("Error de acceso a la base de datos: No existe ninguna liga para los nuevos usuarios");
            }
            resultSet.next();
            String ligaMin = resultSet.getString("nombre");
            // Insertar el nuevo usuario en la liga más baja
            String insert_liga = "INSERT INTO pertenece_liga (usuario, liga) VALUES ('" + u.getUsername() + "', '" + ligaMin + "')";
            statement.executeUpdate(insert_liga);

            connection.commit();

        } catch (MySQLIntegrityConstraintViolationException e) {
            throw new ExceptionCampoInexistente("Error de acceso a la base de datos: Usuario: " + u.getUsername() + " ya existente");
        } finally {
            if (statement != null) statement.close();
            if (connection != null) connection.close();
        }
    }

    public static boolean autentificarUsuario(String username, String plaintextPassword, ComboPooledDataSource pool) throws  SQLException, ExceptionCampoInexistente  {
        boolean password_verified = false;
        Connection connection = null;
        Statement statement = null;
        connection = pool.getConnection();
        statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT pw_hash FROM usuario WHERE username = '" + username + "'");
        resultSet.next();
        String stored_hash = resultSet.getString("pw_hash");

        if(null == stored_hash) {
            throw new ExceptionCampoInexistente("El usuario " + username + " no tiene password");
        }

        if (statement != null) statement.close();
        if (connection != null) connection.close();

        return  BCrypt.checkpw(plaintextPassword, stored_hash);
    }



    public static UsuarioVO obtenerDatosUsuario(String username, ComboPooledDataSource pool) throws SQLException, ExceptionCampoInexistente {
        Connection connection = null;
        Statement statement = null;
        connection = pool.getConnection();
        statement = connection.createStatement();
        String query = "SELECT * FROM usuario WHERE username = '" + username + "'";
        ResultSet resultSet = statement.executeQuery(query);

        if(!resultSet.isBeforeFirst()){
            throw new ExceptionCampoInexistente("Error de acceso a la base de datos: Usuario: " + username + "  no existente");
        }

        UsuarioVO u = new UsuarioVO();
        resultSet.next();
        u.setUsername(resultSet.getString("username"));
        u.setCorreo(resultSet.getString("correo"));
        u.setNombre(resultSet.getString("nombre"));
        u.setApellidos(resultSet.getString("apellidos"));
        u.setAdmin(resultSet.getBoolean("admin"));
        u.setFechaNac(resultSet.getDate("fechaNac"));
        u.setTimeCreacion(resultSet.getTimestamp("timeCreacion"));

        if (statement != null) statement.close();
        if (connection != null) connection.close();

        return u;
    }

    /* 
     * No elimina al usuario por completo, solo le impide el acceso poniendo los campos de autenticación: pw_hash y fb_token a null 
     */
    public static void eliminarUsuario(String username, ComboPooledDataSource pool) throws  SQLException, ExceptionCampoInexistente {
        Connection connection = null;
        Statement statement = null;
        connection = pool.getConnection();
        statement = connection.createStatement();
        String delete = "UPDATE usuario SET pw_hash = null, fb_auth = 0 WHERE username = " + "'" + username + "'";
        if(statement.executeUpdate(delete) == 0) {
            throw new ExceptionCampoInexistente("Error de acceso a la base de datos: Usuario: " + username + " no existente");
        }
        if (statement != null) statement.close();
        if (connection != null) connection.close();
    }

    public static void modificarDatosUsuario(UsuarioVO u, ComboPooledDataSource pool) throws ExceptionCampoInexistente, SQLException {
        Connection connection = null;
        Statement statement = null;
        connection = pool.getConnection();
        statement = connection.createStatement();
        String updatepre = "UPDATE usuario SET";
        String updatepost = " WHERE username = " + "'" + u.getUsername() + "'";
        if(u.getAdmin()) updatepre = updatepre + " admin = TRUE";
        else updatepre = updatepre + " admin = FALSE";
        if(u.getCorreo()!=null) updatepre = updatepre + ", correo = '" + u.getCorreo() + "'";
        if(u.getNombre()!=null) updatepre = updatepre + ", nombre = '" + u.getNombre() + "'";
        if(u.getApellidos()!=null) updatepre = updatepre + ", apellidos = '" + u.getApellidos() + "'";
        if(u.getFechaNac()!=null) updatepre = updatepre + ", fechaNac = '" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(u.getFechaNac()) + "'";
        if(u.getPlaintextPassword()!= null) updatepre = updatepre + ", pw_hash = '" + UsuarioDAO.hashPassword(u.getPlaintextPassword()) + "'";
        if(statement.executeUpdate(updatepre+updatepost) == 0){
            throw new ExceptionCampoInexistente("Error de acceso a la base de datos: Usuario: " + u.getUsername() + " no existente");
        }

        if (statement != null) statement.close();
        if (connection != null) connection.close();
    }

    public static boolean esAdministrador(String username, ComboPooledDataSource pool) throws SQLException, ExceptionCampoInexistente{
        Connection connection = null;
        Statement statement = null;
        boolean admin = false;
        connection = pool.getConnection();
        statement = connection.createStatement();
        String query = "SELECT admin FROM usuario WHERE username = '" + username + "'";
        ResultSet resultSet = statement.executeQuery(query);

        if(!resultSet.isBeforeFirst()){
            throw new ExceptionCampoInexistente("Error de acceso a la base de datos: Usuario: " + username + " no existente");
        }
        else{
            resultSet.next();
            admin = resultSet.getBoolean("admin");
        }
        
        if (statement != null) statement.close();
        if (connection != null) connection.close();
        return admin;
    }
        
}
