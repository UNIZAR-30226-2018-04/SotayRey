/**
 * @Autores: Crisan, Marius Sorin; Ignacio Bitrian; Victor Soria
 * @Fecha: 11-03-18
 * @Fichero: Estado de la partida del guiñote que permite ver las cartas de la partida y sus jugadores
 */

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Mazo:  almacena todas las cartas no utilizadas de la baraja española
 * CartasEnTapete: las cartas que están en la mesa en un turno
 * Jugadores:  información de cada uno de los jugadores que están
 * participando, almacenada en el mismo orden en el que jugarán
 * Turno:  jugador que tiene que lanzar
 * lanzar la carta
 * Triunfo:  carta que indica el palo al que se juega
 */
public class EstadoPartida {
    private ArrayList<Carta> mazo;
    private ArrayList<Carta> cartasEnTapete;
    private ArrayList<Jugador> jugadores;
    private Jugador turno;
    private Carta triunfo;
    private Random random;

    public EstadoPartida(EstadoPartida p){
        //this.mazo = new ArrayList<>(p.mazo);

    }

    public EstadoPartida(){
        barajear();
        this.cartasEnTapete = new ArrayList<>();
        //this.turno =
        this.triunfo = new Carta();
        this.random = new Random(); //Utilizado para generar baraja
    }

    /**
     * Devuelve la lista de cartas de la baraja española
     * @return List<Carta>
     */
    private ArrayList<Carta> crearBaraja(){
        ArrayList<Carta> baraja = new ArrayList<>();
        try {
            Carta a;
            int num;
            for (int i = 0; i < 40; ++i){
                num = i%10+1;
                if (num == 8 || num == 9) {
                    num += 3;
                }
                baraja.add(new Carta(num, i/10+1));
            }
        } catch (Exception e){
            System.err.println("Excepción generando baraja: " + e.getMessage());
        }
        return baraja;
    }


    /**
     * Asigna al mazo las 4o cartas de la baraja española en un orden aleatorio.
     */
    public void barajear(){
        mazo = crearBaraja();
        Carta uno, dos;
        int num;
        for (int i = 0; i < 40; ++i){
            num = random.nextInt()%40;
            uno = mazo.get(num);
            dos = mazo.get(i);
            mazo.set(num, dos);
            mazo.set(i, uno);
        }
    }


    /**
     * Devuelve una lista con los identificadores de todos los jugadores
     * @return List<Integer>
     */
    public ArrayList<String> getJugadores(){
        ArrayList<String> res = new ArrayList<>();
        for (Jugador j: jugadores) {
            res.add(j.getId());
        }
        return res;
    }


    /**
     * Busca un jugador identificado por "id" == "jugador" en la partida.
     * @param jugador
     * @return
     * @throws ExceptionJugadorIncorrecto
     */
    private Jugador encuentraJugador(String jugador) throws ExceptionJugadorIncorrecto{
        for (Jugador actual : jugadores){
            if(jugador.equals(actual.getId())){
                return actual;
            }
        }
        throw new ExceptionJugadorIncorrecto();
    }


    /**
     * Devuelve la lista de cartas de el jugador "jugador". Si "jugador" no
     * está en la partida lanza una excepcion
     * @param jugador
     * @return
     * @throws ExceptionJugadorIncorrecto
     */
    public List<Carta> getCartas(String jugador) throws ExceptionJugadorIncorrecto{
        Jugador jugadorEncontrado = encuentraJugador(jugador);
        return jugadorEncontrado.getCartasEnMano();
    }


    /**
     * Añade una carta al jugador "jugador". Si "jugador" no está en la
     * partida lanza una excepción. Si el jugador posee la carta u otras 6
     * cartas lanza otras excepciones.
     * @param jugador
     * @param carta
     * @throws ExceptionNumeroMaximoCartas
     * @throws ExceptionJugadorIncorrecto
     * @throws ExceptionCartaYaExiste
     */
    public void anyadirCartaJugador(String jugador, Carta carta) throws
            ExceptionNumeroMaximoCartas, ExceptionJugadorIncorrecto,
            ExceptionCartaYaExiste {
        Jugador jugadorEncontrado = encuentraJugador(jugador);
        jugadorEncontrado.anyadirCartaEnMano(carta);
    }



