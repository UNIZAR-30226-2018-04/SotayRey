package main.java;

/*
 * Autor: Crisan, Marius Sorin, Ignacio Bitrian, Victor Soria
 * Fecha: 13-03-18
 * Fichero: Fichero de la lógica del guiñote accesible desde al interfaz
 */

import java.util.ArrayList;
import java.util.List;

public class LogicaPartida {

    private EstadoPartida estado;

    /**
     * Constructor que crea un estado de la partida nuevo compuesto por jugadores identificados por los strings
     * "jugadores"
     * @param jugadores
     * @throws ExceptionEquipoIncompleto si el número de jugadores es incorrecto
     */
    public LogicaPartida(ArrayList<String> jugadores) throws
           ExceptionEquipoIncompleto {
        estado = new EstadoPartida(jugadores);
    }


    /**
     * Reparte 6 cartas a cada jugador y establece el triunfo.
     * @return
     * @throws ExceptionNumeroMaximoCartas
     * @throws ExceptionCartaYaExiste
     * @throws ExceptionJugadorIncorrecto
     * @throws ExceptionMazoVacio
     */
    public EstadoPartida crearPartida() throws ExceptionNumeroMaximoCartas,
            ExceptionCartaYaExiste, ExceptionJugadorIncorrecto,
            ExceptionMazoVacio {

        // Reparte 6 cartas a cada jugador
        for (int i = 0; i < 6; i++) {
            for (String j: estado.getJugadoresId()) {
                repartirCarta(j);
            }
        }

        // Elige triunfo
        Carta triunfo = estado.getPrimeraCartaMazo();
        estado.setTriunfo(triunfo);
        return new EstadoPartida(estado);
    }


    /**
     * Pre: Jugador debe pertenecer a la lista de jugadores de la partida
     * Post: Coge la primera carta del mazo y se la asigna al "Jugador". Si
     * el jugador no existe, no quedan cartas en el mazo, o ya tiene esa
     * carta o 6 cartas en la mano lanza una excepción.
     * @param jugador
     * @throws ExceptionJugadorIncorrecto si el jugador no pertenece a la
     * partida
     * @throws ExceptionMazoVacio si no quedan cartas en mazo
     * @throws ExceptionNumeroMaximoCartas si el jugador tiene 6 o más cartas
     * en la mano
     * @throws ExceptionCartaYaExiste si ya tiene esa carta en la mano
     */
   public EstadoPartida repartirCarta(String jugador) throws
           ExceptionJugadorIncorrecto, ExceptionMazoVacio,
           ExceptionNumeroMaximoCartas, ExceptionCartaYaExiste{
       Carta a = estado.getPrimeraCartaMazo();
       estado.anyadirCartaJugador(jugador, a);
       return new EstadoPartida(estado);
   }

    /**
     * Lanza la carta "c" por el Jugador "jugador". Si no es su turno, no posee dicha carta o incumple alguna de las
     * reglas del guiñote, lanza una excepción.
     * @param jugador
     * @param c
     * @return
     * @throws ExceptionJugadorIncorrecto
     * @throws ExceptionCartaIncorrecta
     * @throws ExceptionTurnoIncorrecto
     * @throws ExceptionJugadorSinCarta
     */
   public EstadoPartida lanzarCarta(String jugador, Carta c)throws
           ExceptionJugadorIncorrecto, ExceptionCartaIncorrecta,
           ExceptionTurnoIncorrecto, ExceptionJugadorSinCarta {
        estado.lanzarCartaJugador(jugador, c);
        return new EstadoPartida(estado);
   }

    /**
     * Asigna el turno y puntuación de las cartas del tapete de cada ronda
     * al ganador de la ronda.
     * @return
     * @throws ExceptionRondaNoAcabada
     * @throws ExceptionCartaYaExiste
     */
   public EstadoPartida siguienteRonda() throws ExceptionRondaNoAcabada,
           ExceptionCartaYaExiste {
       estado.terminarRonda();
       return new EstadoPartida(estado);
   }

