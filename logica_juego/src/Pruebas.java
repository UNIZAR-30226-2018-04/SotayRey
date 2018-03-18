/**
 * @Autores: Crisan, Marius Sorin; Ignacio Bitrian; Victor Soria
 * @Fecha: 11-03-18
 * @Fichero: Fichero de pruebas del módulo de lógica del juego
 */


import org.omg.CORBA.TRANSACTION_MODE;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Pruebas {
    public static void main(String[] args){

        pruebasCartas();
        pruebasJugador();

    }

    /**
     * Prueba que las cartas han sido construidas correctamente
     * @param a
     * @param b
     * @param c
     * @param d
     */
    private static void comprobarCartas(Carta a, Carta b, Carta c, Carta d){
        if (a.getPalo()=="B" && a.getValor() == 1)
            System.out.println("[[CORRECTO]]: 1 ");
        else
            System.out.println("[[IN--CORRECTO]]: 1");
        if (b.getPalo()=="C" && b.getValor() == 7)
            System.out.println("[[CORRECTO]]: 2 ");
        else
            System.out.println("[[IN--CORRECTO]]: 2");
        if (c.getPalo()=="E" && c.getValor() == 10)
            System.out.println("[[CORRECTO]]: 3 ");
        else
            System.out.println("[[IN--CORRECTO]]: 3");
        if (d.getPalo()=="O" && d.getValor() == 12)
            System.out.println("[[CORRECTO]]: 4 ");
        else 
            System.out.println("[[IN--CORRECTO]]: 4");
    }

    /**
     * Prueba los casos nó validos de las cartas con constructor con string
     * @param valores
     * @param palos
     */
    private static void provocarErrorCarta(ArrayList<Integer> valores,
                                           ArrayList<String> palos,
                                           ArrayList<Integer> palosInt){
        int tam = valores.size();
        if (tam >0 && (palos.size() == tam || tam == palosInt.size()) ){
            try {
                if (palos.size()> 0){
                    Carta a = new Carta(valores.remove(0), palos.remove(0));
                } else {
                    Carta a = new Carta(valores.remove(0), palosInt.remove
                            (0));
                }
                System.err.println("[ERROR]: no ha provocado excepción");
            } catch (ExceptionCartaIncorrecta e2){
                System.out.println("[[CORRECTO]]: CASO NO VALIDO- " +
                        e2.getMessage());
                provocarErrorCarta(valores, palos, palosInt);
            }
        }
    }


    /**
     * Pruebas de caja negra de la clase Carta con clases de equivalencia.
     */
    public static void pruebasCartas() {
        /** Para el parámetro del valor se han utilizado también pruebas de
         * análisis de valores límite. Como los setters son iguales que los
         * constructores no se prueban.Para verificar el funcionamiento de
         * los getters se utilizan para comprobar el resultado en las pruebas
         * de los constructores
         */

        System.out.println("---- INICIO PRUEBAS CARTA ----\n Pruebas " +
                "Constructor con string");
        try {
            Carta a = new Carta(1, "B");
            Carta b = new Carta(7, "C");
            Carta c = new Carta(10, "E");
            Carta d = new Carta(12, "O");
            comprobarCartas(a, b, c, d);
        } catch (ExceptionCartaIncorrecta e){
                System.err.println("CASOS VÁLIDOS, NO DEBE HABER EXCEPCIÓN:"
                        + e.getMessage());
        }
        ArrayList<Integer> valores = new ArrayList<>();
        valores.addAll(Arrays.asList(0, 8, 9, 13, 6));
        ArrayList<String> palos = new ArrayList<>();
        palos.addAll(Arrays.asList("B", "B", "B", "B", "D"));
        provocarErrorCarta(valores, palos, new ArrayList<Integer>());

        System.out.println("\n Pruebas Constructor con enteros");
        try {
            Carta a = new Carta(1, 1);
            Carta b = new Carta(7, 2);
            Carta c = new Carta(10, 3);
            Carta d = new Carta(12, 4);
            comprobarCartas(a, b, c, d);
        } catch (ExceptionCartaIncorrecta e){
            System.err.println("CASOS VÁLIDOS, NO DEBE HABER EXCEPCIÓN:"
                    + e.getMessage());
        }
        valores = new ArrayList<>();
        valores.addAll(Arrays.asList(0, 8, 9, 13, 6));
        ArrayList<Integer> palosInt = new ArrayList<>();
        palosInt.addAll(Arrays.asList(1, 2, 3 , 4, 5));
        provocarErrorCarta(valores, new ArrayList<String>(), palosInt);
    }


    /**
     * Pruebas para la clase Jugador de caja negra, clases de equivalencia y
     * análisis de extremos, y preubas de caja blanca con pruebas de caminos
     * para bucles.
     */
    public static void pruebasJugador(){
        System.out.println("---- INICIO PRUEBAS JUGADOR----\n");
        /* CONSTRUCTORES, GETTERS Y QUITAR CARTAS EN MANO*/
        try {
            Carta c1 = new Carta();
            Carta c2 = new Carta();
            c1 = new Carta(1, "B");
            c2 = new Carta(2, "E");
            ArrayList<Carta> cartasEnMano = new ArrayList<>();
            ArrayList<Carta> cartasGanadas = new ArrayList<>();
            cartasEnMano.add(c1);
            cartasGanadas.add(c2);
            Jugador j1 = new Jugador(0, cartasEnMano,
                    cartasGanadas, 0);
            Jugador j2 = new Jugador(j1);
            cartasEnMano = j2.getCartasEnMano();
            if (cartasEnMano.size() == 1 && cartasEnMano.get(0).equals(c1))
                    System.out.println("[[CORRECTO]]: Pruebas de constructores y " +
                            "getCartasEnMano SUPERADAS");
            else System.out.println("[[IN--CORRECTO]]: Algún error en " +
                        "constructores o getCartasEnMano");
            try {
                j2.quitarCartaEnMano(c1);
                System.out.println("[[CORRECTO]]: quitarCartasEnMano v1");
                j2.quitarCartaEnMano(c1);
                System.out.println("[[IN--CORRECTO]]: quitarCartasEnMano v2");
            } catch (Exception e){
                System.out.println("[[CORRECTO]]: quitarCartasEnMano v2");
            }

            /* GET GANADAS*/
            cartasGanadas = j2.getCartasGanadas();
            if (cartasGanadas.size() == 1 && cartasGanadas.get(0).equals(c2))
                System.out.println("[[CORRECTO]]: getCartasGanadas");
            else System.out.println("[[IN--CORRECTO]]: getCartasGanadas");

            /* ANYADIR CARTA EN MANO */
            j1 = new Jugador(0, new ArrayList<>(), new ArrayList<>(), 0);
            try {
                j1.anyadirCartaEnMano(c1);
                System.out.println("[[CORRECTO]]: anyadirCartasEnMano v1");
                j1.anyadirCartaEnMano(c1);
                System.out.println("[[IN--CORRECTO]]: anyadirCartasEnMano v2");
            } catch (ExceptionCartaYaExiste e){
                System.out.println("[[CORRECTO]]: anyadirCartasEnMano v2");
            } catch (ExceptionNumeroMaximoCartas e){
                System.out.println("[[IN--CORRECTO]]: anyadirCartasEnMano v2");
            }
            try {
                for (int i = 2; i <= 6; ++i) {
                    Carta a = new Carta(i, "C");
                    j1.anyadirCartaEnMano(a);
                }
            } catch (Exception e){
                System.out.println("[[IN--CORRECTO]]: anyadirCartasEnMano " +
                        "antes v3");
            }
            Carta c6 = new Carta(6, "C");
            Carta c7 = new Carta(7, "C");

            try {
                j1.anyadirCartaEnMano(c7);
            } catch (ExceptionNumeroMaximoCartas e){
                System.out.println("[[CORRECTO]]: anyadirCartasEnMano v3");
                try {
                    j1.anyadirCartaEnMano(c6);
                } catch (ExceptionNumeroMaximoCartas e1){
                    System.out.println("[[CORRECTO]]: anyadirCartasEnMano v4");
                }  catch (Exception e1) {
                    System.out.println("[[IN--CORRECTO]]: anyadirCartasEnMano v4");
                }
            } catch (Exception e){
                System.out.println("[[IN--CORRECTO]]: anyadirCartasEnMano v3 ");
            }

            /* pruebas anyadirCartasGanadas */
            try {
                cartasGanadas = new ArrayList<>();
                cartasGanadas.add(c7);
                j1.anyadirCartasGanadas(cartasGanadas);
                System.out.println("[[CORRECTO]]: anyadirCartasGanadas v1");
                j1.anyadirCartasGanadas(cartasGanadas);
                System.out.println("[[IN--CORRECTO]]: anyadirCartasGanadas v2");
            } catch (ExceptionCartaYaExiste e){
                System.out.println("[[CORRECTO]]: anyadirCartasGanadas v2");
            }

        } catch (Exception e){
            e.printStackTrace();
        }
    }
}

