import modele.implementation.amis.PortailAmis;
import modele.implementation.connexion.connecteur.ConnecteurSession;
import modele.gestionnaire.GestionnaireJoueur;
import modele.gestionnaire.GestionnaireSalleAttente;
import modele.gestionnaire.GestionnaireSession;

import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;

public class Server {

    public static final String HOST = "127.0.0.1";
    public static final int PORT = 6000;

    public static void main(String[] args) throws Exception {
        /*
         * Le serveur verifiera toutes les 30 secondes si un objet distant est reference
         */
        System.setProperty("java.rmi.dgc.leaseValue", "30000");

        /*
         * Initialisation de tous les services
         */
        LocateRegistry.createRegistry(PORT);
        GestionnaireSession.getInstance();
        GestionnaireJoueur.getInstance();
        PortailAmis.getInstance();
        GestionnaireSalleAttente.getInstance();
        Naming.rebind("rmi://" + HOST + ":" + PORT + "/connexion", ConnecteurSession.getInstance());
    }
}
