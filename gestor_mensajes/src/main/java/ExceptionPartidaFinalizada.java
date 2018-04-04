package main.java;

/**
 * @Autores: Crisan, Marius Sorin; Ignacio Bitrian; Victor Soria
 * @Fecha: 3-03-18
 * @Fichero: Excepción provocada cuando una partida ha finalizado y se invoca
 * algún método.
 */


public class ExceptionPartidaFinalizada extends Exception {

    public ExceptionPartidaFinalizada() { super(); }
    public ExceptionPartidaFinalizada(String message) { super(message); }
    public ExceptionPartidaFinalizada(String message, Throwable cause) {
        super(message, cause); }
    public ExceptionPartidaFinalizada(Throwable cause) { super(cause); }

}
