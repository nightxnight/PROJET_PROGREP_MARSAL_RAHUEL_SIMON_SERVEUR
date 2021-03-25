package modele.serveur.stub.connexion.connecteur;

import modele.serveur.stub.connexion.session.SessionIF;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.time.LocalDate;

public interface ConnecteurSessionIF extends Remote {

    public abstract SessionIF seConnecter(String pseudo, String motDePasse) throws RemoteException;
    public abstract SessionIF seInscrire(String pseudo, String motDePasse, String mail, LocalDate anniv) throws RemoteException;

}
