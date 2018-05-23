package sophia;
import main.java.Carta;
import main.java.EstadoPartida;

import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class Sophia {
    private EstadoPartidaIA estado;

    /* Se inicializa la IA a partir del estado de una nueva partida
     * El EstadoPartida debe ser una partida con las cartas recién repartidas
     * ya sea de idas o de vueltas. Las cartas de ep deben tener el orden correcto en
     * el que se van a ir robando.
     *
     * Si es partida de vueltas, el parametro vueltas es true y el EstadoPartida ep
     * debe contener los puntos de los jugadores
     */
    public Sophia(EstadoPartida ep, boolean vueltas) {
        this.estado = new EstadoPartidaIA(ep, vueltas);
    }

    private  Sophia() {

    }


    /* El jugador rival tira la carta c
     */
    public void tiraCartaRival(Carta c) {
        estado.tiraCartaRival(c);
    }

    /* El jugador rival realiza los cantes indicados en cantes
     * cantes = {Bastos,Copas,Espadas,Oros}
     */
    public void cantaRival(ArrayList<Boolean> cantes) {
        estado.cantaRival(cantes);
    }

    /* El jugador rival cambia el 7 de triunfo por el triunfo de la mesa
     */
    public void cambiaSieteRival() {
        estado.cambiaSieteRival();
    }

    public AccionIA obtenerAccion() {
        CartaIA m = ismcts(estado,20000);
        return estado.realizarMovimiento(m,0);
    }

    private static CartaIA ismcts(EstadoPartidaIA estadoRaiz, int iteraciones) {
        Nodo raiz = new Nodo();
        Random rand = new Random();
        for (int i=0; i<iteraciones; i++) {
            Nodo nodo = raiz;

            EstadoPartidaIA estado = estadoRaiz.determinizar(estadoRaiz.getTurno());

            //Seleccion
            while (!estado.obtenerMovimientos().isEmpty() && nodo.obtenerMovimientosNoProbados(estado.obtenerMovimientos()).isEmpty()) {
                nodo = nodo.seleccionUCB(estado.obtenerMovimientos());
                estado.realizarMovimiento(nodo.getMovimiento(),estado.getTurno());
            }

            //Expansion
            ArrayList<CartaIA> movimientosNoProbados = nodo.obtenerMovimientosNoProbados(estado.obtenerMovimientos());
            if (!movimientosNoProbados.isEmpty()) {
                CartaIA m = movimientosNoProbados.get(rand.nextInt(movimientosNoProbados.size()));
                int jugador = estado.getTurno();
                estado.realizarMovimiento(m,jugador);
                nodo = nodo.nuevoHijo(m,jugador);
            }

            // Simulacion
            while (!estado.obtenerMovimientos().isEmpty()) {
                ArrayList<CartaIA> movs = estado.obtenerMovimientos();
                estado.realizarMovimiento(movs.get(rand.nextInt(movs.size())),estado.getTurno());
            }

            // Backpropagation
            while (nodo != null) {
                nodo.actualizar(estado);
                nodo = nodo.getPadre();
            }
        }
        int maxVisitas = 0;
        CartaIA maxCarta = null;
        for (Nodo n: raiz.getHijos()) {
            if (n.getVisitas()>=maxVisitas) {
                maxVisitas = n.getVisitas();
                maxCarta = n.getMovimiento();
            }
        }
        return maxCarta;
    }
    public static void main(String[] args) {
        CartaIA kk = new CartaIA(7,'B');
        kk.toCarta();
        Sophia sp = new Sophia();
        sp.estado = EstadoPartidaIA.nuevaPartida();

        while (sp.estado.obtenerMovimientos().size()>0) {
            System.out.println(sp.estado);

            CartaIA m;
            if (sp.estado.getTurno() == 0) {
                AccionIA aia = sp.obtenerAccion();
                System.out.println("Carta IA: "+aia.carta);
            } else {
                Scanner in = new Scanner(System.in);
                System.out.printf("Quieres cambiar el 7?:  ");
                int siete = in.nextInt();
                if(siete>0){
                    sp.cambiaSieteRival();
                }
                System.out.printf("Que 20s/40 quieres cantar? (B,C,E,O):  ");
                ArrayList<Boolean> veintes = new ArrayList<>(4);
                veintes.add(in.nextBoolean());
                veintes.add(in.nextBoolean());
                veintes.add(in.nextBoolean());
                veintes.add(in.nextBoolean());
                sp.cantaRival(veintes);
                System.out.printf("Que carta quieres jugar?:  ");
                //  m = Sophia.ismcts(estado, 10000);
                m = sp.estado.obtenerMovimientos().get(in.nextInt());
                sp.tiraCartaRival(m.toCarta());
                // m = estado.obtenerMovimientos().get(random.nextInt(estado.obtenerMovimientos().size()));
                System.out.println("Carta rival: " + m.toCarta()+"\n");
            }
            System.out.println("Es el turno "+(sp.estado.getTurno()+1));
        }

        System.out.println("\n\nFinal de la Partida\n\n");
        System.out.println(sp.estado);
        if (sp.estado.obtenerResultado(0)>0) {
            System.out.println("\n\nGana la IA");
        } else {
            System.out.println("\n\nGana el rival");
        }
    }

}
