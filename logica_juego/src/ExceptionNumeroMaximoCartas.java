/**
 * @Autores: Crisan, Marius Sorin; Ignacio Bitrian; Victor Soria
 * @Fecha: 18-03-18
 * @Fichero: Excepción provocada porque hay demasiadas cartas
 */


public class ExceptionNumeroMaximoCartas extends Exception {

    public ExceptionNumeroMaximoCartas() { super(); }
    public ExceptionNumeroMaximoCartas(String message) { super(message); }
    public ExceptionNumeroMaximoCartas(String message, Throwable cause) { super(message, cause); }
    public ExceptionNumeroMaximoCartas(Throwable cause) { super(cause); }

}
