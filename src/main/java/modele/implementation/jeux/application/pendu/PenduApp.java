package modele.implementation.jeux.application.pendu;

import modele.client.stub.jeux.JeuxListenerIF;
import modele.client.stub.jeux.pendu.PenduListenerIF;
import modele.implementation.jeux.application.Application;
import modele.serveur.stub.jeux.application.JeuxIF;
import modele.serveur.stub.jeux.application.ResultatPartieEnum;
import modele.serveur.stub.jeux.application.pendu.PenduIF;
import utils.ListeOrdonnee;
import utils.Paire;

import java.rmi.NoSuchObjectException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Random;

public class PenduApp extends Application implements PenduIF {

    private ListeOrdonnee<Paire<String, PenduListenerIF>> listeJoueurs;

    private final int NB_ERREUR_MAX = 11;
    private int nbErreur = 0 ;

    private final PenduParams parametres;
    private final String mot;
    private ArrayList<Paire<Character, Boolean>> lettres;

    public PenduApp(PenduParams parametres) throws RemoteException {
        super();
        this.listeJoueurs = new ListeOrdonnee<Paire<String, PenduListenerIF>>();
        this.parametres = parametres;

        Random random = new Random();
        int idx = random.nextInt(parametres.getMapMots().get(parametres.getLangueMots()).length);
        this.mot = parametres.getMapMots().get(parametres.getLangueMots())[idx];
        this.lettres = new ArrayList<Paire<Character, Boolean>>();
        for(Character c : this.mot.toCharArray())
            this.lettres.add(new Paire<Character, Boolean>(c, false));
    }

    @Override
    public JeuxIF rejoindrePartie(String pseudo, JeuxListenerIF clientListener) throws RemoteException {
        listeJoueurs.getListe().add(new Paire<String, PenduListenerIF>(
                pseudo,
                (PenduListenerIF) clientListener));
        return (PenduIF) this;
    }

    @Override
    public void lancerPartie() throws RemoteException {
        for(Paire<String, PenduListenerIF> pairePseudoListener : listeJoueurs.getListe()) {
            char[] motDepart = actualiserMot(' ');
            pairePseudoListener.getSecond().actualiserMot(motDepart,false, "");
            pairePseudoListener.getSecond().lancerJeu();
        }
        this.partieLancer = true;
        new Thread(this).start();
        listeJoueurs.premier().getSecond().faireJouer();
    }

    @Override
    public void run() {
        while (partieLancer) {
            try { autokick(); } catch (Exception e) { /* Ne peut pas arriver, appel interne */ }
        }
        if(resultat == null) return;

        if (resultat == ResultatPartieEnum.GAGNE) {
            String message = this.dernierePersonneJouee + " a trouve le mot! C'etait : " + this.mot + ".";
            for(Paire<String, PenduListenerIF> pairePseudoListener : listeJoueurs.getListe()) {
                ResultatPartieEnum resultatJoueur = (pairePseudoListener.getPremier().equals(this.dernierePersonneJouee)) ? ResultatPartieEnum.GAGNE : ResultatPartieEnum.PERDU;
                try {
                    pairePseudoListener.getSecond().arreterJeu(resultatJoueur, message);
                } catch (RemoteException re) {
                    // Should not occur
                }
            }
        } else if (resultat == ResultatPartieEnum.GAGNANT_PAR_FORFAIT) {
            String message = "Vous avez gagner par forfait. Le mot etait : " + this.mot + ".";
            for(Paire<String, PenduListenerIF> pairePseudoListener : listeJoueurs.getListe()) {
                try {
                    pairePseudoListener.getSecond().arreterJeu(resultat, message);
                } catch (RemoteException re ) {
                    re.printStackTrace();
                }
            }
        } else {
            String message = "Le pendu a ete pendu! Le mot etait : " + this.mot + ".";
            for(Paire<String, PenduListenerIF> pairePseudoListener : listeJoueurs.getListe())
                try {
                    pairePseudoListener.getSecond().arreterJeu(ResultatPartieEnum.EGALITE, message);
                } catch (RemoteException re) {
                    // Should not occur
                }
        }
        unreferenced();
    }