    /**
     * Intenta realizar un cante, ya sea de 20 o 40 por el jugador "jugador". Si no es su turno o no posee ningún cante
     * lanza una excepcción.
     * @param jugador
     * @return
     * @throws ExceptionJugadorIncorrecto si el jugador no pertenece a la
     * partida
     */
    public EstadoPartida cantar(String jugador) throws    ExceptionJugadorIncorrecto,
                                                            ExceptionRondaNoAcabada,
                                                            ExceptionNoPuedesCantar,
                                                            ExceptionNoPuedesCantar {
        ArrayList<String> jugadores = estado.getJugadoresId();
        if(jugadores.contains(jugador)){
            if(estado.getCartasEnTapete().size() == 0){
                estado.sumaCante(jugador);
            }
            else {
                throw new ExceptionRondaNoAcabada();
            }
        }
        else{
            throw new ExceptionJugadorIncorrecto();
        }
        return new EstadoPartida(estado);
    }


    /**
     * Si la carta del jugador es un 7 del mismo palo que el triunfo y el
     * triunfo es la SOTA, el CABALLO, el REY, el AS o el TRES las intercambia.
     * En caso contrario, lanza una excepción.
     * @param jugador
     * @param c
     * @throws ExceptionJugadorIncorrecto si el jugador no pertenece a la
     * partida
     * @throws ExceptionJugadorSinCarta si el jugador no tiene esa carta
     * @throws ExceptionCartaIncorrecta si la carta no cumple las normas del
     * guiñote para cambiarla por el triungo
     * @throws ExceptionCartaYaExiste si el jugador ya tiene esa carta en la
     * mano
     * @throws ExceptionNumeroMaximoCartas si el jugador tiene 6 o más cartas
     * en la mano
     */
    public EstadoPartida cambiarCartaPorTriunfo(String jugador, Carta c) throws
            ExceptionJugadorIncorrecto, ExceptionJugadorSinCarta,
            ExceptionCartaIncorrecta, ExceptionCartaYaExiste,
            ExceptionNumeroMaximoCartas, ExceptionRondaNoAcabada {
        Carta triunfo = estado.getTriunfo();

        int n_cartas_mazo = estado.getMazo().size();
        //Ha terminado la ronda y ha sido el ganador
        if (estado.getJugadoresId().get(estado.getTurno()).equals(jugador)
                && estado.getCartasEnTapete().size() == 0 &&
                n_cartas_mazo > 0
        /**
         * TODO: hace falta?? && n_cartas_mazo <= 40-(estado.getJugadoresId())
         * .size()*6)
         */
        ) {

            //Cambia la carta si y solo si es un 7 del mismo palo y su
            // puntuación es mayor
            if (triunfo.getPalo().equals(c.getPalo
                    ()) && triunfo
                    .getValor()
                    == 7 && (triunfo.getPuntuación() > 0)){
                estado.setTriunfo(c);
                estado.quitarCartaJugador(jugador, c);
                estado.anyadirCartaJugador(jugador, triunfo);
            } else {
                throw new ExceptionCartaIncorrecta("Palo incorrecto o valor " +
                        "de la carta menor que el triunfo");
            }
        } else {
            throw new ExceptionRondaNoAcabada("No se puede cambiar el " +
                    "triunfo si no has sido ganador o no ha terminado la " +
                    "ronda");
        }

        return new EstadoPartida(estado);
    }


    /**
     * Devuelve los acumulados por el jugador.
     * @param jugador
     * @return
     * @throws ExceptionJugadorIncorrecto
     */
    public int consultarPuntos(String jugador) throws
            ExceptionJugadorIncorrecto{
        return estado.getPuntosJugador(jugador);
    }

    public EstadoPartida getEstado(){
        return new EstadoPartida(estado);
    }

}
