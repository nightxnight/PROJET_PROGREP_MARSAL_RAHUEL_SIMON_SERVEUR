package modele.implementation.salleattente;

import modele.client.stub.salleattente.ListenerSalleAttenteIF;
import modele.implementation.amis.PortailAmis;
import modele.implementation.connexion.joueur.JoueurProxy;
import modele.implementation.jeux.application.Application;
import modele.implementation.connexion.session.Session;
import modele.gestionnaire.GestionnaireSalleAttente;
import modele.gestionnaire.GestionnaireSession;
import modele.implementation.jeux.connecteur.ConnecteurJeux;
import modele.serveur.stub.salleattente.SalleAttenteIF;
import modele.serveur.stub.salleattente.SalleAttenteProprietaireIF;
import utils.Paire;

import java.lang.reflect.Array;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.rmi.server.Unreferenced;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class SalleAttente extends UnicastRemoteObject implements SalleAttenteIF, SalleAttenteProprietaireIF, Runnable, Unreferenced {

    private Thread thread;
    private boolean reference = true;

    private final String nomSalle;
    private Session proprietaire;
    private SalleAttenteParametres parametres;
    private boolean peutRejoindre = true;

    /*
     * Variables volatile pour s'assurer que chaque thread manipule la derniere
     * version de cette objet
     */
    private volatile Application application = null;
    private volatile HashMap<String, Paire<ListenerSalleAttenteIF, Boolean>> mapJoueurs;

    public SalleAttente(String pseudoProprietaire, String nomSalle, String motDePasse, boolean publique) throws RemoteException, IllegalArgumentException {
        super();
        this.proprietaire = GestionnaireSession.getInstance().getSessionFromPseudo(pseudoProprietaire);
        this.nomSalle = nomSalle;
        this.parametres = new SalleAttenteParametres(this, motDePasse, publique);
        this.mapJoueurs = new HashMap<String, Paire<ListenerSalleAttenteIF, Boolean>>();
        this.thread = new Thread(this);
        this.thread.start();
    }

    /*
     * Thread qui verifie si tous le monde est pret
     * si c'est le cas il lance la partie au bout de 10 secondes
     * Il s'occupe aussi de kicker automatiquement les joueurs deconnecte
     */
    @Override
    public void run() {
        while (reference) {
            try {
                autokick();
                if (joueursPret()) {
                    peutRejoindre = false;
                    if (decompteLancement()) {
                        while (true) {
                            if(!application.isReference()) {
                                autokick();
                                break;
                            }
                        }
                        ArrayList<String> listePseudo = new ArrayList<String>(mapJoueurs.keySet());
                        boolean nombreJoueurValide = verifierNombreJoueur(mapJoueurs.size());
                        for (String s : listePseudo) {
                            mapJoueurs.get(s).getPremier().activerPret(nombreJoueurValide);
                        }
                        this.designerProprietaire(proprietaire.getJoueur().getPseudo());
                        peutRejoindre = true;
                    } else peutRejoindre = true;
                }
            } catch (Exception e) {
                // Ne rien faire
            }
        }
    }

    /*
     * Methode synchronise pour s'assurer qu'un seul thread execute la fonction
     */
    private synchronized void autokick() throws RemoteException {
        ArrayList<String> listePseudo = new ArrayList<String>(mapJoueurs.keySet());
        for(int i = 0; i < listePseudo.size(); i++) {
            try {
                GestionnaireSession.getInstance().getSessionFromPseudo(listePseudo.get(i));
            } catch (IllegalArgumentException iae) {
                sortir(listePseudo.get(i), "est partie. (Perte de connexion)");
            }
        }
    }

    /*
     * Methode appele par le connecteur pour faire rentrer un joueur dans cette salle d'attente
     */
    public SalleAttente entrer(String pseudoEntrant, ListenerSalleAttenteIF clientListener, String motDePasse) throws RemoteException, IllegalArgumentException {
        if (!this.parametres.getMotDePasse().equals(motDePasse))
            throw new IllegalArgumentException("Les informations transmises n'ont pas permis de vous faire rejoindre cette salle d'attente");
        else if (this.parametres.getNombreJoueurSalle() <= this.mapJoueurs.size())
            throw new IllegalArgumentException("Cette salle est complete.");
        else if (!peutRejoindre)
            throw new IllegalArgumentException("Cette salle est en pleine partie.");
        else if(this.mapJoueurs.containsKey(pseudoEntrant))
            throw new IllegalArgumentException("Vous etes deja dans cette salle d'attente.");

        JoueurProxy joueurEntrant = new JoueurProxy(pseudoEntrant, true); // renvoie une erreur qui interruptera la fonction si le joueur n'existe pas

        for(String pseudo : mapJoueurs.keySet()) {
            mapJoueurs.get(pseudo).getPremier().connexionJoueur(joueurEntrant);
        }

        boolean nombreJoueurRequis = verifierNombreJoueur(mapJoueurs.size() + 1);
        if(!nombreJoueurRequis) toutLeMondePasPret();
        for(String pseudo : mapJoueurs.keySet()) {
            mapJoueurs.get(pseudo).getPremier().activerPret(nombreJoueurRequis);
        }

        envoyerMessage("serveur", pseudoEntrant + " vient d'entrer dans la salle.");
        mapJoueurs.put(pseudoEntrant, new Paire<ListenerSalleAttenteIF, Boolean>(clientListener, false));
        return this;
    }

    public synchronized boolean verifierNombreJoueur(int nombre) {
        return parametres.getJeu().getSecond().getJOUEUR_MIN() <= nombre
                && nombre <= parametres.getJeu().getSecond().getJOUEUR_MAX();
    }

    public synchronized void toutLeMondePasPret() {
        ArrayList<String> listePseudo = new ArrayList<String>(mapJoueurs.keySet());
        for(int i = 0; i < listePseudo.size(); i++) {
            mapJoueurs.get(listePseudo.get(i)).setSecond(false);
        }
    }

    public synchronized void designerProprietaire(String pseudoProprietaire) throws RemoteException, IllegalArgumentException {
        this.proprietaire = GestionnaireSession.getInstance().getSessionFromPseudo(pseudoProprietaire);;
        mapJoueurs.get(pseudoProprietaire).getPremier().designerProprietaire((SalleAttenteProprietaireIF) this);
        for(String pseudo : mapJoueurs.keySet())
            mapJoueurs.get(pseudo).getPremier().actualiserJoueur();
    }

    /*
     * Methodes accessibles a tous les joueurs de la salle d'attente
     */

    @Override
    public String whoIsProprietaire() throws RemoteException {
        return proprietaire.getJoueur().getPseudo();
    }

    @Override
    public HashMap<JoueurProxy, Boolean> getListeJoueur() throws RemoteException {
        HashMap<JoueurProxy, Boolean> mapJoueursProxy = new HashMap<JoueurProxy, Boolean>();
        for(String pseudo : mapJoueurs.keySet()) {
            JoueurProxy joueurProxy = new JoueurProxy(pseudo, true);
            mapJoueursProxy.put(joueurProxy, mapJoueurs.get(pseudo).getSecond());
        }
        return mapJoueursProxy;
    }

    @Override
    public void envoyerMessage(String pseudoEnvoyeur, String message) throws RemoteException {
        for(String pseudo : mapJoueurs.keySet())
            mapJoueurs.get(pseudo).getPremier().recupererMessage(pseudoEnvoyeur, message);
    }

    @Override
    public void changerEtat(String pseudoChangement, boolean pret) throws RemoteException {
        mapJoueurs.get(pseudoChangement).setSecond(pret);
        for (String pseudo : mapJoueurs.keySet())
            mapJoueurs.get(pseudo).getPremier().changerEtatJoueur(pseudoChangement, pret);
    }

    @Override
    public void sortir(String pseudoSortant) throws RemoteException {
        sortir(pseudoSortant, "vient de partie");
    }

    @Override
    public void inviterAmi(String pseudo, String pseudoAmi) throws RemoteException, IllegalArgumentException {
        if(mapJoueurs.containsKey(pseudoAmi))
            throw new IllegalArgumentException("Cet ami est deja dans la salle d'attente");
        PortailAmis.getInstance().envoyerInvitationSalle(pseudo, pseudoAmi, this.nomSalle, parametres.getMotDePasse());
        envoyerMessage("serveur", pseudo + " a invite " + pseudoAmi);
    }

    private void sortir(String pseudoSortant, String message) throws RemoteException {
        mapJoueurs.remove(pseudoSortant);
        if(mapJoueurs.size() == 0) {
            unreferenced();
            return;
        }

        heritageProprietaire(pseudoSortant);

        boolean nombreJoueurRequis = verifierNombreJoueur(mapJoueurs.size());
        if(!nombreJoueurRequis) toutLeMondePasPret();
        for(String pseudo : mapJoueurs.keySet()) {
            mapJoueurs.get(pseudo).getPremier().activerPret(nombreJoueurRequis);
            mapJoueurs.get(pseudo).getPremier().deconnexionJoueur(pseudoSortant);
        }
        envoyerMessage("serveur", pseudoSortant + " " + message + ".");
    }

    private void heritageProprietaire(String pseudoSortant) throws RemoteException {
         if (this.proprietaire.getJoueur().getPseudo().equals(pseudoSortant)) {
            designerProprietaire(((String) mapJoueurs.keySet().toArray()[0]));
            envoyerMessage("serveur", proprietaire.getJoueur().getPseudo() + " est le nouveau proprietaire");
        }
    }

    private synchronized boolean joueursPret() {
        boolean toutLeMondePret = true;
        ArrayList<String> listePseudo = new ArrayList<String>(mapJoueurs.keySet());
        for(int i = 0; i < listePseudo.size(); i++) {
            toutLeMondePret = toutLeMondePret && mapJoueurs.get(listePseudo.get(i)).getSecond();
        }
        return (verifierNombreJoueur(mapJoueurs.size())) && toutLeMondePret;
    }

    private synchronized boolean decompteLancement() throws RemoteException {
        for (int i = 10 ; 0 < i; i--) {
            if(!joueursPret()) {
                envoyerMessage("annulation", "un joueur vient de se retirer."); return false;
            } else  {
                envoyerMessage("serveur", "debut dans " + i + " seconde(s).");
                try { TimeUnit.SECONDS.sleep(1); } catch (Exception e) { /* Should not occurs */ }
            }
        }
        envoyerConnecteur();
        return true;
    }

    /*
     * Methode permettant d'envoyer le connecteur de jeu aux clients
     */
    private void envoyerConnecteur() throws RemoteException {
        this.application = Application.creerApplication(parametres.getJeu().getPremier(), parametres.getJeu().getSecond());

        ConnecteurJeux connecteurJeux = new ConnecteurJeux(this.application, new ArrayList<String>(mapJoueurs.keySet()));

        ArrayList<String> listePseudo = new ArrayList<String>(mapJoueurs.keySet());
        for(int i = 0; i < listePseudo.size(); i++) {
            mapJoueurs.get(listePseudo.get(i)).getPremier().connexionPartie(connecteurJeux, parametres.getJeu().getPremier());
        }
        toutLeMondePasPret();
        for (int i = 0; i < listePseudo.size(); i++) {
            mapJoueurs.get(listePseudo.get(i)).getPremier().actualiserJoueur();
            mapJoueurs.get(listePseudo.get(i)).getPremier().activerPret(false);
        }


    }

    /*
     * Methodes accessibles uniquement au proprietaire
     */

    @Override
    public void nommerProprietaire(String ancienProprietaire, String nouveauProprietaire) throws RemoteException, IllegalArgumentException {
        if(!this.proprietaire.getJoueur().getPseudo().equals(ancienProprietaire)) throw new IllegalArgumentException("Vous n'etes pas le proprietaire de cette salle.");
        try {
            designerProprietaire(nouveauProprietaire);
            envoyerMessage("serveur", nouveauProprietaire + " est le nouveau proprietaire.");
            for(String pseudo : mapJoueurs.keySet())
                mapJoueurs.get(pseudo).getPremier().actualiserJoueur();
        } catch (IllegalArgumentException iae) {
            throw new IllegalArgumentException("Ce joueur n'est pas present dans la salle d'attente.");
        }
    }

    @Override
    public void virerJoueur(String pseudoProprietaire, String pseudoExclue) throws RemoteException, IllegalArgumentException {
        if(!this.proprietaire.getJoueur().getPseudo().equals(pseudoProprietaire))
            throw new IllegalArgumentException("Vous n'etes pas le proprietaire de cette salle.");
        mapJoueurs.get(pseudoExclue).getPremier().exclusion();
        sortir(pseudoExclue, "s'est fait exclure");
    }

    @Override
    public void changerParametreSalle(String pseudoProprietaire, String nomParam, Object valeur) throws RemoteException, IllegalArgumentException, ClassCastException {
        if(!this.proprietaire.getJoueur().getPseudo().equals(pseudoProprietaire))
            throw new IllegalArgumentException("Vous n'etes pas le proprietaire de cette salle.");
        parametres.changerParametreSalle(nomParam, valeur);
        for(String pseudo : mapJoueurs.keySet())
            mapJoueurs.get(pseudo).getPremier().changerParametreSalle(nomParam, valeur);
    }

    @Override
    public void changerParametreJeu(String pseudoProprietaire, String nomParam, Object valeur) throws RemoteException, IllegalArgumentException, ClassCastException {
        if(!this.proprietaire.getJoueur().getPseudo().equals(pseudoProprietaire))
            throw new IllegalArgumentException("Vous n'etes pas le proprietaire de cette salle.");
        parametres.changerParametreJeu(nomParam, valeur);
        for(String pseudo : mapJoueurs.keySet())
            mapJoueurs.get(pseudo).getPremier().changerParametreJeu(nomParam, valeur);
    }

    /*
     * Parametres de la salle
     */

    @Override
    public HashMap<String, Object> getParametresSalle() throws RemoteException{
        return parametres.getParametresSalle();
    }

    @Override
    public HashMap<String, Object> getParametresJeu() throws RemoteException {
        return parametres.getParametresJeu();
    }

    /*
     * Getters
     */

    public HashMap<String, Paire<ListenerSalleAttenteIF, Boolean>> getMapJoueurs() {
        return mapJoueurs;
    }

    public String getNomSalle() {
        return nomSalle;
    }

    public SalleAttenteParametres getParametres() {
        return parametres;
    }

    @Override
    public void unreferenced() {
        try {
            GestionnaireSalleAttente.getInstance().detruireSalleAttente(this.nomSalle);
            this.reference = false;
            unexportObject(this, true);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
