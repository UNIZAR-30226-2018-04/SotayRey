import java.beans.PropertyVetoException;
import java.io.IOException;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws PropertyVetoException, SQLException, IOException {
        ConexionBD.instancia().crearLiga("Oro","Una liga guay",10,20);
        ConexionBD.instancia().mostrarLigas();
    }
}