package main.java;

/**
 * @Autores: Crisan, Marius Sorin; Ignacio Bitrian; Victor Soria
 * @Fecha: 26-03-18
 * @Fichero: Excepción que indica que el jugador especificado es incorrecto
 */
public class ExceptionNoPuedesCantar extends Exception{

    public ExceptionNoPuedesCantar() { super(); }
    public ExceptionNoPuedesCantar(String message) { super(message); }
    public ExceptionNoPuedesCantar(String message, Throwable cause) { super(message, cause); }
    public ExceptionNoPuedesCantar(Throwable cause) { super(cause); }
}
