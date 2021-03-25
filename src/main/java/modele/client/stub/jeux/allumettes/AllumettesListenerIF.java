package modele.client.stub.jeux.allumettes;

import modele.client.stub.jeux.JeuxListenerIF;

import java.rmi.RemoteException;

public interface AllumettesListenerIF extends JeuxListenerIF {

    public abstract void recupererParametres(int nbAllumettesInitial, int nbAllumettesMaxTour) throws RemoteException;
    public abstract void actualiserNbMaxAllumettesTour(int nombre) throws RemoteException;
    public abstract void retirerAllumettes(String pseudo, int nbAllumettes, String message) throws RemoteException;

}
