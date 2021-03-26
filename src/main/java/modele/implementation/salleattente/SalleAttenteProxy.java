package modele.implementation.salleattente;

import java.io.Serializable;

/*
 * Classe permettant d'obtenir les informations sur une salle
 * sans pourtant avoir acces a une salle d'attente reel.
 */
public class SalleAttenteProxy implements Serializable {

    private final String nomSalle;
    private final String nomJeu;
    private final int nbJoueursConnecte;
    private final int nbJoueursMax;
    private final boolean besoinMdp;
    private final boolean publique;

    public SalleAttenteProxy(String nomSalle, String nomJeu, int nbJoueurConnecte,
                             int nbJoueursMax, boolean besoinMdp, boolean publique) {
       this.nomSalle = nomSalle;
       this.nomJeu = nomJeu;
       this.nbJoueursConnecte = nbJoueurConnecte;
       this.nbJoueursMax = nbJoueursMax;
       this.besoinMdp = besoinMdp;
       this.publique = publique;
    }

    public String getNomSalle() {
        return nomSalle;
    }

    public String getNomJeu() {
        return nomJeu;
    }

    public int getNbJoueursConnecte() {
        return nbJoueursConnecte;
    }

    public int getNbJoueursMax() {
        return nbJoueursMax;
    }

    public boolean isBesoinMdp() {
        return besoinMdp;
    }

    public boolean isPublique() {
        return publique;
    }
}
