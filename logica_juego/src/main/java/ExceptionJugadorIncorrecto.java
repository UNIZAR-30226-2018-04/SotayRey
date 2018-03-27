package main.java;

/**
 * @Autores: Crisan, Marius Sorin; Ignacio Bitrian; Victor Soria
 * @Fecha: 18-03-18
 * @Fichero: Excepción que indica que el jugador especificado es incorrecto
 */
public class ExceptionJugadorIncorrecto extends Exception{

    public ExceptionJugadorIncorrecto() { super(); }
    public ExceptionJugadorIncorrecto(String message) { super(message); }
    public ExceptionJugadorIncorrecto(String message, Throwable cause) { super(message, cause); }
    public ExceptionJugadorIncorrecto(Throwable cause) { super(cause); }
}
