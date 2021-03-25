package serveur.modele.stub.connexion.session;

import serveur.modele.client.stub.amis.AmisListenerIF;
import serveur.modele.stub.amis.PortailAmisIF;
import serveur.modele.implementation.connexion.joueur.Joueur;
import serveur.modele.stub.salleattente.connecteur.ConnecteurSalleAttenteIF;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.time.LocalDate;

public interface SessionIF extends Remote {

    public abstract void logout() throws RemoteException;
    public abstract ConnecteurSalleAttenteIF getConnecteurSalleAttente() throws RemoteException;
    public abstract PortailAmisIF getPortailsAmis(AmisListenerIF listener) throws RemoteException;
    public abstract Joueur getProfil() throws RemoteException;
    public abstract boolean modifierProfil(String ancienMotDePasse, String nouveauMotDePasse, String mail, LocalDate anniv) throws IllegalArgumentException, RemoteException;

}
