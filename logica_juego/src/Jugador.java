/*
 * Autor: Crisan, Marius Sorin
 * Fecha: 11-03-18
 * Fichero: Clase jugador de la lógica del guiñote
 */

import java.util.ArrayList;
import java.util.List;

public class Jugador {
    private ArrayList<Carta> cartasEnMano;
    private ArrayList<Carta> cartasGanadas;
    private int puntos;
    private int id;

    /**
     * Constructor que genera un nuevo jugador con identificador "id". Sin cartas en la mano, con 0 puntos y sin cartas
     * ganadas.
     * @param id
     */
    public Jugador(int id) {
        cartasEnMano = new ArrayList<>();
        cartasGanadas = new ArrayList<>();
        puntos = 0;
        this.id = id;
    }

    /**
     * Constructor que genera un nuevo jugador asignando id, cartas en la mano, ganadas y puntos.
     * @param id
     * @param cartasEnMano
     * @param cartasGanadas
     * @param puntos
     */
    public Jugador(int id, ArrayList<Carta> cartasEnMano, ArrayList<Carta> cartasGanadas, int puntos) {
        this.id = id;
        this.puntos = puntos;

        this.cartasEnMano = new ArrayList<>();
        for(Carta iterador : cartasEnMano){
            Carta nueva = new Carta(iterador);
            this.cartasEnMano.add(nueva);
        }

        this.cartasGanadas = new ArrayList<>();
        for(Carta iterador : cartasGanadas){
            Carta nueva = new Carta(iterador);
            this.cartasGanadas.add(nueva);
        }
    }

    /**
     * Constructor que genera una copia identica de "jugador"
     * @param jugador
     */
    public Jugador(Jugador jugador){
        this.id = jugador.id;
        this.puntos = jugador.puntos;
        this.cartasEnMano = new ArrayList<>();
        this.cartasGanadas = new ArrayList<>();

        for(Carta iterador : jugador.cartasEnMano){
            Carta nueva = new Carta(iterador);
            this.cartasEnMano.add(nueva);
        }

        for(Carta iterador : jugador.cartasGanadas){
            Carta nueva = new Carta(iterador);
            this.cartasGanadas.add(nueva);
        }
    }

    public int getId(){
        return id;
    }

    /*
     * Devuelve una lista con las cartas en mano de cada jugador
     */
    public ArrayList<Carta> getCartasEnMano() {
        ArrayList<Carta> copia = new ArrayList<>();
        for(Carta iterador : this.cartasEnMano){
            Carta nueva = new Carta(iterador);
            copia.add(nueva);
        }
        return copia;
    }


    /**
     * Añade una carta c al conjunto de cartas en mano si no está
     * @param c
     */
    public void anyadirCartaEnMano(Carta c) {
        if (!this.cartasEnMano.contains(c)){
            Carta copia = new Carta(c);
            this.cartasEnMano.add(copia);
        }
    }


    /**
     * Quita la carta c de las cartasEnMano sy y solo si está en el conjunto
     * @param c
     */
    public void quitarCartaEnMano(Carta c){
        if (this.cartasEnMano.contains(c)){
            this.cartasEnMano.remove(c);
        }
    }


    /**
     * Devuelve las cartas ganadas por el jugador
     * @return
     */
    public ArrayList<Carta> getCartasGanadas() {
        ArrayList<Carta> copia = new ArrayList<>();
        for(Carta iterador : cartasGanadas){
            Carta nueva = new Carta(iterador);
            copia.add(nueva);
        }
        return copia;
    }

    /*
     */

    /**
     * Añade cada una de las cartas del conjunto de cartas a las cartasGanadas sin repeticiones de cartas
     * @param cartas
     */
    public void anyadirCartasGanadas(ArrayList<Carta> cartas) {
        for (Carta c: cartas) {
            if(!this.cartasGanadas.contains(c)){
                Carta copia = new Carta(c)
                this.cartasGanadas.add(copia);
            }
        }
    }

    /**
     * Devuelve los puntos acumulados por el jugador
     * @return
     */
    public int getPuntos() {
        return puntos;
    }



    /**
     * Pre: puntos > 0
     * Añade puntos a los puntos acumulados por el jugador hasta el momento
     * @param puntos
     */
    public void sumarPuntos(int puntos) {
        if (puntos>0){
            this.puntos += puntos;
        }
    }

    /**
     *
     * @param o
     * @return
     */
    @Override
    public boolean equals(Object o) {
        // self check
        if (this == o)
            return true;
        // null check
        if (o == null)
            return false;
        // type check and cast
        if (getClass() != o.getClass())
            return false;
        Jugador jugador = (Jugador) o;
        // field comparison
        return     Object.equals(id, jugador.id)
                && Object.equals(puntos, jugador.puntos)
                && Object.equals(cartasEnMano, jugador.cartasEnMano)
                && Object.equals(cartasGanadas, jugador.cartasGanadas);
    }
}
