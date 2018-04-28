package basedatos.modelo;

import java.math.BigInteger;
import java.util.ArrayList;

public class FaseVO {
    private ArrayList<PartidaVO> parejas;
    private ArrayList<UsuarioVO> participantes;
    private int premioPuntos;
    private int premioDivisa;
    private BigInteger torneoId;
    private int num; // 1 para final 2 semifinal...

    public FaseVO(BigInteger torneoId, int num, int premioPuntos, int premioDivisa) {
        this.premioDivisa = premioDivisa;
        this.premioPuntos = premioPuntos;
        this.num = num;
        this.torneoId = torneoId;
        this.parejas = new ArrayList<>();
        this.participantes = new ArrayList<>();
    }

    public FaseVO(BigInteger torneoId, int num) {
        this.torneoId = torneoId;
        this.num = num;
    }

    public BigInteger getTorneoId() {

        return torneoId;
    }

    public void setTorneoId(BigInteger torneoId) {
        this.torneoId = torneoId;
    }

    public ArrayList<PartidaVO> getParejas() {
        return parejas;
    }

    public void setParejas(ArrayList<PartidaVO> parejas) {
        this.parejas = parejas;
    }

    public ArrayList<UsuarioVO> getParticipantes() {
        return participantes;
    }

    public void setParticipantes(ArrayList<UsuarioVO> participantes) {
        this.participantes = participantes;
    }

    public int getPremioPuntos() {
        return premioPuntos;
    }

    public void setPremioPuntos(int premioPuntos) {
        this.premioPuntos = premioPuntos;
    }

    public int getPremioDivisa() {
        return premioDivisa;
    }

    public void setPremioDivisa(int premioDivisa) {
        this.premioDivisa = premioDivisa;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }
}
