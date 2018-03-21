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
            prevalues = prevalues + ")";
            postvalues = postvalues + ")";
            statement.executeUpdate(prevalues+postvalues);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (statement != null) try { statement.close(); } catch (SQLException e) {e.printStackTrace();}
            if (connection != null) try { connection.close(); } catch (SQLException e) {e.printStackTrace();}
        }
    }

    public static UsuarioVO obtenerDatosUsuario(String username, ComboPooledDataSource pool) {
        Connection connection = null;
        Statement statement = null;
        try {
            connection = pool.getConnection();
            statement = connection.createStatement();
            String query = "SELECT * FROM usuario WHERE username = '" + username + "'";
            ResultSet resultSet = statement.executeQuery(query);

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
        
}
