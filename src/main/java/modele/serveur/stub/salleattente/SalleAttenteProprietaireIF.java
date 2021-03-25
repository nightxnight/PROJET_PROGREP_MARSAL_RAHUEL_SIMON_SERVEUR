package modele.serveur.stub.salleattente;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface SalleAttenteProprietaireIF extends Remote {

    public abstract void changerParametreSalle(String pseudoProprietaire, String nomParam, Object valeur) throws RemoteException, IllegalArgumentException, ClassCastException;
    public abstract void changerParametreJeu(String pseudoProprietaire, String nomParam, Object valeur) throws RemoteException, IllegalArgumentException, ClassCastException;
    public abstract void virerJoueur(String pseudoProprietaire, String pseudo) throws RemoteException, IllegalArgumentException;
    public abstract void nommerProprietaire(String ancienProprietaire, String nouveauProprietaire) throws RemoteException, IllegalArgumentException;
}
