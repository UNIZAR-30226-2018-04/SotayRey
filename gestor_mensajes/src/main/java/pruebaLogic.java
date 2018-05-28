package main.java;

import java.util.ArrayList;
import java.util.Scanner;

public class pruebaLogic {

    public static void main(String argc[]) {
        ArrayList<String> jugs = new ArrayList<>();
        jugs.add("jug1");
        jugs.add("jug2");
        jugs.add("jug3");
        jugs.add("jug4");
        LogicaPartida l = null;
        try {
            l = new LogicaPartida(jugs);
        } catch (Exception e) {}

        try {
            l.crearPartida();
        } catch (Exception e) {}

        while (true) {
            System.out.println("Triungo: " + l.getEstado().getTriunfo());
            System.out.println("Cartas restantes: " + l.getEstado().getMazo());
            // Uno
            ArrayList<Carta> cartas = l.getEstado().getJugadores().get(l.getEstado().getTurno()).getCartasEnMano();
            System.out.println("Turno " + l.getEstado().getTurno() + " " + l.getEstado().getJugadores().get(l.getEstado().getTurno()).getId() + ": " + cartas);
            Scanner in = new Scanner(System.in);
            System.out.println("Que carta quieres tirar:");
            int c = in.nextInt();
            System.out.println("Quieres cambiar, que carta?:");
            int cambiar = in.nextInt();
            System.out.println("Quieres cantar?");
            int cantar = in.nextInt();
            if (cambiar > 0) {
                try {
                    l.cambiarCartaPorTriunfo(l.getEstado().getJugadores().get(l.getEstado().getTurno()).getId(), cartas.get(cambiar));
                } catch (Exception e) {
                    System.out.println(e);
                }
            }
            if (cantar > 0) {
                try {
                    l.cantar(l.getEstado().getJugadores().get(l.getEstado().getTurno()).getId());
                } catch (Exception e) {
                    System.out.println(e);
                }
            }
            try {
                l.lanzarCarta(l.getEstado().getJugadores().get(l.getEstado().getTurno()).getId(), cartas.get(c));
            } catch (Exception e) {
                System.out.println(e);
            }
            // Otro
            ArrayList<Carta> cartas2 = l.getEstado().getJugadores().get(l.getEstado().getTurno()).getCartasEnMano();
            System.out.println("Turno " + l.getEstado().getTurno() + " " + l.getEstado().getJugadores().get(l.getEstado().getTurno()).getId() + ": " + cartas2);
            Scanner in2 = new Scanner(System.in);
            System.out.println("Que carta quieres tirar:");
            int c2 = in2.nextInt();
            System.out.println("Quieres cambiar, que carta?:");
            int cambiar2 = in2.nextInt();
            System.out.println("Quieres cantar?");
            int cantar2 = in2.nextInt();
            if (cambiar2 > 0) {
                try {
                    l.cambiarCartaPorTriunfo(l.getEstado().getJugadores().get(l.getEstado().getTurno()).getId(), cartas.get(cambiar2));
                } catch (Exception e) {
                    System.out.println(e);
                }
            }
            if (cantar2 > 0) {
                try {
                    l.cantar(l.getEstado().getJugadores().get(l.getEstado().getTurno()).getId());
                } catch (Exception e) {
                    System.out.println(e);
                }
            }
            try {
                l.lanzarCarta(l.getEstado().getJugadores().get(l.getEstado().getTurno()).getId(), cartas2.get(c2));
            } catch (Exception e) {
                System.out.println(e);
            }


            // Tres
            ArrayList<Carta> cartas3 = l.getEstado().getJugadores().get(l.getEstado().getTurno()).getCartasEnMano();
            System.out.println("Turno " + l.getEstado().getTurno() + " " + l.getEstado().getJugadores().get(l.getEstado().getTurno()).getId() + ": " + cartas3);
            Scanner in3 = new Scanner(System.in);
            System.out.println("Que carta quieres tirar:");
            int c3 = in3.nextInt();
            System.out.println("Quieres cambiar, que carta?:");
            int cambiar3 = in3.nextInt();
            System.out.println("Quieres cantar?");
            int cantar3 = in3.nextInt();
            if (cambiar3 > 0) {
                try {
                    l.cambiarCartaPorTriunfo(l.getEstado().getJugadores().get(l.getEstado().getTurno()).getId(), cartas.get(cambiar3));
                } catch (Exception e) {
                    System.out.println(e);
                }
            }
            if (cantar3 > 0) {
                try {
                    l.cantar(l.getEstado().getJugadores().get(l.getEstado().getTurno()).getId());
                } catch (Exception e) {
                    System.out.println(e);
                }
            }
            try {
                l.lanzarCarta(l.getEstado().getJugadores().get(l.getEstado().getTurno()).getId(), cartas.get(c3));
            } catch (Exception e) {
                System.out.println(e);
            }

            // Cuatro
            ArrayList<Carta> cartas4 = l.getEstado().getJugadores().get(l.getEstado().getTurno()).getCartasEnMano();
            System.out.println("Turno " + l.getEstado().getTurno() + " " + l.getEstado().getJugadores().get(l.getEstado().getTurno()).getId() + ": " + cartas4);
            Scanner in4 = new Scanner(System.in);
            System.out.println("Que carta quieres tirar:");
            int c4 = in4.nextInt();
            System.out.println("Quieres cambiar, que carta?:");
            int cambiar4 = in4.nextInt();
            System.out.println("Quieres cantar?");
            int cantar4 = in4.nextInt();
            if (cambiar4 > 0) {
                try {
                    l.cambiarCartaPorTriunfo(l.getEstado().getJugadores().get(l.getEstado().getTurno()).getId(), cartas.get(cambiar4));
                } catch (Exception e) {
                    System.out.println(e);
                }
            }
            if (cantar4 > 0) {
                try {
                    l.cantar(l.getEstado().getJugadores().get(l.getEstado().getTurno()).getId());
                } catch (Exception e) {
                    System.out.println(e);
                }
            }
            try {
                l.lanzarCarta(l.getEstado().getJugadores().get(l.getEstado().getTurno()).getId(), cartas.get(c4));
            } catch (Exception e) {
                System.out.println(e);
            }


            // Acabar ronda
            try {
                l.siguienteRonda();
            } catch (Exception e) {
                System.out.println(e);
            }

            for (String j : l.getEstado().getJugadoresRepartirCartas()) {
                try {
                    l.repartirCarta(j);
                } catch (Exception e) {
                    System.out.println(e);
                }
            }
        }

    }

}
