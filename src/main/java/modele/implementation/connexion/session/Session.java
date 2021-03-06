package modele.implementation.connexion.session;

import modele.client.stub.amis.AmisListenerIF;
import modele.implementation.amis.PortailAmis;
import modele.serveur.stub.amis.PortailAmisIF;
import modele.implementation.connexion.joueur.Joueur;
import modele.gestionnaire.GestionnaireJoueur;
import modele.gestionnaire.GestionnaireSession;
import modele.implementation.salleattente.connecteur.ConnecteurSalleAttente;
import modele.serveur.stub.salleattente.connecteur.ConnecteurSalleAttenteIF;
import modele.serveur.stub.connexion.session.SessionIF;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.rmi.server.Unreferenced;
import java.time.LocalDate;

public class Session extends UnicastRemoteObject implements SessionIF, Unreferenced {

    private final Joueur joueur;

    public Session(Joueur joueur) throws RemoteException {
        this.joueur = joueur;
    }

    @Override
    public void logout() throws RemoteException {
        GestionnaireSession.getInstance().enregistreDeconnexion(this.joueur.getPseudo());
        try { PortailAmis.getInstance().seDeconnecter(this.getJoueur().getPseudo()); } catch (IllegalArgumentException iae) { /* ne rien faire */ }
        unexportObject(this, true);
    }

    @Override
    public ConnecteurSalleAttenteIF getConnecteurSalleAttente() throws RemoteException {
        return (ConnecteurSalleAttenteIF) ConnecteurSalleAttente.getInstance();
    }

    @Override
    public PortailAmisIF getPortailsAmis(AmisListenerIF listener) throws RemoteException {
        try {
            PortailAmis.getInstance().seConnecter(this.getJoueur().getPseudo(), listener);
        } catch (IllegalArgumentException iae) {
            // Ne rien faire;
        }
        return (PortailAmisIF) PortailAmis.getInstance();
    }

    @Override
    public Joueur getProfil() throws RemoteException {
        return this.joueur;
    }

    @Override
    public boolean modifierProfil(String ancienMotDePasse, String nouveauMotDePasse, String mail, LocalDate anniv) throws IllegalArgumentException, RemoteException {
        GestionnaireJoueur.getInstance().enregistrerModification(this.getJoueur().getPseudo(), ancienMotDePasse, nouveauMotDePasse, mail, anniv);
        return true;
    }

    @Override
    public void unreferenced() {
        try {
            GestionnaireSession.getInstance().enregistreDeconnexion(this.getJoueur().getPseudo());
            PortailAmis.getInstance().seDeconnecter(this.getJoueur().getPseudo());
            unexportObject(this, true);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public Joueur getJoueur() {
        return this.joueur;
    }
}
