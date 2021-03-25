package modele.client.stub.jeux.morpion;

import modele.client.stub.jeux.JeuxListenerIF;

import java.rmi.RemoteException;

public interface MorpionListenerIF extends JeuxListenerIF {

    public abstract void recupererParametres(int taille, char symbole) throws RemoteException;
    public abstract void recupererCaseBloque(int ligne, int colonne, char symbole) throws RemoteException;

}
