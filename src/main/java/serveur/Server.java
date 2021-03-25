package serveur;

import serveur.modele.implementation.amis.PortailAmis;
import serveur.modele.implementation.connexion.connecteur.ConnecteurSession;
import serveur.modele.gestionnaire.GestionnaireJoueur;
import serveur.modele.gestionnaire.GestionnaireSalleAttente;
import serveur.modele.gestionnaire.GestionnaireSession;

import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;

public class Server {

    public static final String HOST = "127.0.0.1";
    public static final int PORT = 6000;

    public static void main(String[] args) throws Exception {
        System.setProperty("java.rmi.dgc.leaseValue", "30000");
        LocateRegistry.createRegistry(PORT);

        GestionnaireSession.getInstance();
        GestionnaireJoueur.getInstance();
        PortailAmis.getInstance();
        GestionnaireSalleAttente.getInstance();
        Naming.rebind("rmi://" + HOST + ":" + PORT + "/connexion", ConnecteurSession.getInstance());
    }
}
