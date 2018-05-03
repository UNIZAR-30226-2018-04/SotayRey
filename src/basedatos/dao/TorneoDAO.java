/*
 * Autor: Guerrero Viu, Julia & Izquierdo Barranco, Sergio
 * Fecha: 25-04-2018
 * Fichero: TorneoDAO.java
 */

package basedatos.dao;

import basedatos.exceptions.ExceptionCampoInexistente;
import basedatos.exceptions.ExceptionCampoInvalido;
import basedatos.modelo.PartidaVO;
import basedatos.modelo.TorneoVO;
import basedatos.modelo.UsuarioVO;
import com.mchange.v2.c3p0.ComboPooledDataSource;

import java.math.BigInteger;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;

public class TorneoDAO {
	
	public static void crearTorneo(TorneoVO t, ComboPooledDataSource pool) throws ExceptionCampoInvalido, SQLException {
		Connection connection = null;
        Statement statement = null;
        connection = pool.getConnection();
        statement = connection.createStatement();
		connection.setAutoCommit(false);
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		Timestamp actualts = new Timestamp(System.currentTimeMillis());
		
//		if(actualts.after(t.getTimeInicio())){
//			throw new ExceptionCampoInvalido("El tiempo de inicio debe ser posterior a la fecha actual");
//		}
		
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

		// Realmente no sería necesario borrar sus fases porque se borran en CASCADE
//		delete = "DELETE FROM fase WHERE torneo = " + id;
//		statement.executeUpdate(delete);

		connection.commit();
        if (statement != null)  statement.close();
        if (connection != null) {connection.setAutoCommit(true); connection.close();}
	}

	public static TorneoVO obtenerDatosTorneo(BigInteger id, ComboPooledDataSource pool) throws ExceptionCampoInexistente, SQLException {
		Connection connection = null;
        Statement statement = null;
        connection = pool.getConnection();
        statement = connection.createStatement();
		connection.setAutoCommit(false);

		TorneoVO t = new TorneoVO();
		t.setId(id);

		String query = "SELECT * FROM torneo WHERE id = " + id;
        ResultSet resultSet = statement.executeQuery(query);

        if(!resultSet.isBeforeFirst()){
            throw new ExceptionCampoInexistente("Error de acceso a la base de datos: Torneo " + id + "  no existente");
        }
		resultSet.next();
		t.setNombre(resultSet.getString("nombre"));
		t.setDescripcion(resultSet.getString("descripcion"));
		t.setTimeCreacion(resultSet.getTimestamp("timeCreacion"));
		t.setTimeInicio(resultSet.getTimestamp("timeInicio"));
		t.setIndividual(resultSet.getBoolean("individual"));

		query = "SELECT COUNT(*) num FROM fase WHERE torneo = " + id;
        resultSet = statement.executeQuery(query);

        if(!resultSet.isBeforeFirst()){
            t.setNumFases(0);
        }
		else {
			resultSet.next();
			t.setNumFases(resultSet.getInt("num"));
			query = "SELECT * FROM fase WHERE torneo = " + id + " and num = " + t.getNumFases();
        	resultSet = statement.executeQuery(query);
			resultSet.next();
			t.setPremioPuntuacionPrimera(resultSet.getInt("premioPunt"));
			t.setPremioDivisaPrimera(resultSet.getInt("premioDiv"));
		}
		
		connection.commit();
		if (statement != null)  statement.close();
        if (connection != null) {connection.setAutoCommit(true); connection.close();}

		return t;
	}

	public static ArrayList<TorneoVO> obtenerTorneosProgramados(ComboPooledDataSource pool) throws SQLException {
		Connection connection = null;
        Statement statement = null;
        connection = pool.getConnection();
        statement = connection.createStatement();
		connection.setAutoCommit(false);

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Timestamp actualts = new Timestamp(System.currentTimeMillis());

		// Selecciona aquellos torneos que no están llenos ni han comenzado todavía
        String query = "SELECT * FROM torneo t WHERE t.timeInicio >= '" + actualts +
			"' or (((SELECT COUNT(*) FROM participa_fase where fase_torneo = t.id) < POW(2,(SELECT COUNT(*) FROM fase where torneo = t.id)) and t.individual = true) " +
			" or ((SELECT COUNT(*) FROM participa_fase where fase_torneo = t.id) < POW(4,(SELECT COUNT(*) FROM fase where torneo = t.id)) and t.individual = false))";
        ResultSet resultSet = statement.executeQuery(query);

        HashMap<BigInteger,TorneoVO> dic = new HashMap<>();

        while (resultSet.next()) {
            TorneoVO t = new TorneoVO();
            t.setId(BigInteger.valueOf(resultSet.getLong("id")));
            t.setNombre(resultSet.getString("nombre"));
            t.setDescripcion(resultSet.getString("descripcion"));
            t.setTimeCreacion(resultSet.getTimestamp("timeCreacion"));
            t.setTimeInicio(resultSet.getTimestamp("timeInicio"));
            t.setIndividual(resultSet.getBoolean("individual"));
            dic.put(t.getId(), t);
        }

        query = "SELECT * FROM fase f WHERE num = (SELECT COUNT(*) FROM fase f2 where f2.torneo = f.torneo)";
        ResultSet resultSet2 = statement.executeQuery(query);

		while (resultSet2.next()) {
			BigInteger torneo = BigInteger.valueOf(resultSet2.getLong("torneo"));
			if(dic.containsKey(torneo)){
				TorneoVO aux;
				aux = dic.get(torneo);
				aux.setNumFases(resultSet2.getInt("num"));
				aux.setPremioPuntuacionPrimera(resultSet2.getInt("premioPunt"));
				aux.setPremioDivisaPrimera(resultSet2.getInt("premioDiv"));
				dic.put(torneo, aux);		
			}
        }

		// Rellenar el array con los valores del hashmap
		ArrayList<TorneoVO> programados = new ArrayList<TorneoVO>(dic.values());

		connection.commit();
        if (statement != null)  statement.close();
        if (connection != null) {connection.setAutoCommit(true); connection.close();}

        return programados;
	}

