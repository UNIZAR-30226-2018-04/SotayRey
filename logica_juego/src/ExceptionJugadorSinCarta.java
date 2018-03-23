/**
 * @Autores: Crisan, Marius Sorin; Ignacio Bitrian; Victor Soria
 * @Fecha: 18-03-18
 * @Fichero: Excepción provocada porque un jugador no tiene más cartas
 */


public class ExceptionJugadorSinCarta extends Exception {

    public ExceptionJugadorSinCarta() { super(); }
    public ExceptionJugadorSinCarta(String message) { super(message); }
    public ExceptionJugadorSinCarta(String message, Throwable cause) { super(message, cause); }
    public ExceptionJugadorSinCarta(Throwable cause) { super(cause); }
}
