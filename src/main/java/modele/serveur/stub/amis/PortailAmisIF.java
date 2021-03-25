package modele.serveur.stub.amis;

import modele.implementation.amis.chat.Message;
import modele.implementation.amis.chat.SimpleMessage;
import modele.implementation.connexion.joueur.JoueurProxy;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface PortailAmisIF extends Remote {

    public abstract void demandeAmis(String pseudo, String pseudoDemande) throws RemoteException, IllegalArgumentException;
    public abstract void accepterDemande(String pseudo, String pseudoAccepte, boolean accepter) throws RemoteException, IllegalArgumentException;
    public abstract void supprimerAmis(String pseudo, String pseudoSupprime) throws RemoteException, IllegalArgumentException;

    public abstract ArrayList<JoueurProxy> getListeAmis(String pseudo) throws RemoteException, IllegalArgumentException;
    public abstract ArrayList<JoueurProxy> getListeDemande(String pseudo) throws RemoteException, IllegalArgumentException;
    public abstract ArrayList<Message> getConversation(String pseudoDemandeur, String pseudoConversation) throws RemoteException, IllegalArgumentException;

    public abstract void envoyerMessage(String de, String pour, SimpleMessage message) throws RemoteException, IllegalArgumentException;
}
