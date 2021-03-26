package modele.serveur.stub.salleattente.connecteur;

import modele.client.stub.salleattente.ListenerSalleAttenteIF;
import modele.serveur.stub.salleattente.SalleAttenteIF;
import modele.serveur.stub.salleattente.SalleAttenteProprietaireIF;
import modele.implementation.salleattente.SalleAttenteProxy;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface ConnecteurSalleAttenteIF extends Remote {

    /*
     * Permet de creer une salle d'attente, renvoie une interface proprietaire
     */
    public abstract SalleAttenteProprietaireIF creer(String pseudoPropietaire, String nomSalle, String motDePasse, boolean publique) throws RemoteException, IllegalArgumentException, IllegalAccessException;
    /*
     * Permet de rejoindre une salle d'attente, renvoie une interface de salle d'attente
     */
    public abstract SalleAttenteIF rejoindre(String pseudo, ListenerSalleAttenteIF clientListener, String nomSalle, String motDePasseSalle) throws RemoteException, IllegalArgumentException;
    /*
     * Permet d'obtenir la liste des salles d'attentes publiques
     */
    public abstract ArrayList<SalleAttenteProxy> getListeSallesAttentes() throws RemoteException;
    /*
     * Permet de filtrer la liste des salles d'attentes publiques
     */
    public abstract ArrayList<SalleAttenteProxy> rechercher(String nomJeu, boolean masquerPleine, boolean masquerMdp) throws RemoteException;
}
