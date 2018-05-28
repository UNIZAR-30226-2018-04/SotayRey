package main.java;

import java.util.ArrayList;
import java.util.Scanner;

public class pruebaLogic {

    public static void main(String argc[]) {
        ArrayList<String> jugs = new ArrayList<>();
        jugs.add("jug1");
        jugs.add("jug2");
        LogicaPartida l = null;
        try {
            l = new LogicaPartida(jugs);
        } catch (Exception e) {}

        try {
            l.crearPartida();
        } catch (Exception e) {}

        while (true) {
            System.out.println("Triungo: "+l.getEstado().getTriunfo());
            System.out.println("Cartas restantes: "+l.getEstado().getMazo());
            // Uno
            ArrayList<Carta> cartas = l.getEstado().getJugadores().get(l.getEstado().getTurno()).getCartasEnMano();
            System.out.println("Turno "+ l.getEstado().getTurno() +": " + cartas);
            Scanner in = new Scanner(System.in);
            System.out.println("Que carta quieres tirar:");
            int c = in.nextInt();
            System.out.println("Quieres cambiar, que carta?:");
            int cambiar = in.nextInt();
            System.out.println("Quieres cantar?");
            int cantar = in.nextInt();
            if (cambiar>0) {
                try {
                    l.cambiarCartaPorTriunfo(l.getEstado().getJugadores().get(l.getEstado().getTurno()).getId(), cartas.get(cambiar));
                } catch (Exception e) {System.out.println(e);}
            }
            if (cantar>0) {
                try {
                    l.cantar(l.getEstado().getJugadores().get(l.getEstado().getTurno()).getId());
                } catch (Exception e) {System.out.println(e);}
            }
            try {
                l.lanzarCarta(l.getEstado().getJugadores().get(l.getEstado().getTurno()).getId(),cartas.get(c));
            } catch (Exception e) {System.out.println(e);}
            // Otro
            ArrayList<Carta> cartas2 = l.getEstado().getJugadores().get(l.getEstado().getTurno()).getCartasEnMano();
            System.out.println("Turno "+ l.getEstado().getTurno() +": " + cartas2);
            Scanner in2 = new Scanner(System.in);
            System.out.println("Que carta quieres tirar:");
            int c2 = in2.nextInt();
            System.out.println("Quieres cambiar, que carta?:");
            int cambiar2 = in2.nextInt();
            System.out.println("Quieres cantar?");
            int cantar2 = in2.nextInt();
            if (cambiar2>0) {
                try {
                    l.cambiarCartaPorTriunfo(l.getEstado().getJugadores().get(l.getEstado().getTurno()).getId(), cartas.get(cambiar2));
                } catch (Exception e) {System.out.println(e);}
            }
            if (cantar2>0) {
                try {
                    l.cantar(l.getEstado().getJugadores().get(l.getEstado().getTurno()).getId());
                } catch (Exception e) {System.out.println(e);}
            }
            try {
                l.lanzarCarta(l.getEstado().getJugadores().get(l.getEstado().getTurno()).getId(),cartas2.get(c2));
            } catch (Exception e) {System.out.println(e);}

            // Acabar ronda
            try {
                l.siguienteRonda();
            } catch (Exception e) {System.out.println(e);}
            if (l.getEstado().getTurno()==0) {
                try {
                    l.repartirCarta("jug1");
                    l.repartirCarta("jug2");
                } catch (Exception e) {System.out.println(e);}
            } else if (l.getEstado().getTurno()==1) {
                try {
                    l.repartirCarta("jug2");
                    l.repartirCarta("jug1");
                } catch (Exception e) {System.out.println(e);}
            } else {
                System.out.println("Que pinche wei");
            }
        }

    }

}
