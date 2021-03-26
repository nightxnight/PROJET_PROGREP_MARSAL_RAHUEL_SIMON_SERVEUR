package modele.serveur.stub.salleattente;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface SalleAttenteProprietaireIF extends Remote {

    /*
     * Permet de changer un parametre de la salle
     */
    public abstract void changerParametreSalle(String pseudoProprietaire, String nomParam, Object valeur) throws RemoteException, IllegalArgumentException, ClassCastException;
    /*
     * Permet de changer les parametres de jeu
     */
    public abstract void changerParametreJeu(String pseudoProprietaire, String nomParam, Object valeur) throws RemoteException, IllegalArgumentException, ClassCastException;
    /*
     * Permet de virer un joueur de la salle d'attente
     */
    public abstract void virerJoueur(String pseudoProprietaire, String pseudo) throws RemoteException, IllegalArgumentException;
    /*
     * Permet de nommer un nouveau proprietaire
     */
    public abstract void nommerProprietaire(String ancienProprietaire, String nouveauProprietaire) throws RemoteException, IllegalArgumentException;
}
