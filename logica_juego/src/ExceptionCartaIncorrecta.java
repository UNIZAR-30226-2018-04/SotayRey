/**
 * @Autores: Crisan, Marius Sorin; Ignacio Bitrian; Victor Soria
 * @Fecha: 11-03-18
 * @Fichero: Excepción provocada porque el valor o palo de una Carta es incorrecto
 */

public class ExceptionCartaIncorrecta extends Exception{

    public ExceptionCartaIncorrecta() { super(); }
    public ExceptionCartaIncorrecta(String message) { super(message); }
    public ExceptionCartaIncorrecta(String message, Throwable cause) { super(message, cause); }
    public ExceptionCartaIncorrecta(Throwable cause) { super(cause); }
}
