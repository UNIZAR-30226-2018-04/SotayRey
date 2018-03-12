/*
 * Autor: Crisan, Marius Sorin
 * Fecha: 11-03-18
 * Fichero: Ficheroq que contiene todas las excepciones personalizadas del guiñote
 */

public class ExceptionCartaIncorrecta extends Exception{

    public ExceptionCartaIncorrecta() { super(); }
    public ExceptionCartaIncorrecta(String message) { super(message); }
    public ExceptionCartaIncorrecta(String message, Throwable cause) { super(message, cause); }
    public ExceptionCartaIncorrecta(Throwable cause) { super(cause); }

}
