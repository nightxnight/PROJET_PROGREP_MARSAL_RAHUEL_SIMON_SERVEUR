package utils;

import java.util.Objects;

public class Paire <A, B> {

    private A premier;
    private B second;

    public Paire(A premier, B second) {
        this.premier = premier;
        this.second = second;
    }

    public A getPremier() {
        return premier;
    }

    public void setPremier(A premier) {
        this.premier = premier;
    }

    public B getSecond() {
        return second;
    }

    public void setSecond(B second) {
        this.second = second;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Paire<?, ?> paire = (Paire<?, ?>) o;
        return Objects.equals(getPremier(), paire.getPremier()) &&
                Objects.equals(getSecond(), paire.getSecond());
    }
}
