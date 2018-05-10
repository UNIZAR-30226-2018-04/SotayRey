package main.java;

public class Cante {
    public enum TipoCante{
        LAS20, LAS40;
    }


    private TipoCante tipo;
    private String palo;

    public Cante(TipoCante tipo, String palo){
        this.tipo = tipo;
        this.palo = palo;
    }

    public void setTipo(TipoCante tipo){
        this.tipo = tipo;
    }

    public void setPalo(String palo){
        this.palo = palo;
    }

    public TipoCante getTipo() {
        return tipo;
    }

    public String getPalo() {
        return palo;
    }

}
