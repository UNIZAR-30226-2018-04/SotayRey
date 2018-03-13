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

    public EstadoPartida(List<Jugador> jugadores){
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
