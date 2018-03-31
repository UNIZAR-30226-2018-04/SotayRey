/*
 * Autor: Guerrero Viu, Julia
 * Fecha: 19-03-2018
 * Fichero: Excepciones de acceso a la base de datos
 */

package basedatos.exceptions;

public class ExceptionCampoInvalido extends Exception{

    public ExceptionCampoInvalido() { super(); }
    public ExceptionCampoInvalido(String message) { super(message); }
    public ExceptionCampoInvalido(String message, Throwable cause) { super(message, cause); }
    public ExceptionCampoInvalido(Throwable cause) { super(cause); }

}
