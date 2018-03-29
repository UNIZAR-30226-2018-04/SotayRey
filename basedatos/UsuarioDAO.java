/*
 * Autor: Guerrero Viu, Julia
 * Fecha: 20-03-2018
 * Fichero: UsuarioDAO.java
 */

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

public class UsuarioDAO {

    private static String hashPassword(String password_plaintext) {
        String salt = org.mindrot.jbcrypt.BCrypt.gensalt(15);
        String hashed_password = org.mindrot.jbcrypt.BCrypt.hashpw(password_plaintext, salt);

        return(hashed_password);
    }

    public static void crearUsuario(UsuarioVO u, ComboPooledDataSource pool) {
        Connection connection = null;
        Statement statement = null;
        try {
            connection = pool.getConnection();
            statement = connection.createStatement();

            String prevalues = "INSERT INTO usuario (username, correo, nombre, apellidos, admin";
            String postvalues = " VALUES ('" + u.getUsername() + "', '" + u.getCorreo() + "', '" + u.getNombre() + "', '" + u.getApellidos() + "'";
            if (u.getAdmin()) postvalues = postvalues + ", TRUE";
            else postvalues = postvalues + ", FALSE";
            if (u.getFechaNac() != null) {
                prevalues = prevalues + ", fechaNac";
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                postvalues = postvalues + ", '" + sdf.format(u.getFechaNac()) + "'";
            }
            prevalues = prevalues + ", fb_auth";
            if (u.getPlaintextPassword() == null) {
                postvalues = postvalues + ", 1";
            } else {
                prevalues = prevalues + ", pw_hash";
                postvalues = postvalues + ", 0,'" + hashPassword(u.getPlaintextPassword()) + "'";
            }
            prevalues = prevalues + ")";
            postvalues = postvalues + ")";
            statement.executeUpdate(prevalues+postvalues);
            
            //Buscar la liga más baja
            String query = "SELECT nombre FROM liga WHERE porcentaje_max = 100";
            resultSet = statement.executeQuery(query);

            if(!resultSet.isBeforeFirst()){
                throw new ExceptionCampoInexistente("Error de acceso a la base de datos: No existe ninguna liga para los nuevos usuarios");
            }
            resultSet.next();
            String ligaMin = resultSet.getString("nombre");
            // Insertar el nuevo usuario en la liga más baja
            String insert_liga = "INSERT INTO pertenece_liga (usuario, liga) VALUES ('"+ u.getUsername() + "', '" + ligaMin + "')";
            statement.executeUpdate(insert_liga);

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (statement != null) try { statement.close(); } catch (SQLException e) {e.printStackTrace();}
            if (connection != null) try { connection.close(); } catch (SQLException e) {e.printStackTrace();}
        }
    }

    public static boolean autentificarUsuario(String username, String plaintextPassword, ComboPooledDataSource pool) throws  ExceptionCampoInexistente {
        boolean password_verified = false;
        Connection connection = null;
        Statement statement = null;
        try {
            connection = pool.getConnection();
            statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT pw_hash FROM usuario WHERE username = '" + username + "'");
            resultSet.next();
            String stored_hash = resultSet.getString("pw_hash");
            if(null == stored_hash) {
                throw new ExceptionCampoInexistente("El usuario " + username + " no tiene password");
            }
            password_verified = org.mindrot.jbcrypt.BCrypt.checkpw(plaintextPassword, stored_hash);

            return(password_verified);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (statement != null) try { statement.close(); } catch (SQLException e) {e.printStackTrace();}
            if (connection != null) try { connection.close(); } catch (SQLException e) {e.printStackTrace();}
        }
        return password_verified;
    }

    public static UsuarioVO obtenerDatosUsuario(String username, ComboPooledDataSource pool) throws ExceptionCampoInexistente{
        Connection connection = null;
        Statement statement = null;
        try {
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
            return u;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } finally {
            if (statement != null) try { statement.close(); } catch (SQLException e) {e.printStackTrace();}
            if (connection != null) try { connection.close(); } catch (SQLException e) {e.printStackTrace();}
        }
    }

    /* 
     * No elimina al usuario por completo, solo le impide el acceso poniendo los campos de autenticación: pw_hash y fb_token a null 
     */
    public static void eliminarUsuario(String username, ComboPooledDataSource pool){
        Connection connection = null;
        Statement statement = null;
        try {
            connection = pool.getConnection();
            statement = connection.createStatement();
            String delete = "UPDATE usuario SET pw_hash = null, fb_auth = 0 WHERE username = " + "'" + username + "'";
            statement.executeUpdate(delete);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (statement != null) try { statement.close(); } catch (SQLException e) {e.printStackTrace();}
            if (connection != null) try { connection.close(); } catch (SQLException e) {e.printStackTrace();}
        }
    }

    public static void modificarDatosUsuario(UsuarioVO u, ComboPooledDataSource pool){
        Connection connection = null;
        Statement statement = null;
        try {
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
            

            statement.executeUpdate(updatepre+updatepost);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (statement != null) try { statement.close(); } catch (SQLException e) {e.printStackTrace();}
            if (connection != null) try { connection.close(); } catch (SQLException e) {e.printStackTrace();}
        }
    }

    public static boolean esAdministrador(String username, ComboPooledDataSource pool) throws ExceptionCampoInexistente{
        Connection connection = null;
        Statement statement = null;
        try {
            connection = pool.getConnection();
            statement = connection.createStatement();
            String query = "SELECT admin FROM usuario WHERE username = '" + username + "'";
            ResultSet resultSet = statement.executeQuery(query);
            
            if(!resultSet.isBeforeFirst()){
                throw new ExceptionCampoInexistente("Error de acceso a la base de datos: Usuario: " + username + "  no existente");
            }

            resultSet.next();
            return resultSet.getBoolean("admin");
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            if (statement != null) try { statement.close(); } catch (SQLException e) {e.printStackTrace();}
            if (connection != null) try { connection.close(); } catch (SQLException e) {e.printStackTrace();}
        }
    }
        
}
