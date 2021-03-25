package serveur.modele.stub.jeux.connecteur;

import serveur.modele.client.stub.jeux.JeuxListenerIF;
import serveur.modele.stub.jeux.application.JeuxIF;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ConnecteurJeuxIF extends Remote {

    public abstract JeuxIF rejoindrePartie(String pseudo, JeuxListenerIF clientListener) throws RemoteException;

}
