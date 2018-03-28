package test.java;

/**
 * @Autores: Crisan, Marius Sorin; Ignacio Bitrian; Victor Soria
 * @Fecha: 24-03-18
 * @Fichero: Fichero de pruebas con JUnit de la clase LogicaPartida
 */

import main.java.*;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.*;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class LogicaPartidaTest {

    /**
     * Prueba que asigna primera 6 cartas a cada jugador y que el triunfo es
     * la siguiente carta
     */
    @Test
    public void prueba1CrearPartida(){
        try {
            ArrayList<String> jugadores = new ArrayList<>(Arrays.asList("j1",
                    "j2", "j3", "j4"));
            LogicaPartida logica = new LogicaPartida(jugadores);
            EstadoPartida estado = logica.getEstado();
            ArrayList<Carta> cartas = estado.getMazo();
            EstadoPartida resultado = logica.crearPartida();

            for (int i = 0; i < 6; i++) {
                for (String j: jugadores) {
                    Carta c = cartas.remove(0);
                    assertTrue("Asignada mal carta: " + c + " a jugador: "
                            + jugadores, c.equals(resultado.getCartasEnMano(j)
                            .get(i)));
                    System.out.println("Superado... carta: " + c + " asignada" +
                            " a: " + j);
                }
            }
            assertTrue("Triunfo diferente", resultado.getTriunfo().equals
                    (cartas.remove(0)));
            System.out.println("Superado... triunfo asigndo correctamente");
            System.out.println("Superado... crearPartida");
        } catch (Exception e){
            fail();
        }
    }


    /**
     * Prueba que hace cantar a un jugador
     */
    @Test
    public void prueba2cantar(){
        try {
            ArrayList<String> jugadores = new ArrayList<>(Arrays.asList("j1",
                    "j2", "j3", "j4"));
            LogicaPartida logica = new LogicaPartida(jugadores);
            EstadoPartida estado = logica.getEstado();

            Carta c = new Carta(7, "E");
            estado.setTriunfo(c);

            Carta c1 = new Carta(11, "E");
            Carta c2 = new Carta(12, "E");
            Carta c3 = new Carta(11, "O");
            Carta c4 = new Carta(12, "O");
            Carta c5 = new Carta(7, "O");
            Carta c6 = new Carta(2, "O");

            estado.anyadirCartaJugador("j1",c1);
            estado.anyadirCartaJugador("j1",c2);
            estado.anyadirCartaJugador("j1",c3);
            estado.anyadirCartaJugador("j1",c4);
            estado.anyadirCartaJugador("j1",c5);
            estado.anyadirCartaJugador("j1",c6);

            logica.cantar("j1");

        } catch (ExceptionNoPuedesCantar e){
            System.out.println("Superado... no hay nada que cantar");
        } catch (ExceptionCartaIncorrecta | ExceptionCartaYaExiste |
                ExceptionNumeroMaximoCartas | ExceptionJugadorIncorrecto  |
                ExceptionRondaNoAcabada | ExceptionEquipoIncompleto e1) {
            fail("Excepción incorrecta");
        }
        try {
            ArrayList<String> jugadores = new ArrayList<>(Arrays.asList("j1",
                    "j2", "j3", "j4"));
            LogicaPartida logica = new LogicaPartida(jugadores);
            EstadoPartida estado = logica.getEstado();

            Carta c = new Carta(7, "E");
            estado.setTriunfo(c);

            Carta c1 = new Carta(10, "E");
            Carta c2 = new Carta(12, "E");
            Carta c3 = new Carta(10, "O");
            Carta c4 = new Carta(12, "O");
            Carta c5 = new Carta(7, "O");
            Carta c6 = new Carta(2, "O");

            estado.anyadirCartaJugador("j1",c1);
            estado.anyadirCartaJugador("j1",c2);
            estado.anyadirCartaJugador("j1",c3);
            estado.anyadirCartaJugador("j1",c4);
            estado.anyadirCartaJugador("j1",c5);
            estado.anyadirCartaJugador("j1",c6);

            logica.cantar("j1");
        }
        catch (Exception e){
            ;
        }
    }
}
