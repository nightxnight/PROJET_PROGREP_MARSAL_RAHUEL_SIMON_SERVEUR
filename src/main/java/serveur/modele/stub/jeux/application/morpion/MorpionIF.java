package serveur.modele.stub.jeux.application.morpion;

import serveur.modele.stub.jeux.application.JeuxIF;

import java.rmi.RemoteException;

public interface MorpionIF extends JeuxIF {

    public abstract void bloquerCase(String pseudo, int ligne, int colonne, char symbole) throws RemoteException, IllegalArgumentException;

}
