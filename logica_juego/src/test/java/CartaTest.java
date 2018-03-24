package test.java;

/**
 * @Autores: Crisan, Marius Sorin; Ignacio Bitrian; Victor Soria
 * @Fecha: 11-03-18
 * @Fichero: Fichero de pruebas con JUnit de la clase Carta
 */


import main.java.Carta;
import main.java.ExceptionCartaIncorrecta;

import org.junit.Test;
import static org.junit.Assert.*;


public class CartaTest {

    /**
     * Pruebas de caja negra para los constructores de la clase carta. Para
     * el parámetro del valor se han utilizado también pruebas de análisis de
     * valores límite. Como los setters son iguales que los constructores no
     * se prueban.
     */



    @Test
    public void pruebasConstructores() {
        System.out.println("\n---- INICIO PRUEBAS CARTA ----\n");
        System.out.println("Pruebas Constructores con enteros y cadenas");
        try {
            Carta a = new Carta(1, "B");
            comprobarCartaValida(a, 1, "B");
            Carta b = new Carta(1, 1);
            sonCartasIguales(a,b);
            a = new Carta(7, "C");
            comprobarCartaValida(a, 7, "C");
            b = new Carta(7, 2);
            sonCartasIguales(a,b);
            a = new Carta(10, "E");
            comprobarCartaValida(a, 10, "E");
            b = new Carta(10, 3);
            sonCartasIguales(a,b);
            a = new Carta(12, "O");
            b = new Carta(12, 4);
            sonCartasIguales(a,b);
            comprobarCartaValida(a, 12, "O");
        } catch (ExceptionCartaIncorrecta e) {
            fail("Carta válida no provocan excepción");
        }
        comprobarCartaNoValida(0, "B", 1);
        comprobarCartaNoValida(8, "B", 1);
        comprobarCartaNoValida(9, "B", 1);
        comprobarCartaNoValida(13, "B", 1);
        comprobarCartaNoValida(6, "D", 5);
    }


    /**
     * Comprueba que la puntuación de una carta es correcta. Análisis de
     * valores límite
     */
    @Test
    public void pruebaPuntuacionCartas(){

        System.out.println("\nPruebas puntuación Carta");

        compruebaPuntuacionCarta(1,11);
        compruebaPuntuacionCarta(3, 10);
        compruebaPuntuacionCarta(12, 4);
        compruebaPuntuacionCarta(10, 3);
        compruebaPuntuacionCarta(11, 2);

        try {
            Carta a = new Carta (2, "E");
            assertTrue("Puntuación incorrecta de: " + a, a.getPuntuación()
                    == 0);
            System.out.println("Superado... " + a);
        } catch (ExceptionCartaIncorrecta e){
            fail("No debe generar interrupción una carta correcta: [2,E]");
        }
    }


    /******************* FUNCIONES AUXILIARES *********************************/

    /**
     * Prueba que las carta a es del "palo" y "valor"
     * @param a
     * @param palo
     * @param value
     */
    private void comprobarCartaValida(Carta a, int value, String palo){
        assertTrue("Palo incorrecto: " + a, a.getPalo().equals(palo));
        assertTrue("Valor incorrecto: " + a, a.getValor() == value);
        System.out.println("Superado... [" + value + "," + palo + "] ");
    }


    /**
     * Comprueba si la carta a y b son las misma
     * "palo" y
     * "valor"
     * @param a
     * @param b
     */
    private void sonCartasIguales(Carta a, Carta b){
        assertTrue("Palo incorrecto: " + a, a.getPalo().equals(b.getPalo()));
        assertTrue("Valor incorrecto: " + a, a.getValor() == b.getValor());
        System.out.println("Superado... " + b);
    }

    /**
     * Al contruir una carta no valida del palo y valor debe lanzarse excepción
     * @param palo
     * @param value
     */
    private void comprobarCartaNoValida(int value, String palo, int palo_int){
        try {
            new Carta(value, palo);
            fail("No ha generado excepción constructor con string");
        } catch (ExceptionCartaIncorrecta e){
            System.out.println("Superado... [" + value + "," + palo + "] ");
            try {
                new Carta(value, palo_int);
                fail("No ha generado excepción constructor con enteros");
            } catch (ExceptionCartaIncorrecta e1){
                System.out.println("Superado... [" + value + "," + palo_int +
                        "] ");
            }
        }
    }

    /**
     * Comprueba que la puntuación de una carta con valor
     * igual a "value" corresponde con punt
     * @param value
     * @param punt
     */
    private void compruebaPuntuacionCarta(int value, int punt){
        Carta a;
        try {
            a = new Carta(value, "B");
            assertTrue("Puntuación incorrecta de: " + a + " --> " + a
                    .getPuntuación(), a.getPuntuación() == punt);
            System.out.println("Superado... " + a);
        } catch (ExceptionCartaIncorrecta e){
            fail("No debe generar interrupción una carta correcta: [" + value
                    + ",B]");
        }
    }
}

