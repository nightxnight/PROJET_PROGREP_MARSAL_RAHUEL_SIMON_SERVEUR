package utils;

import java.util.ArrayList;

public class ListeOrdonnee<T> {

    ArrayList<T> liste;
    private int idx;

    public ListeOrdonnee() {
        this.liste = new ArrayList<T>();
        this.idx = 0;
    }

    public ListeOrdonnee(ArrayList<T> liste) {
        this.liste = liste;
        this.idx = 0;
    }

    public T premier() {
        idx = 0;
        return elementCourant();
    }

    public T dernier() {
        idx = (liste.size() - 1);
        return elementCourant();
    }

    public T suivant() {
        idx = (idx + 1) % liste.size();
        return elementCourant();
    }

    public T precedent() {
        idx = ((idx -= 1) == -1) ? (liste.size() - 1) : idx;
        return elementCourant();
    }

    public T elementCourant() {
        return liste.get(idx);
    }

    public ArrayList<T> getListe() {
        return liste;
    }

    public void setListe(ArrayList<T> liste) {
        this.liste = liste;
    }
}
