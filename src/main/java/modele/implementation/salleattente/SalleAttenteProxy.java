package modele.implementation.salleattente;

import java.io.Serializable;
import java.rmi.RemoteException;

public class SalleAttenteProxy implements Serializable {

    private final String nomSalle;
    private final String nomJeu;
    private final int nbJoueursConnecte;
    private final int nbJoueursMax;
    private final boolean besoinMdp;
    private final boolean publique;

    public SalleAttenteProxy(SalleAttente salleAttenteReel) throws RemoteException {
       this.nomSalle = salleAttenteReel.getNomSalle();
       this.nomJeu = salleAttenteReel.getParametres().getJeu().getPremier().getNomJeu();
       this.nbJoueursConnecte = salleAttenteReel.getMapJoueurs().size();
       this.nbJoueursMax = salleAttenteReel.getParametres().getNombreJoueurSalle();
       this.besoinMdp = !salleAttenteReel.getParametres().getMotDePasse().equals("");
       this.publique = salleAttenteReel.getParametres().isPublique();
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
