import main.java.Carta;

import java.util.ArrayList;

public class Nodo {
    private Carta movimiento;       // Carta que ha llevado hasta este nodo (null para la raiz)
    private Nodo padre;             // Padre del nodo (null para la raiz)

    public Carta getMovimiento() {
        return movimiento;
    }

    public void setMovimiento(Carta movimiento) {
        this.movimiento = movimiento;
    }

    public Nodo getPadre() {
        return padre;
    }

    public void setPadre(Nodo padre) {
        this.padre = padre;
    }

    public ArrayList<Nodo> getHijos() {
        return hijos;
    }

    public void setHijos(ArrayList<Nodo> hijos) {
        this.hijos = hijos;
    }

    public int getVictorias() {
        return victorias;
    }

    public void setVictorias(int victorias) {
        this.victorias = victorias;
    }

    public int getVisitas() {
        return visitas;
    }

    public void setVisitas(int visitas) {
        this.visitas = visitas;
    }

    public int getAvails() {
        return avails;
    }

    public void setAvails(int avails) {
        this.avails = avails;
    }

    public int getJugadorTirador() {
        return jugadorTirador;
    }

    public void setJugadorTirador(int jugadorTirador) {
        this.jugadorTirador = jugadorTirador;
    }

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

    public Nodo(Carta movimiento, Nodo padre, int jugadorTirador) {
        this.movimiento = movimiento;
        this.padre = padre;
        this.hijos = new ArrayList<>();
        this.victorias = 0;
        this.visitas = 0;
        this.avails = 1;
        this.jugadorTirador = jugadorTirador;
    }

    public ArrayList<Carta> obtenerMovimientosNoProbados(ArrayList<Carta> movimientosLegales) {
        ArrayList<Carta> probados = new ArrayList<>();
        for (Nodo h: this.hijos) {
            probados.add(h.movimiento);
        }
        ArrayList<Carta> resultado = new ArrayList<>();
        for (Carta c: movimientosLegales) {
            if (!probados.contains(c)) {
                resultado.add(c);
            }
        }
        return resultado;
    }

    public Nodo seleccionUCB(ArrayList<Carta> movimientosLegales) {
        double maxUCB = 0;
        Nodo max = null;
        for (Nodo h: this.hijos) {
            if (movimientosLegales.contains(h.movimiento)) {
                double auxUCB = ((h.victorias)/(double)(h.visitas))+0.7*Math.sqrt(Math.log(h.avails)/(double)(h.visitas));
                if (maxUCB < auxUCB) {
                   maxUCB = auxUCB;
                   max = h;
                }
                h.avails++;
            }
        }
        return max;
    }

    public Nodo nuevoHijo(Carta m, int jugador) {
        Nodo n = new Nodo(m,this,jugador);
        this.hijos.add(n);
        return n;
    }

    public void actualizar(EstadoGuinote e) {
        this.visitas += 1;
        if (this.jugadorTirador != -1) {
            this.victorias += e.obtenerResultado();
        }
    }

}
