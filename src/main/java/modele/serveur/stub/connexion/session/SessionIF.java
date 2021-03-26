package modele.serveur.stub.connexion.session;

import modele.client.stub.amis.AmisListenerIF;
import modele.serveur.stub.amis.PortailAmisIF;
import modele.implementation.connexion.joueur.Joueur;
import modele.serveur.stub.salleattente.connecteur.ConnecteurSalleAttenteIF;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.time.LocalDate;

public interface SessionIF extends Remote {

    /*
     * Permet a la session d'indiquer qu'elle se deconnecte
     */
    public abstract void logout() throws RemoteException;
    /*
     * Permet a une session d'obtenir le connecteur des salles d'attentes
     */
    public abstract ConnecteurSalleAttenteIF getConnecteurSalleAttente() throws RemoteException;
    /*
     * Permet a une session d'obtenir le portails des amis et de realiser ses operations
     */
    public abstract PortailAmisIF getPortailsAmis(AmisListenerIF listener) throws RemoteException;
    /*
     * Permet a un joueur d'obtenir son profil
     */
    public abstract Joueur getProfil() throws RemoteException;
    /*
     * Permet a un joueur de modifier son profil
     */
    public abstract boolean modifierProfil(String ancienMotDePasse, String nouveauMotDePasse, String mail, LocalDate anniv) throws IllegalArgumentException, RemoteException;

}
