package serveur.modele.implementation.jeux.application.allumettes;

import serveur.modele.implementation.jeux.application.Parametres;

import java.util.HashMap;

public class AllumettesParams extends Parametres {

    private final static int NB_ALLUMETTES_MIN = 8;
    private final static int NB_ALLUMETTES_MAX = 21;

    private int nbAllumettesMaxParTour = 3;

    public AllumettesParams() {
        super(2, 2);
    }

    @Override
    public void changerParametre(String nomParametre, Object valeur) throws IllegalArgumentException, ClassCastException {
        switch(nomParametre) {
            case "nb_allumettes_par_tour" : setNbAllumettesMaxParTour((int) valeur); break;
            default : throw new IllegalArgumentException("Ce parametre n'existe pas");
        }
    }

    @Override
    public HashMap<String, Object> getMapParametres() {
        HashMap<String, Object> mapParametres = new HashMap<String, Object>();
        mapParametres.put("nb_allumettes_par_tour", nbAllumettesMaxParTour);
        return mapParametres;
    }

    public void setNbAllumettesMaxParTour(int nbAllumettesMaxParTour) {
        this.nbAllumettesMaxParTour = nbAllumettesMaxParTour;
    }

    public int getNB_ALLUMETTES_MIN() {
        return NB_ALLUMETTES_MIN;
    }

    public int getNB_ALLUMETTES_MAX() {
        return NB_ALLUMETTES_MAX;
    }

    public int getNbAllumettesMaxParTour() {
        return nbAllumettesMaxParTour;
    }
}
