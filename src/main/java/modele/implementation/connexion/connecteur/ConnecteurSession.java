package modele.implementation.connexion.connecteur;

import modele.serveur.stub.connexion.session.SessionIF;
import modele.gestionnaire.GestionnaireSession;
import modele.serveur.stub.connexion.connecteur.ConnecteurSessionIF;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.time.LocalDate;

public class ConnecteurSession extends UnicastRemoteObject implements ConnecteurSessionIF {

    /*
     * Singleton Design Pattern
     */
    private static ConnecteurSession instance = null;
    public static ConnecteurSession getInstance() throws RemoteException {
        if (instance == null) instance = new ConnecteurSession();
        return instance;
    }

    private ConnecteurSession() throws RemoteException {
        super();
    }

    @Override
    public SessionIF seConnecter(String pseudo, String motDePasse) throws RemoteException {
        return (SessionIF) GestionnaireSession.getInstance().enregistreConnexion(pseudo, motDePasse);
    }

    @Override
    public SessionIF seInscrire(String pseudo, String motDePasse, String mail, LocalDate anniv) throws RemoteException {
        return (SessionIF) GestionnaireSession.getInstance().enregistreInscription(pseudo, motDePasse, mail, anniv);
    }
}
