/**
 * @Autores: Crisan, Marius Sorin; Ignacio Bitrian; Victor Soria
 * @Fecha: 22-03-18
 * @Fichero: Excepción que indica que faltan jugadores por añadir a la
 * partida
 */
public class ExceptionEquipoIncompleto extends Exception{

    public ExceptionEquipoIncompleto() { super(); }
    public ExceptionEquipoIncompleto(String message) { super(message); }
    public ExceptionEquipoIncompleto(String message, Throwable cause) { super(message, cause); }
    public ExceptionEquipoIncompleto(Throwable cause) { super(cause); }
}
