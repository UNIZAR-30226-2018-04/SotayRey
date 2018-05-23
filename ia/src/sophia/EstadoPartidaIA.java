/*
 * @Autores: Crisan, Marius Sorin; Ignacio Bitrian; Victor Soria
 * @Fecha: 11-03-18
 * @Fichero: Estado de la partida del guiñote que permite ver las cartas de la partida y sus jugadores
 */
package sophia;


import main.java.EstadoPartida;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * Mazo:  almacena todas las cartas no utilizadas en la partida de la baraja española
 * CartasEnTapete: las cartas que están en la mesa en un turno
 * Jugadores:  información de cada uno de los jugadores que están
 * participando, almacenada en el mismo orden en el que jugarán. Si se juega
 * por equipos se almacena [j1_equipo1, j2_equipo2, j3_equipo1, j4_equipo2]
 * Turno:  indíce del jugador que tiene que lanzar lanzar la carta
 * Triunfo:  carta que indica el palo al que se juega
 */
public class EstadoPartidaIA {

    // El jugador 0 es la IA (1 es rival)
    private int turno;
    // Puntos de cada jugador
    private ArrayList<Integer> puntos;
    // Cartas de cada jugador
    private ArrayList<ArrayList<CartaIA>> manos;
    // Es partida de vueltas
    private boolean vueltas;
    // Cartas que ningún jugador conoce
    private ArrayList<CartaIA> restantes;
    // Que cantes ha habido {B,C,E,O}
    private ArrayList<Boolean> cantes;
    // Carta del triunfo
    private CartaIA triunfo;
    // La carta que ha sido tirada en el tapete
    private CartaIA cartaTirada;
    // Semilla
    private Random random;

    /**
     * Crea un estado para la IA vacío
     */
    public EstadoPartidaIA() {
    }

    /**
     * Crea un estado para la IA ha partir del estado de la partida
     *
     * @param p
     */
    public EstadoPartidaIA(EstadoPartida p, boolean v) {
        // Turno
        this.turno = p.getTurno();
        // Puntos iniciales
        this.puntos = new ArrayList<>(2);
        this.puntos.add(p.getJugadores().get(0).getPuntos());
        this.puntos.add(p.getJugadores().get(1).getPuntos());
        // Cartas iniciales
        this.manos = new ArrayList<>(2);
        this.manos.add(CartaIA.toArray(p.getJugadores().get(0).getCartasEnMano()));
        this.manos.add(CartaIA.toArray(p.getJugadores().get(1).getCartasEnMano()));
        // Es partidad de vueltas
        this.vueltas = v;
        // Cartas restantes (es necesario eliminar el triunfo del final)
        this.restantes = CartaIA.toArray(p.getMazo());
        this.restantes.remove(this.restantes.size() - 1);
        //Cantes
        this.cantes = new ArrayList<Boolean>(Collections.nCopies(4, false));
        // El triunfo
        this.triunfo = new CartaIA(p.getTriunfo());
        // No hay ninguna carta tirada al principio
        this.cartaTirada = null;
        // Semilla
        this.random = new Random();
    }

    public static EstadoPartidaIA nuevaPartida() {
        EstadoPartidaIA np = new EstadoPartidaIA();

        // Semilla
        np.random = new Random();
        // Turno
        np.turno = 1; //np.random.nextInt(2);
        // Puntos iniciales
        np.puntos = new ArrayList<>(Collections.nCopies(2, 0));
        // Cartas iniciales
        np.manos = new ArrayList<>(2);
        np.manos.add(new ArrayList<>());
        np.manos.add(new ArrayList<>());
        // Es partidad de vueltas
        np.vueltas = false;
        //Cantes ya estan inicializadas
        np.cantes = new ArrayList<Boolean>(Collections.nCopies(4, false));
        // No hay ninguna carta tirada al principio
        np.cartaTirada = null;

        // Se reparten las cartas
        np.barajar();

        return np;
    }

