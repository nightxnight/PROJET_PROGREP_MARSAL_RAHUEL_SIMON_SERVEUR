package modele.implementation.jeux.application.morpion;

import modele.client.stub.jeux.JeuxListenerIF;
import modele.client.stub.jeux.morpion.MorpionListenerIF;
import modele.implementation.jeux.application.Application;
import modele.serveur.stub.jeux.application.JeuxIF;
import modele.serveur.stub.jeux.application.ResultatPartieEnum;
import modele.serveur.stub.jeux.application.morpion.MorpionIF;
import utils.ListeOrdonnee;
import utils.Paire;

import java.rmi.NoSuchObjectException;
import java.rmi.RemoteException;

public class MorpionApp extends Application implements MorpionIF {

    private ListeOrdonnee<Paire<String, MorpionListenerIF>> listeJoueurs;

    private final MorpionParams parametres;
    private final char[] SYMBOLE = {'X', 'O'};
    private int tour = 0;
    private int maxTour;
    private char[][] tableau;

    public MorpionApp(MorpionParams parametres) throws RemoteException, IllegalArgumentException {
        super();
        this.listeJoueurs = new ListeOrdonnee<Paire<String, MorpionListenerIF>>();
        this.parametres = parametres;
        this.tableau = new char[this.parametres.getTailleTableau()][this.parametres.getTailleTableau()];
        for(int i = 0; i < this.parametres.getTailleTableau(); i++)
            for(int j = 0; j < this.parametres.getTailleTableau(); j++)
                tableau[i][j] = '_';
        this.maxTour = parametres.getTailleTableau() * parametres.getTailleTableau();
    }

    @Override
    public void run() {
        while (partieLancer) {
            try { autokick(); } catch (Exception e) { /* Ne peut pas arriver, appel interne */}
        }
        if(resultat == null) return;

        if(resultat == ResultatPartieEnum.PERDU) {
            for(Paire<String, MorpionListenerIF> pairePseudoListener : listeJoueurs.getListe())
                try {
                pairePseudoListener.getSecond().arreterJeu(ResultatPartieEnum.PERDU, "Personne n'a gagne, il n'y a plus de case libre.");
                } catch (Exception e) {
                    // Ne rien faire
                }
            return;
        }

        for (Paire<String, MorpionListenerIF> pairePseudoListener : listeJoueurs.getListe()) {
            try {
                String message = (resultat == ResultatPartieEnum.GAGNANT_PAR_FORFAIT) ? "vous avez gagne par forfait." : dernierePersonneJouee + " a gagne la partie!";
                pairePseudoListener.getSecond().arreterJeu((dernierePersonneJouee.equals(pairePseudoListener.getPremier())) ? ResultatPartieEnum.GAGNE : ResultatPartieEnum.PERDU, message);
            } catch (Exception e) {
                // should not occur.
            }
        }
        this.reference = false;
    }

    private synchronized void autokick() throws RemoteException {
        for (Paire<String, MorpionListenerIF> pairePseudoListener : listeJoueurs.getListe()) {
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
    public JeuxIF rejoindrePartie(String pseudo, JeuxListenerIF clientListener) throws RemoteException {
        listeJoueurs.getListe().add(new Paire<String, MorpionListenerIF>(
                pseudo,
                (MorpionListenerIF) clientListener));
        return (MorpionIF) this;
    }

    @Override
    public void lancerPartie() throws RemoteException {
        for(int i = 0; i < listeJoueurs.getListe().size(); i++) {
            listeJoueurs.getListe().get(i).getSecond().recupererParametres(parametres.getTailleTableau(), SYMBOLE[i]);
            listeJoueurs.getListe().get(i).getSecond().lancerJeu();
        }

        this.partieLancer = true;
        new Thread(this).start();
        listeJoueurs.premier().getSecond().faireJouer();
    }



    @Override
    public void bloquerCase(String pseudo, int ligne, int colonne, char symbole) throws RemoteException, IllegalArgumentException {
        if (!partieLancer) return;

        if(!listeJoueurs.elementCourant().getPremier().equals(pseudo))
            throw new IllegalArgumentException("Ce n'est pas au tour de ce joueur de jouer.");
        else if(tableau[ligne][colonne] != '_') throw new IllegalArgumentException("Cette case est deja occupe");
        else {
            this.dernierePersonneJouee = pseudo;
            this.tour += 1;

            tableau[ligne][colonne] = symbole;

            for (Paire<String, MorpionListenerIF> pairePseudoListener : listeJoueurs.getListe())
                pairePseudoListener.getSecond().recupererCaseBloque(ligne, colonne, symbole);

            if(verifierTableau())  {
                resultat = ResultatPartieEnum.GAGNE;
                partieLancer = false;
            } else {
                if(tour == maxTour) {
                    resultat = ResultatPartieEnum.PERDU;
                    partieLancer = false;
                } else {
                    listeJoueurs.suivant().getSecond().faireJouer();
                }
            }
        }
    }

    private boolean verifierTableau() {
        char premier;
        char res;

        for(int i = 0; i < parametres.getTailleTableau(); i++) {
            premier = tableau[i][0];
            res = tableau[i][0];
            for(int j = 1; j < parametres.getTailleTableau(); j++) {
                res = (tableau[i][j] != premier) ? tableau[i][j] : res;
            }
            if(premier == res && premier != '_') return true;
        }

        for(int j = 0; j < parametres.getTailleTableau(); j++) {
            premier  = tableau[0][j];
            res = tableau[0][j];
            for(int i = 1; i < parametres.getTailleTableau(); i++) {
                res = (tableau[i][j] != premier) ? tableau[i][j] : res;
            }
            if(premier == res && premier != '_') return true;
        }

        premier = tableau[0][0];
        res = tableau[0][0];
        for(int i = 1; i < parametres.getTailleTableau(); i++) {
            res = (tableau[i][i] != premier) ? tableau[i][i] : res;
        }
        if(premier == res && premier != '_') return true;

        premier = tableau[parametres.getTailleTableau()-1][0];
        res = tableau[parametres.getTailleTableau()-1][0];
        for(int i = parametres.getTailleTableau() - 2; 0 <= i; i--) {
            res = (tableau[i][(parametres.getTailleTableau() - 1) - i] != premier) ? tableau[i][(parametres.getTailleTableau() - 1) - i] : res;
        }
        return premier == res && premier != '_';
    }

    @Override
    public void unreferenced() {
        try {
            unexportObject(this, true);
        } catch (NoSuchObjectException nsoe) {
            // Should never occur.
        }
    }
}
