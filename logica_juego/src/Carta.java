/**
 * @Autores: Crisan, Marius Sorin; Ignacio Bitrian; Victor Soria
 * @Fecha: 11-03-18
 * @Fichero: Clase carta de la lógica del guiñote
 */


/**
 * Los valores de una carta pueden ser [1..7, 10..12]y los palos [C,O,B,E]
 * siendo cada letra la inicial de cada uno de los palos de la baraja española
 */
public class Carta {
    private int valor;
    private String palo;


    /**
     * Constructor que crea una carta de 1 de espadas.
     */
    public Carta(){
        valor = 1;
        palo = "E";
    }


    /**
     * Constructor que realiza una copia exacta de "carta".
     * @param carta
     */
    public Carta(Carta carta) {
        this.palo = new String(carta.palo);
        this.valor = carta.valor;
    }


    /**
     * Constructor que crea una carta con "valor" y "palo" siendo palo una
     * referencia a cada uno de los palos de la baraja española:
     * 1 --> bastos, 2 --> copas, 3 --> espadas, 4 --> oros
     * 0 < Valor < 8, 9 < Valor <= 12
     * @param valor
     * @param palo
     * @throws ExceptionCartaIncorrecta
     */
    public Carta(int valor, int palo) throws ExceptionCartaIncorrecta{
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
            case 4:
                this.palo = "O";
                break;
            default:
                throw new ExceptionCartaIncorrecta("Palo incorrecto: " + palo);
        }
    }


    /**
     * Constructor que crea una carta con "valor" y "palo" siendo palo la
     * inicial de uno de los palos de la baraja española
     * @param valor
     * @param palo
     * @throws ExceptionCartaIncorrecta
     */
    public Carta(int valor, String palo) throws ExceptionCartaIncorrecta {
        if ((valor>0 && valor<8) || (valor >= 10 && valor <= 12)){
            this.valor = valor;
        } else throw new ExceptionCartaIncorrecta("Valor incorrecto: " + valor);

        if (palo == "C" || palo == "B" || palo == "O"|| palo == "E"){
            this.palo = palo;
        } else throw new ExceptionCartaIncorrecta("Palo incorrecto: " + palo);
    }


    /**
     * Devuelve el valor de una carta
     * @return
     */
    public int getValor() {
        return valor;
    }

    /**
     * Asigna a la carta "valor" si y solo si es correcto
     * @param valor
     * @throws ExceptionCartaIncorrecta
     */
    public void setValor(int valor) throws ExceptionCartaIncorrecta {
        if ((valor>0 && valor<8) || (valor >= 10 && valor <= 12)){
            this.valor = valor;
        } else throw new ExceptionCartaIncorrecta("Valor incorrecto: " + valor);
    }


    /**
     * Devuelve la incial del palo de la carta, según la baraja española
     * @return
     */
    public String getPalo() {
        String aux = new String(palo);
        return aux;
    }

    /**
     * Se le asigna al palo de la baraja el valor de "palo" si y solo si
     * "palo" es una inicial de los posibles palos de la baraja española.
     * @param palo
     * @throws ExceptionCartaIncorrecta
     */
    public void setPalo(String palo) throws ExceptionCartaIncorrecta {
        if (palo == "C" || palo == "B" || palo == "O"|| palo == "E"){
            this.palo = palo;
        } else throw new ExceptionCartaIncorrecta("Palo incorrecto: " + palo);
    }

    /**
     * Pre: ---
     * Post: Devuelve la puntuación de la carta
     * @return
     */
    //TODO: que pruebas hacer
    public int getPuntuación(){
        switch (this.valor){
            case 1:
                return 11;
            case 3:
                return 10;
            case 12:
                return 4;
            case 10:
                return 3;
            case 11:
                return 2;
            default:
                return 0;
        }
    }




    /**
     * Devuelve un cadena con el valor y palo de la carta: "[ <valor>, <palo> ]"
     * @return
     */
    public String toString(){
        return "[" + valor + "," + palo + "]";
    }


    @Override
    public boolean equals(Object o) {
        // self check
         if (this == o)
            return true;
        // null check
        if (o == null)
            return false;
        // type check and cast
        if (getClass() != o.getClass())
            return false;
        Carta carta = (Carta) o;
        // field comparison
        return (valor == carta.valor)
                && palo.equals(carta.palo);
    }

    /**
     * Devuelve true si y solo las cartas son del mismo palo
     * @param otra
     * @return
     */
    public boolean esMismoPalo(Carta otra){
        return otra.getPalo().equals(palo);
    }

    /**
     * Devuelve true si y solo la carta "otra" aporta más puntuación que la
     * actual
     * @param otra
     * @return
     */
    public boolean masPuntuacion(Carta otra){
        return otra.getPuntuación() > getPuntuación();
    }

    /*
    public boolean mata(Carta otra, Carta triunfo){
        // Son del mismo palo
        if (palo.equals(otra.getPalo())){
            return getPuntuación()> otra.getPuntuación();
        }
        else{// No son del mismo palo
            // La carta es triunfo
            if (palo.equals(triunfo.getPalo())){

            }

        }

        return otra.getPalo().equals(triunfo.getPalo()) && mia.getPuntuación
                () > otra.getPuntuación() && mia.getPalo().equals(triunfo
                .getPalo());


    }
    */
}
