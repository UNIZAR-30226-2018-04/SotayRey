import com.mchange.v2.c3p0.ComboPooledDataSource;
import java.beans.PropertyVetoException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;
import java.io.FileInputStream;

public class ConexionBD {

    private static ConexionBD condb;
    private ComboPooledDataSource cpds;

    private ConexionBD() throws IOException, PropertyVetoException {
        //Fichero properties
        Properties dbProps = new Properties();
        dbProps.load(new FileInputStream("bbdd/db.properties"));
        cpds = new ComboPooledDataSource();

        cpds.setDriverClass(dbProps.getProperty("driver")); //loads the jdbc driver
        cpds.setJdbcUrl(dbProps.getProperty("url"));
        cpds.setUser(dbProps.getProperty("user"));
        cpds.setPassword(dbProps.getProperty("password"));

        // the settings below are optional -- c3p0 can work with defaults
        cpds.setMinPoolSize(5);
        cpds.setAcquireIncrement(5);
        cpds.setMaxPoolSize(20);
        cpds.setMaxStatements(180);

    }

    public static ConexionBD instancia() throws IOException, PropertyVetoException {
        if (condb == null) {
            condb = new ConexionBD();
            return condb;
        } else {
            return condb;
        }
    }

    public void crearLiga(String nombre, String desc, int min, int max) {
        Connection connection = null;
        Statement statement = null;
        try {
            connection = this.cpds.getConnection();
            statement = connection.createStatement();
            String insert = "INSERT INTO liga VALUES ('" + nombre + "','" + desc + "'," + min + ")";
            statement.executeUpdate(insert);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (statement != null) try { statement.close(); } catch (SQLException e) {e.printStackTrace();}
            if (connection != null) try { connection.close(); } catch (SQLException e) {e.printStackTrace();}
        }
    }

    public void mostrarLigas() {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            connection = this.cpds.getConnection();
            statement = connection.createStatement();
            String query = "SELECT * FROM liga";
            resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                System.out.println("nombre: " + resultSet.getString("nombre"));
                System.out.println("desc: " + resultSet.getString("descripcion"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (resultSet != null) try { resultSet.close(); } catch (SQLException e) {e.printStackTrace();}
            if (statement != null) try { statement.close(); } catch (SQLException e) {e.printStackTrace();}
            if (connection != null) try { connection.close(); } catch (SQLException e) {e.printStackTrace();}
        }
    }
}
