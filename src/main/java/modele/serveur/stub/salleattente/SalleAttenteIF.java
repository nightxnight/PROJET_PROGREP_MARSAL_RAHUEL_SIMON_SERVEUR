package modele.serveur.stub.salleattente;

import modele.implementation.connexion.joueur.JoueurProxy;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.HashMap;

public interface SalleAttenteIF extends Remote {

    public abstract String whoIsProprietaire() throws RemoteException;
    public abstract HashMap<JoueurProxy, Boolean> getListeJoueur() throws RemoteException;
    public abstract void envoyerMessage(String pseudo, String message) throws RemoteException;
    public abstract void changerEtat (String pseudo, boolean pret) throws RemoteException;
    public abstract void sortir(String pseudo) throws RemoteException;
    public abstract void inviterAmi(String pseudo, String pseudoAmi) throws RemoteException, IllegalArgumentException;

    public abstract HashMap<String, Object> getParametresSalle() throws RemoteException;
    public abstract HashMap<String, Object> getParametresJeu() throws RemoteException;
}
