package basedatos.dao;

import basedatos.modelo.TorneoPeriodicoVO;
import com.mchange.v2.c3p0.ComboPooledDataSource;

import java.sql.*;
import java.util.ArrayList;

public class TorneoPeriodicoDAO {
    public static void crearTorneoPeriodico(TorneoPeriodicoVO t, ComboPooledDataSource pool) throws SQLException {
        Connection connection = null;
        PreparedStatement statement = null;

        connection = pool.getConnection();

        String evento = "CREATE EVENT `torneo_" + t.getNombre() +
                "` ON SCHEDULE EVERY "+ t.getDias() + " DAY STARTS '"+t.getTimePrimero()+"'" +
                " DO BEGIN DECLARE i INTEGER; DECLARE p INTEGER; DECLARE d INTEGER;" +
                " SET i = "+t.getNumFases()+";" +
                " SET p = "+t.getPremioPuntuacionPrimera()+";" +
                " SET d = "+t.getPremioDivisaPrimera()+";" +
                " INSERT INTO torneo (nombre,timeInicio,individual,descripcion) VALUES ('"+t.getNombre()+"', NOW(),TRUE,'"+t.getDescripcion()+"');" +
                " WHILE i > 0 DO " +
                " INSERT INTO fase (num,torneo,premioPunt,premioDiv) VALUES (i, (SELECT MAX(id) FROM torneo WHERE nombre = '"+t.getNombre()+"'),p,d);" +
                " SET i = i - 1;" +
                " SET p = p*2;" +
                " SET d = d*2;" +
                " END WHILE;" +
                " END";

        statement = connection.prepareStatement(evento);
        statement.executeUpdate();

        if (statement != null) statement.close();
        if (connection != null) connection.close();
    }

    public static void eliminarTorneoPeriodico(String nombreEvento, ComboPooledDataSource pool) throws SQLException {
        Connection connection = null;
        PreparedStatement statement = null;

        connection = pool.getConnection();

        statement = connection.prepareStatement("DROP EVENT IF EXISTS torneo_"+nombreEvento);
        statement.executeUpdate();

        if (statement != null) statement.close();
        if (connection != null) connection.close();
    }

    /* Si existe el evento pero aún no se ha creado ningún torneo en concreto, no rellena todos los campos de TorneoPeriodicoVO */
    public static ArrayList<TorneoPeriodicoVO> obtenerTorneosPeriodicos(ComboPooledDataSource pool) throws SQLException {
        Connection connection = null;
        Statement statement = null;

        connection = pool.getConnection();

        statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SHOW EVENTS WHERE Name LIKE 'torneo_%'");

        ArrayList<TorneoPeriodicoVO> al = new ArrayList<>();


        while(resultSet.next()) {

            Statement statement1 = connection.createStatement();
            ResultSet res2 = statement1.executeQuery("SELECT t.descripcion, f.num, f.premioDiv, f.premioPunt FROM torneo t, fase f WHERE t.nombre='torneo_"+resultSet.getString("Name").substring(7)+"' and f.num = (SELECT COUNT(*) FROM fase f2 where f2.torneo = f.torneo)");
			
            TorneoPeriodicoVO t;
            if (res2.next()) {
                t = new TorneoPeriodicoVO(resultSet.getString("Name").substring(7), res2.getString("descripcion"), resultSet.getTimestamp("Starts"), resultSet.getInt("Interval value"), res2.getInt("num"), res2.getInt("premioPunt"), res2.getInt("premioDiv"));
            } else {
                t = new TorneoPeriodicoVO(resultSet.getString("Name").substring(7), resultSet.getTimestamp("Starts"), resultSet.getInt("Interval value"));
            }
            al.add(t);
        }

        if (statement != null) statement.close();
        if (connection != null) connection.close();
        return al;
    }

}
