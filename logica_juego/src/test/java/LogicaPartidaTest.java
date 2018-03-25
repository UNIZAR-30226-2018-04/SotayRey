package test.java;

/**
 * @Autores: Crisan, Marius Sorin; Ignacio Bitrian; Victor Soria
 * @Fecha: 24-03-18
 * @Fichero: Fichero de pruebas con JUnit de la clase LogicaPartida
 */

import main.java.*;

import org.junit.FixMethodOrder;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
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
}
