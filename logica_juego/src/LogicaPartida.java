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
   public void repartirCarta(String jugador) throws
           ExceptionJugadorIncorrecto, ExceptionMazoVacio,
           ExceptionNumeroMaximoCartas, ExceptionCartaYaExiste{
       Carta a = estado.getPrimeraCartaMazo();
       estado.anyadirCartaJugador(jugador, a);
   }


   public void lanzarCarta(String jugador, Carta c)throws
           ExceptionJugadorIncorrecto, ExceptionCartaIncorrecta,
           ExceptionTurnoIncorrecto, ExceptionJugadorSinCarta {
        estado.lanzarCartaJugador(jugador, c);
   }

    /**
     *
     * @param jugador
     * @return
     * @throws ExceptionJugadorIncorrecto si el jugador no pertenece a la
     * partida
     */
    public EstadoPartida cantar(String jugador) throws    ExceptionJugadorIncorrecto,
                                                            ExceptionRondaNoAcabada,
                                                            ExceptionTurnoIncorrecto{
        ArrayList<String> jugadores = estado.getJugadores();
        if(jugadores.contains(jugador)){
            if(estado.getCartasEnTapete().size() == 0){
                if(jugador.equals(estado.getTurno())){
                    estado.sumaCante(jugador);
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
        //TODO: no hay que devolver otro estado
        //EstadoPartida res = new EstadoPartida(estado);
        return estado;
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
    public void cambiarCartaPorTriunfo(String jugador, Carta c) throws
            ExceptionJugadorIncorrecto, ExceptionJugadorSinCarta,
            ExceptionCartaIncorrecta, ExceptionCartaYaExiste,
            ExceptionNumeroMaximoCartas {
            Carta triunfo = estado.getTriunfo();
            //Cambia la carta si y solo si es un 7 del mismo palo y su
            // puntuación es mayor
            if (triunfo.getPalo().equals(c.getPalo()) && triunfo.getValor()
                    == 7 && (triunfo.getPuntuación() > 0)){
                //TODO: hacer que solo se cambie después de ganar la ultima baza
                estado.setTriunfo(c);
                estado.quitarCartaJugador(jugador, c);
                estado.anyadirCartaJugador(jugador, triunfo);
            } else {
                throw new ExceptionCartaIncorrecta("Palo incorrecto o valor " +
                        "de la carta menor que el triunfo");
            }
    }


    public int consultarPuntos(String jugador) throws
            ExceptionJugadorIncorrecto{
        return estado.getPuntosJugador(jugador);
    }

   //TODO: al iniciar la partida hay que hacer setTriunfo y poner la carta al final
}
