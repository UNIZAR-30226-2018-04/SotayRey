package sophia;


import main.java.Carta;

import java.util.ArrayList;
import java.util.Objects;
import java.util.stream.Collectors;

public class CartaIA {
    // rank: 9-As 8-3 7-Rey 6-Sota 5-Caballo 4-7 3-6 2-5 1-4 0-2
    public int rank;
    // palo: 'B', 'C', 'E', 'O'
    public char palo;

    public CartaIA(int rank, char palo) {
        this.rank = rank;
        this.palo = palo;
    }

    public CartaIA(CartaIA c) {
        this.rank = c.rank;
        this.palo = c.palo;
    }

    public CartaIA(Carta carta) {
        switch (carta.getValor()) {
            case 1:
                this.rank = 9;
                break;
            case 3:
                this.rank = 8;
                break;
            case 12:
                this.rank = 7;
                break;
            case 10:
                this.rank = 6;
                break;
            case 11:
                this.rank = 5;
                break;
            case 7:
                this.rank = 4;
                break;
            case 6:
                this.rank = 3;
                break;
            case 5:
                this.rank = 2;
                break;
            case 4:
                this.rank = 1;
                break;
            case 2:
                this.rank = 0;
                break;
        }
        this.palo = carta.getPalo().charAt(0);
    }

    public int getPaloInt() {
        switch (this.palo) {
            case 'B':
                return 0;
            case 'C':
                return 1;
            case 'E':
                return 2;
            case 'O':
                return 3;
            default:
                return -1;
        }
    }

    public int getPuntos() {
        switch (this.rank) {
            case 9:
                return 11;
            case 8:
                return 10;
            case 7:
                return 4;
            case 6:
                return 3;
            case 5:
                return 2;
            default:
                return 0;

        }
    }

    public Carta toCarta() {
        Carta c = new Carta();
        try {
            switch (this.rank) {
                case 9:
                    c.setValor(1);
                    break;
                case 8:
                    c.setValor(3);
                    break;
                case 7:
                    c.setValor(12);
                    break;
                case 6:
                    c.setValor(10);
                    break;
                case 5:
                    c.setValor(11);
                    break;
                case 4:
                    c.setValor(7);
                    break;
                case 3:
                    c.setValor(6);
                    break;
                case 2:
                    c.setValor(5);
                    break;
                case 1:
                    c.setValor(4);
                    break;
                case 0:
                    c.setValor(2);
                    break;
                default:
                    c.setValor(-1);
                    break;
            }
            c.setPalo(String.valueOf(this.palo));
        } catch (Exception e) {}
        return c;
    }

    public static ArrayList<CartaIA> toArray(ArrayList<Carta> cartas) {
        return cartas.stream().map(CartaIA::new).collect(Collectors.toCollection(ArrayList::new));
    }

    // Devuelve cierto si y solo si la carta del tapete (this) mata a c

    public boolean mata(char t, CartaIA c) {
        return (this.palo==c.palo && this.rank>c.rank) || (this.palo!=c.palo && c.palo!=t);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CartaIA cartaIA = (CartaIA) o;
        return rank == cartaIA.rank &&
                palo == cartaIA.palo;
    }

    @Override
    public int hashCode() {

        return Objects.hash(rank, palo);
    }

    @Override
    public String toString() {
        switch (this.rank) {
            case 9:
                return "1" + this.palo;
            case 8:
                return "3" + this.palo;
            case 7:
                return "R" + this.palo;
            case 6:
                return "S" + this.palo;
            case 5:
                return "C" + this.palo;
            case 4:
                return "7" + this.palo;
            case 3:
                return "6" + this.palo;
            case 2:
                return "5" + this.palo;
            case 1:
                return "4" + this.palo;
            case 0:
                return "2" + this.palo;
            default:
                return "CartaErronea";
        }
    }

}
