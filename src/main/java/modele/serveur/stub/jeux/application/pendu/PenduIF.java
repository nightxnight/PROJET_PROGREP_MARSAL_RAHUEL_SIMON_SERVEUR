package modele.serveur.stub.jeux.application.pendu;

import modele.serveur.stub.jeux.application.JeuxIF;

import java.rmi.RemoteException;

public interface PenduIF extends JeuxIF {

    public abstract void proposerLettre(String pseudo, char lettre) throws RemoteException, IllegalArgumentException;
    public abstract void proposerMot(String pseudo, String mot) throws RemoteException, IllegalArgumentException;

}
