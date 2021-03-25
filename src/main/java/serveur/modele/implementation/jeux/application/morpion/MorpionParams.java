package serveur.modele.implementation.jeux.application.morpion;

import serveur.modele.implementation.jeux.application.Parametres;

import java.util.HashMap;

public class MorpionParams extends Parametres {

    /*
     * Parametres personnalisable du morpion
     */
    private final int TAILLE_MIN_TABLEAU = 3;
    private final int TAILLE_MAX_TABLEAU = 5;
    private int tailleTableau = 3;

    public MorpionParams() {
        super(2,2);
    }



    @Override
    public void changerParametre(String nomParametre, Object valeur) throws IllegalArgumentException, ClassCastException {
        switch(nomParametre) {
            case "taille_tableau" : setTailleTableau((int) valeur); break;
            default : throw new IllegalArgumentException("Ce parametre n'existe pas.");
        }
    }

    @Override
    public HashMap<String, Object> getMapParametres() {
        HashMap<String, Object> mapParametres = new HashMap<String, Object>();
        mapParametres.put("taille_tableau", this.tailleTableau);
        return mapParametres;
    }

    public int getTailleTableau() {
        return tailleTableau;
    }

    private void setTailleTableau(int tailleTableau) throws IllegalArgumentException {
        if (tailleTableau < TAILLE_MIN_TABLEAU || TAILLE_MAX_TABLEAU < tailleTableau)
            throw new IllegalArgumentException("Cette taille de morpion est invalide");
        this.tailleTableau = tailleTableau;
    }
}