    /**
     * Devuelve un estado IA idéntico realizando copias profundas
     */
    public EstadoPartidaIA clonar() {
        EstadoPartidaIA clon = new EstadoPartidaIA();
        // Turno
        clon.turno = this.turno;
        // Puntos
        clon.puntos = new ArrayList<>(2);
        clon.puntos.add(this.puntos.get(0));
        clon.puntos.add(this.puntos.get(1));
        // Manos
        clon.manos = new ArrayList<>(2);
        clon.manos.add(new ArrayList<>());
        clon.manos.add(new ArrayList<>());
        this.manos.get(0).forEach(c -> clon.manos.get(0).add(new CartaIA(c)));
        this.manos.get(1).forEach(c -> clon.manos.get(1).add(new CartaIA(c)));
        // Vueltas
        clon.vueltas = this.vueltas;
        // Cartas que ningún jugador conoce
        clon.restantes = new ArrayList<>(this.restantes.size());
        this.restantes.forEach(c -> clon.restantes.add(new CartaIA(c)));
        // Que cantes ha habido {B,C,E,O}
        clon.cantes = new ArrayList<>(4);
        clon.cantes.addAll(this.cantes);
        // Carta del triunfo
        clon.triunfo = new CartaIA(this.triunfo);
        // La carta que ha sido tirada en el tapete
        clon.cartaTirada = this.cartaTirada == null ? null : new CartaIA(this.cartaTirada);
        // Semilla
        clon.random = new Random();

        return clon;
    }


    public EstadoPartidaIA determinizar(int observador) {
        EstadoPartidaIA clon = this.clonar();

        // Todas las cartas que el observador no conoce
        // (cartas del mazo + cartas del rival)
        ArrayList<CartaIA> desconocidas = new ArrayList<>();
        desconocidas.addAll(clon.restantes);
        desconocidas.addAll(clon.manos.get(1 - observador));

        // Se barajean y se reparten al rival
        Collections.shuffle(desconocidas);
        desconocidas.add(clon.triunfo);
        for (int i = 0; i < clon.manos.get(1-observador).size(); i++) {
            clon.manos.get(1-observador).set(i, desconocidas.get(0));
            desconocidas.remove(0);
        }

        // Se vuelve a quitar el triunfo
        if (desconocidas.size() > 0) {
            desconocidas.remove(desconocidas.size() - 1);
        }

        // Se devuelven las restantes al mazo
        clon.restantes = desconocidas;

        return clon;
    }

    public void realizarMovimiento(CartaIA movimiento, int jugador) {
        // Se elimina la carta del jugador
        this.manos.get(jugador).remove(movimiento);

        // Si es el que saca no hay más que hacer
        if (this.cartaTirada == null) {
            cartaTirada = movimiento;
            this.turno = 1 - this.turno;
            return;
        }
        // Si no
        if (this.cartaTirada.mata(this.triunfo.palo, movimiento)) {
            // Pierde el jugador de este movimiento
            jugador = 1 - jugador;
        }

        // Se actualizan los puntos del jugador que ha ganado
        int puntosJugador = this.puntos.get(jugador);
        puntosJugador += movimiento.getPuntos() + this.cartaTirada.getPuntos();
        this.puntos.set(jugador, puntosJugador);


        // Robar cartas
        if (this.restantes.size() >= 1) {
            this.restantes.add(this.triunfo);
            this.manos.get(jugador).add(this.restantes.get(0));
            this.restantes.remove(0);
            this.manos.get(1-jugador).add(this.restantes.get(0));
            this.restantes.remove(0);
            if (this.restantes.size()>0) {
                this.restantes.remove(this.restantes.size() - 1);
            }
        }

        // Se quita la carta del tapete
        this.cartaTirada = null;

        // Se comprueban los cates
        for (CartaIA c1 : this.manos.get(jugador)) {
            for (CartaIA c2 : this.manos.get(jugador)) {
                if (c1.rank == 6 && c2.rank == 7 && c1.palo == c2.palo && !this.cantes.get(c1.getPaloInt())) {
                    this.puntos.set(jugador, this.puntos.get(jugador) + 20 + 20 * ((c1.palo == this.triunfo.palo) ? 1 : 0));
                    this.cantes.set(c1.getPaloInt(), true);
                }
            }
        }

        // Las diez últimas
        if (this.manos.get(0).size() == 0 && this.manos.get(1).size() == 0) {
            this.puntos.set(jugador, this.puntos.get(jugador) + 10);
        }

        // Si tiene el 7 de triunfo lo cambia
        if (this.manos.get(jugador).contains(new CartaIA(4, this.triunfo.palo))) {
            this.manos.get(jugador).remove(new CartaIA(4, this.triunfo.palo));
            this.manos.get(jugador).add(this.triunfo);
            this.triunfo = new CartaIA(4, this.triunfo.palo);
        }

        // Si se ha acabado la partida de ida sin ganador se reparten las vueltas
        if (this.manos.get(0).size() == 0 && this.puntos.get(0) < 101 && this.puntos.get(1) < 101) {
            jugador = 1 - jugador;
            this.vueltas = true;
            this.cantes = new ArrayList<Boolean>(Collections.nCopies(4, false));
            this.barajar();
        }

        this.turno = jugador;


    }


