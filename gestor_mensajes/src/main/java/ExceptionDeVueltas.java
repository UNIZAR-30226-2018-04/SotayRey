package main.java;

/**
 * @Autores: Crisan, Marius Sorin; Ignacio Bitrian; Victor Soria
 * @Fecha: 5-04-18
 * @Fichero: Excepción que indica que el juego está de vueltas
 */
public class ExceptionDeVueltas extends Exception{

    private EstadoPartida estado;

    public ExceptionDeVueltas(EstadoPartida estado) {
        super();
        this.estado = estado;
    }

    public EstadoPartida getEstado() {
        return estado;
    }

    public ExceptionDeVueltas(EstadoPartida estado, String message) {
        super (message);
        this.estado = estado;
    }
    public ExceptionDeVueltas(EstadoPartida estado, String message, Throwable
            cause) {
        super(message, cause);
        this.estado = estado;
    }
    public ExceptionDeVueltas(EstadoPartida estado, Throwable cause) {
        super(cause);
        this.estado = estado;
    }
}
