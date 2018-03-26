package main.java;

/**
 * @Autores: Crisan, Marius Sorin; Ignacio Bitrian; Victor Soria
 * @Fecha: 11-03-18
 * @Fichero: Clase jugador de la lógica del guiñote
 */

import java.util.ArrayList;
//TODO: que pasa si un jugador tiene una carta ganada y también la tiene otro jugador o la tiene el en la mano

/**
 * Clase que representa abstractamente a un jugador. Este posee unas cartas en la mano, y unas cartas ganadas
 * Además cada jugador debe ser identificable respecto al resto. La clase Jugador almacena adicionalmente los
 * puntos ganados y los cantes realizados para calcular facilmente los puntos
 */
public class Jugador {
    private ArrayList<Carta> cartasEnMano;
    private ArrayList<Carta> cartasGanadas;
    private int puntos;
    private String id;
    private boolean cantes[] = {false, false, false, false};

    /**
     * Constructor que genera un nuevo jugador con identificador "id". Sin
     * cartas en la mano, con 0 puntos y sin cartas ganadas.
     * @param id
     */
    public Jugador(String id) {
        cartasEnMano = new ArrayList<>();
        cartasGanadas = new ArrayList<>();
        puntos = 0;
        String copia = new String(id);
        this.id = copia;
    }


    /**
     * Constructor que genera un nuevo jugador asignando id, cartas en la
     * mano, ganadas y puntos.
     * @param id
     * @param cartasEnMano
     * @param cartasGanadas
     * @param puntos
     */
    public Jugador(String id, ArrayList<Carta> cartasEnMano,
                   ArrayList<Carta> cartasGanadas, int puntos) {
        String copia = new String(id);
        this.id = copia;
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
     * Constructor que genera una copia identica de "jugador" de forma segura (sin utilizar los mismos objetos).
     * @param jugador
     */
    public Jugador(Jugador jugador){
        String copia = new String(jugador.id);
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


    /**
     * Devuelve el id del jugador, de forma segura.
     * @return
     */
    public String getId(){
        String copia = new String(id);
        return copia;
    }


    /**
     * Devuelve una lista con las cartas en mano de cada jugador
     * @return
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
     * Añade una carta c al conjunto de cartas en mano si no está en el
     * conjunto y si el jugador tiene menos de 6 cartas
     * @param c
     * @throws ExceptionNumeroMaximoCartas,ExceptionCartaYaExiste
     */
    public void anyadirCartaEnMano(Carta c) throws
            ExceptionNumeroMaximoCartas, ExceptionCartaYaExiste {
        if (this.cartasEnMano.size() == 6){
            throw new ExceptionNumeroMaximoCartas();
        }
        if (!this.cartasEnMano.contains(c)){
            Carta copia = new Carta(c);
            this.cartasEnMano.add(copia);
        } else {
            throw new ExceptionCartaYaExiste();
        }
    }


    /**
     * Quita la carta c de las cartasEnMano si y solo si, está en las cartas
     * en mano del jugador
     * @param c
     * @throws ExceptionJugadorSinCarta
     */
    public void quitarCartaEnMano(Carta c) throws ExceptionJugadorSinCarta{
        if (this.cartasEnMano.contains(c)){
            this.cartasEnMano.remove(c);
        } else {
            throw new ExceptionJugadorSinCarta();
        }
    }


    /**
     * Devuelve una copia de las cartas ganadas por el jugador
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


    /**
     * Añade la carta c a las cartasGanadas. Si la carta ya está en el
     * conjunto se lanza una excepción.
     * @param c
     * @throws ExceptionCartaYaExiste
     */
    public void anyadirCartaGanadas(Carta c) throws
            ExceptionCartaYaExiste {
        if(!this.cartasGanadas.contains(c)){
            Carta copia = new Carta(c);
            this.cartasGanadas.add(copia);
        } else {
            throw new ExceptionCartaYaExiste();
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
     * Función que consulta si se puede cantar con la sota y el rey de
     * cualquier palo. Si no puede cantar lanza una excepcción.
     */
    public void anyadirCante(Carta triunfo) throws
               ExceptionNoHayCantes {
        boolean reyes[] = {false, false, false, false};
        boolean sotas[] = {false, false, false, false};

        for(Carta iterador : cartasEnMano){
            if(iterador.getValor() == 10){
                marcaSotaORey(sotas, iterador);
            }
            if(iterador.getValor() == 12){
                marcaSotaORey(reyes, iterador);
            }
        }
        int sumaTotal=0;
        for(int i = 0; i < 4; i++){
            if(reyes[i] && sotas[i] && !cantes[i]){
                cantes[i] = true;
                if(esPaloTriunfo(triunfo,i)) {
                    sumaTotal += 40;
                }
                else{
                    sumaTotal += 20;
                }
            }
        }
        puntos += sumaTotal;
        if (sumaTotal == 0){
            throw new ExceptionNoHayCantes();
        }
    }

    /**
     * Función que redefine la comparación entre objetos jugdor.
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
        return     (id == jugador.id)
                && (puntos == puntos)
                && cartasEnMano.equals(jugador.cartasEnMano)
                && cartasGanadas.equals(jugador.cartasGanadas);
    }


    /*********************** FUNCIONES PRIVADAS *******************************/


    /**
     * Devuelve true si "i" corresponde al palo de triunfo.
     * @param triunfo
     * @param i
     * @return
     */
    private boolean esPaloTriunfo(Carta triunfo, int i){
        String palo="B";
        switch (i){
            case 0:
                palo="B";
                break;
            case 1:
                palo="C";
                break;
            case 2:
                palo="E";
                break;
            case 3:
                palo="O";
                break;
        }

        if (triunfo.getPalo().equals(palo)){
            return true;
        }
        else{
            return false;
        }
    }

    /**
     * Dado un vector de booleanos marca la posicion correspondiente al palo de la carta iterador como true.
     * @param vectorApariciones
     * @param iterador
     */
    private void marcaSotaORey(boolean[] vectorApariciones, Carta iterador) {
        switch (iterador.getPalo()){
            case "B":
                vectorApariciones[0] = true;
                break;
            case "C":
                vectorApariciones[1] = true;
                break;
            case "E":
                vectorApariciones[2] = true;
                break;
            case "O":
                vectorApariciones[3] = true;
                break;
        }
    }
}
