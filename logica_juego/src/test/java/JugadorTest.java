package test.java;

/**
 * @Autores: Crisan, Marius Sorin; Ignacio Bitrian; Victor Soria
 * @Fecha: 24-03-18
 * @Fichero: Fichero de pruebas con JUnit de la clase Jugador
 */

import main.java.*;

import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runners.MethodSorters;
import java.util.ArrayList;
import static org.junit.Assert.*;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)

public class JugadorTest {

    @Rule
    public ExpectedException exception = ExpectedException.none();

    /**
     * Pruebas de caja blanca con caminos para los constructores
     * getCartasEnMano, getCartasGanadas. Prueba de caja negra para
     * quitarCartaEnMano.
     */
    @Test
    public void prueba1Constructor() {
        System.out.println("\n---- INICIO PRUEBAS JUGADOR----");
        System.out.println("Probando constructores jugador");

        Jugador j2;
        try {
            // cartasEnMano = [1,B]
            Carta c1 = new Carta(1, "B");
            ArrayList<Carta> cartasEnMano = new ArrayList<>();
            cartasEnMano.add(c1);

            // cartasGanadas = [1,E]
            Carta c2 = new Carta(2, "E");
            ArrayList<Carta> cartasGanadas = new ArrayList<>();
            cartasGanadas.add(c2);
            Jugador j1 = new Jugador("0", cartasEnMano,
                    cartasGanadas, 0);
            j2 = new Jugador(j1);

            assertTrue("Constructor no añade bien cartasEnMano",
                    cartasEnMano.equals(j2.getCartasEnMano()));
            System.out.println("Superado... CartasEnMano constructor");
            assertTrue("Constructor no añade bien cartasGanadas",
                    cartasGanadas.equals(j2.getCartasGanadas()));
            System.out.println("Superado... CartasGanadas constructor");

            j2.quitarCartaEnMano(c1);
            System.out.println("Superado... quitarCartasEnMano constructor " +
                    "existente");
            j2.quitarCartaEnMano(c1);
            exception.expect(ExceptionJugadorSinCarta.class);
            fail("Se esperaba que saltara excepción: quitar carta no " +
                    "existente");
        } catch (ExceptionJugadorSinCarta e) {

            System.out.println("Superado... quitarCartasEnMano constructor NO" +
                    " existente");
        } catch (ExceptionCartaIncorrecta e1) {
            fail("No debe saltar excepción al construir carta correcta");
        }
    }


    /**
     * Pruebas de caja negra y análisis de valores extremos para método
     * anyadirCartasEnMano
     */
    @Test
    public void prueba2AnyadirCartasEnMano() {
        System.out.println("\nProbando anyadirCartaEnMano");
        Jugador j1 = new Jugador("0", new ArrayList<>(), new ArrayList<>(), 0);
        Carta c1 = null;
        try {
            c1 = new Carta(1, "B");
            j1.anyadirCartaEnMano(c1);
            assertTrue("Fallo al insertar:" + c1, j1.getCartasEnMano().size
                    () == 1);
            System.out.println("Superado... añadida " + c1);
            j1.anyadirCartaEnMano(c1);
            exception.expect(ExceptionCartaYaExiste.class);
        } catch (ExceptionCartaIncorrecta e) {
            fail("Excepción al crea carta");
        } catch (ExceptionCartaYaExiste | ExceptionNumeroMaximoCartas e1) {
            System.out.println("Superado... añadida repetida" + c1);
        }
        try {
            for (int i = 2; i <= 6; ++i) {
                Carta a = new Carta(i, "C");
                j1.anyadirCartaEnMano(a);
            }
            assertTrue("Fallo al insertar resto cartas",
                    j1.getCartasEnMano().size
                    () == 6);
            Carta c6 = new Carta(6, "C");
            Carta c7 = new Carta(7, "C");
            noDebeAnyadirMasCartas(j1, c6);
            noDebeAnyadirMasCartas(j1, c7);

        } catch (Exception e) {
            fail("Provocada otra excepción");
        }
    }


    /**
     * Pruebas de caja negra para método anyadirAGanadas
     */
    @Test
    public void pruebas3AnyadirAGanadas(){

        System.out.println("\nProbando anyadirCartaGanadas");
        try {
            Jugador j1 = new Jugador("a");
            Carta c = new Carta(7, "E");
            j1.anyadirCartaGanadas(c);
            assertTrue("Insertando carta" + c + " que no pertenecia",
                    j1.getCartasGanadas().get(0).equals(c));
            j1.anyadirCartaGanadas(c);
            exception.expect(ExceptionCartaYaExiste.class);
        } catch (ExceptionCartaYaExiste e){
            System.out.println("Superado... anyadirCartaGanadas");
        } catch (Exception e1){
            fail("Excepción incorrecta");
        }

    }

    /**************************** FUNCIONES AUXILIARES ************************/

    private void noDebeAnyadirMasCartas(Jugador j, Carta c){
        try {
            j.anyadirCartaEnMano(c);
            exception.expect(ExceptionNumeroMaximoCartas.class);
        } catch (ExceptionNumeroMaximoCartas e1) {
            System.out.println("Superado... anyadirCartasEnMano" + c);
        } catch (Exception e){
            fail("Provocada otra excepción");
        }

    }

}
