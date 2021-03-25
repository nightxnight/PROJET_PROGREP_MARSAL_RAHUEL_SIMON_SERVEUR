package modele.serveur.stub.jeux.application;

import java.util.ArrayList;

public enum JeuxEnum {

    MORPION("Morpion"),
    PENDU("Pendu"),
    ALLUMETTES("Les allumettes");

    private final String nomJeu;

    JeuxEnum(String nomJeu) {
        this.nomJeu = nomJeu;
    }

    public String getNomJeu() {
        return this.nomJeu;
    }

    public static ArrayList<JeuxEnum> getListeJeu() {
        ArrayList<JeuxEnum> listeJeu = new ArrayList<JeuxEnum>();
        listeJeu.add(MORPION);
        listeJeu.add(PENDU);
        listeJeu.add(ALLUMETTES);
        return listeJeu;
    }

    public static JeuxEnum fromNomJeu(String nomJeu) {
        switch(nomJeu) {
            case "Morpion" : return MORPION;
            case "Pendu" : return PENDU;
            case "Les allumettes" : return ALLUMETTES;
            default: throw new IllegalArgumentException("Ce jeu n'existe pas");
        }
    }
}