    public ArrayList<CartaIA> obtenerMovimientos() {
        if (this.vueltas && (this.puntos.get(0)>=101 || this.puntos.get(1)>=101)) {
            // Alguien ha ganado la partida de vueltas
            return new ArrayList<>();
        } else if (this.restantes.size()>0 || this.cartaTirada == null) {
            // Puede tirar cualquier carta
            return this.manos.get(this.turno);
        } else {
            // Es el arrastre

            // Cartas del palo
            ArrayList<CartaIA> cartasPalo = this.manos.get(this.turno).stream().filter(c -> c.palo == this.cartaTirada.palo).collect(Collectors.toCollection(ArrayList::new));
            // Cartas triunfo
            ArrayList<CartaIA> cartasTriunfo = this.manos.get(this.turno).stream().filter(c -> c.palo == this.triunfo.palo).collect(Collectors.toCollection(ArrayList::new));

            if (cartasPalo.size()>0) {
                // Si tiene cartas del palo tiene que matar si puede
                ArrayList<CartaIA> cartasPaloMatan = cartasPalo.stream().filter(c -> c.rank > this.cartaTirada.rank).collect(Collectors.toCollection(ArrayList::new));
                return cartasPaloMatan.size() > 0 ? cartasPaloMatan : cartasPalo;
            } else if (cartasTriunfo.size()>0) {
                // Si tiene triunfo debe tirar
                return cartasTriunfo;
            } else {
                // Si no tiene nada que tire lo que tenga
                return this.manos.get(this.turno);
            }
        }
    }

    public int obtenerResultado(int jugador) {
        return this.puntos.get(jugador) >= 101 ? 1 : 0;
    }

    /*
     * Baraja como al principio de una partida
     */
    private void barajar() {
        this.restantes = new ArrayList<>();
        char palos[] =  {'B','C','E','O'};
        // Se crea la baraja entera
        for (char p : palos) {
            for (int i=0; i<10; i++) {
                this.restantes.add(new CartaIA(i,p));
            }
        }

        // Se baraja
        Collections.shuffle(this.restantes);

        // Se reparten las cartas
        for (int i=0; i<6; i++) {
            this.manos.get(0).add(this.restantes.get(0));
            this.restantes.remove(0);
            this.manos.get(1).add(this.restantes.get(0));
            this.restantes.remove(0);
        }

        // Se saca el triunfo
        this.triunfo = this.restantes.get(0);
        this.restantes.remove(0);
    }

    public int getTurno() {
        return turno;
    }

    @Override
    public String toString() {
        String result = "";
        result += "Puntos 1: " + this.puntos.get(0) + "\n";
        result += "Puntos 2: " + this.puntos.get(1) + "\n";
        result += "Cartas 1: ";
        for (CartaIA c : this.manos.get(0)) {
            result += c + ",";
        }
        result += "\nCartas 2: ";
        for (CartaIA c : this.manos.get(1)) {
            result += c + ",";
        }
        result += "\nTriunfo: " + this.triunfo + "\n";
        result += "Cantadas: ";
        char palos[] =  {'B','C','E','O'};
        for (int i=0; i<this.cantes.size(); i++) {
            if (this.cantes.get(i)) {
                result += palos[i];
            }
        }
        result += "\nCartas Restantes: ";
        for (CartaIA c : this.restantes) {
            result += c + ",";
        }
        result += "\nVueltas: " + (this.vueltas? "Sí":"No");
        result += "\nTurno: " + this.turno + "\n";

        return result;
    }
}



