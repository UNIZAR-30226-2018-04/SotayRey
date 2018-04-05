package test.java;

/**
 * @Autores: Crisan, Marius Sorin; Ignacio Bitrian; Victor Soria
 * @Fecha: 24-03-18
 * @Fichero: Fichero de pruebas con JUnit de la clase LogicaPartida
 */

import main.java.*;

import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runners.MethodSorters;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.*;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class LogicaPartidaTest {

    @Rule
    public ExpectedException exception = ExpectedException.none();

    /**
     * Prueba que asigna primera 6 cartas a cada jugador y que el triunfo es
     * la siguiente carta
     */
    @Test
    public void prueba1CrearPartida(){
        try {
            ArrayList<String> jugadores = new ArrayList<>(Arrays.asList("j1",
                    "j2", "j3", "j4"));
            LogicaPartida logica = new LogicaPartida(jugadores);
            EstadoPartida estado = logica.getEstado();
            ArrayList<Carta> cartas = estado.getMazo();
            EstadoPartida resultado = logica.crearPartida();

            for (int i = 0; i < 6; i++) {
                for (String j: jugadores) {
                    Carta c = cartas.remove(0);
                    assertTrue("Asignada mal carta: " + c + " a jugador: "
                            + jugadores, c.equals(resultado.getCartasEnMano(j)
                            .get(i)));
                    System.out.println("Superado... carta: " + c + " asignada" +
                            " a: " + j);
                }
            }
            assertTrue("Triunfo diferente", resultado.getTriunfo().equals
                    (cartas.remove(0)));
            System.out.println("Superado... triunfo asigndo correctamente");
            System.out.println("Superado... crearPartida");
        } catch (Exception e){
            fail();
        }
    }


    /**
     * Prueba que hace cantar a un jugador
     */
    @Test
    public void prueba2cantar(){
        try {
            ArrayList<String> jugadores = new ArrayList<>(Arrays.asList("j1",
                    "j2", "j3", "j4"));
            LogicaPartida logica = new LogicaPartida(jugadores);
            EstadoPartida estado = logica.getPunteroAEstado();



            Carta c = new Carta(7, "E");
            estado.setTriunfo(c);

            ArrayList<Carta> cartas = new ArrayList<>();
            cartas.add(new Carta(10, "E"));
            cartas.add(new Carta(12, "E"));
            cartas.add(new Carta(10, "O"));
            cartas.add(new Carta(12, "O"));
            cartas.add(new Carta(7, "O"));
            cartas.add(new Carta(2, "O"));

            darCartas(estado,"j1",cartas);

            logica.cantar("j1");
            exception.expect(ExceptionNoPuedesCantar.class);

        } catch (ExceptionNoPuedesCantar e){
            System.out.println("Superado... no suma al no haber matado");
        } catch (ExceptionCartaIncorrecta | ExceptionCartaYaExiste |
                ExceptionNumeroMaximoCartas | ExceptionJugadorIncorrecto  |
                ExceptionRondaNoAcabada | ExceptionEquipoIncompleto e1) {
            fail("Excepción incorrecta");
        }

        try {
            ArrayList<String> jugadores = new ArrayList<>(Arrays.asList("j1",
                    "j2", "j3", "j4"));
            LogicaPartida logica = new LogicaPartida(jugadores);
            EstadoPartida estado = logica.getPunteroAEstado();

            Carta c = new Carta(7, "E");
            estado.setTriunfo(c);

            ArrayList<Carta> cartas = new ArrayList<>();
            cartas.add(new Carta(11, "E"));
            cartas.add(new Carta(12, "E"));
            cartas.add(new Carta(11, "O"));
            cartas.add(new Carta(12, "O"));
            cartas.add(new Carta(7, "O"));
            cartas.add(new Carta(2, "O"));

            darCartas(estado,"j1",cartas);

            estado.setGanadorUltimaRonda(1);
            logica.cantar("j1");
            exception.expect(ExceptionNoPuedesCantar.class);

        }
        catch (ExceptionNoPuedesCantar e){
            System.out.println("Superado... no hay nada que cantar");
        } catch (ExceptionCartaIncorrecta | ExceptionCartaYaExiste |
                ExceptionNumeroMaximoCartas | ExceptionJugadorIncorrecto  |
                ExceptionRondaNoAcabada | ExceptionEquipoIncompleto e1) {
            fail("Excepción incorrecta");
        }
        try {
            ArrayList<String> jugadores = new ArrayList<>(Arrays.asList("j1",
                    "j2", "j3", "j4"));
            LogicaPartida logica = new LogicaPartida(jugadores);
            EstadoPartida estado = logica.getPunteroAEstado();

            Carta c = new Carta(7, "E");
            estado.setTriunfo(c);

            ArrayList<Carta> cartas = new ArrayList<>();
            cartas.add(new Carta(10, "E"));
            cartas.add(new Carta(12, "E"));
            cartas.add(new Carta(10, "O"));
            cartas.add(new Carta(12, "O"));
            cartas.add(new Carta(7, "O"));
            cartas.add(new Carta(2, "O"));

            darCartas(estado,"j1",cartas);

            estado.setGanadorUltimaRonda(0);

            logica.cantar("j1");

            assertEquals("Debería sumar 60 puntos",60,logica.consultarPuntos("j1"));
            System.out.println("Superado... el jugador tiene 60 puntos");
        }
        catch ( ExceptionNoPuedesCantar | ExceptionCartaIncorrecta | ExceptionCartaYaExiste |
                ExceptionNumeroMaximoCartas | ExceptionJugadorIncorrecto  |
                ExceptionRondaNoAcabada | ExceptionEquipoIncompleto e1) {
            fail("Excepción incorrecta");
        }
    }

    /**
     * Prueba que hace lanzar carta al jugador cuando juegan 2
     */
    @Test
    public void prueba2lanzarCarta2Jugadores() {
        ArrayList<String> jugadores = new ArrayList<>(Arrays.asList("j1", "j2"));
        LogicaPartida logica;
        try {
            logica = new LogicaPartida(jugadores);
            prepararArrastre(logica,0);
            //triunfo es oros
            Carta c = new Carta(10,"E");
            logica.lanzarCarta("j1",c);
            c = new Carta(1,"O");
            logica.lanzarCarta("j2",c);

            exception.expect(ExceptionCartaIncorrecta.class);

        }catch (ExceptionCartaIncorrecta e) {
            System.out.println("Superado... no se puede lanzar otra carta teniendo del mismo palo");
        } catch (ExceptionTurnoIncorrecto | ExceptionCartaYaExiste |
                ExceptionNumeroMaximoCartas | ExceptionJugadorIncorrecto
                | ExceptionEquipoIncompleto | ExceptionJugadorSinCarta e1) {
            fail("Excepción incorrecta");
        }

        try {
            logica = new LogicaPartida(jugadores);
            prepararArrastre(logica,0);
            Carta c = new Carta(10,"E");
            logica.lanzarCarta("j1",c);
            c = new Carta(2,"E");
            logica.lanzarCarta("j2",c);

            exception.expect(ExceptionCartaIncorrecta.class);

        }catch (ExceptionCartaIncorrecta e) {
            System.out.println("Superado... no se puede lanzar si se puede matar");
        } catch (ExceptionTurnoIncorrecto | ExceptionCartaYaExiste |
                ExceptionNumeroMaximoCartas | ExceptionJugadorIncorrecto  |
                ExceptionEquipoIncompleto | ExceptionJugadorSinCarta e1) {
            fail("Excepción incorrecta");
        }

        try {
            logica = new LogicaPartida(jugadores);
            prepararArrastre(logica,1);
            Carta c = new Carta(10,"E");
            logica.lanzarCarta("j1",c);
            c = new Carta(2,"C");
            logica.lanzarCarta("j2",c);

            exception.expect(ExceptionCartaIncorrecta.class);

        }catch (ExceptionCartaIncorrecta e) {
            System.out.println("Superado... no se puede lanzar si se tiene un triunfo");
        } catch (ExceptionTurnoIncorrecto | ExceptionCartaYaExiste |
                ExceptionNumeroMaximoCartas | ExceptionJugadorIncorrecto  |
                ExceptionEquipoIncompleto | ExceptionJugadorSinCarta e1) {
            fail("Excepción incorrecta");
        }
        try {
            logica = new LogicaPartida(jugadores);
            prepararArrastre(logica,0);
            //triunfo es oros
            Carta c = new Carta(10,"E");
            logica.lanzarCarta("j1",c);
            c = new Carta(1,"E");
            logica.lanzarCarta("j2",c);
            System.out.println("Superado... se lanza correctamente una carta del mismo palo superior");

        } catch (ExceptionCartaIncorrecta |ExceptionTurnoIncorrecto |
                ExceptionCartaYaExiste | ExceptionNumeroMaximoCartas |
                ExceptionJugadorIncorrecto | ExceptionEquipoIncompleto |
                ExceptionJugadorSinCarta e1) {
            fail("Excepción incorrecta");
        }
        try {
            logica = new LogicaPartida(jugadores);
            prepararArrastre(logica,2);
            //triunfo es oros
            Carta c = new Carta(10,"E");
            logica.lanzarCarta("j1",c);
            c = new Carta(2,"E");
            logica.lanzarCarta("j2",c);
            System.out.println("Superado... se lanza una carta del mismo palo si no supera");

        } catch (ExceptionCartaIncorrecta |ExceptionTurnoIncorrecto |
                ExceptionCartaYaExiste | ExceptionNumeroMaximoCartas |
                ExceptionJugadorIncorrecto | ExceptionEquipoIncompleto |
                ExceptionJugadorSinCarta e1) {
            fail("Excepción incorrecta");
        }
        try {
            logica = new LogicaPartida(jugadores);
            prepararArrastre(logica,1);
            //triunfo es oros
            Carta c = new Carta(10,"E");
            logica.lanzarCarta("j1",c);
            c = new Carta(1,"O");
            logica.lanzarCarta("j2",c);
            System.out.println("Superado... se lanza triunfo si no tiene del mismo palo");

        } catch (ExceptionCartaIncorrecta |ExceptionTurnoIncorrecto |
                ExceptionCartaYaExiste | ExceptionNumeroMaximoCartas |
                ExceptionJugadorIncorrecto | ExceptionEquipoIncompleto |
                ExceptionJugadorSinCarta e1) {
            fail("Excepción incorrecta");
        }
        try {
            logica = new LogicaPartida(jugadores);
            prepararArrastre(logica,3);
            //triunfo es oros
            Carta c = new Carta(10,"E");
            logica.lanzarCarta("j1",c);
            c = new Carta(3,"B");
            logica.lanzarCarta("j2",c);
            System.out.println("Superado... se lanza cualquiera si no se tiene del palo" +
                    " ni de triunfo");

        } catch (ExceptionCartaIncorrecta |ExceptionTurnoIncorrecto |
                ExceptionCartaYaExiste | ExceptionNumeroMaximoCartas |
                ExceptionJugadorIncorrecto | ExceptionEquipoIncompleto |
                ExceptionJugadorSinCarta e1) {
            fail("Excepción incorrecta");
        }
    }

    

    /**************************** FUNCIONES AUXILIARES ************************/

    private void darCartas(EstadoPartida estado, String j, ArrayList<Carta> cartas) throws ExceptionCartaYaExiste,
            ExceptionJugadorIncorrecto, ExceptionNumeroMaximoCartas {
        for(int i=0; i<cartas.size();i++){
            estado.anyadirCartaJugador(j,cartas.get(i));
        }
    }

    private void prepararArrastre(LogicaPartida logica, int i) throws ExceptionCartaYaExiste,
            ExceptionJugadorIncorrecto, ExceptionNumeroMaximoCartas, ExceptionCartaIncorrecta {

        EstadoPartida estado = logica.getPunteroAEstado();

        Carta c = new Carta(7, "O");
        estado.setTriunfo(c);
        ArrayList<Carta> cartas = new ArrayList<>();
        cartas.add(new Carta(10, "E"));
        cartas.add(new Carta(12, "E"));
        cartas.add(new Carta(10, "O"));
        cartas.add(new Carta(12, "O"));
        cartas.add(new Carta(7, "O"));
        cartas.add(new Carta(2, "O"));


        darCartas(estado,"j1",cartas);

        cartas = new ArrayList<>();
        if(i==0) {
            cartas.add(new Carta(1, "E"));
            cartas.add(new Carta(2, "E"));
            cartas.add(new Carta(1, "O"));
        }
        if(i==1) {
            cartas.add(new Carta(1, "C"));
            cartas.add(new Carta(2, "C"));
            cartas.add(new Carta(1, "O"));
        }
        if(i==2) {
            cartas.add(new Carta(1, "C"));
            cartas.add(new Carta(2, "E"));
            cartas.add(new Carta(1, "O"));
        }
        if(i==3) {
            cartas.add(new Carta(1, "C"));
            cartas.add(new Carta(2, "C"));
            cartas.add(new Carta(3, "C"));
        }
        cartas.add(new Carta(3, "B"));
        cartas.add(new Carta(7, "B"));
        cartas.add(new Carta(2, "B"));

        darCartas(estado,"j2",cartas);
        estado.eliminaMazo();
    }

    private void prepararArrastre2(LogicaPartida logica) throws ExceptionCartaYaExiste,
            ExceptionJugadorIncorrecto, ExceptionNumeroMaximoCartas, ExceptionCartaIncorrecta {

        EstadoPartida estado = logica.getPunteroAEstado();

        Carta c = new Carta(7, "O");
        estado.setTriunfo(c);

        ArrayList<Carta> cartas = new ArrayList<>();
        cartas.add(new Carta(10, "E"));
        cartas.add(new Carta(12, "E"));
        cartas.add(new Carta(10, "O"));
        cartas.add(new Carta(12, "O"));
        cartas.add(new Carta(7, "O"));
        cartas.add(new Carta(2, "O"));

        darCartas(estado,"j1",cartas);

        cartas = new ArrayList<>();
        cartas.add(new Carta(1, "E"));
        cartas.add(new Carta(3, "E"));
        cartas.add(new Carta(1, "O"));
        cartas.add(new Carta(3, "O"));
        cartas.add(new Carta(7, "O"));
        cartas.add(new Carta(2, "O"));

        darCartas(estado,"j2",cartas);

        cartas = new ArrayList<>();
        cartas.add(new Carta(10, "E"));
        cartas.add(new Carta(12, "E"));
        cartas.add(new Carta(10, "O"));
        cartas.add(new Carta(12, "O"));
        cartas.add(new Carta(7, "O"));
        cartas.add(new Carta(2, "O"));

        darCartas(estado,"j3",cartas);

        cartas = new ArrayList<>();
        cartas.add(new Carta(4, "E"));
        cartas.add(new Carta(2, "E"));
        cartas.add(new Carta(6, "O"));
        cartas.add(new Carta(5, "O"));
        cartas.add(new Carta(4, "O"));
        cartas.add(new Carta(2, "O"));

        darCartas(estado,"j4",cartas);
        estado.eliminaMazo();
    }
}
