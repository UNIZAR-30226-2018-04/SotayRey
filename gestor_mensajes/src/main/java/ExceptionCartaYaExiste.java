package main.java;

/**
 * @Autores: Crisan, Marius Sorin; Ignacio Bitrian; Victor Soria
 * @Fecha: 18-03-18
 * @Fichero: Excepción que indica que ya existe una carta en un conjunto
 */


public class ExceptionCartaYaExiste extends Exception {

    public ExceptionCartaYaExiste() { super(); }
    public ExceptionCartaYaExiste(String message) { super(message); }
    public ExceptionCartaYaExiste(String message, Throwable cause) { super(message, cause); }

    public ExceptionCartaYaExiste(Throwable cause) { super(cause); }
}
