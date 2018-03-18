/**
 * @Autores: Crisan, Marius Sorin; Ignacio Bitrian; Victor Soria
 * @Fecha: 11-03-18
 * @Fichero: Excepción provocada al no haber terminado una ronda
 */

public class ExceptionRondaNoAcabada extends Exception{

    public ExceptionRondaNoAcabada() { super(); }
    public ExceptionRondaNoAcabada(String message) { super(message); }
    public ExceptionRondaNoAcabada(String message, Throwable cause) { super(message, cause); }
    public ExceptionRondaNoAcabada(Throwable cause) { super(cause); }
}
