import java.beans.PropertyVetoException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Main {
    public static void main(String[] args) throws PropertyVetoException, SQLException, IOException {
        Date timeInicio = new Date();
        boolean publica = true;
        List<UsuarioVO> usuarios = new ArrayList<>();

        InterfazDatos.instancia().crearNuevaPartida(new PartidaVO(timeInicio,publica,usuarios));
    }
}