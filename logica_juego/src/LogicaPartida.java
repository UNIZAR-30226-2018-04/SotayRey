/*
 * Autor: Crisan, Marius Sorin, Ignacio Bitrian, Victor Soria
 * Fecha: 13-03-18
 * Fichero: Fichero de la lógica del guiñote accesible desde al interfaz
 */

import java.util.List;

public class LogicaPartida {

    private EstadoPartida estado;

    public LogicaPartida(List<Integer> jugadores){
        estado = new EstadoPartida(jugadores);
    }

    public void crearPartida(List<Integer> jugadores){
        estado.crearBaraja();
        estado.barajar();
    }

    public EstadoPartida lanzarCarta(int jugador, Carta carta) {

    }
}
