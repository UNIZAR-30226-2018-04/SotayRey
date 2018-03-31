/*
 * Autor: Guerrero Viu, Julia
 * Fecha: 27-03-2018
 * Fichero: Excepciones de acceso a la base de datos
 */
package basedatos.exceptions;

public class ExceptionCampoInexistente extends Exception{

    public ExceptionCampoInexistente() { super(); }
    public ExceptionCampoInexistente(String message) { super(message); }
    public ExceptionCampoInexistente(String message, Throwable cause) { super(message, cause); }
    public ExceptionCampoInexistente(Throwable cause) { super(cause); }

}
