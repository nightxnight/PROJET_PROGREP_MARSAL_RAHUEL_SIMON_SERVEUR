package serveur.modele.stub.jeux.application.pendu;

import serveur.modele.stub.jeux.application.JeuxIF;

import java.rmi.RemoteException;

public interface PenduIF extends JeuxIF {

    public abstract void proposerLettre(String pseudo, char lettre) throws RemoteException, IllegalArgumentException;
    public abstract void proposerMot(String pseudo, String mot) throws RemoteException, IllegalArgumentException;

}
