package sophia;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class Sophia {
    public static CartaIA ismcts(EstadoPartidaIA estadoRaiz, int iteraciones) {
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
        EstadoPartidaIA estado = EstadoPartidaIA.nuevaPartida();
        Random random = new Random();

        while (estado.obtenerMovimientos().size()>0) {
            System.out.println(estado);

            CartaIA m;
            if (estado.getTurno() == 0) {
                m = Sophia.ismcts(estado, 20000);
            } else {
                Scanner in = new Scanner(System.in);
                System.out.printf("Que carta quieres jugar?:  ");
                //  m = Sophia.ismcts(estado, 10000);
                m = estado.obtenerMovimientos().get(in.nextInt());
                // m = estado.obtenerMovimientos().get(random.nextInt(estado.obtenerMovimientos().size()));
            }
            System.out.println("Es el turno "+estado.getTurno());
            System.out.println("\nMejor movimiento: " + m+"\n");
            estado.realizarMovimiento(m, estado.getTurno());
        }

        System.out.println("\n\nFinal de la Partida\n\n");
        System.out.println(estado);
        if (estado.obtenerResultado(0)>0) {
            System.out.println("\n\nGana la IA");
        } else {
            System.out.println("\n\nGana el rival");
        }
    }

}
