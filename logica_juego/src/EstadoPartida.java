/*
 * Autor: Crisan, Marius Sorin
 * Fecha: 11-03-18
 * Fichero: Estado de la partida del guiñote que permite ver las cartas de la partida y sus jugadores
 */

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class EstadoPartida {
    private List<Jugador> jugadores;
    private List<Carta> cartasEnTapete;
    private List<Carta> mazo;
    private Jugador turno;
    private Carta triunfo;
    private Random random;

    public EstadoPartida(List<Usuarios> jugadores){
        this.random = new Random();
        this.mazo = crearBaraja();
        //this.mazo = barajar(this.mazo);
    }

    /**
     * Devuelve la lista de cartas de el jugador "jugador". Si "jugador" no está en la partida lanza una excepcion
     * @return List<Carta>
     */
    public List<Carta> crearBaraja(){
        List<Carta> baraja = new ArrayList<>();
        Carta a = new Carta();
        int num;
        for (int i = 0; i < 40; ++i){
            try {
                num = i%10+1;
                if (num == 8 || num == 9) {
                    num += 3;
                }
                a = new Carta(num, i/10+1);
                System.out.println(i/10+1);
            } catch (Exception e){
                System.err.println("Excepción generando baraja: " + e.getMessage());
            }
            baraja.add(a);
        }
        return baraja;
    }

    public List<Carta> barajar(List<Carta> baraja){
        Carta uno, dos;
        int num;
        for (int i = 0; i < 40; ++i){
            num = random.nextInt()%40;
            uno = baraja.get(num);
            dos = baraja.get(i);
            baraja.set(num, dos);
            baraja.set(i, uno);
        }
        return baraja;
    }

    private Jugador encuentraJugador(int jugador){
        for (Jugador actual : jugadores){
            if(actual.getId() == jugador){
                return actual;
            }
        }
        return null;
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
    public List<Carta> getCartas(int jugador) throws Exception{
        Jugador jugadorEncontrado = encuentraJugador(jugador);
        if( jugadorEncontrado != null){
            return jugadorEncontrado.getCartasEnMano();
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
    public void addCartaJugador(int jugador, Carta carta) throws Exception{
        Jugador jugadorEncontrado = encuentraJugador(jugador);
        if( jugadorEncontrado != null){
            if(jugadorEncontrado.getCartasEnMano().size() > 6){
                throw new Exception();
            }
            else{
                jugadorEncontrado.anyadirCartaEnMano(carta);
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
    public void moverCartaJugadorTapete(int jugador, Carta carta) throws Exception{
        Jugador jugadorEncontrado = encuentraJugador(jugador);
        if( jugadorEncontrado != null){
            if(turno.equals(jugadorEncontrado)) {
                if (jugadorEncontrado.getCartasEnMano().contains(carta)) {
                    jugadorEncontrado.quitarCartaEnMano(carta);
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

    /**
     * Devuelve las cartas que están encima de la mesa.
     * @return
     */
    public  List<Carta> getCartasEnTapete(){
        return cartasEnTapete;
    }

    /**
     * Devuelve el triunfo de la partida.
     * @return
     */
    public Carta getTriunfo(){
        return triunfo;
    }

    /**
     * Cambia el triunfo por nuevoTriunfo.
     * @param nuevoTriunfo
     */
    public void setTriunfo(Carta nuevoTriunfo){
        this.triunfo = nuevoTriunfo;
    }

    /**
     * Mueve las cartas del tapete a ganadas del jugador identificado por el entero "jugador". Si no existe ningún
     * jugador en la partida con ese identificador lanza una excepcion. Si todos los jugadores no han lanzado su carta
     * lanza una excepcion.
     * TODO: ExceptionRondaNoAcabada
     * @param jugador
     */
    public void anyadirAGanadas(int jugador) throws{
        Jugador jugadorEncontrado = encuentraJugador(jugador);
        if( jugadorEncontrado != null){
            if(cartasEnTapete.size() == jugadores.size()){
                jugadorEncontrado.anyadirCartasGanadas(cartasEnTapete);
                cartasEnTapete = new ArrayList<Carta>();
            }
        }
        else{
            throw new Exception();
        }
    }
}
