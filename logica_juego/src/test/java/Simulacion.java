package test.java;

import main.java.Carta;
import main.java.EstadoPartida;
import main.java.ExceptionEquipoIncompleto;
import main.java.LogicaPartida;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

/**
 * @Autores: Crisan, Marius Sorin; Ignacio Bitrian; Victor Soria
 * @Fecha: 25-03-18
 * @Fichero: Simula una partida del guiñote mediante terminal
 */

public class Simulacion {

    private Scanner scanner;

    private Simulacion(){
        this.scanner = new Scanner(System.in);
        this.scanner.useDelimiter(" ");
    }

    public static void main(String[] args) {



        Simulacion sim = new Simulacion();



        System.out.print("Escriba el nombre de los jugadores: ");
        //String line = scanner.nextLine();
        //ArrayList<String> jugadores = new ArrayList<>(Arrays.asList(line
        //        .split(" ")));
        ArrayList<String> jugadores = new ArrayList<>(Arrays.asList("a",
                "b"));

        sim.simularPartida(jugadores);
    }

    private void simularPartida(ArrayList<String> jugadores){

        try {


            LogicaPartida logica = new LogicaPartida(jugadores);
            EstadoPartida estado = logica.getEstado();
            System.out.println("Jugadores de la partida: " + estado.getJugadoresId());

            estado = logica.crearPartida();

            System.out.println("Cartas del mazo: " + imprimeCartas(estado
                    .getMazo()));
            System.out.println("Triunfo: " + estado.getTriunfo() + "\n");

            boolean finPartida = false;
            int i = 0;
            while (!finPartida) {

                ++i;
                String jug = jugadores.get(estado.getTurno());
                ArrayList<Carta> cartas_jug = estado.getCartasEnMano(jug);
                System.out.print("Jugador: " + jug + imprimeCartas(estado
                        .getCartasEnMano(jug)));
                System.out.print("Elige una carta <value> <palo>: ");
                int valor = scanner.nextInt();
                String palo = scanner.nextLine();
                palo = palo.split(" ")[1].toUpperCase();
                Carta c = new Carta(valor, palo);
                estado = logica.lanzarCarta(jug, c);
                System.out.println("Cartas en la mesa: " + imprimeCartas
                        (estado.getCartasEnTapete()));
                if (i%jugadores.size() == 0){ // Ronda terminada
                    if (estado.getMazo().size() == 0){
                        //TODO: simula solo ronda de descarte
                        finPartida = true;
                    } else {
                        estado = logica.siguienteRonda();
                        for (String j: jugadores) {
                            estado = logica.repartirCarta(j);
                        }
                    }
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }


    private String imprimeCartas(ArrayList<Carta> cartas){
        String res = "\n";
        int i = 5;
        for (Carta c : cartas) {
            res += c + "\t";
            --i;
            if (i == 0){
                i = 5;
                res += "\n";
            }

        }
        return res + "\n";
    }
}
