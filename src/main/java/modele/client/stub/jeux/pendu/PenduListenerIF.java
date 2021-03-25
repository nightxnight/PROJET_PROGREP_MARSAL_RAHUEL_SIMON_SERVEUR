package modele.client.stub.jeux.pendu;

import modele.client.stub.jeux.JeuxListenerIF;

import java.rmi.RemoteException;

public interface PenduListenerIF extends JeuxListenerIF {

    public abstract void actualiserLettre(char lettre, boolean valide, String message) throws RemoteException;
    public abstract void actualiserMot(char[] motActualiser, boolean valide, String message) throws RemoteException;
    public abstract void actualiserNbErreur(int nbErreur) throws RemoteException;

}
