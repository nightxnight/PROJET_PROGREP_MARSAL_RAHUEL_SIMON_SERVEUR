package modele.serveur.stub.jeux.application.morpion;

import modele.serveur.stub.jeux.application.JeuxIF;

import java.rmi.RemoteException;

public interface MorpionIF extends JeuxIF {

    /*
     * Permet a un joueur de bloquer une case
     */
    public abstract void bloquerCase(String pseudo, int ligne, int colonne, char symbole) throws RemoteException, IllegalArgumentException;

}
