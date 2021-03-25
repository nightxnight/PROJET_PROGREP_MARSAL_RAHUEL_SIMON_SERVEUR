package serveur.modele.implementation.jeux.application.allumettes;

import serveur.modele.client.stub.jeux.JeuxListenerIF;
import serveur.modele.client.stub.jeux.allumettes.AllumettesListenerIF;
import serveur.modele.implementation.jeux.application.Application;
import serveur.modele.stub.jeux.application.JeuxIF;
import serveur.modele.implementation.jeux.application.ResultatPartieEnum;
import serveur.modele.stub.jeux.application.allumettes.AllumettesIF;
import utils.ListeOrdonnee;
import utils.Paire;

import java.rmi.NoSuchObjectException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.stream.Collectors;

public class AllumettesApp extends Application implements AllumettesIF {

    private ListeOrdonnee<Paire<String, AllumettesListenerIF>> listeJoueurs;
    private HashMap<String, Integer> mapAllumettesPrisent;

    private final AllumettesParams parametres;
    private final int nbAllumettesInitial;
    private int nbAllumettes;

    public AllumettesApp(AllumettesParams parametres) throws RemoteException {
        super();
        this.parametres = parametres;
        this.listeJoueurs = new ListeOrdonnee<Paire<String, AllumettesListenerIF>>();

        Random random = new Random();
        this.nbAllumettesInitial = random.nextInt(parametres.getNB_ALLUMETTES_MAX() - parametres.getNB_ALLUMETTES_MIN() + 1)
                + parametres.getNB_ALLUMETTES_MAX();
        this.nbAllumettes = nbAllumettesInitial;

        this.mapAllumettesPrisent = new HashMap<String, Integer>();
    }

    @Override
    public JeuxIF rejoindrePartie(String pseudo, JeuxListenerIF clientListener) throws RemoteException {
        listeJoueurs.getListe().add(new Paire<String, AllumettesListenerIF>(
                pseudo,
                (AllumettesListenerIF) clientListener));
        mapAllumettesPrisent.put(pseudo, 0);
        return (AllumettesIF) this;
    }

    @Override
    public void lancerPartie() throws RemoteException {
        for(Paire<String, AllumettesListenerIF> pairePseudoListener : listeJoueurs.getListe()) {
            pairePseudoListener.getSecond().recupererParametres(nbAllumettesInitial, parametres.getNbAllumettesMaxParTour());
            pairePseudoListener.getSecond().lancerJeu();
        }
        this.partieLancer = true;
        new Thread(this).start();
        listeJoueurs.premier().getSecond().faireJouer();
    }


    @Override
    public void run() {
        while(partieLancer) {
            try { autokick(); } catch (Exception e) { /* Ne peut pas arriver, appel interne */ }
        }
        if(resultat == null) return;

        if(resultat == ResultatPartieEnum.GAGNANT_PAR_FORFAIT) {
            for(Paire<String, AllumettesListenerIF> pairePseudoListener : listeJoueurs.getListe()) {
                try {
                    pairePseudoListener.getSecond().arreterJeu(ResultatPartieEnum.GAGNANT_PAR_FORFAIT, "vous avez gagne par forfait.");
                } catch (RemoteException re) {
                    System.out.println(re.getMessage());
                }
            }
        } else {
            ResultatPartieEnum resultat;
            ArrayList<String> pseudosGagnant = new ArrayList<String>();
            if(
                    mapAllumettesPrisent.keySet().stream().allMatch(pseudo -> (mapAllumettesPrisent.get(pseudo) % 2 == 1))
                            ||
                            mapAllumettesPrisent.keySet().stream().allMatch(pseudo -> (mapAllumettesPrisent.get(pseudo) % 2 == 0))
            ) {
                resultat = ResultatPartieEnum.EGALITE;
            }
            else {
                resultat = ResultatPartieEnum.GAGNE;
                pseudosGagnant = mapAllumettesPrisent.keySet().stream().filter(pseudo -> (mapAllumettesPrisent.get(pseudo) % 2 == 1)).collect(Collectors.toCollection(ArrayList::new));
            }

            for(Paire<String, AllumettesListenerIF> pairePseudoListener : listeJoueurs.getListe()) {
                ResultatPartieEnum resultatJoueur;
                String message;
                if(resultat == ResultatPartieEnum.EGALITE) {
                    resultatJoueur = resultat;
                    message = "Egalite! Tous le monde a un nombre d'allumettes paire ou impaire.";
                } else if (pseudosGagnant.contains(pairePseudoListener.getPremier())) {
                    resultatJoueur = ResultatPartieEnum.GAGNE;
                    message = "Bravo! Ton nombre d'allumettes est impaire!";
                } else {
                    resultatJoueur = ResultatPartieEnum.PERDU;
                    message = "Dommage, tu as un nombre d'allumettes paire.";
                }
                try { pairePseudoListener.getSecond().arreterJeu(resultatJoueur, message); } catch (RemoteException re) { System.out.println(re.getMessage()); }
            }
        }
        unreferenced();
    }

    private synchronized void autokick() throws RemoteException {
        for (Paire<String, AllumettesListenerIF> pairePseudoListener : listeJoueurs.getListe()) {
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
    public void prendreAllumettes(String pseudo, int nombre) throws RemoteException, IllegalArgumentException {
        if (!partieLancer) return;

        if(!listeJoueurs.elementCourant().getPremier().equals(pseudo))
            throw new IllegalArgumentException("Ce n'est pas a ce joueur de jouer.");
        else if(this.nbAllumettes < nombre) throw new IllegalArgumentException("Il ne reste plus assez d'allumettes.");
        else {
            this.dernierePersonneJouee = pseudo;

            mapAllumettesPrisent.put(pseudo, mapAllumettesPrisent.get(pseudo) + nombre);
            boolean plusAllumettes = plusAllumettesRestante(this.nbAllumettes -= nombre);

            if(plusAllumettes) {
                resultat = ResultatPartieEnum.GAGNE;
                partieLancer = false;
            } else {
                if(this.nbAllumettes < this.parametres.getNbAllumettesMaxParTour()) {
                    this.parametres.setNbAllumettesMaxParTour(this.nbAllumettes);
                    for(Paire<String, AllumettesListenerIF> pairePseudoListener : listeJoueurs.getListe())
                        pairePseudoListener.getSecond().actualiserNbMaxAllumettesTour(parametres.getNbAllumettesMaxParTour());
                }

                for (Paire<String, AllumettesListenerIF> pairePseudoListener : listeJoueurs.getListe()) {
                    pairePseudoListener.getSecond().retirerAllumettes(pseudo, nombre, pseudo + " a retire " + nombre + " allumette(s)");
                }
                listeJoueurs.suivant().getSecond().faireJouer();
            }
        }
    }

    private boolean plusAllumettesRestante(int nombre) {
        return nombre <= 0;
    }

    @Override
    public void unreferenced() {
        try {
            unexportObject(this, true);
            this.resultat = null;
            this.partieLancer = false;
            this.reference = false;
        } catch (NoSuchObjectException nsoe) {
            // Should never occur
        }
    }
}
