package serveur.modele.stub.jeux.application.allumettes;

import serveur.modele.stub.jeux.application.JeuxIF;

import java.rmi.RemoteException;

public interface AllumettesIF extends JeuxIF {

    public abstract void prendreAllumettes(String pseudo, int nombre) throws RemoteException, IllegalArgumentException;

}
