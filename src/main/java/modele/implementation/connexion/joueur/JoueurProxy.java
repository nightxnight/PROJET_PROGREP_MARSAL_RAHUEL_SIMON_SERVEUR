package modele.implementation.connexion.joueur;

import modele.gestionnaire.GestionnaireSession;

import java.io.Serializable;

/*
 * Classe proxy joueur permettant d'obtenir des informations a partir d'un vrai
 * joueur tout en limitant l'acces aux methodes et attributs du joueur reel
 */
public class JoueurProxy implements Serializable {

    private final String pseudo;
    private boolean enLigne;

    public JoueurProxy(String pseudo, boolean enLigne) {
        this.pseudo = pseudo;
        this.enLigne = enLigne;
    }

    public String getPseudo() {
        return pseudo;
    }
    public boolean isEnLigne() {
        return enLigne;
    }
}
