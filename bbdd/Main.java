import java.beans.PropertyVetoException;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws ExceptionCampoInvalido, PropertyVetoException, SQLException, IOException {
        Timestamp timeInicio = new Timestamp(new java.util.Date().getTime());
        boolean publica = true;
        List<UsuarioVO> usuarios = new ArrayList<>();
        UsuarioVO julia = new UsuarioVO("juliagviu", "prueba@unizar.es", "Julia", "Guerrero", true);
        UsuarioVO sergio = new UsuarioVO("serizba", "serizba@unizar.es", "Sergio", "Izquierdo", true);
        UsuarioVO manolo = new UsuarioVO("manolo", "manolo@unizar.es", "Julia", "Guerrero", true);
        UsuarioVO julian = new UsuarioVO("eljuli", "juli@unizar.es", "Sergio", "Izquierdo", true);
        InterfazDatos.instancia().crearUsuario(julia);
        InterfazDatos.instancia().crearUsuario(sergio);
        InterfazDatos.instancia().crearUsuario(manolo);
        InterfazDatos.instancia().crearUsuario(julian);
        usuarios.add(julia);
        usuarios.add(sergio);
        usuarios.add(manolo);
        usuarios.add(julian);
        PartidaVO p = new PartidaVO(timeInicio,publica,usuarios);
        InterfazDatos.instancia().crearNuevaPartida(p);

        // darle una paliza a julia
        p.setTimeFin(new Timestamp(new java.util.Date().getTime()));
        p.setGanador('2');
        List<Integer> cuarentas = new ArrayList<>();
        cuarentas.add(0); cuarentas.add(1); cuarentas.add(0); cuarentas.add(0);
        List<Integer> veintes = new ArrayList<>();
        veintes.add(0); veintes.add(0); veintes.add(0); veintes.add(0);
        p.setCuarentas(cuarentas);
        p.setVeintes(veintes);
        p.setPuntos1(1);
        p.setPuntos2(102);
        p.setAbandonador(3);
        //InterfazDatos.instancia().finalizarPartida(p);


        //partida 2
        timeInicio = new Timestamp(new java.util.Date().getTime());
        usuarios = new ArrayList<>();
        usuarios.add(julia);
        usuarios.add(sergio);
        p = new PartidaVO(timeInicio,publica,usuarios);
        InterfazDatos.instancia().crearNuevaPartida(p);

        // darle una paliza a julia
        p.setTimeFin(new Timestamp(new java.util.Date().getTime()));
        p.setGanador('2');
        cuarentas = new ArrayList<>();
        cuarentas.add(0); cuarentas.add(1);
        veintes = new ArrayList<>();
        veintes.add(0); veintes.add(0);
        p.setCuarentas(cuarentas);
        p.setVeintes(veintes);
        p.setPuntos1(1);
        p.setPuntos2(102);
        p.setAbandonador(0);
        //InterfazDatos.instancia().finalizarPartida(p);

        ArrayList<PartidaVO> partis = InterfazDatos.instancia().obtenerPartidasPublicasCurso();
        for (PartidaVO pp : partis) {
            System.out.println("---------"+ pp.getId() + "-------");
            for (UsuarioVO uu : pp.getUsuarios()) {
                System.out.print(uu.getUsername() + " ");
                System.out.println();
            }
        }

    }
}