    public void pasarTurno(String jugador){
        //Asigna el turno al siguiente jugador
        int n_jug = jugadores.size();
        for (int i = 0; i < n_jug; i++) {
            if (jugador.equals(jugadores.get(i))) {
                turno = jugadores.get((i + 1) % n_jug);
                break;
            }
        }
    }
    /**
     * Mueve una carta de jugador "jugador" al tapete.Si "jugador" no está en
     * la partida lanza una excepcion. Si el turno no es de "jugador" lanza
     * otra excepcion. Y si "jugador" no posee la carta lanza otra excepcion.
     * @param jugador
     * @param carta
     * @throws ExceptionJugadorIncorrecto
     * @throws ExceptionJugadorSinCarta
     * @throws ExceptionTurnoIncorrecto
     */
    public void lanzarCartaJugador(String jugador, Carta carta) throws
            ExceptionJugadorIncorrecto, ExceptionJugadorSinCarta,
            ExceptionTurnoIncorrecto {
        Jugador jugadorEncontrado = encuentraJugador(jugador);
        /*
        if(turno.equals(jugadorEncontrado)) {
            if (jugadorEncontrado.getCartasEnMano().contains(carta)) {
                //Ronda de descarte o es el primero
                if (mazo.size() > 0 || cartasEnTapete.size() == 0) {
                    jugadorEncontrado.quitarCartaEnMano(carta);
                    cartasEnTapete.add(carta);
                    pasarTurno(jugador);
                    }
                }
                //Ronda de arrastre
                else{
                    //Obligación de jugar al Palo de arrastre
                    Carta inicial = cartasEnTapete.get(0);
                    /*
                    if (carta.getPalo().equals(inicial.getPalo())){
                        //es del mismo palo
                        if (carta.getPuntuación()>inicial.getPuntuación()){
                            //tira la carta porque es de mayor valor que la
                            // que hay
                            jugadorEncontrado.quitarCartaEnMano(carta);
                            cartasEnTapete.add(new Carta(carta));
                            pasarTurno(jugador);
                        } else {
                            //se mira el resto de cartas para ver si tiene
                            // alguna mayor del mismo palo
                            if (1){
                            } else {
                                //si tiene una del mismo palo mayor que la
                                // inicial lanzar excepción
                                //TODO: mirar en la mano
                                jugadorEncontrado.quitarCartaEnMano(carta);
                                cartasEnTapete.add(new Carta(carta));
                                pasarTurno(jugador);
                            }
                        }
                    }
                    //Obligación de hacer baza
                    else{//DIFERENTE PALO
                        if (){
                            //mirar si tiene del mismo palo
                            throw new ExceptionCartaIncorrecta("Tienes otra " +
                                    "carta del arrastre");
                        } else if (){
                            //mirar si tiene triunfo
                            // y no

                        }

                    }
                    //cualquier carta

                }
            }
            else {
                throw new ExceptionJugadorSinCarta();
            }
        } else{
            throw new ExceptionTurnoIncorrecto();
        } */
    }


    /**
     * Devuelve la primera carta del mazo y la elimina. Si en el mazo no
     * quedan cartas lanza una excepcion.
     * @return
     * @throws ExceptionMazoVacio
     */
    public Carta getPrimeraCartaMazo() throws ExceptionMazoVacio {
        if(mazo.size() != 0){
            return new Carta(mazo.remove(0));
        }
        else{
            throw new ExceptionMazoVacio();
        }
    }


    /**
     * Devuelve las cartas que están encima de la mesa.
     * @return
     */
    public  ArrayList<Carta> getCartasEnTapete(){
        ArrayList<Carta> copia = new ArrayList<>();
        for(Carta iterador : cartasEnTapete){
            Carta nueva =  new Carta(iterador);
            copia.add(nueva);
        }
        return copia;
    }


    /**
     * Devuelve el triunfo de la partida.
     * @return
     */
    public Carta getTriunfo(){
        Carta copia = new Carta(triunfo);
        return copia;
    }


    /**
     * Cambia el triunfo por nuevoTriunfo.
     * @param nuevoTriunfo
     */
    public void setTriunfo(Carta nuevoTriunfo){
        Carta copia = new Carta(nuevoTriunfo);
        this.triunfo = copia;
    }


    /**
     * Mueve las cartas del tapete a ganadas del jugador identificado por el
     * entero "jugador". Si no existe ningún jugador en la partida con ese
     * identificador lanza una excepcion. Si todos los jugadores no han
     * lanzado su carta lanza otra excepcion. Si la carta ya está añadida a
     * ganadas lanza otra excepción. Además cambia el siguiente turno al jugador
     * identificado por el string "jugador"
     * @param jugador
     * @throws ExceptionRondaNoAcabada
     * @throws ExceptionJugadorIncorrecto
     * @throws ExceptionCartaYaExiste
     */
    public void anyadirAGanadas(String jugador) throws  ExceptionJugadorIncorrecto,
                                                        ExceptionRondaNoAcabada,
                                                        ExceptionCartaYaExiste {
        Jugador jugadorEncontrado = encuentraJugador(jugador);
        if(cartasEnTapete.size() == jugadores.size()){
            jugadorEncontrado.anyadirCartasGanadas(cartasEnTapete);
            cartasEnTapete = new ArrayList<>();
            turno = jugadorEncontrado;
        } else {
            throw new ExceptionRondaNoAcabada();
        }
    }

    /**
     * Devuelve el identificador del jugador que debe lanzar la siguiente carta
     * @return
     */
    public String getTurno() {
        String copia = new String(this.turno.getId());
        return copia;
    }

    /**
     * Función que encuentra un usuario e intenta cantar 20 por el jugador
     * identificado por "jugador". Si no existe ningún jugador en la partida
     * con ese identificador lanza una excepcion. Si el jugador no puede cantar
     * las 20 lanza una excepción.
     * @param jugador
     * @throws ExceptionJugadorIncorrecto
     */
    public void sumaCante20(String jugador) throws ExceptionJugadorIncorrecto{
        Jugador jugadorEncontrado = encuentraJugador(jugador);
        jugadorEncontrado.anyadirCante20();
    }


    /**
     * Devuelve las cartas ganadas por un jugador. Si el jugador no pertenece
     * a la partida lanza un excepción.
     * @param jugador
     * @return
     * @throws ExceptionJugadorIncorrecto
     */
    public ArrayList<Carta> getCartasGanadas(String jugador) throws
            ExceptionJugadorIncorrecto {
        Jugador j = encuentraJugador(jugador);
        return j.getCartasGanadas();
    }
}
