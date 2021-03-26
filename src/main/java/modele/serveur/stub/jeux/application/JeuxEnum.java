package modele.serveur.stub.jeux.application;

import java.util.ArrayList;

public enum JeuxEnum {

    MORPION("Morpion", "Le morpion se joue a deux, vous devrez aligner plusieurs fois le meme symbole pour gagner."),
    PENDU("Pendu", "Trouvez un mot choisit aleatoirement, mais attention si vous vous trompez trop..."),
    ALLUMETTES("Les allumettes", "Retirez des allumettes chaque tour pour essayer d'en avoir un nombre impaire a la fin!");

    private final String nomJeu;
    private final String description;

    JeuxEnum(String nomJeu, String description) {
        this.nomJeu = nomJeu;
        this.description = description;
    }

    public String getNomJeu() {
        return this.nomJeu;
    }

    public String getDescription() {
        return this.description;
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
