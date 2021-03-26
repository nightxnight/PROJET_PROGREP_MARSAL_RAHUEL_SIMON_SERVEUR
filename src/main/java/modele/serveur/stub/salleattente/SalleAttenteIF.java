package modele.serveur.stub.salleattente;

import modele.implementation.connexion.joueur.JoueurProxy;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.HashMap;

public interface SalleAttenteIF extends Remote {

    /*
     * Permet de savoir qui est le proprietaire
     */
    public abstract String whoIsProprietaire() throws RemoteException;
    /*
     * Permet de recuperer la liste des joueurs
     */
    public abstract HashMap<JoueurProxy, Boolean> getListeJoueur() throws RemoteException;
    /*
     * Permet d'envoyer un message dans le chat de salle
     */
    public abstract void envoyerMessage(String pseudo, String message) throws RemoteException;
    /*
     * Permet de changer l'etat d'un joueur dans la salle
     */
    public abstract void changerEtat (String pseudo, boolean pret) throws RemoteException;
    /*
     * Permet de sortir de la salle
     */
    public abstract void sortir(String pseudo) throws RemoteException;
    /*
     * Permet d'inviter un amis dans la salle d'attente
     */
    public abstract void inviterAmi(String pseudo, String pseudoAmi) throws RemoteException, IllegalArgumentException;

    /*
     * Permet d'obtenir la map des parametres de la salle
     */
    public abstract HashMap<String, Object> getParametresSalle() throws RemoteException;
    /*
     * Permet d'obtenir la map des parametres de jeu
     */
    public abstract HashMap<String, Object> getParametresJeu() throws RemoteException;
}
