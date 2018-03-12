/*
 * Autor: Crisan, Marius Sorin
 * Fecha: 11-03-18
 * Fichero: Estado de la partida del guiñote que permite ver las cartas de la partida y sus jugadores
 */

import java.util.ArrayList;
import Carta

public class EstadoPartida {
    private List<Jugador> jugadores;
    private List<Carta> cartasEnTapete;
    private List<Carta> mazo;
    private Jugador turno;
    private Carta triunfo;

    public EstadoPartida(LinkedList<Jugador> jugadores){

    }

    public List<Jugador> getJugadores() {
        return jugadores;
    }

    public List<Cartas> getCartas(Jugador jugador){
        if(jugadores.contains(jugador)){
            return jugador.getCartasEnMano();
        }
    }
}
