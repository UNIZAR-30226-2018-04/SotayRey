/*
 * Autor: Sergio Izquierdo Barranco
 * Fecha: 20-03-18
 * Fichero: Objeto PartidaDAO con las funciones para interaccionar con la base de datos
 */

package basedatos.dao;

import basedatos.exceptions.ExceptionCampoInvalido;
import basedatos.modelo.FaseVO;
import basedatos.modelo.PartidaVO;
import basedatos.modelo.UsuarioVO;
import com.mchange.v2.c3p0.ComboPooledDataSource;

import java.math.BigInteger;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class PartidaDAO {
    public static void insertarNuevaPartida(PartidaVO p, ComboPooledDataSource pool) throws SQLException {
        Connection connection = null;
        Statement statement = null;

        connection = pool.getConnection();
        statement = connection.createStatement();
        connection.setAutoCommit(false);

        String insertPartida;
        if (p.getTimeInicio() != null) {//Comienza transacción
            SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            insertPartida = "INSERT INTO partida (timeInicio, publica) VALUES ('" +
                    sd.format(p.getTimeInicio()) + "'," + p.isPublica() + ")";
        } else {
            if (p.getTorneoId() == BigInteger.valueOf(0)) {
                insertPartida = "INSERT INTO partida (publica) VALUES (" + (p.isPublica() ? 1 : 0) + ")";
            } else {
                insertPartida = "INSERT INTO partida (fase_num, fase_torneo,publica) VALUES (" + p.getFaseNum() + "," + p.getTorneoId() + ",1)";
            }
        }

        statement.executeUpdate(insertPartida);

        //Conseguir id partida
        ResultSet res = statement.executeQuery("SELECT LAST_INSERT_ID()");
        res.next();
        String p_id = res.getString("LAST_INSERT_ID()");
        p.setId(new BigInteger(p_id));

        // Recorremos los usuarios e insertamos la relacion juega
        for (int i = 0; i < p.getUsuarios().size(); i++) {
            String insertJuega = "INSERT INTO juega (usuario, partida, equipo) VALUES ('" +
                    p.getUsuarios().get(i).getUsername() + "'," + p_id + ",'" + (char) (((i % 2) + 1) + '0') + "')";
            statement.executeUpdate(insertJuega);
        }
        connection.commit();

        if (statement != null) {
            statement.close();
        }
        if (connection != null) {
            connection.setAutoCommit(true);
            connection.close();
        }
    }

    public static boolean finalizarPartida(PartidaVO p, ComboPooledDataSource pool) throws SQLException, ExceptionCampoInvalido {
        Connection connection = null;
        Statement statement = null;

        connection = pool.getConnection();
        statement = connection.createStatement();
        connection.setAutoCommit(false);

        boolean faseLlena = false;
        //Comienza transacción
        SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String updatePartida = "UPDATE partida SET timeFin = '" +
                sd.format(p.getTimeFin()) + "', ganador = '" + p.getGanador() + "' WHERE id = " +
                p.getId().toString();

        statement.executeUpdate(updatePartida);

        // Recorremos los usuarios e insertamos la relacion juega
        for (int i = 0; i < p.getUsuarios().size(); i++) {
            int puntos;
            if (i % 2 == 0) {
                puntos = p.getPuntos1();
            } else {
                puntos = p.getPuntos2();
            }
            String updateJuega = "UPDATE juega SET puntos = " + puntos +
                    ", veintes = " + p.getVeintes().get(i) +
                    ", cuarentas = " + p.getCuarentas().get(i) +
                    ", abandonador = " + (p.getAbandonador() == i + 1) +
                    " WHERE partida = " + p.getId() +
                    " AND usuario = '" + p.getUsuarios().get(i).getUsername() + "' AND equipo = '" + (char) ((((i % 2) + 1)) + '0') + "'";
            statement.executeUpdate(updateJuega);
        }

        //Obtención de premios
        int premioPunt = 3;
        int premioDiv = 5;
        ResultSet res;
        if (p.getFaseNum() > 0) {
            res = statement.executeQuery("SELECT premioPunt, premioDiv FROM fase WHERE torneo = " + p.getTorneoId() + " AND num = " + p.getFaseNum());
            res.next();
            premioPunt = res.getInt("premioPunt");
            premioDiv = res.getInt("premioDiv");

            if (p.getFaseNum() > 1) {
                int multip = 0;
                res = statement.executeQuery("SELECT MAX(multip)+1 m FROM participa_fase WHERE usuario = 'marIA' AND fase_num = " + (p.getFaseNum()-1) + " AND fase_torneo = " + p.getTorneoId());
                if (res.isBeforeFirst()) {
                    // ya existe una IA participando en esa fase, se añade una nueva multiplicidad
                    res.next();
                    multip = res.getInt("m");
                }

                if (p.getGanador() == 'A') {
                    if (p.getAbandonador() == 1) {
                        statement.executeUpdate("INSERT INTO participa_fase (usuario, fase_num, fase_torneo, multip) VALUES ('" + p.getUsuarios().get(2).getUsername() + "'," + (p.getFaseNum() - 1) + "," + p.getTorneoId() + "," + multip + ")");
                    } else {
                        statement.executeUpdate("INSERT INTO participa_fase (usuario, fase_num, fase_torneo, multip) VALUES ('" + p.getUsuarios().get(1).getUsername() + "'," + (p.getFaseNum() - 1) + "," + p.getTorneoId() + "," + multip + ")");
                    }
                } else if (p.getGanador() == '1') {
                    statement.executeUpdate("INSERT INTO participa_fase (usuario, fase_num, fase_torneo, multip) VALUES ('" + p.getUsuarios().get(1).getUsername() + "'," + (p.getFaseNum() - 1) + "," + p.getTorneoId() + "," + multip + ")");
                } else {
                    statement.executeUpdate("INSERT INTO participa_fase (usuario, fase_num, fase_torneo, multip) VALUES ('" + p.getUsuarios().get(2).getUsername() + "'," + (p.getFaseNum() - 1) + "," + p.getTorneoId() + "," + multip + ")");
                }

                res = statement.executeQuery("SELECT COUNT(*) total FROM participa_fase p WHERE p.fase_num =" + (p.getFaseNum() - 1) + " AND p.fase_torneo=" + p.getTorneoId());
                res.next();
                int participantes = res.getInt("total");

                if (participantes == Math.pow(2, p.getFaseNum() - 1)) {
                    faseLlena = true;
                    // El torneo esta lleno, se produce el emparejamiento
                    res = statement.executeQuery("SELECT p.usuario FROM participa_fase p WHERE p.fase_num =" + (p.getFaseNum() - 1) + " AND p.fase_torneo=" + p.getTorneoId());

                    UsuarioVO u1;
                    UsuarioVO u2;
                    ArrayList<UsuarioVO> ual;
                    PartidaVO pp;
                    System.out.println(res.getFetchSize());
                    while (res.next()) {
                        System.out.println(res.getFetchSize());
                        u1 = new UsuarioVO();
                        u1.setUsername(res.getString("usuario"));
                        u2 = new UsuarioVO();
                        res.next();
                        u2.setUsername(res.getString("usuario"));
                        ual = new ArrayList<>();
                        ual.add(u1);
                        ual.add(u2);
                        pp = new PartidaVO(p.getFaseNum() - 1, p.getTorneoId(), true, ual);

                        //Codigo repetido de insertar una partida
                        String insertPartida;
                        insertPartida = "INSERT INTO partida (fase_num, fase_torneo,publica) VALUES (" + pp.getFaseNum() + "," + pp.getTorneoId() + ",1)";

                        Statement statement1 = connection.createStatement();
                        statement1.executeUpdate(insertPartida);

                        //Conseguir id partida
                        ResultSet res3 = statement1.executeQuery("SELECT LAST_INSERT_ID()");
                        res3.next();
                        String p_id = res3.getString("LAST_INSERT_ID()");

                        // Recorremos los usuarios e insertamos la relacion juega
                        for (int i = 0; i < pp.getUsuarios().size(); i++) {
                            String insertJuega = "INSERT INTO juega (usuario, partida, equipo) VALUES ('" +
                                    pp.getUsuarios().get(i).getUsername() + "'," + p_id + ",'" + (char) (((i % 2) + 1) + '0') + "')";
                            statement1.executeUpdate(insertJuega);
                        }
                        statement1.close();

                    }
                }
            }
        }

        //Actualizamos los puntos
        // Actualizar datos de los usuarios implicados
        char gan = p.getGanador();

        List<UsuarioVO> lista = p.getUsuarios();
        // Partida NO abandonada
        if (gan == '1' || gan == '2') {
            for (int i = 0; i < lista.size(); i = i + 2) {
                if (gan == '1') {
                    //ganador
                    res = statement.executeQuery("SELECT puntuacion,divisa FROM usuario WHERE username = '" + lista.get(i).getUsername() + "'");
                    res.next();
                    statement.executeUpdate("UPDATE usuario SET puntuacion = " + (res.getInt("puntuacion") + premioPunt) + ", divisa = " + (res.getInt("divisa") + premioDiv) + " WHERE username = '" + lista.get(i).getUsername() + "'");

                    //Perdedor
                    res = statement.executeQuery("SELECT puntuacion,divisa FROM usuario WHERE username = '" + lista.get(i + 1).getUsername() + "'");
                    res.next();
                    statement.executeUpdate("UPDATE usuario SET divisa = " + (res.getInt("divisa") + 1) + " WHERE username = '" + lista.get(i + 1).getUsername() + "'");

                } else {
                    //ganador
                    res = statement.executeQuery("SELECT puntuacion,divisa FROM usuario WHERE username = '" + lista.get(i + 1).getUsername() + "'");
                    res.next();
                    statement.executeUpdate("UPDATE usuario SET puntuacion = " + (res.getInt("puntuacion") + premioPunt) + ", divisa = " + (res.getInt("divisa") + premioDiv) + " WHERE username = '" + lista.get(i + 1).getUsername() + "'");

                    //Perdedor
                    res = statement.executeQuery("SELECT puntuacion,divisa FROM usuario WHERE username = '" + lista.get(i).getUsername() + "'");
                    res.next();
                    statement.executeUpdate("UPDATE usuario SET divisa = " + (res.getInt("divisa") + 1) + " WHERE username = '" + lista.get(i).getUsername() + "'");


                }
            }
        }
        // Partida abandonada
        else if (gan == 'A') {
            for (int i = 0; i < lista.size(); i++) {
                res = statement.executeQuery("SELECT puntuacion,divisa FROM usuario WHERE username = '" + lista.get(i).getUsername() + "'");
                res.next();
                if (p.getAbandonador() == i) {
                    statement.executeUpdate("UPDATE usuario SET puntuacion = " + Math.max(0,res.getInt("puntuacion") - 1) + " WHERE username = '" + lista.get(i).getUsername() + "'");
                } else {
                    statement.executeUpdate("UPDATE usuario SET puntuacion = " + (res.getInt("puntuacion") + premioPunt) + ", divisa = " + (res.getInt("divisa") + premioDiv) + " WHERE username = '" + lista.get(i).getUsername() + "'");
                }
            }
        } else {
            throw new ExceptionCampoInvalido("La partida debe estar finalizada (con ganador o abandonador)");
        }

        connection.commit();

        if (statement != null) {
            statement.close();
        }
        if (connection != null) {
            connection.setAutoCommit(true);
            connection.close();
        }
        return faseLlena;
    }

    public static ArrayList<PartidaVO> obtenerHistorialPartidas(String user, ComboPooledDataSource pool) throws SQLException {
        Connection connection = null;
        Statement statement = null;
        ResultSet res;
        ArrayList<PartidaVO> historial = new ArrayList<>();
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
            historial.add(new PartidaVO(id, timeInicio, timeFin, publica, ganador, usuarios, cuarentas, veintes, puntos1, puntos2, abandonador));
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

            for (int i = 0; i < 4; i++) {
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
            historial.add(new PartidaVO(id, timeInicio, timeFin, publica, ganador, usuarios, cuarentas, veintes, puntos1, puntos2, abandonador));
        }

        if (statement != null) statement.close();
        if (connection != null) connection.close();

        return historial;
    }

    public static ArrayList<PartidaVO> obtenerPartidasPublicasCurso(ComboPooledDataSource pool) throws SQLException {
        Connection connection = null;
        Statement statement = null;
        ResultSet res;
        ArrayList<PartidaVO> partidasCurso = new ArrayList<>();
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
            PartidaVO p = new PartidaVO(timeInicio, true, usuarios);
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

            for (int i = 0; i < 4; i++) {
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
            PartidaVO p = new PartidaVO(timeInicio, true, usuarios);
            p.setId(id);
            partidasCurso.add(p);
        }

        if (statement != null) statement.close();
        if (connection != null) connection.close();

        return partidasCurso;
    }

    public static void obtenerPartidasFaseTorneo(FaseVO f, ComboPooledDataSource pool) throws SQLException {
        Connection connection = null;
        Statement statement = null;

        connection = pool.getConnection();
        statement = connection.createStatement();

        UsuarioVO u1;
        UsuarioVO u2;
        ArrayList<UsuarioVO> usersp;
        ArrayList<UsuarioVO> ual = new ArrayList<>();
        ArrayList<PartidaVO> pal = new ArrayList<>();

        ResultSet res = statement.executeQuery("select p.id, j.usuario usuario1, jj.usuario usuario2, j.equipo equipo1, jj.equipo equipo2 from partida p, juega j, juega jj where j.partida=p.id and jj.partida=p.id and j.usuario > jj.usuario AND p.fase_torneo = " + f.getTorneoId() + " AND p.fase_num = " + f.getNum());

        while (res.next()) {
            u1 = new UsuarioVO();
            u1.setUsername(res.getString("usuario1"));
            u2 = new UsuarioVO();
            u2.setUsername(res.getString("usuario2"));
            ual.add(u1);
            ual.add(u2);
            usersp = new ArrayList<>();
            if (res.getString("equipo1").charAt(0) == '1') {
                usersp.add(u1);
                usersp.add(u2);
            } else {
                usersp.add(u2);
                usersp.add(u1);
            }
            pal.add(new PartidaVO(new BigInteger(res.getString("id")), f.getNum(), f.getTorneoId(), true, usersp));
        }

        f.setParticipantes(ual);
        f.setParejas(pal);


        if (statement != null) {
            statement.close();
        }
        if (connection != null) {
            connection.close();
        }
    }
    public static BigInteger obtenerIdUltimaPartida(ComboPooledDataSource pool) throws SQLException {
        Connection connection = null;
        Statement statement = null;

        connection = pool.getConnection();
        statement = connection.createStatement();

        ResultSet res = statement.executeQuery("SELECT MAX(id) mid FROM partida");
        res.next();
        BigInteger bi =  new BigInteger(res.getString("mid"));

        if (statement != null) {
            statement.close();
        }
        if (connection != null) {
            connection.close();
        }
        return bi;
    }
}
