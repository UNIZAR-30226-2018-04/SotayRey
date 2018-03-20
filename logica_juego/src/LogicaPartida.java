/*
 * Autor: Crisan, Marius Sorin, Ignacio Bitrian, Victor Soria
 * Fecha: 13-03-18
 * Fichero: Fichero de la lógica del guiñote accesible desde al interfaz
 */

import java.util.ArrayList;
import java.util.List;

public class LogicaPartida {

    private EstadoPartida estado;

   /* public LogicaPartida(ArrayList<Usuarios> jugadores){
        estado = new EstadoPartida(jugadores);
    }

    public void crearPartida(ArrayList<Usuarios> jugadores){
        estado.crearBaraja();
        estado.barajar();
    }

    public EstadoPartida lanzarCarta(int jugador, Carta carta) {

    }
    */

    /**
     *
     * @param jugador
     * @return
     * @throws ExceptionJugadorIncorrecto
     */
    public EstadoPartida cantar20(String jugador) throws    ExceptionJugadorIncorrecto,
                                                            ExceptionRondaNoAcabada,
                                                            ExceptionTurnoIncorrecto{
        ArrayList<String> jugadores = estado.getJugadores();
        if(jugadores.contains(jugador)){
            if(estado.getCartasEnTapete().size() == 0){
                if(jugador.equals(estado.getTurno())){
                    estado.sumaCante20(jugador);
                }
                else{
                    throw new ExceptionTurnoIncorrecto();
                }
            }
            else {
                throw new ExceptionRondaNoAcabada();
            }
        }
        else{
            throw new ExceptionJugadorIncorrecto();
        }
        return estado;
    }


   //TODO: al iniciar la partida hay que hacer setTriunfo y poner la carta al
    // final
}
