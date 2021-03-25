package modele.gestionnaire;

import modele.implementation.connexion.session.Session;

import java.rmi.RemoteException;
import java.time.LocalDate;
import java.util.HashMap;

public class GestionnaireSession {

    /*
     * Singleton Design Pattern
     */

    private static GestionnaireSession instance = null;
    public static GestionnaireSession getInstance() throws RemoteException {
        if (instance == null) instance = new GestionnaireSession();
        return instance;
    }

    private HashMap<String, Session> mapSession;

    private GestionnaireSession() {
        super();
        this.mapSession = new HashMap<String, Session>();
    }

    public Session enregistreInscription(String pseudo, String motDePasse, String mail, LocalDate anniv) throws IllegalArgumentException, RemoteException {
        Session session = new Session(GestionnaireJoueur.getInstance().enregistrerInscription(pseudo, motDePasse, mail, anniv));
        mapSession.put(pseudo, session);
        return session;
    }

    public Session enregistreConnexion(String pseudo, String motDePasse) throws IllegalArgumentException, RemoteException {
        if(mapSession.containsKey(pseudo)) throw new IllegalArgumentException("Ce compte est deja connecte.");
        Session session = new Session(GestionnaireJoueur.getInstance().autoriseConnexion(pseudo, motDePasse));
        mapSession.put(pseudo, session);
        return session;
    }

    public void enregistreDeconnexion(String pseudo) {
        mapSession.remove(pseudo);
    }

    public Session getSessionFromPseudo(String pseudo) throws IllegalArgumentException {
        Session s = mapSession.get(pseudo);
        if (s == null) throw new IllegalArgumentException("Aucune session avec ce pseudo n'existe.");
        else return s;
    }

    public HashMap<String, Session> getMapSession() {
        return mapSession;
    }

}
