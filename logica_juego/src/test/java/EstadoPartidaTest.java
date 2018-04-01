package test.java;

/**
 * @Autores: Crisan, Marius Sorin; Ignacio Bitrian; Victor Soria
 * @Fecha: 24-03-18
 * @Fichero: Fichero de pruebas con JUnit de la clase EstadoPartida
 */

import main.java.*;

import org.junit.FixMethodOrder;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runners.MethodSorters;
import java.util.ArrayList;
import static org.junit.Assert.*;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)


public class EstadoPartidaTest {

    private int N_ITERACIONES = 100;

    @Rule
    public ExpectedException exception = ExpectedException.none();


    //@Ignore
    @Test
    public void prueba1Constructores(){

        System.out.println("\n---- INICIO PRUEBAS ESTADO----");
        ArrayList<String> jug = new ArrayList<>();
        try {
            new EstadoPartida(jug);
            exception.expect(ExceptionEquipoIncompleto.class);
        } catch (ExceptionEquipoIncompleto e){
            System.out.println("Superado.. Constructor con 0 jugadores");
        }
        jug.add("j1");
        jug.add("j2");
        comprobarJugadores(jug);
        jug.add(0, "j4");
        jug.add("j3");
        comprobarJugadores(jug);

    }

    /**
     * Pruebas de caja negra
     */
    //@Ignore
    @Test
    public void prueba2GetPrimeraCartaMazo(){


        try {
            System.out.println("\nPruebas getPrimeraCartaMazo");
            ArrayList<String> jugadores = new ArrayList<>();
            jugadores.add("j1");
            jugadores.add("j2");
            EstadoPartida estado = new EstadoPartida(jugadores);

            ArrayList<Carta> cartas = estado.barajear();
            estado.setMazo(cartas);

            Carta c = estado.getPrimeraCartaMazo();
            assertTrue("Primera carta cogida del mazo incorrecta: " + c, c
                    .equals(cartas.remove(0)));
            System.out.println("Superado... getPrimeraMazo: " + c);
            estado.anyadirCartaJugador(jugadores.get(1), c);
            estado.setTriunfo(estado.getPrimeraCartaMazo());
            Carta triunfo = cartas.remove(0);

            for (int i = 0; i < 38; ++i){
                comprobarPrimeraCarta(estado, cartas.remove(0));
            }

            System.out.println("Mazo sin cartas: devuelve triunfo ?");
            assertTrue("Triunfo no es el mismo que en el estado", triunfo
                    .equals(estado.getTriunfo()));
            comprobarPrimeraCarta(estado, triunfo);
            estado.anyadirCartaJugador(jugadores.get(1), triunfo);
            try {
                estado.getPrimeraCartaMazo();
                exception.expect(ExceptionMazoVacio.class);
            } catch (ExceptionMazoVacio e){
                System.out.println("Superado... prueba getPrimeraCartaMazo");
            }

        } catch (Exception e){
            fail();
        }


    }

    /**
     * Prueba que al barajear varias veces un conjunto de cartas se obtienen
     * resultados diferentes
     */
    //@Ignore
    @Test
    public void prueba3BarajearVariasVeces(){

        System.out.println("\nPruebas barajear varias veces sobre el mismo " +
                "estado");

        EstadoPartida estado = new EstadoPartida();
        ArrayList<Carta> cartasAntiguas = estado.barajear();
        ArrayList<Carta> cartasNuevas;
        for (int i = 0; i < N_ITERACIONES; i++) {
            cartasNuevas = estado.barajear();
            for (int j = 0; j < 40; j++) {
                if (!cartasAntiguas.get(j).equals(cartasNuevas.get(j))){
                    cartasAntiguas = null;
                    break;
                }
            }
            assertNull("Son las mismas cartas", cartasAntiguas);
            cartasAntiguas = new ArrayList<>(cartasNuevas);
            System.out.println("Superado... it: " + i + " cartas en otro " +
                    "orden");
        }
    }


    @Test
    public void prueba4BarajearInicialDiferente(){

        System.out.println("\nPruebas barajear varias veces sobre el " +
                "distintos estados");
        EstadoPartida estado = new EstadoPartida();
        ArrayList<Carta> cartasAntiguas = estado.barajear();
        for (int i = 0; i < N_ITERACIONES; i++) {
            EstadoPartida estadoI = new EstadoPartida();
            ArrayList<Carta> cartasNuevas = estadoI.barajear();
            for (int j = 0; j < 40; j++) {
                if (!cartasAntiguas.get(j).equals(cartasNuevas.get(j))){
                    cartasAntiguas = null;
                    break;
                }
            }
            assertNull("Son las mismas cartas", cartasAntiguas);
            cartasAntiguas = new ArrayList<>(cartasNuevas);
            System.out.println("Superado... it: " + i + " cartas en otro " +
                    "orden al principio");
        }
    }

    /**************************** FUNCIONES AUXILIARES ************************/

    private void comprobarPrimeraCarta(EstadoPartida estado, Carta c){
        try {
            Carta a = estado.getPrimeraCartaMazo();
            assertTrue("No son la misma carta", a.equals(c));
            System.out.println("Superado... getPrimeraMazo: " + a);

        } catch (ExceptionMazoVacio e){
            fail("No debe generar excepción, aun quedan cartas");
        }
    }


    private void comprobarJugadores(ArrayList<String> jugadores){
        try {
            EstadoPartida estado = new EstadoPartida(jugadores);
            assertTrue("Primer jugador correcto", estado.getJugadoresId
                    ().get(estado.getTurno()).equals(jugadores.get(0)));
            assertTrue("Numero de jugadores correcto", estado.getJugadoresId
                    ().size() == jugadores.size());
            System.out.println("Superado... constructor con " + jugadores
                    .size() + " jugadores");
        } catch (ExceptionEquipoIncompleto e){
            fail("No de debe generar excepción");
        }
    }

}
