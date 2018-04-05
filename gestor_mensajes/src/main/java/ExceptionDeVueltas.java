package main.java;

/**
 * @Autores: Crisan, Marius Sorin; Ignacio Bitrian; Victor Soria
 * @Fecha: 5-04-18
 * @Fichero: Excepción que indica que el juego está de vueltas
 */
public class ExceptionDeVueltas extends Exception{

    private EstadoPartida estado;

    public ExceptionDeVueltas(EstadoPartida estado) {
        this.estado = estado;
    }

    public EstadoPartida getEstado() {
        return estado;
    }

    public ExceptionDeVueltas(String message) { super(message); }
    public ExceptionDeVueltas(String message, Throwable cause) { super(message, cause); }
    public ExceptionDeVueltas(Throwable cause) { super(cause); }
}
