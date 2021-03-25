package serveur.modele.implementation.connexion.joueur;

import serveur.modele.gestionnaire.GestionnaireSession;

import java.io.Serializable;

public class JoueurProxy implements Serializable {

    private final String pseudo;
    private boolean enLigne;

    public JoueurProxy(Joueur joueurReel) {
        this.pseudo = joueurReel.getPseudo();
        try {
            GestionnaireSession.getInstance().getSessionFromPseudo(pseudo);
            this.enLigne = true;
        } catch (Exception e) {
            this.enLigne = false;
        }
    }

    public String getPseudo() {
        return pseudo;
    }
    public boolean isEnLigne() {
        return enLigne;
    }
}
