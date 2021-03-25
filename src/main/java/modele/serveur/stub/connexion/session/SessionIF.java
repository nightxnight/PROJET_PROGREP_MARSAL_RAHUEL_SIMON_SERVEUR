package modele.serveur.stub.connexion.session;

import modele.client.stub.amis.AmisListenerIF;
import modele.serveur.stub.amis.PortailAmisIF;
import modele.implementation.connexion.joueur.Joueur;
import modele.serveur.stub.salleattente.connecteur.ConnecteurSalleAttenteIF;

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
