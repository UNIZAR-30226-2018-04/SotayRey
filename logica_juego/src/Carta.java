/*
 * Autor: Crisan, Marius Sorin
 * Fecha: 11-03-18
 * Fichero: Clase carta de la lógica del guiñote
 */

/*
 * Los valores de una carta pueden ser [1..7, 10..12]y los palos [C,O,B,E] siendo cada letra la inicial de cada uno de
 * los palos de la baraja española
 */

public class Carta {
    private int valor;
    private String palo;


    public Carta(){
        valor = 1;
        palo = "E";
    }

    public Carta(int valor, int palo) throws Exception{
        if ((valor>0 && valor<8) || (valor >= 10 && valor <= 12)){
            this.valor = valor;
        } else throw new ExceptionCartaIncorrecta("Valor incorrecto: " + valor);
        switch (palo){
            case 1:
                this.palo = "B";
                break;
            case 2:
                this.palo = "C";
                break;
            case 3:
                this.palo = "E";
                break;
            default:
                this.palo = "O";
        }
    }

    public Carta(int valor, String palo) throws Exception {
        if ((valor>0 && valor<8) || (valor >= 10 && valor <= 12)){
            this.valor = valor;
        } else throw new ExceptionCartaIncorrecta("Valor incorrecto: " + valor);

        if (palo == "C" || palo == "B" || palo == "O"|| palo == "E"){
            this.palo = palo;
        } else throw new ExceptionCartaIncorrecta("Palo incorrecto: " + palo);
    }


    public int getValor() {
        return valor;
    }

    public void setValor(int valor) throws ExceptionCartaIncorrecta {
        if ((valor>0 && valor<8) || (valor >= 10 && valor <= 12)){
            this.valor = valor;
        } else throw new ExceptionCartaIncorrecta("Valor incorrecto: " + valor);
    }

    public String getPalo() {
        return palo;
    }

    public void setPalo(String palo) throws ExceptionCartaIncorrecta {
        if (palo == "C" || palo == "B" || palo == "O"|| palo == "E"){
            this.palo = palo;
        } else throw new ExceptionCartaIncorrecta("Palo incorrecto: " + palo);
    }

    public String toString(){
        return "[" + valor + "," + palo + "]";
    }
}
