package basedatos;

import java.beans.PropertyVetoException;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.math.BigInteger;

import basedatos.modelo.*;
import basedatos.exceptions.*;

public class PruebaBases {

    public static void main(String[] args) throws ExceptionCampoInexistente, ExceptionCampoInvalido, PropertyVetoException, SQLException, IOException {
        //System.out.println(hashPassword("burroloco"));
        //System.out.println(hashPassword("burroloco"));
        //System.out.println(checkPassword("burrodloco",hashed));


        //        Timestamp timeInicio = new Timestamp(new java.util.Date().getTime());
//        boolean publica = true;
//        List<UsuarioVO> usuarios = new ArrayList<>();
//        UsuarioVO julia = new UsuarioVO("juliagviu", "caca123","prueba@unizar.es", "Julia", "Guerrero", true);
//        UsuarioVO sergio = new UsuarioVO("serizba", null, "serizba@unizar.es", "Sergio", "Izquierdo", true);
//        UsuarioVO manolo = new UsuarioVO("manolo", "1234", "manolo@unizar.es", "Julia", "Guerrero", true);
//        UsuarioVO julian = new UsuarioVO("eljuli", "0000","juli@unizar.es", "Sergio", "Izquierdo", true);
//        InterfazDatos.instancia().crearUsuario(julia);
//        InterfazDatos.instancia().crearUsuario(sergio);
//        InterfazDatos.instancia().crearUsuario(manolo);
//        InterfazDatos.instancia().crearUsuario(julian);

//        InterfazDatos.instancia().eliminarUsuario("serizba");
//        InterfazDatos.instancia().eliminarUsuario("juliagviu");

//        System.out.println(InterfazDatos.instancia().autentificarUsuario("juliagviu", "caca123"));
//        System.out.println(InterfazDatos.instancia().autentificarUsuario("juliagviu", "1234"));
//        System.out.println(InterfazDatos.instancia().autentificarUsuario("serizba","caca"));


//        usuarios.add(julia);
//        usuarios.add(sergio);
//        usuarios.add(manolo);
//        usuarios.add(julian);
//        PartidaVO p = new PartidaVO(timeInicio,publica,usuarios);
//        InterfazDatos.instancia().crearNuevaPartida(p);
//
//        // darle una paliza a julia
//        p.setTimeFin(new Timestamp(new java.util.Date().getTime()));
//        p.setGanador('2');
//        List<Integer> cuarentas = new ArrayList<>();
//        cuarentas.add(0); cuarentas.add(1); cuarentas.add(0); cuarentas.add(0);
//        List<Integer> veintes = new ArrayList<>();
//        veintes.add(0); veintes.add(0); veintes.add(0); veintes.add(0);
//        p.setCuarentas(cuarentas);
//        p.setVeintes(veintes);
//        p.setPuntos1(1);
//        p.setPuntos2(102);
//        p.setAbandonador(3);
//        //InterfazDatos.instancia().finalizarPartida(p);
//
//
//        //partida 2
        Timestamp timeInicio = new Timestamp(new java.util.Date().getTime());
        List<UsuarioVO> usuarios = new ArrayList<>();
        UsuarioVO jul = new UsuarioVO();
        jul.setUsername("juliagviu");
        UsuarioVO ser = new UsuarioVO();
        ser.setUsername("serizba");
        usuarios.add(jul);
        usuarios.add(ser);
        PartidaVO p = new PartidaVO(timeInicio, true,usuarios);
        BigInteger id = new BigInteger("1");
        p.setId(id);
//        InterfazDatos.instancia().crearNuevaPartida(p);
//
//        // darle una paliza a julia
        p.setTimeFin(new Timestamp(new java.util.Date().getTime()));
        p.setGanador('1');
        List<Integer> cuarentas = new ArrayList<>();
        cuarentas.add(0); cuarentas.add(1);
        List<Integer> veintes = new ArrayList<>();
        veintes.add(0); veintes.add(0);
        p.setCuarentas(cuarentas);
        p.setVeintes(veintes);
        p.setPuntos1(1);
        p.setPuntos2(102);
        p.setAbandonador(0);
        InterfazDatos.instancia().finalizarPartida(p);
//
//        ArrayList<PartidaVO> partis = InterfazDatos.instancia().obtenerPartidasPublicasCurso();
//        for (PartidaVO pp : partis) {
//            System.out.println("---------"+ pp.getId() + "-------");
//            for (UsuarioVO uu : pp.getUsuarios()) {
//                System.out.print(uu.getUsername() + " ");
//                System.out.println();
//            }
//        }
//

        LigaVO nueval = new LigaVO("ligaDiamantes", "La mejor liga super chachi guay de todas", 0, 10);
        LigaVO liga1 = new LigaVO();
        liga1.setNombre("liga1");
        liga1.setPorcentajeMin(10);
        InterfazDatos.instancia().modificarDatosLiga(liga1);
        InterfazDatos.instancia().crearLiga(nueval);

        LigaVO miliga = InterfazDatos.instancia().obtenerDatosLiga("ligaDiamantes");
        System.out.println("-----------MI LIGA---------");
        System.out.println("nombre: " + miliga.getNombre() + " descripcion: " + miliga.getDescripcion() + " pmin: " + miliga.getPorcentajeMin() + " pmax: " + miliga.getPorcentajeMax());

//        InterfazDatos.instancia().eliminarLiga("liga2");
        InterfazDatos.instancia().eliminarLiga("ligaDiamantes");
        
        ArrayList<StatsUsuarioVO> miclasi = new ArrayList<>();
        miclasi = InterfazDatos.instancia().obtenerClasificacionLiga("liga2");

        for (StatsUsuarioVO su : miclasi) {
            System.out.println("usuario: " + su.getUsername() + "   puntuacion: " + su.getPuntuacion());
        }

        StatsUsuarioVO su = new StatsUsuarioVO("juliagviu");

        su = InterfazDatos.instancia().obtenerTodasStatsUsuario("juliagviu");
        System.out.println("--------------- todas las stats de juliagviu --------------");
        System.out.println("puntuacion: " + su.getPuntuacion() + " divisa: " + su.getDivisa() + " mi puesto: "+ su.getPuesto());
        System.out.println("ligaActual: " + su.getLigaActual() + " ligaMaxima: " + su.getLigaMaxima());
        System.out.println("ganadas: " + su.getGanadas() + " perdidas: " + su.getPerdidas() + " teabandonaron "+ su.getTeAbandonaron() + " abandonaste: " + su.getAbandonaste());
    }
}
