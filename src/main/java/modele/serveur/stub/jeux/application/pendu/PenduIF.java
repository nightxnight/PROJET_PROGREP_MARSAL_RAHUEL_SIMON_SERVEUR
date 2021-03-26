package modele.serveur.stub.jeux.application.pendu;

import modele.serveur.stub.jeux.application.JeuxIF;

import java.rmi.RemoteException;

public interface PenduIF extends JeuxIF {

    /*
     * Permet a un joueur de proposer une lettre
     */
    public abstract void proposerLettre(String pseudo, char lettre) throws RemoteException, IllegalArgumentException;
    /*
     * Permet a un joueur de proposer un mot
     */
    public abstract void proposerMot(String pseudo, String mot) throws RemoteException, IllegalArgumentException;

}
