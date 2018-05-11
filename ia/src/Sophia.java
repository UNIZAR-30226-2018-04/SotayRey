import main.java.Carta;

import java.util.ArrayList;
import java.util.Random;

public class Sophia {
    public static Carta ismcts(EstadoGuinote estadoRaiz, int iteraciones) {
        Nodo raiz = new Nodo();
        Random rand = new Random();
        for (int i=0; i<iteraciones; i++) {
            Nodo nodo = raiz;

            EstadoGuinote estado = estadoRaiz.determinizar();

            //Seleccion
            while (!estado.obtenerMovimientos().isEmpty() && nodo.obtenerMovimientosNoProbados(estado.obtenerMovimientos()).isEmpty()) {
                nodo = nodo.seleccionUCB(estado.obtenerMovimientos());
                estado.realizarMovimiento(nodo.getMovimiento());
            }

            //Expansion
            ArrayList<Carta> movimientosNoProbados = nodo.obtenerMovimientosNoProbados(estado.obtenerMovimientos());
            if (!movimientosNoProbados.isEmpty()) {
                Carta m = movimientosNoProbados.get(rand.nextInt(movimientosNoProbados.size()));
                int jugador = estado.obtenerSiguienteJugador();
                estado.realizarMovimiento(m);
                nodo = nodo.nuevoHijo(m,jugador);
            }

            // Simulacion
            while (!estado.obtenerMovimientos.isEmpty()) {
                ArrayList<Carta> movs = estado.obtenerMovimientos();
                estado.realizarMovimiento(movs.get(rand.nextInt(movs.size())));
            }

            // Backpropagation
            while (nodo != null) {
                nodo.actualizar(estado);
                nodo = nodo.getPadre();
            }
        }
        int maxVisitas = 0;
        Carta maxCarta = null;
        for (Nodo n: raiz.getHijos()) {
            if (n.getVisitas()>maxVisitas) {
                maxVisitas = n.getVisitas();
                maxCarta = n.getMovimiento();
            }
        }
        return maxCarta;
    }
}
