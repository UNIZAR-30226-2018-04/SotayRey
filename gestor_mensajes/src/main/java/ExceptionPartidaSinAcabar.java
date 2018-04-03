package main.java;

/**
 * @Autores: Crisan, Marius Sorin; Ignacio Bitrian; Victor Soria
 * @Fecha: 18-03-18
 * @Fichero: Excepción provocada cuando una partida no ha finalizado.
 */


public class ExceptionPartidaSinAcabar extends Exception {

    public ExceptionPartidaSinAcabar() { super(); }
    public ExceptionPartidaSinAcabar(String message) { super(message); }
    public ExceptionPartidaSinAcabar(String message, Throwable cause) {
        super(message, cause); }
    public ExceptionPartidaSinAcabar(Throwable cause) { super(cause); }

}