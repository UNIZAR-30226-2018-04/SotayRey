public class UsuarioDAO {

    public static crear_usuario(Usuario u, ComboPooledDataSource pool) {
        Connection connection = null;
        Statement statement = null;
        try {
            connection = pool.getConnection();
            statement = connection.createStatement();
            String insert = "INSERT INTO usuario VALUES ('" + u.get_username() + "','" + u.get_nombre() + ")";
            statement.executeUpdate(insert);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (statement != null) try { statement.close(); } catch (SQLException e) {e.printStackTrace();}
            if (connection != null) try { connection.close(); } catch (SQLException e) {e.printStackTrace();}
        }
    }
        
}
