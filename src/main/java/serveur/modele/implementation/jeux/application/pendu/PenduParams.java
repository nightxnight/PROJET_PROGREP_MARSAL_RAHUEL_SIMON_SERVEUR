package serveur.modele.implementation.jeux.application.pendu;

import serveur.modele.implementation.jeux.application.Parametres;

import java.util.HashMap;

public class PenduParams extends Parametres {

    private String langueMots = "Francais";
    private boolean erreurActive = true;

    public PenduParams() {
        super(2, 4);
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

    public void setLangueMots(String langueMots) {
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
}
