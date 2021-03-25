package serveur.modele.implementation.jeux.application;

import serveur.modele.implementation.jeux.application.allumettes.AllumettesParams;
import serveur.modele.implementation.jeux.application.morpion.MorpionParams;
import serveur.modele.implementation.jeux.application.pendu.PenduParams;

import java.util.HashMap;

public abstract class Parametres {

    protected final int JOUEUR_MIN;
    protected final int JOUEUR_MAX;

    public Parametres(int joueur_min, int joueur_max) {
        this.JOUEUR_MIN = joueur_min;
        this.JOUEUR_MAX = joueur_max;
    }

    public static Parametres getParametres(JeuxEnum jeu) {
        switch(jeu) {
            case MORPION: return new MorpionParams();
            case PENDU: return new PenduParams();
            case ALLUMETTES: return new AllumettesParams();
            default: throw new IllegalArgumentException("should never occurs.");
        }
    }

    public abstract void changerParametre(String nomParametre, Object valeur) throws IllegalArgumentException, ClassCastException;
    public abstract HashMap<String, Object> getMapParametres();

    public int getJOUEUR_MIN() {
        return JOUEUR_MIN;
    }

    public int getJOUEUR_MAX() {
        return JOUEUR_MAX;
    }
}
