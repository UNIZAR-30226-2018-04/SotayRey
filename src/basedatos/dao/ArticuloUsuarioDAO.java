
/*
 * Autor: Sergio Izquierdo Barranco
 * Fecha: 22-03-18
 * Fichero: Acceso a Datos de ArtículoUsuario
 */

package basedatos.dao;

import basedatos.exceptions.ExceptionCampoInexistente;
import basedatos.modelo.ArticuloUsuarioVO;
import basedatos.modelo.LigaVO;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;


public class ArticuloUsuarioDAO {
    public static ArrayList<ArticuloUsuarioVO> obtenerArticulosUsuario(String username, ComboPooledDataSource pool) throws  SQLException {
        Connection connection;
        Statement statement;
        ResultSet res;
        ArrayList<ArticuloUsuarioVO> result = new ArrayList<>();
        connection = pool.getConnection();
        statement = connection.createStatement();

        String obtener = "SELECT * FROM articulo a, posee p WHERE p.usuario = '" + username + "' AND p.articulo = a.nombre";
        res = statement.executeQuery(obtener);

        while(res.next()) {
            ArticuloUsuarioVO a = new ArticuloUsuarioVO(res.getString("nombre"), res.getInt("precio"), res.getString("descripcion"), res.getString("rutaImagen"), res.getString("tipo").charAt(0), true, res.getBoolean("preferido"), true, username);
            String liga = res.getString("requiere_liga");
            if (!res.wasNull()) {
                LigaVO l = new LigaVO();
                l.setNombre(liga);
                a.setRequiere(l);
            }
            result.add(a);
        }
        if (statement != null) statement.close();
        if (connection != null) connection.close();
        return result;
    }

    public static void crearArticuloUsuario(ArticuloUsuarioVO a, ComboPooledDataSource pool) throws ExceptionCampoInexistente, SQLException {
        Connection connection = pool.getConnection();
        Statement statement = connection.createStatement();
        // Eliminar el anterior favorito del usuario
        try {
            statement.executeUpdate("INSERT INTO posee (usuario, articulo, preferido) VALUES ('" + a.getUsername() + "','" + a.getNombre() + "'," + (a.isFavorito() ? 1 : 0) + ") ON DUPLICATE KEY UPDATE preferido = " + (a.isFavorito() ? 1 : 0));
            if (a.isFavorito()) {
                statement.executeUpdate("UPDATE posee p, articulo a SET p.preferido = 0 WHERE p.articulo = a.nombre AND a.nombre != '" + a.getNombre() + "' AND p.usuario = '" + a.getUsername() + "' AND a.tipo = '" + a.getTipo() + "'");
            }
        } catch (MySQLIntegrityConstraintViolationException e) {
            throw new ExceptionCampoInexistente("Error de acceso a la base de datos: Articulo:" + a.getNombre() + " o Usuario: " + a.getUsername() + " no existente");
        } finally {
            if (statement != null) statement.close();
            if (connection != null) connection.close();
        }
    }

    public static ArrayList<ArticuloUsuarioVO> obtenerArticulosTienda(String username, ComboPooledDataSource pool) throws  SQLException, ExceptionCampoInexistente {
        Connection connection;
        Statement statement;
        ResultSet res;
        ArrayList<ArticuloUsuarioVO> result = new ArrayList<>();
        connection = pool.getConnection();
        statement = connection.createStatement();

        try {
            String obtener = "(SELECT aa.nombre, aa.precio, aa.descripcion, aa.rutaImagen, aa.tipo, aa.requiere_liga, coalesce(pp.preferido, '0') as preferido, coalesce(pp.preferido*0+1, '0') as comprado, coalesce(aa.porcentaje_min>=(SELECT MIN(lig.porcentaje_min) FROM pertenece_liga pert, liga lig WHERE pert.liga = lig.nombre AND pert.usuario = '" + username + "'), '1') as disponible FROM (SELECT a.nombre, a.precio, a.descripcion, a.rutaImagen, a.tipo, a.requiere_liga, l.porcentaje_min FROM articulo a LEFT JOIN liga l ON l.nombre=a.requiere_liga) aa LEFT JOIN (SELECT * FROM posee p WHERE p.usuario = '" + username + "') pp ON aa.nombre = pp.articulo)";
            res = statement.executeQuery(obtener);

            while (res.next()) {
                ArticuloUsuarioVO a = new ArticuloUsuarioVO(res.getString("nombre"), res.getInt("precio"), res.getString("descripcion"), res.getString("rutaImagen"), res.getString("tipo").charAt(0), res.getBoolean("disponible"), res.getBoolean("preferido"), res.getBoolean("comprado"), username);
                String liga = res.getString("requiere_liga");
                if (!res.wasNull()) {
                    LigaVO l = new LigaVO();
                    l.setNombre(liga);
                    a.setRequiere(l);
                }
                result.add(a);
            }
        } catch (MySQLIntegrityConstraintViolationException e) {
            throw new ExceptionCampoInexistente("Error de acceso a la base de datos: Usuario: " + username + " no existente");

        } finally {
            if (statement != null) statement.close();
            if (connection != null) connection.close();
        }
        return result;
    }
}
