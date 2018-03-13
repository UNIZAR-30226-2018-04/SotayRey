/*
 * Autor: Crisan, Marius Sorin, Ignacio Bitrian, Victor Soria
 * Fecha: 11-03-18
 * Fichero: Fichero de pruebas del módulo de lógica del juego
 */


import java.util.ArrayList;
import java.util.List;

public class Pruebas {
    public static void main(String[] args){
        EstadoPartida estado = new EstadoPartida(new ArrayList<>());
        List<Carta> baraja = estado.crearBaraja();
        baraja = estado.barajar(baraja);
        baraja = estado.barajar(baraja);

        /*
        System.out.println("---- INICIO PRUEBAS CARTAS----");
        // PRUEBAS DE CAJA NEGRA
        List<Carta> lista_cartas = new ArrayList<Carta>();
        //Se prueba solo el contructor porque los setter son copia del constructor
        Carta a = new Carta();
        try {
            a = new Carta(1, "B");
            lista_cartas.add(a);
            a = new Carta(7, "O");
            lista_cartas.add(a);
            a = new Carta(10, "E");
            lista_cartas.add(a);
            a = new Carta(12, "C");
            lista_cartas.add(a);
            String res="";
            for (Carta c: lista_cartas) {
                res += c;
            }
            System.out.println("CARTAS GENERADDAS CORRECTAMENTE:" + res);
            System.out.println("EXCEPCIÓN POR VALOR 3 VECES:");
            a.setValor(0);

        } catch (ExceptionCartaIncorrecta e){
            System.out.println(e.getMessage());
            try{
                a.setValor(8);
            } catch (ExceptionCartaIncorrecta e2){
                System.out.println(e2.getMessage());
                try{
                    a.setValor(13);
                } catch (ExceptionCartaIncorrecta e3){
                    System.out.println(e3.getMessage());
                    System.out.println("EXCEPCIÓN POR VALOR 1 VEZ:");
                    try {
                        a.setPalo("b");
                    } catch (ExceptionCartaIncorrecta e4){
                        System.out.println(e4.getMessage());
                    }
                }
            }
        }




        /*
        System.out.println("---- INICIO PRUEBAS JUGADOR ----");
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
}
