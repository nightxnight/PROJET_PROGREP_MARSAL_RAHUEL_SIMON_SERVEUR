package modele.implementation.connexion.joueur;

import modele.gestionnaire.GestionnaireSession;

import java.io.Serializable;

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