    public static boolean apuntarTorneo(UsuarioVO p, TorneoVO t, ComboPooledDataSource pool) throws ExceptionCampoInvalido, SQLException {
        Connection connection = null;
        Statement statement = null;
        connection = pool.getConnection();
        statement = connection.createStatement();
        connection.setAutoCommit(false);

        boolean faseLlena = false;

        UsuarioVO u1;
        UsuarioVO u2;
        ArrayList<UsuarioVO> ual;
        PartidaVO pp;

        ResultSet res = statement.executeQuery("SELECT COUNT(*) total FROM participa_fase p WHERE p.fase_num ="+t.getNumFases() + " AND p.fase_torneo="+t.getId());
        res.next();
        int participantes = res.getInt("total");
        if ((new Timestamp(System.currentTimeMillis()).after(t.getTimeInicio())) && participantes < Math.pow(2,t.getNumFases())) {
            // Cabe un jugador más
            statement.executeUpdate("INSERT INTO participa_fase (usuario, fase_num, fase_torneo) VALUES ('"+ p.getUsername() +"',"+t.getNumFases()+","+t.getId()+")");
            if (participantes + 1 == Math.pow(2,t.getNumFases())) {
                // El torneo esta lleno, se produce el emparejamiento
                faseLlena = true;
                res = statement.executeQuery("SELECT p.usuario FROM participa_fase p WHERE p.fase_num ="+t.getNumFases() + " AND p.fase_torneo="+t.getId());
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

        connection.commit();

        if (statement != null) { statement.close(); }
        if (connection != null) {connection.setAutoCommit(true);connection.close();}
        return faseLlena;
    }

	public static void abandonarTorneo(UsuarioVO u, TorneoVO t, ComboPooledDataSource pool) throws ExceptionCampoInexistente, SQLException {
		Connection connection = null;
        Statement statement = null;
        connection = pool.getConnection();
        statement = connection.createStatement();
        connection.setAutoCommit(false);

		// Saber en qué fase se encontraba el usuario
		ResultSet res = statement.executeQuery("SELECT fase_num fase FROM participa_fase p WHERE p.fase_torneo ="+t.getId() + " AND p.usuario = '"+u.getUsername() + "' ORDER BY fase_num ASC");
		if(!res.isBeforeFirst()){
            throw new ExceptionCampoInexistente("Error de acceso a la base de datos: Usuario " + u.getUsername() + "  no está apuntado en el torneo " + t.getId());
        }
		res.next();
		int fase = res.getInt("fase");
		
		// Eliminar la relación participa_fase, eliminando al usuario del torneo
		String delete = "DELETE FROM participa_fase WHERE usuario = '" + u.getUsername() + "' AND fase_num = "+ fase + " AND fase_torneo = " + t.getId();
        statement.executeUpdate(delete);
	
		// Sustituir al usuario eliminado por la IA si no es la primera fase
		if(fase<t.getNumFases()){
			int multip = 0;
			ResultSet resultSet = statement.executeQuery("SELECT MAX(multip)+1 m FROM participa_fase WHERE usuario = 'marIA' AND fase_num = " + fase + " AND fase_torneo = "+ t.getId());
			if(resultSet.isBeforeFirst()){
				// ya existe una IA participando en esa fase, se añade una nueva multiplicidad
            	resultSet.next();
				multip= resultSet.getInt("m");
        	}
			String insert = "INSERT INTO participa_fase (usuario, fase_num, fase_torneo, multip) VALUES('marIA', " + fase + ", " + t.getId() + ", " + multip + ")";
        	statement.executeUpdate(insert);
		}

		connection.commit();

        if (statement != null) { statement.close(); }
        if (connection != null) {connection.setAutoCommit(true);connection.close();}
	}
}
