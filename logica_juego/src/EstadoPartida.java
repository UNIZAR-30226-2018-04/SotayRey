/*
 * Autor: Crisan, Marius Sorin
 * Fecha: 11-03-18
 * Fichero: Estado de la partida del guiñote que permite ver las cartas de la partida y sus jugadores
 */

import java.util.List;

public class EstadoPartida {
    private List<Jugador> jugadores;
    private List<Carta> cartasEnTapete;
    private List<Carta> mazo;
    private Jugador turno;
    private Carta triunfo;

    public EstadoPartida(List<Jugador> jugadores){

    }

    public List<Jugador> getJugadores() {
        return jugadores;
    }

    /**
     * Devuelve la lista de cartas de el jugador "jugador". Si "jugador" no está en la partida lanza una excepcion
     *
     * TODO: ExceptionJugadorIncorrecto
     *
     * @param jugador
     * @return
     * @throws Exception
     */
    public List<Carta> getCartas(Jugador jugador) throws Exception{
        if(jugadores.contains(jugador)){
            return jugador.getCartasEnMano();
        }
        else{
            throw new Exception();
        }
    }

    /**
     * Añade una carta al jugador "jugador". Si "jugador" no está en la partida lanza una excepcion. Si el jugador
     * posee ya 6 cartas lanza otra excepcion
     *
     * TODO: ExceptionNumeroCartasMaximo
     *
     * @param jugador
     * @param carta
     * @throws Exception
     */
    public void addCartaJugador(Jugador jugador, Carta carta) throws Exception{
        if(jugadores.contains(jugador)){
            if(jugador.getCartasEnMano().size() > 6){
                throw new Exception();
            }
            else{
                jugador.anyadirCartaEnMano(carta);
            }
        }
        else{
            throw new Exception();
        }
    }

    /**
     * Mueve una carta de jugador "jugador" al tapete.Si "jugador" no está en la partida lanza una excepcion.
     * Si el turno no es de "jugador" lanza otra excepcion. Y si "jugador" no posee la carta lanza otra excepcion.
     *
     * TODO: ExceptionTurnoIncorrecto, ExceptionCartaIncorrecta.
     *
     * @param jugador
     * @param carta
     * @throws Exception
     */
    public void moverCartaJugadorTapete(Jugador jugador, Carta carta) throws Exception{
        if(jugadores.contains(jugador)){
            if(turno.equals(jugador)) {
                if (jugador.getCartasEnMano().contains(carta)) {
                    jugador.quitarCartaEnMano(carta);
                    cartasEnTapete.add(carta);
                } else {
                    throw new Exception();
                }
            }
            else{
                throw new Exception();
            }
        }
        else{
            throw new Exception();
        }
    }

    /**
     * Devuelve la primera carta del mazo y la elimina. Si en el mazo no quedan cartas lanza una excepcion.
     *
     * TODO: ExceptionMazoVacio
     *
     * @return
     * @throws Exception
     */
    public Carta getPrimeraCartaMazo() throws Exception{
        if(mazo.size() != 0){
            return mazo.remove(0);
        }
        else{
            throw new Exception();
        }
    }



}
