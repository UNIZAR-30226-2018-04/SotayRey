/*
 * Autor: Guerrero Viu, Julia & Izquierdo Barranco, Sergio
 * Fecha: 25-04-2018
 * Fichero: TorneoDAO.java
 */

package basedatos.dao;

import java.text.SimpleDateFormat;
import basedatos.exceptions.*;
import basedatos.modelo.*;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import java.math.BigInteger;

import java.sql.*;
import java.util.ArrayList;

public class TorneoDAO {
	
	public static void crearTorneo(TorneoVO t, ComboPooledDataSource pool) throws ExceptionCampoInvalido, SQLException {
		Connection connection = null;
        Statement statement = null;
        connection = pool.getConnection();
        statement = connection.createStatement();
		connection.setAutoCommit(false);
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		Timestamp actualts = new Timestamp(System.currentTimeMillis());
		
		if(actualts.after(t.getTimeInicio())){
			throw new ExceptionCampoInvalido("El tiempo de inicio debe ser posterior a la fecha actual");		
		}
		
		String prevalues = "INSERT INTO torneo (nombre";
        String postvalues = " VALUES ('" + t.getNombre() + "'";
        if (t.getDescripcion() != null) {
        	prevalues = prevalues + ", descripcion";
            postvalues = postvalues + ", '" + t.getDescripcion() + "'";
        }

        prevalues = prevalues + ", timeInicio";
        postvalues = postvalues + ", '" + sdf.format(t.getTimeInicio()) + "'";

		prevalues = prevalues + ", individual";
		if(t.esIndividual()){
			postvalues = postvalues + ", TRUE";
		}
		else{
			postvalues = postvalues + ", FALSE";
		}
        prevalues = prevalues + ")";
        postvalues = postvalues + ")";
        statement.executeUpdate(prevalues+postvalues);
		
		// Conseguir id torneo
        ResultSet res = statement.executeQuery("SELECT LAST_INSERT_ID()");
        res.next();
        String t_id = res.getString("LAST_INSERT_ID()");
        t.setId(new BigInteger(t_id));
	
		// Insertar cada una de las fases del torneo
		int premiopuntuacion = t.getPremioPuntuacionPrimera();
		int premiodivisa = t.getPremioDivisaPrimera();
		int numf = t.getNumFases();
		for(int i=0; i<numf; i++){
			String insertFase = "INSERT INTO fase (num, torneo, premioPunt, premioDiv) VALUES (";
			insertFase = insertFase + (numf - i) + ", " + t.getId() + ", " + premiopuntuacion * Math.pow(2,i) + ", " + premiodivisa * Math.pow(2,i) + ")";
            statement.executeUpdate(insertFase);
		}
		
      	connection.commit();

        if (statement != null) statement.close();
        if (connection != null) {connection.setAutoCommit(true); connection.close();}
	}

	public static void modificarTorneo(TorneoVO t, ComboPooledDataSource pool) throws ExceptionCampoInexistente, ExceptionCampoInvalido, SQLException {
		Connection connection = null;
        Statement statement = null;
        connection = pool.getConnection();
        statement = connection.createStatement();
		connection.setAutoCommit(false);

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Timestamp actualts = new Timestamp(System.currentTimeMillis());

		// Comprobar que el torneo existe pero todavía no ha comenzado y se puede modificar
		String query = "SELECT timeInicio FROM torneo WHERE id = " + t.getId();
        ResultSet resultSet = statement.executeQuery(query);

        if(!resultSet.isBeforeFirst()){
            throw new ExceptionCampoInexistente("Error de acceso a la base de datos: Torneo: " + t.getId() + "  no existente");
        }
		resultSet.next();
		Timestamp tIni = resultSet.getTimestamp("timeInicio");
			
		if(actualts.after(tIni)){
			throw new ExceptionCampoInvalido("El torneo no se puede modificar: ya ha comenzado");
		}
		
		
        String updatepre = "UPDATE torneo SET";
        String updatepost = " WHERE id = " + t.getId();
        if(t.esIndividual()) updatepre = updatepre + " individual = TRUE";
        else updatepre = updatepre + " individual = FALSE";
        if(t.getDescripcion()!=null) updatepre = updatepre + ", descripcion = '" + t.getDescripcion() + "'";
        if(t.getNombre()!=null) updatepre = updatepre + ", nombre = '" + t.getNombre() + "'";
        if(t.getTimeInicio()!=null && actualts.before(t.getTimeInicio())) updatepre = updatepre + ", timeInicio = '" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(t.getTimeInicio()) + "'";
		
		statement.executeUpdate(updatepre+updatepost);

		if(t.getTimeInicio()!=null && actualts.after(t.getTimeInicio())){
			throw new ExceptionCampoInvalido("El tiempo de inicio debe ser posterior a la fecha actual");		
		}

		// Modificar las fases (eliminarlas y re-crearlas) 
		if(t.getNumFases()!=-1 && t.getPremioPuntuacionPrimera()!=-1 && t.getPremioDivisaPrimera()!=-1){
			String delete = "DELETE FROM fase WHERE torneo = " + t.getId();
			statement.executeUpdate(delete);
			int premiopuntuacion = t.getPremioPuntuacionPrimera();
			int premiodivisa = t.getPremioDivisaPrimera();
			int numf = t.getNumFases();
			for(int i=0; i<numf; i++){
				String insertFase = "INSERT INTO fase (num, torneo, premioPunt, premioDiv) VALUES (";
				insertFase = insertFase + (numf - i) + ", " + t.getId() + ", " + premiopuntuacion * Math.pow(2,i) + ", " + premiodivisa * Math.pow(2,i) + ")";
		        statement.executeUpdate(insertFase);
			}
		}
		
		connection.commit();

        if (statement != null) statement.close();
        if (connection != null) {connection.setAutoCommit(true); connection.close();}
    }

	public static void eliminarTorneo(BigInteger id, ComboPooledDataSource pool) throws ExceptionCampoInexistente, ExceptionCampoInvalido, SQLException {
		Connection connection = null;
        Statement statement = null;
        connection = pool.getConnection();
        statement = connection.createStatement();
		connection.setAutoCommit(false);

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Timestamp actualts = new Timestamp(System.currentTimeMillis());

		// Comprobar que el torneo existe pero todavía no ha comenzado y se puede modificar
		String query = "SELECT timeInicio FROM torneo WHERE id = " + id;
        ResultSet resultSet = statement.executeQuery(query);

        if(!resultSet.isBeforeFirst()){
            throw new ExceptionCampoInexistente("Error de acceso a la base de datos: Torneo: " + id + "  no existente");
        }
		resultSet.next();
		Timestamp tIni = resultSet.getTimestamp("timeInicio");
			
		if(actualts.after(tIni)){
			throw new ExceptionCampoInvalido("El torneo no se puede eliminar: ya ha comenzado");
		}

		// Eliminar el torneo y todas sus fases
        String delete = "DELETE FROM torneo WHERE id = " + id;
        statement.executeUpdate(delete);

		delete = "DELETE FROM fase WHERE torneo = " + id;
		statement.executeUpdate(delete);

		connection.commit();
        if (statement != null)  statement.close();
        if (connection != null) {connection.setAutoCommit(true); connection.close();}
	}

	public static TorneoVO obtenerDatosTorneo(BigInteger id, ComboPooledDataSource pool) throws ExceptionCampoInvalido, SQLException {
		TorneoVO t = new TorneoVO();
		return t;
	}

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
