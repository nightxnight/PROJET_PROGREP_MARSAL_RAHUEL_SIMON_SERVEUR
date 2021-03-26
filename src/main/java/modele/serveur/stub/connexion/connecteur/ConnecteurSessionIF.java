package modele.serveur.stub.connexion.connecteur;

import modele.serveur.stub.connexion.session.SessionIF;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.time.LocalDate;

public interface ConnecteurSessionIF extends Remote {

    /*
     * Permet a un joueur de se connecter et d'obtenir une interface Session
     */
    public abstract SessionIF seConnecter(String pseudo, String motDePasse) throws RemoteException;
    /*
     * Permet a un joueur de s'inscrire, et de recuperer par l'occasion une session
     */
    public abstract SessionIF seInscrire(String pseudo, String motDePasse, String mail, LocalDate anniv) throws RemoteException;

}
