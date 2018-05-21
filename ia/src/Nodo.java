package src;
import java.util.ArrayList;

public class Nodo {
    private CartaIA movimiento;       // CartaIA que ha llevado hasta este nodo (null para la raiz)
    private Nodo padre;             // Padre del nodo (null para la raiz)
    private ArrayList<Nodo> hijos;  // Hijos del nodo
    private int victorias;
    private int visitas;
    private int avails;             // No se que es
    private int jugadorTirador;     // Quien tiró movimiento para llegar hasta aquí


    public Nodo() {
        this.movimiento = null;
        this.padre = null;
        this.hijos = new ArrayList<>();
        this.victorias = 0;
        this.visitas = 0;
        this.avails = 1;
        this.jugadorTirador = -1;
    }

    public Nodo(CartaIA movimiento, Nodo padre, int jugadorTirador) {
        this.movimiento = movimiento;
        this.padre = padre;
        this.hijos = new ArrayList<>();
        this.victorias = 0;
        this.visitas = 0;
        this.avails = 1;
        this.jugadorTirador = jugadorTirador;
    }

    public ArrayList<CartaIA> obtenerMovimientosNoProbados(ArrayList<CartaIA> movimientosLegales) {
        ArrayList<CartaIA> probados = new ArrayList<>();
        for (Nodo h: this.hijos) {
            probados.add(h.movimiento);
        }
        ArrayList<CartaIA> resultado = new ArrayList<>();
        for (CartaIA c: movimientosLegales) {
            if (!probados.contains(c)) {
                resultado.add(c);
            }
        }
        return resultado;
    }

    public Nodo seleccionUCB(ArrayList<CartaIA> movimientosLegales) {
        double maxUCB = 0;
        Nodo max = null;
        for (Nodo h: this.hijos) {
            if (movimientosLegales.contains(h.movimiento)) {
                double auxUCB = ((h.victorias)/(double)(h.visitas))+0.7*Math.sqrt(Math.log(h.avails)/(double)(h.visitas));
                if (maxUCB <= auxUCB) {
                   maxUCB = auxUCB;
                   max = h;
                }
                h.avails++;
            }
        }
        return max;
    }

    public Nodo nuevoHijo(CartaIA m, int jugador) {
        Nodo n = new Nodo(m,this,jugador);
        this.hijos.add(n);
        return n;
    }

    public void actualizar(EstadoPartidaIA e) {
        this.visitas += 1;
        if (this.jugadorTirador != -1) {
            this.victorias += e.obtenerResultado(this.jugadorTirador);
        }
    }

    // Setter and getters
    public CartaIA getMovimiento() {
        return movimiento;
    }

    public Nodo getPadre() {
        return padre;
    }

    public ArrayList<Nodo> getHijos() {
        return hijos;
    }

    public int getVictorias() {
        return victorias;
    }

    public int getVisitas() {
        return visitas;
    }

    public int getAvails() {
        return avails;
    }

    public int getJugadorTirador() {
        return jugadorTirador;
    }
}
