package basedatos.dao;

import basedatos.exceptions.ExceptionCampoInvalido;
import basedatos.modelo.PartidaVO;
import basedatos.modelo.UsuarioVO;
import com.mchange.v2.c3p0.ComboPooledDataSource;

import java.sql.*;
import java.util.ArrayList;

public class TorneoDAO {

    public static void apuntarTorneo(UsuarioVO p, TorneoVO t, ComboPooledDataSource pool) throws ExceptionCampoInvalido, SQLException {
        Connection connection = null;
        Statement statement = null;
        try {
            connection = pool.getConnection();
            statement = connection.createStatement();

            UsuarioVO u1;
            UsuarioVO u2;
            ArrayList<UsuarioVO> ual;
            PartidaVO pp;

            ResultSet res = statement.executeQuery("SELECT COUNT(*) total FROM participa_fase p WHERE p.fase_num ="+t.getNumFases() + "AND p.fase_torneo="+t.getId());
            res.next();
            int participantes = res.getInt("total");
            if ((new Timestamp(System.currentTimeMillis()).after(t.getTimeInicio())) && participantes < Math.pow(2,t.getNumFases())) {
                // Cabe un jugador más
                statement.executeUpdate("INSERT INTO participa_fase (usuario, fase_num, fase_torneo) VALUES ('"+ p.getUsername() +"',"+t.getNumFases()+","+t.getId()+")");
                if (participantes + 1 == Math.pow(2,t.getNumFases())) {
                    // El torneo esta lleno, se produce el emparejamiento
                    res = statement.executeQuery("SELECT p.usuario FROM participa_fase p WHERE p.fase_num ="+t.getNumFases() + "AND p.fase_torneo="+t.getId());
                    while (res.next()) {
                        u1 = new UsuarioVO();
                        u1.setUsername(res.getString("usuario"));
                        res.next();
                        u2 = new UsuarioVO();
                        u2.setUsername(res.getString("usuario"));
                        ual = new ArrayList<>();
                        ual.add(u1);
                        ual.add(u2);
                        pp = new PartidaVO(t.getNumFases(),t.getId(),true,ual);
                        PartidaDAO.insertarNuevaPartida(pp,pool);
                    }

                }
            } else {
                throw new ExceptionCampoInvalido("El Torneo ya está lleno o todavía no ha empezado");
            }
        } catch (SQLException e ) {
            if (connection != null) {
                System.err.print("Transaction is being rolled back");
                connection.rollback();
            }
            throw e;
        } finally {
            if (statement != null) try { statement.close(); } catch (SQLException e) {e.printStackTrace();}
            if (connection != null) try { connection.close();} catch (SQLException e) {e.printStackTrace();}
        }
    }
}
