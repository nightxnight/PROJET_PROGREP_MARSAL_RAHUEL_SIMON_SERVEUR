package modele.serveur.stub.amis;

import modele.implementation.amis.chat.Message;
import modele.implementation.amis.chat.SimpleMessage;
import modele.implementation.connexion.joueur.JoueurProxy;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface PortailAmisIF extends Remote {

    /*
     * Permet a un joueur de demande en amis un autre,
     */
    public abstract void demandeAmis(String pseudo, String pseudoDemande) throws RemoteException, IllegalArgumentException;
    /*
     * Permet a un joueur d'accepter une demande d'amis
     */
    public abstract void accepterDemande(String pseudo, String pseudoAccepte, boolean accepter) throws RemoteException, IllegalArgumentException;
    /*
     * Permet a un joueur de supprimer un amis
     */
    public abstract void supprimerAmis(String pseudo, String pseudoSupprime) throws RemoteException, IllegalArgumentException;

    /*
     * Permet d'obtenir la liste des amis d'un joueur
     */
    public abstract ArrayList<JoueurProxy> getListeAmis(String pseudo) throws RemoteException, IllegalArgumentException;
    /*
     * Permet d'obtenir la liste des demandes d'amis d'un joueur
     */
    public abstract ArrayList<JoueurProxy> getListeDemande(String pseudo) throws RemoteException, IllegalArgumentException;
    /*
     * Permet d'obtenir la conversation entre deux joueurs
     */
    public abstract ArrayList<Message> getConversation(String pseudoDemandeur, String pseudoConversation) throws RemoteException, IllegalArgumentException;
    /*
     * Permet d'envoyer un message a un joueur
     */
    public abstract void envoyerMessage(String de, String pour, SimpleMessage message) throws RemoteException, IllegalArgumentException;
}
