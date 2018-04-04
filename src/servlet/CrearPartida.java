package servlet;

import basedatos.InterfazDatos;
import basedatos.modelo.PartidaVO;
import basedatos.modelo.UsuarioVO;

import java.util.ArrayList;

public class CrearPartida {
    private static CrearPartida ourInstance = new CrearPartida();
    private static ArrayList<UsuarioVO> jugadores = null;
    private static int numJugadores;

    public static CrearPartida getInstance() {
        return ourInstance;
    }

    private CrearPartida() {
        jugadores = new ArrayList<>();
        numJugadores = 0;
    }

    public void addJugador(UsuarioVO jugador){
        if (numJugadores < 4){
            jugadores.add(jugador);
            numJugadores++;
        }

    }

    public void addJugador(ArrayList<UsuarioVO> jugadores){
        if(numJugadores + jugadores.size() < 4){
            for(UsuarioVO jugador : jugadores){
                jugadores.add(jugador);
                numJugadores++;
            }
        }
    }

    public boolean lanzaPartida(){
        if(numJugadores == 2 || numJugadores == 4 ){
            PartidaVO nuevaPartida = new PartidaVO(true,jugadores);
            InterfazDatos facade = null;
            try{
                facade = InterfazDatos.instancia();
            }catch(Exception e){
                System.out.println(e.toString());
                return false;
            }

            try{
                facade.crearNuevaPartida(nuevaPartida);
            } catch(Exception e){
                System.out.println(e.toString());
                return false;
            }
            return true;
        }
        else{
            return false;
        }
    }
}
