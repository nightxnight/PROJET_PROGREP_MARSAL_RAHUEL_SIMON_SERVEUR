package serveur.modele.gestionnaire;

import serveur.modele.implementation.connexion.joueur.Joueur;

import java.io.*;
import java.time.LocalDate;
import java.util.HashMap;

public class GestionnaireJoueur {

    private static GestionnaireJoueur instance = null;
    public static GestionnaireJoueur getInstance() {
        if (instance == null) instance = new GestionnaireJoueur();
        return instance;
    }

    private HashMap<String, Joueur> mapJoueur;

    private GestionnaireJoueur() {
        try {
            File fichier = new File("./data/joueurs.ser");
            ObjectInputStream in = new ObjectInputStream(new FileInputStream(fichier));
            this.mapJoueur = (HashMap<String, Joueur>) in.readObject();
        } catch (Exception e) {
            this.mapJoueur = new HashMap<>();
        }
    }



    public Joueur enregistrerInscription(String pseudo, String motDePasse, String mail, LocalDate anniv) throws IllegalArgumentException {
        if (mapJoueur.containsKey(pseudo)) throw new IllegalArgumentException("Ce pseudo est deja utilise.");
        Joueur joueur = new Joueur(pseudo, motDePasse, mail, anniv);
        mapJoueur.put(joueur.getPseudo(), joueur);
        sauvegarderChangements();
        return joueur;
    }

    public void enregistrerModification(String pseudo, String ancienMotDePasse, String nouveauMotDePasse, String mail, LocalDate anniv) throws IllegalArgumentException {
        mapJoueur.get(pseudo).modifierInformation(ancienMotDePasse, nouveauMotDePasse, mail, anniv);
        sauvegarderChangements();
    }

    public Joueur autoriseConnexion(String pseudo, String motDePasse) throws IllegalArgumentException {
        if (!mapJoueur.containsKey(pseudo)) throw new IllegalArgumentException("Il n'existe pas de compte avec ce pseudo");
        else if (!mapJoueur.get(pseudo).motDePasseEqual(motDePasse)) throw new IllegalArgumentException("Les informations transmises n'ont pas permis de vous connecter.");
        else return mapJoueur.get(pseudo);
    }

    public Joueur getJoueurFromPseudo(String pseudo) throws IllegalArgumentException {
        if(!mapJoueur.containsKey(pseudo)) throw new IllegalArgumentException("Ce joueur n'existe pas.");
        else return mapJoueur.get(pseudo);
    }

    public HashMap<String, Joueur> getMapJoueur() {
        return mapJoueur;
    }

    public void sauvegarderChangements() {
        try {
            File fichier = new File("./data/joueurs.ser");
            if (!fichier.createNewFile()) fichier.delete();
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(fichier));
            out.writeObject(mapJoueur);
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
