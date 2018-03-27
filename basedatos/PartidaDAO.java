/*
 * Autor: Sergio Izquierdo Barranco
 * Fecha: 20-03-18
 * Fichero: Objeto PartidaDAO con las funciones para interaccionar con la base de datos
 */

import java.math.BigInteger;
import java.sql.*;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class PartidaDAO {
    public static void insertarNuevaPartida(PartidaVO p, ComboPooledDataSource pool) {
        Connection connection = null;
        Statement statement = null;
        try {
            connection = pool.getConnection();
            statement = connection.createStatement();
            connection.setAutoCommit(false);

            //Comienza transacción
            SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String insertPartida = "INSERT INTO partida (timeInicio, publica) VALUES ('" +
                    sd.format(p.getTimeInicio()) + "'," + p.isPublica() + ")";

            statement.executeUpdate(insertPartida);

            //Conseguir id partida
            ResultSet res = statement.executeQuery("SELECT LAST_INSERT_ID()");
            res.next();
            String p_id = res.getString("LAST_INSERT_ID()");
            p.setId(new BigInteger(p_id));

            // Recorremos los usuarios e insertamos la relacion juega
            for (int i = 0; i<p.getUsuarios().size(); i++) {
                String insertJuega = "INSERT INTO juega (usuario, partida, equipo) VALUES ('" +
                                     p.getUsuarios().get(i).getUsername() + "'," + p_id + ",'" + (char)(((i%2)+1)+'0') +"')";
                statement.executeUpdate(insertJuega);
            }
            connection.commit();


        } catch (SQLException e ) {
            e.printStackTrace();
            if (connection != null) {
                try {
                    System.err.print("Transaction is being rolled back");
                    connection.rollback();
                } catch(SQLException excep) {
                    excep.printStackTrace();
                }
            }
        } finally {
            if (statement != null) try { statement.close(); } catch (SQLException e) {e.printStackTrace();}
            if (connection != null) try { connection.setAutoCommit(true); connection.close();} catch (SQLException e) {e.printStackTrace();}
        }
    }

    public static void finalizarPartida(PartidaVO p, ComboPooledDataSource pool) {
        Connection connection = null;
        Statement statement = null;
        try {
            connection = pool.getConnection();
            statement = connection.createStatement();
            connection.setAutoCommit(false);

            //Comienza transacción
            SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String updatePartida = "UPDATE partida SET timeFin = '" +
                    sd.format(p.getTimeFin()) + "', ganador = '" + p.getGanador() + "' WHERE id = " +
                    p.getId().toString();

            statement.executeUpdate(updatePartida);

            // Recorremos los usuarios e insertamos la relacion juega
            for (int i = 0; i<p.getUsuarios().size(); i++) {
                int puntos;
                if (i%2 == 0) { puntos = p.getPuntos1(); }
                else {puntos = p.getPuntos2(); }
                String updateJuega = "UPDATE juega SET puntos = " + puntos +
                        ", veintes = " + p.getVeintes().get(i) +
                        ", cuarentas = " + p.getCuarentas().get(i) +
                        ", abandonador = " + (p.getAbandonador()==i+1) +
                        " WHERE partida = " + p.getId() +
                        " AND usuario = '" + p.getUsuarios().get(i).getUsername() + "'";
                statement.executeUpdate(updateJuega);
            }
            connection.commit();


        } catch (SQLException e ) {
            e.printStackTrace();
            if (connection != null) {
                try {
                    System.err.print("Transaction is being rolled back");
                    connection.rollback();
                } catch(SQLException excep) {
                    excep.printStackTrace();
                }
            }
        } finally {
            if (statement != null) try { statement.close(); } catch (SQLException e) {e.printStackTrace();}
            if (connection != null) try { connection.setAutoCommit(true); connection.close();} catch (SQLException e) {e.printStackTrace();}
        }
    }

    public static ArrayList<PartidaVO> obtenerHistorialPartidas(String user, ComboPooledDataSource pool) {
        Connection connection = null;
        Statement statement = null;
        ResultSet res;
        ArrayList<PartidaVO> historial = new ArrayList<>();
        try {
            connection = pool.getConnection();
            statement = connection.createStatement();

            String partIndiv = "SELECT p.id, p.timeInicio, p.timeFin, p.publica, p.ganador, \n" +
                    "       j1.usuario usuario1, j2.usuario usuario2, \n" +
                    "       j1.cuarentas cuarentas1, j2.cuarentas cuarentas2, \n" +
                    "       j1.veintes veintes1, j2.veintes veintes2, \n" +
                    "       j1.puntos puntos1, j2.puntos puntos2, \n" +
                    "       (j1.abandonador + 2*j2.abandonador) abandonador  \n" +
                    "FROM partida p, juega j1, juega j2\n" +
                    "WHERE p.id = j1.partida AND p.id = j2.partida \n" +
                    "      AND (j1.usuario = '" + user + "' OR j2.usuario = '" + user + "') \n" +
                    "      AND j1.usuario > j2.usuario \n" +
                    "      AND (NOT EXISTS (SELECT * FROM juega jj WHERE jj.partida = p.id \n" +
                    "      AND j1.usuario <> jj.usuario AND j2.usuario <> jj.usuario));\n";


            res = statement.executeQuery(partIndiv);
            while (res.next()) {
                BigInteger id = new BigInteger(res.getString("id"));
                Timestamp timeInicio = res.getTimestamp("timeInicio");
                Timestamp timeFin = res.getTimestamp("timeFin");
                boolean publica = res.getBoolean("publica");
                char ganador = res.getString("ganador").charAt(0);
                ArrayList<UsuarioVO> usuarios = new ArrayList<>();
                UsuarioVO u1 = new UsuarioVO();
                u1.setUsername(res.getString("usuario1"));
                usuarios.add(u1);
                UsuarioVO u2 = new UsuarioVO();
                u2.setUsername(res.getString("usuario2"));
                usuarios.add(u2);
                ArrayList<Integer> cuarentas = new ArrayList<>();
                cuarentas.add(res.getInt("cuarentas1"));
                cuarentas.add(res.getInt("cuarentas2"));
                ArrayList<Integer> veintes = new ArrayList<>();
                veintes.add(res.getInt("veintes1"));
                veintes.add(res.getInt("veintes2"));
                int puntos1 = res.getInt("puntos1");
                int puntos2 = res.getInt("puntos2");
                int abandonador = res.getInt("abandonador");
                historial.add(new PartidaVO(id,timeInicio,timeFin,publica,ganador,usuarios,cuarentas,veintes,puntos1,puntos2,abandonador));
            }

            String partParejas = "SELECT p.id, p.timeInicio, p.timeFin, p.publica, p.ganador,\n" +
                    "       j1.usuario usuario1, j2.usuario usuario2,\n" +
                    "       j3.usuario usuario3, j4.usuario usuario4,\n" +
                    "       j1.equipo equipo1, j2.equipo equipo2, \n" +
                    "       j3.equipo equipo3, j4.equipo equipo4, \n" +
                    "       j1.cuarentas cuarentas1, j2.cuarentas cuarentas2,\n" +
                    "       j3.cuarentas cuarentas3, j4.cuarentas cuarentas4, \n" +
                    "       j1.veintes veintes1, j2.veintes veintes2, \n" +
                    "       j3.veintes veintes3, j4.veintes veintes4, \n" +
                    "       j1.puntos puntos1, j2.puntos puntos2, \n" +
                    "       j3.puntos puntos3, j4.puntos puntos4, \n" +
                    "       (j1.abandonador + 2*j2.abandonador + 3*j3.abandonador + 4*j4.abandonador) abandonador \n" +
                    "FROM partida p, juega j1, juega j2, juega j3, juega j4\n" +
                    "WHERE p.id = j1.partida AND p.id = j2.partida AND p.id = j3.partida AND p.id = j4.partida \n" +
                    "       AND (j1.usuario = '" + user + "' OR j2.usuario = '" + user + "' \n" +
                    "       OR j3.usuario = '" + user + "' OR j4.usuario = '" + user + "') \n" +
                    "       AND j1.usuario > j2.usuario AND j2.usuario > j3.usuario AND j3.usuario > j4.usuario;\n";

            res = statement.executeQuery(partParejas);
            while (res.next()) {
                BigInteger id = new BigInteger(res.getString("id"));
                Timestamp timeInicio = res.getTimestamp("timeInicio");
                Timestamp timeFin = res.getTimestamp("timeFin");
                boolean publica = res.getBoolean("publica");
                char ganador = res.getString("ganador").charAt(0);

                ArrayList<UsuarioVO> usuarios = new ArrayList<>(4);
                ArrayList<Integer> cuarentas = new ArrayList<>(4);
                ArrayList<Integer> veintes = new ArrayList<>(4);

                for (int i=0; i<4; i++){
                    usuarios.add(new UsuarioVO());
                    cuarentas.add(-1);
                    veintes.add(-1);
                }

                UsuarioVO u1 = new UsuarioVO();
                u1.setUsername(res.getString("usuario1"));
                UsuarioVO u2 = new UsuarioVO();
                u2.setUsername(res.getString("usuario2"));
                UsuarioVO u3 = new UsuarioVO();
                u3.setUsername(res.getString("usuario3"));
                UsuarioVO u4 = new UsuarioVO();
                u4.setUsername(res.getString("usuario4"));

                int puntos1;
                int puntos2;

                if (res.getInt("equipo1") == 1) {
                    usuarios.set(0, u1);
                    cuarentas.set(0, res.getInt("cuarentas1"));
                    veintes.set(0, res.getInt("veintes1"));

                    puntos1 = res.getInt("puntos1");

                    if (res.getInt("equipo2") == 1) {
                        usuarios.set(2, u2);
                        cuarentas.set(2, res.getInt("cuarentas2"));
                        veintes.set(2, res.getInt("veintes2"));

                        puntos2 = res.getInt("puntos3");

                        usuarios.set(1, u3);
                        cuarentas.set(1, res.getInt("cuarentas3"));
                        veintes.set(1, res.getInt("veintes3"));

                        usuarios.set(3, u4);
                        cuarentas.set(3, res.getInt("cuarentas4"));
                        veintes.set(3, res.getInt("veintes4"));
                    } else {
                        puntos2 = res.getInt("puntos2");
                        usuarios.set(1, u2);
                        cuarentas.set(1, res.getInt("cuarentas2"));
                        veintes.set(1, res.getInt("veintes2"));
                        if (res.getInt("equipo3") == 1) {
                            usuarios.set(2, u3);
                            cuarentas.set(2, res.getInt("cuarentas3"));
                            veintes.set(2, res.getInt("veintes3"));

                            usuarios.set(3, u4);
                            cuarentas.set(3, res.getInt("cuarentas4"));
                            veintes.set(3, res.getInt("veintes4"));
                        } else {
                            usuarios.set(3, u3);
                            cuarentas.set(3, res.getInt("cuarentas3"));
                            veintes.set(3, res.getInt("veintes3"));

                            usuarios.set(2, u4);
                            cuarentas.set(2, res.getInt("cuarentas4"));
                            veintes.set(2, res.getInt("veintes4"));
                        }
                    }
                } else {
                    puntos2 = res.getInt("puntos1");
                    usuarios.set(1, u1);
                    cuarentas.set(1, res.getInt("cuarentas1"));
                    veintes.set(1, res.getInt("veintes1"));
                    if (res.getInt("equipo2") == 2) {
                        usuarios.set(3, u2);
                        cuarentas.set(3, res.getInt("cuarentas2"));
                        veintes.set(3, res.getInt("veintes2"));

                        puntos1 = res.getInt("puntos3");

                        usuarios.set(0, u3);
                        cuarentas.set(0, res.getInt("cuarentas3"));
                        veintes.set(0, res.getInt("veintes3"));

                        usuarios.set(2, u4);
                        cuarentas.set(2, res.getInt("cuarentas4"));
                        veintes.set(2, res.getInt("veintes4"));
                    } else {
                        puntos1 = res.getInt("puntos2");
                        usuarios.set(0, u2);
                        cuarentas.set(0, res.getInt("cuarentas2"));
                        veintes.set(0, res.getInt("veintes2"));
                        if (res.getInt("equipo3") == 2) {
                            usuarios.set(3, u3);
                            cuarentas.set(3, res.getInt("cuarentas3"));
                            veintes.set(3, res.getInt("veintes3"));

                            usuarios.set(2, u4);
                            cuarentas.set(2, res.getInt("cuarentas4"));
                            veintes.set(2, res.getInt("veintes4"));
                        } else {
                            usuarios.set(2, u3);
                            cuarentas.set(2, res.getInt("cuarentas3"));
                            veintes.set(2, res.getInt("veintes3"));

                            usuarios.set(3, u4);
                            cuarentas.set(3, res.getInt("cuarentas4"));
                            veintes.set(3, res.getInt("veintes4"));
                        }
                    }
                }

                int abandonador = res.getInt("abandonador");
                historial.add(new PartidaVO(id,timeInicio,timeFin,publica,ganador,usuarios,cuarentas,veintes,puntos1,puntos2,abandonador));
            }

        } catch (SQLException e ) {
            e.printStackTrace();
        } finally {
            if (statement != null) try { statement.close(); } catch (SQLException e) {e.printStackTrace();}
            if (connection != null) try { connection.close();} catch (SQLException e) {e.printStackTrace();}
        }
        return historial;
    }

    public static ArrayList<PartidaVO> obtenerPartidasPublicasCurso(ComboPooledDataSource pool) {
        Connection connection = null;
        Statement statement = null;
        ResultSet res;
        ArrayList<PartidaVO> partidasCurso = new ArrayList<>();
        try {
            connection = pool.getConnection();
            statement = connection.createStatement();

            String partIndiv = "SELECT p.id, p.timeInicio, \n" +
                    "       j1.usuario usuario1, j2.usuario usuario2 \n" +
                    "FROM partida p, juega j1, juega j2\n" +
                    "WHERE p.id = j1.partida AND p.id = j2.partida AND p.publica = 1 AND p.timeFin IS NULL\n" +
                    "      AND j1.usuario > j2.usuario \n" +
                    "      AND (NOT EXISTS (SELECT * FROM juega jj WHERE jj.partida = p.id \n" +
                    "      AND j1.usuario <> jj.usuario AND j2.usuario <> jj.usuario));\n";


            res = statement.executeQuery(partIndiv);
            while (res.next()) {
                BigInteger id = new BigInteger(res.getString("id"));
                Timestamp timeInicio = res.getTimestamp("timeInicio");
                ArrayList<UsuarioVO> usuarios = new ArrayList<>(2);
                UsuarioVO u1 = new UsuarioVO();
                u1.setUsername(res.getString("usuario1"));
                usuarios.add(u1);
                UsuarioVO u2 = new UsuarioVO();
                u2.setUsername(res.getString("usuario2"));
                usuarios.add(u2);
                PartidaVO p = new PartidaVO(timeInicio,true,usuarios);
                p.setId(id);
                partidasCurso.add(p);
            }

            String partParejas = "SELECT p.id, p.timeInicio,\n" +
                    "       j1.usuario usuario1, j2.usuario usuario2,\n" +
                    "       j3.usuario usuario3, j4.usuario usuario4,\n" +
                    "       j1.equipo equipo1, j2.equipo equipo2, \n" +
                    "       j3.equipo equipo3, j4.equipo equipo4 \n" +
                    "FROM partida p, juega j1, juega j2, juega j3, juega j4\n" +
                    "WHERE p.id = j1.partida AND p.id = j2.partida AND p.id = j3.partida AND p.id = j4.partida \n" +
                    "       AND p.publica <> 0 AND p.timeFin IS NULL\n" +
                    "       AND j1.usuario > j2.usuario AND j2.usuario > j3.usuario AND j3.usuario > j4.usuario;\n";

            res = statement.executeQuery(partParejas);
            while (res.next()) {
                BigInteger id = new BigInteger(res.getString("id"));
                Timestamp timeInicio = res.getTimestamp("timeInicio");

                ArrayList<UsuarioVO> usuarios = new ArrayList<>(4);

                for (int i=0; i<4; i++){
                    usuarios.add(new UsuarioVO());
                }

                UsuarioVO u1 = new UsuarioVO();
                u1.setUsername(res.getString("usuario1"));
                UsuarioVO u2 = new UsuarioVO();
                u2.setUsername(res.getString("usuario2"));
                UsuarioVO u3 = new UsuarioVO();
                u3.setUsername(res.getString("usuario3"));
                UsuarioVO u4 = new UsuarioVO();
                u4.setUsername(res.getString("usuario4"));


                if (res.getInt("equipo1") == 1) {
                    usuarios.set(0, u1);


                    if (res.getInt("equipo2") == 1) {
                        usuarios.set(2, u2);


                        usuarios.set(1, u3);

                        usuarios.set(3, u4);
                    } else {
                        usuarios.set(1, u2);
                        if (res.getInt("equipo3") == 1) {
                            usuarios.set(2, u3);

                            usuarios.set(3, u4);
                        } else {
                            usuarios.set(3, u3);

                            usuarios.set(2, u4);
                        }
                    }
                } else {
                    usuarios.set(1, u1);
                    if (res.getInt("equipo2") == 2) {
                        usuarios.set(3, u2);


                        usuarios.set(0, u3);

                        usuarios.set(2, u4);
                    } else {
                        usuarios.set(0, u2);
                        if (res.getInt("equipo3") == 2) {
                            usuarios.set(3, u3);

                            usuarios.set(2, u4);
                        } else {
                            usuarios.set(2, u3);

                            usuarios.set(3, u4);
                        }
                    }
                }
                PartidaVO p = new PartidaVO(timeInicio,true,usuarios);
                p.setId(id);
                partidasCurso.add(p);
            }

        } catch (SQLException e ) {
            e.printStackTrace();
        } finally {
            if (statement != null) try { statement.close(); } catch (SQLException e) {e.printStackTrace();}
            if (connection != null) try { connection.close();} catch (SQLException e) {e.printStackTrace();}
        }
        return partidasCurso;
    }
}

