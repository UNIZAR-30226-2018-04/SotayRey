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
            case 3:
                this.rank = 8;
            case 12:
                this.rank = 7;
            case 10:
                this.rank = 6;
            case 11:
                this.rank = 5;
            case 7:
                this.rank = 4;
            case 6:
                this.rank = 3;
            case 5:
                this.rank = 2;
            case 4:
                this.rank = 1;
            case 2:
                this.rank = 0;
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
