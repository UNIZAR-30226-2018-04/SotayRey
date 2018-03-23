/**
 * @Autores: Crisan, Marius Sorin; Ignacio Bitrian; Victor Soria
 * @Fecha: 11-03-18
 * @Fichero: Excepción provocada al no quedar más cartas en el mazo
 */

public class ExceptionMazoVacio extends Exception{

    public ExceptionMazoVacio() { super(); }
    public ExceptionMazoVacio(String message) { super(message); }
    public ExceptionMazoVacio(String message, Throwable cause) { super(message, cause); }
    public ExceptionMazoVacio(Throwable cause) { super(cause); }

}
