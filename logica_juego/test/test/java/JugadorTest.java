package test.java;

public class JugadorTest {
}


//
//    /**
//     * Pruebas para la clase Jugador de caja negra, clases de equivalencia y
//     * análisis de extremos, y preubas de caja blanca con pruebas de caminos
//     * para bucles.
//     */
//    private static void pruebasJugador(){
//        System.out.println("\n---- INICIO PRUEBAS JUGADOR----");
//        /* CONSTRUCTORES, GETTERS Y QUITAR CARTAS EN MANO*/
//        try {
//            Carta c1 = new Carta(1, "B");
//            Carta c2 = new Carta(2, "E");
//            ArrayList<Carta> cartasEnMano = new ArrayList<>();
//            ArrayList<Carta> cartasGanadas = new ArrayList<>();
//            cartasEnMano.add(c1);
//            cartasGanadas.add(c2);
//            Jugador j1 = new Jugador("0", cartasEnMano,
//                    cartasGanadas, 0);
//            Jugador j2 = new Jugador(j1);
//            cartasEnMano = j2.getCartasEnMano();
//            if (cartasEnMano.size() == 1 && cartasEnMano.get(0).equals(c1))
//                    System.out.println("[[CORRECTO]]: Pruebas de constructores y " +
//                            "getCartasEnMano SUPERADAS");
//            else System.out.println("[[IN--CORRECTO]]: Algún error en " +
//                        "constructores o getCartasEnMano");
//            try {
//                j2.quitarCartaEnMano(c1);
//                System.out.println("[[CORRECTO]]: quitarCartasEnMano v1");
//                j2.quitarCartaEnMano(c1);
//                System.out.println("[[IN--CORRECTO]]: quitarCartasEnMano v2");
//            } catch (Exception e){
//                System.out.println("[[CORRECTO]]: quitarCartasEnMano v2-f");
//            }
//
//            /* GET GANADAS*/
//            cartasGanadas = j2.getCartasGanadas();
//            if (cartasGanadas.size() == 1 && cartasGanadas.get(0).equals(c2))
//                System.out.println("[[CORRECTO]]: getCartasGanadas");
//            else System.out.println("[[IN--CORRECTO]]: getCartasGanadas");
//
//            /* ANYADIR CARTA EN MANO */
//            j1 = new Jugador("0", new ArrayList<>(), new ArrayList<>(), 0);
//            try {
//                j1.anyadirCartaEnMano(c1);
//                System.out.println("[[CORRECTO]]: anyadirCartasEnMano v1");
//                j1.anyadirCartaEnMano(c1);
//                System.out.println("[[IN--CORRECTO]]: anyadirCartasEnMano v2");
//            } catch (ExceptionCartaYaExiste e){
//                System.out.println("[[CORRECTO]]: anyadirCartasEnMano v2");
//            } catch (ExceptionNumeroMaximoCartas e){
//                System.out.println("[[IN--CORRECTO]]: anyadirCartasEnMano v2");
//            }
//            try {
//                for (int i = 2; i <= 6; ++i) {
//                    Carta a = new Carta(i, "C");
//                    j1.anyadirCartaEnMano(a);
//                }
//            } catch (Exception e){
//                System.out.println("[[IN--CORRECTO]]: anyadirCartasEnMano " +
//                        "antes v3");
//            }
//            Carta c6 = new Carta(6, "C");
//            Carta c7 = new Carta(7, "C");
//
//            try {
//                j1.anyadirCartaEnMano(c7);
//            } catch (ExceptionNumeroMaximoCartas e){
//                System.out.println("[[CORRECTO]]: anyadirCartasEnMano v3");
//                try {
//                    j1.anyadirCartaEnMano(c6);
//                } catch (ExceptionNumeroMaximoCartas e1){
//                    System.out.println("[[CORRECTO]]: anyadirCartasEnMano v4-f");
//                }  catch (Exception e1) {
//                    System.out.println("[[IN--CORRECTO]]: anyadirCartasEnMano v4");
//                }
//            } catch (Exception e){
//                System.out.println("[[IN--CORRECTO]]: anyadirCartasEnMano v3 ");
//            }
//
//            /* pruebas anyadirCartasGanadas */
//            try {
//                cartasGanadas = new ArrayList<>();
//                cartasGanadas.add(c7);
//                j1.anyadirCartasGanadas(cartasGanadas);
//                System.out.println("[[CORRECTO]]: anyadirCartasGanadas v1");
//                j1.anyadirCartasGanadas(cartasGanadas);
//                System.out.println("[[IN--CORRECTO]]: anyadirCartasGanadas v2");
//            } catch (ExceptionCartaYaExiste e){
//                System.out.println("[[CORRECTO]]: anyadirCartasGanadas v2-f");
//            }
//
//        } catch (Exception e){
//            e.printStackTrace();
//        }
//    }
//
//    /**
//     *
//     */
//    private static void pruebasEstadoPartida(){
//
//    }