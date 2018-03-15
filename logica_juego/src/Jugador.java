/*
 * Autor: Crisan, Marius Sorin
 * Fecha: 11-03-18
 * Fichero: Clase jugador de la lógica del guiñote
 */

import java.util.ArrayList;
import java.util.List;

public class Jugador {
    private List<Carta> cartasEnMano;
    private List<Carta> cartasGanadas;
    private int puntos;
    private int id;

    public Jugador(int id) {
        cartasEnMano = new ArrayList<>();
        cartasGanadas = new ArrayList<>();
        puntos = 0;
        this.id = id;
    }

    public Jugador(int id, List<Carta> cartasEnMano, List<Carta> cartasGanadas, int puntos) {
        this.id = id;
        this.cartasEnMano = cartasEnMano;
        this.cartasGanadas = cartasGanadas;
        this.puntos = puntos;
    }

    public int getId(){
        return id;
    }

    /*
     * Devuelve una lista con las cartas en mano de cada jugador
     */
    public List<Carta> getCartasEnMano() {
        return cartasEnMano;
    }

    /*
     * Añade una carta c al conjunto de cartas en mano si no está
     */
    public void anyadirCartaEnMano(Carta c) {
        if (!this.cartasEnMano.contains(c)){
            this.cartasEnMano.add(c);
        }
    }

    /*
     * Quita la carta c de las cartasEnMano sy y solo si está en el conjunto
     */
    public void quitarCartaEnMano(Carta c){
        if (this.cartasEnMano.contains(c)){
            this.cartasEnMano.remove(c);
        }
    }

    /*
     * Devuelve las cartas ganadas por el jugador
     */
    public List<Carta> getCartasGanadas() {
        return cartasGanadas;
    }

    /*
     * Añade cada una de las cartas del conjunto de cartas a las cartasGanadas sin repeticiones de cartas
     */
    public void anyadirCartasGanadas(List<Carta> cartas) {
        for (Carta c: cartas) {
            if(!this.cartasGanadas.contains(c)){
                this.cartasGanadas.add(c);
            }
        }
    }

    /*
     * Devuelve los puntos acumulados por el jugador
     */
    public int getPuntos() {
        return puntos;
    }

    /*
     * Pre: puntos > 0
     * Añade puntos a los puntos acumulados por el jugador hasta el momento
     */
    public void sumarPuntos(int puntos) {
        if (puntos>0){
            this.puntos += puntos;
        }
    }
}
