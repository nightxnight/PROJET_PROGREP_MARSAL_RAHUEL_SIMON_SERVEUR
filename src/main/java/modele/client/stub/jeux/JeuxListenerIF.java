package modele.client.stub.jeux;

import modele.serveur.stub.jeux.application.ResultatPartieEnum;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface JeuxListenerIF extends Remote {

    public abstract void lancerJeu() throws RemoteException;
    public abstract void faireJouer() throws RemoteException;
    public abstract void arreterJeu(ResultatPartieEnum resultat, String message) throws RemoteException;

}
