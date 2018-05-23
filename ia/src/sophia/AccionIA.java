package sophia;

import main.java.Carta;

import java.util.ArrayList;

public class AccionIA {
    public Carta carta;
    // {Bastos,Copas,Espadas,Oros}
    public ArrayList<Boolean> cantes;
    public boolean cambiaSiete;

    public AccionIA() {
        this.carta = null;
        this.cantes = new ArrayList<>(4);
        for (int i=0; i<4; i++) {
            this.cantes.add(false);
        }
        this.cambiaSiete = false;
    }
}
