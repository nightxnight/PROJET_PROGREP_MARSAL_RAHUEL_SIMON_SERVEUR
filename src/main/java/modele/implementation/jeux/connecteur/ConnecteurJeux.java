package modele.implementation.jeux.connecteur;

import modele.client.stub.jeux.JeuxListenerIF;
import modele.implementation.jeux.application.Application;
import modele.serveur.stub.jeux.application.JeuxIF;
import modele.serveur.stub.jeux.connecteur.ConnecteurJeuxIF;

import java.rmi.NoSuchObjectException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.rmi.server.Unreferenced;
import java.util.ArrayList;
import java.util.HashMap;

public class ConnecteurJeux extends UnicastRemoteObject implements ConnecteurJeuxIF, Unreferenced, Runnable {

    /*
     * Le connecteur de jeu permet de lancer la partie
     * une fois que tous les joueurs l'ont rejoint.
     *
     * Passer via le connecteur au lieu de leur envoyer directement la partie permet deux choses.
     *    1 - Qu'il passe le listener associé au jeu pour que le serveur communique avec eux
     *    2 - Que la partie se lance avec un thread cote serveur et donc verifier en temps reel les inactifs.
     *    3 - Annuler la partie si un joueur ne s'est pas connecte.
     */
    private Thread thread;
    private boolean reference = true;

    private HashMap<String, Boolean> mapJoueur;
    private Application partie;

    public ConnecteurJeux(Application partie, ArrayList<String> listeJoueur) throws RemoteException {
        super();
        this.partie = partie;
        this.mapJoueur = new HashMap<String, Boolean>();
        for(String pseudo : listeJoueur)
            mapJoueur.put(pseudo, false);
        this.thread = new Thread(this);
        thread.start();
    }


    @Override
    public void run() {
        while(reference) {
            if(tousLeMondePret()) break;
        }
        if(reference)
            try { partie.lancerPartie(); } catch (RemoteException re) { re.printStackTrace(); }
    }
    
    @Override
    public JeuxIF rejoindrePartie(String pseudo, JeuxListenerIF clientListener) throws RemoteException {
        if (!mapJoueur.containsKey(pseudo)) throw new IllegalArgumentException("Ce joueur n'est pas dans cette partie.");
        JeuxIF jeuxIF = partie.rejoindrePartie(pseudo, clientListener);
        mapJoueur.put(pseudo, true);
        return jeuxIF;
    }

    public boolean tousLeMondePret() {
        boolean tousLeMondePret = true;
        for(String pseudo : mapJoueur.keySet())
            tousLeMondePret = tousLeMondePret && mapJoueur.get(pseudo);
        return tousLeMondePret;
    }

    @Override
    public void unreferenced() {
        try {
            this.reference = false;
            unexportObject(this, true);
        } catch(NoSuchObjectException nsoe) {
            System.out.println(nsoe.getMessage());
        }
    }
}
