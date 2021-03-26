package modele.implementation.jeux.application.pendu;

import modele.implementation.jeux.application.Parametres;

import java.util.ArrayList;
import java.util.HashMap;

public class PenduParams extends Parametres {

    private HashMap<String, String[]> mapMots;

    private String langueMots = "Francais";
    private boolean erreurActive = true;

    public PenduParams() {
        super(2, 4);
        String[] motsFrancais = {
                "AQUARIUM", "DINOSAURE", "MONARCHIE", "ATELIER", "TABLE", "CHEVAL", "OISEAUX", "GATEAU", "CHOCOLAT",
                "GRILLE PAIN", "MARAIS", "CANAPE", "VOITURE", "RIVIERE", "PERCHOIR", "ENTONNOIR", "BISCOTTE", "COLLIER",
                "DAUPHIN", "POISSON", "ESPACE", "BALEINE", "BOUILLOIRE", "XYLOPHONE", "TROMPETTE", "ASCENSEUR", "CAVERNE" };
        String[] motsAnglais = {
                "LANDSCAPE", "TROUSER", "ROOFTOP", "COOKING", "WINDOW", "BUILDING", "ROOM MATE", "LEMON", "WATERMELON", "EGGPLANT",
                "MIDNIGHT", "ELECTRICITY", "COCONUT", "DINOSAUR", "PICTURE", "LIBRARY", "COMPUTER", "CARTOON", "BLANKET"};
        String[] motsAllemand = {
                "TASTATUR", "KARTOFFEL", "ABNUTZEN", "LASTWAGEN", "VULKAN", "BLUMENTOPF", "WURZEL", "EINKAUFSZENTRUM"};
        this.mapMots = new HashMap<String, String[]>();
        this.mapMots.put("Francais", motsFrancais);
        this.mapMots.put("Anglais", motsAnglais);
        this.mapMots.put("Allemand", motsAllemand);
    }

    @Override
    public void changerParametre(String nomParametre, Object valeur) throws IllegalArgumentException, ClassCastException {
        switch(nomParametre) {
            case "langue" : setLangueMots((String) valeur); break;
            case "erreur_active" : setErreurActive((boolean) valeur); break;
            default : throw new IllegalArgumentException("Ce parametre n'existe pas");
        }
    }

    @Override
    public HashMap<String, Object> getMapParametres() {
        HashMap<String, Object> mapParametres = new HashMap<String, Object>();
        mapParametres.put("langue", this.langueMots);
        mapParametres.put("erreur_active", this.erreurActive);
        return mapParametres;
    }

    public void setLangueMots(String langueMots) throws IllegalArgumentException {
        if(!mapMots.containsKey(langueMots)) throw new IllegalArgumentException("Parametre de langue illegal");
        this.langueMots = langueMots;
    }

    public String getLangueMots() {
        return langueMots;
    }

    public void setErreurActive(boolean erreurActive) {
        this.erreurActive = erreurActive;
    }

    public boolean isErreurActive() {
        return erreurActive;
    }

    public HashMap<String, String[]> getMapMots() {
        return mapMots;
    }
}