    private synchronized void autokick() throws RemoteException {
        for (Paire<String, PenduListenerIF> pairePseudoListener : listeJoueurs.getListe()) {
            if (super.autokick(pairePseudoListener.getPremier())) {
                listeJoueurs.getListe().remove(pairePseudoListener);
                if(listeJoueurs.getListe().size() < parametres.getJOUEUR_MIN()) {
                    resultat = ResultatPartieEnum.GAGNANT_PAR_FORFAIT;
                    partieLancer = false;
                } else {
                    listeJoueurs.suivant().getSecond().faireJouer();
                }
            }
        }
    }

    @Override
    public void proposerLettre(String pseudo, char lettre) throws RemoteException, IllegalArgumentException {
        if (!partieLancer) return;

        if(!listeJoueurs.elementCourant().getPremier().equals(pseudo))
            throw new IllegalArgumentException("Ce n'est pas a ce joueur de jouer.");
        else  {
            this.dernierePersonneJouee = pseudo;

            boolean lettreValide = false;
            Paire<Character, Boolean> paireChercher = new Paire<Character, Boolean>(lettre, false);
            lettreValide = lettres.stream().anyMatch(paire -> paire.equals(paireChercher));

            char[] motActualiser = actualiserMot(lettre);

            String message = (lettreValide) ? pseudo + " a trouve la lettre " + lettre : pseudo + " a propose " + lettre + ", c'est faux";
            for (Paire<String, PenduListenerIF> pairePseudoListener: listeJoueurs.getListe()) {
                pairePseudoListener.getSecond().actualiserMot(motActualiser, false, "");
                pairePseudoListener.getSecond().actualiserLettre(lettre, lettreValide, message);
            }
            if(!lettreValide) actualiserNbErreur();

            listeJoueurs.suivant().getSecond().faireJouer();
        }
    }

    @Override
    public void proposerMot(String pseudo, String mot) throws RemoteException, IllegalArgumentException {
        if (!partieLancer) return;

        if(!listeJoueurs.elementCourant().getPremier().equals(pseudo))
            throw new IllegalArgumentException("Ce n'est pas a ce joueur de jouer.");
        else {
            this.dernierePersonneJouee = pseudo;

            boolean motTrouve = this.mot.equals(mot);
            char[] motActualiser = (motTrouve) ? this.mot.toCharArray() : actualiserMot(' ');
            String message = pseudo + " a propose : " + mot;

            for (Paire<String, PenduListenerIF> pairePseudoListener : listeJoueurs.getListe()) {
                pairePseudoListener.getSecond().actualiserMot(motActualiser, motTrouve, mot);
            }

            if (!motTrouve) {
                actualiserNbErreur();
                listeJoueurs.suivant().getSecond().faireJouer();
            } else {
                this.resultat = ResultatPartieEnum.GAGNE;
                partieLancer = false;
            }
        }
    }

    private char[] actualiserMot(char lettre) {
        char[] motActualiser = new char[lettres.size()];
        for(int i = 0; i < lettres.size(); i++) {
            if(lettres.get(i).getPremier() == lettre) lettres.get(i).setSecond(true);
            motActualiser[i] = (lettres.get(i).getSecond()) ? lettres.get(i).getPremier() : '_';
        }
        return motActualiser;
    }

    private void actualiserNbErreur() throws RemoteException {
        if(!parametres.isErreurActive()) return;
        this.nbErreur += 1;
        for (Paire<String, PenduListenerIF> pairePseudoListener : listeJoueurs.getListe()) {
            pairePseudoListener.getSecond().actualiserNbErreur(nbErreur);
        }

        if(this.nbErreur == this.NB_ERREUR_MAX) {
            this.resultat = ResultatPartieEnum.EGALITE;
            partieLancer = false;
        }
    }

    @Override
    public void unreferenced() {
        try {
            unexportObject(this, true);
            this.resultat = null;
            this.partieLancer = false;
            this.reference = false;
        } catch (NoSuchObjectException nsoe) {
            // Should never occurs.
        }
    }
}
