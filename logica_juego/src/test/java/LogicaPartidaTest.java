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
    public void prueba3lanzarCarta2Jugadores() {
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
            EstadoPartida estado = logica.getPunteroAEstado();
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

    /**
     * Prueba que hace lanzar carta al jugador cuando juegan 4
     */
    @Test
    public void prueba4lanzarCarta4Jugadores() {
        ArrayList<String> jugadores = new ArrayList<>(Arrays.asList("j1", "j2", "j3", "j4"));
        LogicaPartida logica;
        try {
            logica = new LogicaPartida(jugadores);
            prepararArrastre2(logica, 0);
            //triunfo es oros
            Carta c = new Carta(10, "E");
            logica.lanzarCarta("j1", c);
            c = new Carta(3, "E");
            logica.lanzarCarta("j2", c);
            c = new Carta(7, "B");
            logica.lanzarCarta("j3", c);

            exception.expect(ExceptionCartaIncorrecta.class);

        } catch (ExceptionCartaIncorrecta e) {
            System.out.println("Superado... no se puede lanzar otra carta teniendo del mismo palo");
        } catch (ExceptionTurnoIncorrecto | ExceptionCartaYaExiste |
                ExceptionNumeroMaximoCartas | ExceptionJugadorIncorrecto
                | ExceptionEquipoIncompleto | ExceptionJugadorSinCarta e1) {
            fail("Excepción incorrecta");
        }
        try {
            logica = new LogicaPartida(jugadores);
            prepararArrastre2(logica, 0);
            //triunfo es oros
            Carta c = new Carta(10, "E");
            logica.lanzarCarta("j1", c);
            c = new Carta(3, "E");
            logica.lanzarCarta("j2", c);
            c = new Carta(7, "E");
            logica.lanzarCarta("j3", c);

            exception.expect(ExceptionCartaIncorrecta.class);

        } catch (ExceptionCartaIncorrecta e) {
            System.out.println("Superado... no se puede lanzar otra carta si hay obligacion de matar" +
                    " y el jugador puede");
        } catch (ExceptionTurnoIncorrecto | ExceptionCartaYaExiste |
                ExceptionNumeroMaximoCartas | ExceptionJugadorIncorrecto
                | ExceptionEquipoIncompleto | ExceptionJugadorSinCarta e1) {
            fail("Excepción incorrecta");
        }

        try {
            logica = new LogicaPartida(jugadores);
            prepararArrastre2(logica, 1);
            //triunfo es oros
            Carta c = new Carta(10, "E");
            logica.lanzarCarta("j1", c);
            c = new Carta(3, "E");
            logica.lanzarCarta("j2", c);
            c = new Carta(10, "B");
            logica.lanzarCarta("j3", c);

            exception.expect(ExceptionCartaIncorrecta.class);

        } catch (ExceptionCartaIncorrecta e) {
            System.out.println("Superado... no se puede lanzar otra carta si hay obligacion de matar" +
                    " y se tiene triunfo");
        } catch (ExceptionTurnoIncorrecto | ExceptionCartaYaExiste |
                ExceptionNumeroMaximoCartas | ExceptionJugadorIncorrecto
                | ExceptionEquipoIncompleto | ExceptionJugadorSinCarta e1) {
            fail("Excepción incorrecta");
        }
        try {
            logica = new LogicaPartida(jugadores);
            prepararArrastre2(logica, 2);
            //triunfo es oros
            Carta c = new Carta(5, "E");
            logica.lanzarCarta("j1", c);
            c = new Carta(6, "E");
            logica.lanzarCarta("j2", c);
            c = new Carta(7, "E");
            logica.lanzarCarta("j3", c);
            System.out.println("Superado... se lanza la carta del mismo palo para matar");


        } catch (ExceptionTurnoIncorrecto | ExceptionCartaYaExiste |
                ExceptionNumeroMaximoCartas | ExceptionJugadorIncorrecto
                | ExceptionEquipoIncompleto | ExceptionJugadorSinCarta
                | ExceptionCartaIncorrecta e1) {
            fail("Excepción incorrecta");
        }
        try {
            logica = new LogicaPartida(jugadores);
            prepararArrastre2(logica, 1);
            //triunfo es oros
            Carta c = new Carta(5, "E");
            logica.lanzarCarta("j1", c);
            c = new Carta(3, "E");
            logica.lanzarCarta("j2", c);
            c = new Carta(4, "O");
            logica.lanzarCarta("j3", c);
            System.out.println("Superado... se lanza el triunfo porque no tiene del palo inicial");


        } catch (ExceptionTurnoIncorrecto | ExceptionCartaYaExiste |
                ExceptionNumeroMaximoCartas | ExceptionJugadorIncorrecto
                | ExceptionEquipoIncompleto | ExceptionJugadorSinCarta
                | ExceptionCartaIncorrecta e1) {
            fail("Excepción incorrecta");
        }
        try {
            logica = new LogicaPartida(jugadores);
            prepararArrastre2(logica, 3);
            //triunfo es oros
            Carta c = new Carta(10, "E");
            logica.lanzarCarta("j1", c);
            c = new Carta(6, "E");
            logica.lanzarCarta("j2", c);
            c = new Carta(10, "B");
            logica.lanzarCarta("j3", c);
            System.out.println("Superado... puede lanzar cualquiera porque mata su compañero" +
                    " y no tiene del palo incial");

        } catch (ExceptionTurnoIncorrecto | ExceptionCartaYaExiste |
                ExceptionNumeroMaximoCartas | ExceptionJugadorIncorrecto
                | ExceptionEquipoIncompleto | ExceptionJugadorSinCarta
                | ExceptionCartaIncorrecta e1) {
            fail("Excepción incorrecta");
        }

    }

    /**
     * Prueba que hace cambiar el triunfo a un jugador
     */
    @Test
    public void prueba5cambiarTriunfo(){
        try {
            ArrayList<String> jugadores = new ArrayList<>(Arrays.asList("j1",
                    "j2", "j3", "j4"));
            LogicaPartida logica = new LogicaPartida(jugadores);
            EstadoPartida estado = logica.getPunteroAEstado();

            Carta c = new Carta(1, "O");
            estado.setTriunfo(c);

            ArrayList<Carta> cartas = new ArrayList<>();
            cartas.add(new Carta(10, "E"));
            cartas.add(new Carta(12, "E"));
            cartas.add(new Carta(10, "O"));
            cartas.add(new Carta(12, "O"));
            cartas.add(new Carta(7, "O"));
            cartas.add(new Carta(2, "O"));

            darCartas(estado,"j1",cartas);

            logica.cambiarCartaPorTriunfo("j1",new Carta(7, "O"));
            exception.expect(ExceptionRondaNoAcabada.class);

        } catch (ExceptionRondaNoAcabada e){
            System.out.println("Superado... no se ha ganado la última partida");
        } catch (ExceptionCartaIncorrecta | ExceptionCartaYaExiste |
                ExceptionNumeroMaximoCartas | ExceptionJugadorIncorrecto  |
                ExceptionJugadorSinCarta | ExceptionEquipoIncompleto e1) {
            fail("Excepción incorrecta");
        }
        try {
            ArrayList<String> jugadores = new ArrayList<>(Arrays.asList("j1",
                    "j2", "j3", "j4"));
            LogicaPartida logica = new LogicaPartida(jugadores);
            EstadoPartida estado = logica.getPunteroAEstado();

            Carta c = new Carta(1, "O");
            estado.setTriunfo(c);

            ArrayList<Carta> cartas = new ArrayList<>();
            cartas.add(new Carta(10, "E"));
            cartas.add(new Carta(12, "E"));
            cartas.add(new Carta(10, "O"));
            cartas.add(new Carta(12, "O"));
            cartas.add(new Carta(7, "O"));
            cartas.add(new Carta(2, "C"));

            darCartas(estado,"j1",cartas);
            estado.setGanadorUltimaRonda(0);
            logica.lanzarCarta("j1",new Carta(10, "E"));

            logica.cambiarCartaPorTriunfo("j1",new Carta(7, "O"));
            exception.expect(ExceptionRondaNoAcabada.class);

        } catch (ExceptionRondaNoAcabada e){
            System.out.println("Superado... no es el fin de la ronda");
        } catch (ExceptionCartaIncorrecta | ExceptionCartaYaExiste |
                ExceptionNumeroMaximoCartas | ExceptionJugadorIncorrecto  |
                ExceptionJugadorSinCarta | ExceptionEquipoIncompleto |
                ExceptionTurnoIncorrecto e1) {
            fail("Excepción incorrecta");
        }

        try {
            ArrayList<String> jugadores = new ArrayList<>(Arrays.asList("j1",
                    "j2", "j3", "j4"));
            LogicaPartida logica = new LogicaPartida(jugadores);
            EstadoPartida estado = logica.getPunteroAEstado();

            Carta c = new Carta(1, "O");
            estado.setTriunfo(c);

            ArrayList<Carta> cartas = new ArrayList<>();
            cartas.add(new Carta(10, "E"));
            cartas.add(new Carta(12, "E"));
            cartas.add(new Carta(10, "O"));
            cartas.add(new Carta(12, "O"));
            cartas.add(new Carta(7, "O"));
            cartas.add(new Carta(2, "O"));

            darCartas(estado,"j1",cartas);
            estado.eliminaMazo();
            estado.setGanadorUltimaRonda(0);

            logica.cambiarCartaPorTriunfo("j1",new Carta(7, "O"));
            exception.expect(ExceptionRondaNoAcabada.class);

        } catch (ExceptionRondaNoAcabada e){
            System.out.println("Superado... no quedan cartas en el mazo");
        } catch (ExceptionCartaIncorrecta | ExceptionCartaYaExiste |
                ExceptionNumeroMaximoCartas | ExceptionJugadorIncorrecto  |
                ExceptionJugadorSinCarta | ExceptionEquipoIncompleto e1) {
            fail("Excepción incorrecta");
        }
        try {
            ArrayList<String> jugadores = new ArrayList<>(Arrays.asList("j1",
                    "j2", "j3", "j4"));
            LogicaPartida logica = new LogicaPartida(jugadores);
            EstadoPartida estado = logica.getPunteroAEstado();

            Carta c = new Carta(1, "O");
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

            logica.cambiarCartaPorTriunfo("j1",new Carta(10, "O"));
            exception.expect(ExceptionCartaIncorrecta.class);
        } catch (ExceptionCartaIncorrecta e){
            System.out.println("Superado... no es el siete de triunfo");
        }catch (ExceptionCartaYaExiste |
                ExceptionNumeroMaximoCartas | ExceptionJugadorIncorrecto  |
                ExceptionRondaNoAcabada | ExceptionEquipoIncompleto |
                ExceptionJugadorSinCarta e1) {
            fail("Excepción incorrecta");
        }


        try {
            ArrayList<String> jugadores = new ArrayList<>(Arrays.asList("j1",
                    "j2", "j3", "j4"));
            LogicaPartida logica = new LogicaPartida(jugadores);
            EstadoPartida estado = logica.getPunteroAEstado();

            Carta c = new Carta(1, "O");
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

            logica.cambiarCartaPorTriunfo("j1",new Carta(7, "O"));
            System.out.println("Superado... no suma al no haber matado");
        } catch (ExceptionCartaIncorrecta | ExceptionCartaYaExiste |
                ExceptionNumeroMaximoCartas | ExceptionJugadorIncorrecto  |
                ExceptionRondaNoAcabada | ExceptionEquipoIncompleto |
                ExceptionJugadorSinCarta e1) {
            fail("Excepción incorrecta");
        }

    }


    /**
     * Pruebas de la función siguienteRonda
     */
    @Test
    public void prueba6siguienteRonda() {

            ArrayList<String> jugadores = new ArrayList<>(Arrays.asList("j1", "j2", "j3", "j4"));
            LogicaPartida logica;
            try {
                logica = new LogicaPartida(jugadores);
                prepararEstado(logica);
                //triunfo es oros
                Carta c = new Carta(7, "E");
                logica.lanzarCarta("j1", c);
                c = new Carta(3, "E");
                logica.lanzarCarta("j2", c);
                c = new Carta(7, "B");
                logica.lanzarCarta("j3", c);
                logica.siguienteRonda();
                exception.expect(ExceptionRondaNoAcabada.class);

            } catch (ExceptionRondaNoAcabada e) {
                System.out.println("Superado... no se puede ir a la siguiente ronda si no ha acabado");
            } catch (ExceptionTurnoIncorrecto | ExceptionCartaYaExiste |
                    ExceptionNumeroMaximoCartas | ExceptionJugadorIncorrecto
                    | ExceptionEquipoIncompleto | ExceptionJugadorSinCarta |
                    ExceptionCartaIncorrecta | ExceptionPartidaFinalizada |
                    ExceptionMazoVacio | ExceptionDeVueltas e1) {
                fail("Excepción incorrecta");
            }
            try {
                logica = new LogicaPartida(jugadores);
                prepararEstado(logica);
                //triunfo es oros
                Carta c = new Carta(7, "E");
                logica.lanzarCarta("j1", c);
                c = new Carta(2, "E");
                logica.lanzarCarta("j2", c);
                c = new Carta(7, "B");
                logica.lanzarCarta("j3", c);
                c = new Carta(5, "C");
                logica.lanzarCarta("j4", c);
                logica.siguienteRonda();

                EstadoPartida estado = logica.getPunteroAEstado();
                assertTrue("Turno del siguiente incorrecto: " + estado.getTurno(),
                        estado.getTurno() == estado.getGanadorUltimaRonda() && estado.getTurno() == 0);
                assertTrue("Puntuacion equivocada: " + logica.consultarPuntos("j1"),
                        logica.consultarPuntos("j1") == logica.consultarPuntos("j3") &&
                                logica.consultarPuntos("j1") == 0);
                System.out.println("Superado... Nadie mata al primero");

            } catch (ExceptionTurnoIncorrecto | ExceptionCartaYaExiste |
                    ExceptionNumeroMaximoCartas | ExceptionJugadorIncorrecto
                    | ExceptionEquipoIncompleto | ExceptionJugadorSinCarta |
                    ExceptionCartaIncorrecta | ExceptionPartidaFinalizada |
                    ExceptionMazoVacio | ExceptionDeVueltas |
                    ExceptionRondaNoAcabada e1) {
                fail("Excepción incorrecta");
            }
            try {
                logica = new LogicaPartida(jugadores);
                prepararEstado(logica);
                //triunfo es oros
                Carta c = new Carta(7, "E");
                logica.lanzarCarta("j1", c);
                c = new Carta(3, "E");
                logica.lanzarCarta("j2", c);
                c = new Carta(7, "B");
                logica.lanzarCarta("j3", c);
                c = new Carta(5, "C");
                logica.lanzarCarta("j4", c);
                logica.siguienteRonda();

                EstadoPartida estado = logica.getPunteroAEstado();
                assertTrue("Turno del siguiente incorrecto: " + estado.getTurno(),
                        estado.getTurno() == estado.getGanadorUltimaRonda() && estado.getTurno() == 1);
                assertTrue("Puntuacion equivocada: " + logica.consultarPuntos("j2"),
                        logica.consultarPuntos("j2") == logica.consultarPuntos("j4") &&
                                logica.consultarPuntos("j2") == 10);
                System.out.println("Superado... j2 mata con una carta del palo inicial superior");

            } catch (ExceptionTurnoIncorrecto | ExceptionCartaYaExiste |
                    ExceptionNumeroMaximoCartas | ExceptionJugadorIncorrecto
                    | ExceptionEquipoIncompleto | ExceptionJugadorSinCarta |
                    ExceptionCartaIncorrecta | ExceptionPartidaFinalizada |
                    ExceptionMazoVacio | ExceptionDeVueltas |
                    ExceptionRondaNoAcabada e1) {
                fail("Excepción incorrecta");
            }
            try {
                logica = new LogicaPartida(jugadores);
                prepararEstado(logica);
                //triunfo es oros
                Carta c = new Carta(7, "E");
                logica.lanzarCarta("j1", c);
                c = new Carta(3, "E");
                logica.lanzarCarta("j2", c);
                c = new Carta(5, "O");
                logica.lanzarCarta("j3", c);
                c = new Carta(5, "C");
                logica.lanzarCarta("j4", c);
                logica.siguienteRonda();

                EstadoPartida estado = logica.getPunteroAEstado();
                assertTrue("Turno del siguiente incorrecto: " + estado.getTurno(),
                        estado.getTurno() == estado.getGanadorUltimaRonda() && estado.getTurno() == 2);
                assertTrue("Puntuacion equivocada: " + logica.consultarPuntos("j1"),
                        logica.consultarPuntos("j1") == logica.consultarPuntos("j3") &&
                                logica.consultarPuntos("j1") == 10);
                System.out.println("Superado... j3 mata con tiunfo");

            } catch (ExceptionTurnoIncorrecto | ExceptionCartaYaExiste |
                    ExceptionNumeroMaximoCartas | ExceptionJugadorIncorrecto
                    | ExceptionEquipoIncompleto | ExceptionJugadorSinCarta |
                    ExceptionCartaIncorrecta | ExceptionPartidaFinalizada |
                    ExceptionMazoVacio | ExceptionDeVueltas |
                    ExceptionRondaNoAcabada e1) {
                fail("Excepción incorrecta");
            }
            try {
                logica = new LogicaPartida(jugadores);
                prepararEstado(logica);
                //triunfo es oros
                Carta c = new Carta(7, "E");
                logica.lanzarCarta("j1", c);
                c = new Carta(3, "E");
                logica.lanzarCarta("j2", c);
                c = new Carta(5, "O");
                logica.lanzarCarta("j3", c);
                c = new Carta(6, "O");
                logica.lanzarCarta("j4", c);
                logica.siguienteRonda();

                EstadoPartida estado = logica.getPunteroAEstado();
                assertTrue("Turno del siguiente incorrecto: " + estado.getTurno(),
                        estado.getTurno() == estado.getGanadorUltimaRonda() && estado.getTurno() == 3);
                assertTrue("Puntuacion equivocada: " + logica.consultarPuntos("j2"),
                        logica.consultarPuntos("j2") == logica.consultarPuntos("j4") &&
                                logica.consultarPuntos("j2") == 10);
                System.out.println("Superado... j4 mata con un triunfo aún mayor");

            } catch (ExceptionTurnoIncorrecto | ExceptionCartaYaExiste |
                    ExceptionNumeroMaximoCartas | ExceptionJugadorIncorrecto
                    | ExceptionEquipoIncompleto | ExceptionJugadorSinCarta |
                    ExceptionCartaIncorrecta | ExceptionPartidaFinalizada |
                    ExceptionMazoVacio | ExceptionDeVueltas |
                    ExceptionRondaNoAcabada e1) {
                fail("Excepción incorrecta");
            }
            try {
                logica = new LogicaPartida(jugadores);
                EstadoPartida estado = logica.getPunteroAEstado();
                Carta c = new Carta(7, "O");
                estado.setTriunfo(c);

                ArrayList<Carta> cartas = new ArrayList<>();
                cartas.add(new Carta(7, "E"));
                darCartas(estado, "j1", cartas);

                cartas = new ArrayList<>();
                cartas.add(new Carta(3, "E"));
                darCartas(estado, "j2", cartas);

                cartas = new ArrayList<>();
                cartas.add(new Carta(5, "O"));
                darCartas(estado, "j3", cartas);

                cartas = new ArrayList<>();
                cartas.add(new Carta(6, "O"));
                darCartas(estado, "j4", cartas);
                estado.eliminaMazo();

                c = new Carta(7, "E");
                logica.lanzarCarta("j1", c);
                c = new Carta(3, "E");
                logica.lanzarCarta("j2", c);
                c = new Carta(5, "O");
                logica.lanzarCarta("j3", c);
                c = new Carta(6, "O");
                logica.lanzarCarta("j4", c);
                try {
                    logica.siguienteRonda();
                    exception.expect(ExceptionDeVueltas.class);
                } catch (ExceptionDeVueltas exceptionDeVueltas) {
                    assertTrue("Turno del siguiente incorrecto: " + estado.getTurno(),
                            estado.getTurno() == 0);
                    assertTrue("Ganador de la ultima ronda: " + estado.getGanadorUltimaRonda(),
                            estado.getGanadorUltimaRonda() == -1);
                    assertTrue("Va de vueltas: " + estado.getGanadorUltimaRonda(),
                            logica.getDeVueltas() == true);
                    assertTrue("Puntuacion equivocada: " + logica.consultarPuntos("j2"),
                            logica.consultarPuntos("j2") == logica.consultarPuntos("j4") &&
                                    logica.consultarPuntos("j2") == 20);
                    System.out.println("Superado... en la ultima baza si no se superan los puntos van de vueltas");
                }
            } catch (ExceptionTurnoIncorrecto | ExceptionCartaYaExiste |
                    ExceptionNumeroMaximoCartas | ExceptionJugadorIncorrecto
                    | ExceptionEquipoIncompleto | ExceptionJugadorSinCarta |
                    ExceptionCartaIncorrecta | ExceptionPartidaFinalizada |
                    ExceptionMazoVacio |
                    ExceptionRondaNoAcabada e1) {
                fail("Excepción incorrecta");
            }
            try {
                logica = new LogicaPartida(jugadores);
                EstadoPartida estado = logica.getPunteroAEstado();
                Carta c = new Carta(7, "O");
                estado.setTriunfo(c);

                ArrayList<Carta> cartas = new ArrayList<>();
                cartas.add(new Carta(10, "O"));
                cartas.add(new Carta(12, "O"));
                cartas.add(new Carta(1, "O"));
                darCartas(estado, "j1", cartas);

                cartas = new ArrayList<>();
                cartas.add(new Carta(12, "E"));
                cartas.add(new Carta(3, "E"));
                cartas.add(new Carta(1, "E"));
                darCartas(estado, "j2", cartas);

                cartas = new ArrayList<>();
                cartas.add(new Carta(12, "B"));
                cartas.add(new Carta(3, "B"));
                cartas.add(new Carta(1, "B"));
                darCartas(estado, "j3", cartas);

                cartas = new ArrayList<>();
                cartas.add(new Carta(12, "C"));
                cartas.add(new Carta(3, "C"));
                cartas.add(new Carta(1, "C"));
                darCartas(estado, "j4", cartas);
                estado.eliminaMazo();

                estado.getJugadoresId();

                c = new Carta(1, "O");
                logica.lanzarCarta("j1", c);
                c = new Carta(1, "E");
                logica.lanzarCarta("j2", c);
                c = new Carta(1, "B");
                logica.lanzarCarta("j3", c);
                c = new Carta(1, "C");
                logica.lanzarCarta("j4", c);
                logica.siguienteRonda();

                logica.cantar("j1");

                c = new Carta(10, "O");
                logica.lanzarCarta("j1", c);
                c = new Carta(3, "E");
                logica.lanzarCarta("j2", c);
                c = new Carta(3, "B");
                logica.lanzarCarta("j3", c);
                c = new Carta(3, "C");
                logica.lanzarCarta("j4", c);
                logica.siguienteRonda();

                c = new Carta(12, "O");
                logica.lanzarCarta("j1", c);
                c = new Carta(12, "E");
                logica.lanzarCarta("j2", c);
                c = new Carta(12, "B");
                logica.lanzarCarta("j3", c);
                c = new Carta(12, "C");
                logica.lanzarCarta("j4", c);

                try {
                    logica.siguienteRonda();
                    exception.expect(ExceptionDeVueltas.class);
                } catch (ExceptionPartidaFinalizada exceptionDeVueltas) {

                    ArrayList<String> ganadores = new ArrayList<>();
                    ganadores.add("j1");
                    ganadores.add("j3");
                    assertTrue("Valor de ganadores: " + logica.getGanadoresPartida(),
                            logica.getGanadoresPartida().equals(ganadores));
                    System.out.println("Superado... la partida finaliza al superarse los 100 puntos");
                }
            } catch (ExceptionTurnoIncorrecto | ExceptionCartaYaExiste |
                    ExceptionNumeroMaximoCartas | ExceptionJugadorIncorrecto
                    | ExceptionEquipoIncompleto | ExceptionJugadorSinCarta |
                    ExceptionCartaIncorrecta | ExceptionNoPuedesCantar |
                    ExceptionMazoVacio | ExceptionDeVueltas | ExceptionPartidaSinAcabar |
                    ExceptionRondaNoAcabada | ExceptionPartidaFinalizada e1) {
                fail("Excepción incorrecta");
            }

    }

    /**************************** FUNCIONES AUXILIARES ************************/

    /**
     * Reparte las cartas "cartas" al jugador j
     * @param estado
     * @param j
     * @param cartas
     * @throws ExceptionCartaYaExiste
     * @throws ExceptionJugadorIncorrecto
     * @throws ExceptionNumeroMaximoCartas
     */
    private void darCartas(EstadoPartida estado, String j, ArrayList<Carta> cartas) throws ExceptionCartaYaExiste,
            ExceptionJugadorIncorrecto, ExceptionNumeroMaximoCartas {
        for(int i=0; i<cartas.size();i++){
            estado.anyadirCartaJugador(j,cartas.get(i));
        }
    }

    /**
     * Prepara un estado concreto durante el arrastre para dos jugadores
     * @param logica
     * @param i
     * @throws ExceptionCartaYaExiste
     * @throws ExceptionJugadorIncorrecto
     * @throws ExceptionNumeroMaximoCartas
     * @throws ExceptionCartaIncorrecta
     */
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

    /**
     * Prepara un estado concreto durante el arrastre para cuatro jugadores
     * @param logica
     * @param i
     * @throws ExceptionCartaYaExiste
     * @throws ExceptionJugadorIncorrecto
     * @throws ExceptionNumeroMaximoCartas
     * @throws ExceptionCartaIncorrecta
     */
    private void prepararArrastre2(LogicaPartida logica,int i) throws ExceptionCartaYaExiste,
            ExceptionJugadorIncorrecto, ExceptionNumeroMaximoCartas, ExceptionCartaIncorrecta {

        EstadoPartida estado = logica.getPunteroAEstado();

        Carta c = new Carta(7, "O");
        estado.setTriunfo(c);

        ArrayList<Carta> cartas = new ArrayList<>();
        cartas.add(new Carta(10, "E"));
        cartas.add(new Carta(5, "E"));
        cartas.add(new Carta(10, "O"));
        cartas.add(new Carta(12, "O"));
        cartas.add(new Carta(7, "O"));
        cartas.add(new Carta(2, "O"));

        darCartas(estado,"j1",cartas);

        cartas = new ArrayList<>();
        if(i==0 | i==1) {
            cartas.add(new Carta(3, "E"));
        }
        else if(i==2 || i==3){
            cartas.add(new Carta(6, "E"));
        }
        cartas.add(new Carta(2, "E"));
        cartas.add(new Carta(1, "O"));
        cartas.add(new Carta(3, "O"));
        cartas.add(new Carta(7, "O"));
        cartas.add(new Carta(2, "O"));

        darCartas(estado,"j2",cartas);

        cartas = new ArrayList<>();
        if(i==0 || i==2) {
            cartas.add(new Carta(7, "E"));
            cartas.add(new Carta(1, "E"));
        }
        else if(i==1){
            cartas.add(new Carta(4, "O"));
            cartas.add(new Carta(5, "O"));
        }
        else if(i==3){
            cartas.add(new Carta(4, "O"));
            cartas.add(new Carta(5, "O"));
        }
        cartas.add(new Carta(10, "B"));
        cartas.add(new Carta(12, "B"));
        cartas.add(new Carta(7, "B"));
        cartas.add(new Carta(2, "B"));

        darCartas(estado,"j3",cartas);

        cartas = new ArrayList<>();
        cartas.add(new Carta(4, "E"));
        cartas.add(new Carta(3, "C"));
        cartas.add(new Carta(6, "C"));
        cartas.add(new Carta(5, "C"));
        cartas.add(new Carta(4, "C"));
        cartas.add(new Carta(2, "C"));

        darCartas(estado,"j4",cartas);
        estado.eliminaMazo();
    }

    /**
     * Prepara un estado concreto
     * @param logica
     * @throws ExceptionCartaYaExiste
     * @throws ExceptionJugadorIncorrecto
     * @throws ExceptionNumeroMaximoCartas
     * @throws ExceptionCartaIncorrecta
     */
    private void prepararEstado(LogicaPartida logica) throws ExceptionCartaYaExiste,
            ExceptionJugadorIncorrecto, ExceptionNumeroMaximoCartas, ExceptionCartaIncorrecta {

        EstadoPartida estado = logica.getPunteroAEstado();

        Carta c = new Carta(7, "O");
        estado.setTriunfo(c);

        ArrayList<Carta> cartas = new ArrayList<>();
        cartas.add(new Carta(7, "E"));
        cartas.add(new Carta(5, "E"));
        cartas.add(new Carta(10, "O"));
        cartas.add(new Carta(12, "O"));
        cartas.add(new Carta(7, "O"));
        cartas.add(new Carta(4, "O"));

        darCartas(estado,"j1",cartas);

        cartas = new ArrayList<>();
        cartas.add(new Carta(3, "E"));
        cartas.add(new Carta(2, "E"));
        cartas.add(new Carta(1, "O"));
        cartas.add(new Carta(3, "O"));
        cartas.add(new Carta(7, "O"));
        cartas.add(new Carta(2, "O"));

        darCartas(estado,"j2",cartas);

        cartas = new ArrayList<>();

        cartas.add(new Carta(10, "E"));
        cartas.add(new Carta(1, "E"));
        cartas.add(new Carta(10, "B"));
        cartas.add(new Carta(12, "B"));
        cartas.add(new Carta(7, "B"));
        cartas.add(new Carta(5, "O"));

        darCartas(estado,"j3",cartas);

        cartas = new ArrayList<>();
        cartas.add(new Carta(4, "E"));
        cartas.add(new Carta(3, "C"));
        cartas.add(new Carta(6, "O"));
        cartas.add(new Carta(5, "C"));
        cartas.add(new Carta(4, "C"));
        cartas.add(new Carta(2, "C"));

        darCartas(estado,"j4",cartas);
    }
}
