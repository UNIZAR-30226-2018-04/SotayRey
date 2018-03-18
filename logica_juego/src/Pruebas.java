/**
 * @Autores: Crisan, Marius Sorin; Ignacio Bitrian; Victor Soria
 * @Fecha: 11-03-18
 * @Fichero: Fichero de pruebas del módulo de lógica del juego
 */


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Pruebas {
    public static void main(String[] args){

        pruebasCartas();


    //    EstadoPartida estado = new EstadoPartida(new ArrayList<>());
    //    List<Carta> baraja = estado.crearBaraja();
    //    baraja = estado.barajar(baraja);
    //    baraja = estado.barajar(baraja);

    /*    System.out.println("---- INICIO PRUEBAS JUGADOR ----");
        Jugador j = new Jugador();
        System.out.println("Cartas inicial:" + j.getCartasEnMano());
        Carta c = new Carta();
        List<Carta> lista_cartas = new ArrayList<Carta>();
        try{
            c =  new Carta(4,"E");
        } catch (ExceptionCartaIncorrecta e){
            System.err.print(e.getMessage());
        }
        j.anyadirCartaEnMano(c);
        System.out.println("Cartas en mano tras añadir:" + j.getCartasEnMano());
        j.anyadirCartaEnMano(c);
        System.out.println("Cartas en mano tras añadirla misma carta:" + j.getCartasEnMano());
        j.quitarCartaEnMano(c);
        System.out.println("Quita la carta:" + j.getCartasEnMano());
        System.out.println("Quita una carta que no existe:" + j.getCartasEnMano());
        lista_cartas.add(c);
        j.anyadirCartasGanadas(lista_cartas);
        System.out.println("Cartas añade a cartas ganadas:" + j.getCartasGanadas());
        j.anyadirCartasGanadas(lista_cartas);
        System.out.println("Vuelve a añadir a cartas ganadas la misma:" + j.getCartasGanadas());
        j.sumarPuntos(10);
        System.out.println("Jugador tiene 10 pts:" + j.getPuntos());
*/

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
}

