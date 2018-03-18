/**
 * @Autores: Crisan, Marius Sorin; Ignacio Bitrian; Victor Soria
 * @Fecha: 11-03-18
 * @Fichero: Excepción provocada porque no es el turno del jugador actual
 */

public class ExceptionTurnoIncorrecto extends Exception{

    public ExceptionTurnoIncorrecto() { super(); }
    public ExceptionTurnoIncorrecto(String message) { super(message); }
    public ExceptionTurnoIncorrecto(String message, Throwable cause) { super(message, cause); }
    public ExceptionTurnoIncorrecto(Throwable cause) { super(cause); }
}
