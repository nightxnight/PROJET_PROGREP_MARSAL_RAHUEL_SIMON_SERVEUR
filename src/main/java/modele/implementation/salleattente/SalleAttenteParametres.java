package modele.implementation.salleattente;

import modele.serveur.stub.jeux.application.JeuxEnum;
import modele.implementation.jeux.application.Parametres;
import utils.Paire;

import java.util.HashMap;

/*
 * Classe permettant de stocker les parametres d'une salle d'attente
 */
public class SalleAttenteParametres {

    private SalleAttente salleAttente;

    private final int JOUEUR_MIN_SALLE = 2;
    private final int JOUEUR_MAX_SALLE = 10;

    private String motDePasse;
    private int nombreJoueurSalle;
    private boolean publique;
    private boolean chat;
    private Paire<JeuxEnum, Parametres> jeu;

    public SalleAttenteParametres(SalleAttente salleAttente, String motDePasse, boolean publique) {
        this.salleAttente = salleAttente;
        this.publique = publique;
        this.motDePasse = motDePasse;
        this.nombreJoueurSalle = 5;
        this.chat = true;
        this.jeu = new Paire<JeuxEnum, Parametres>(JeuxEnum.MORPION, Parametres.getParametres(JeuxEnum.MORPION));;
    }

    public void changerParametreSalle(String nom, Object valeur) throws ClassCastException {
        switch(nom) {
            case "nombre_joueur" :
                setNombreJoueurSalle((int) valeur);
                break;
            case "publique" :
                this.publique = (boolean) valeur;
                break;
            case "chat" :
                this.chat = (boolean) valeur;
                break;
            case "mot_de_passe" :
                this.motDePasse = (String) valeur;
                break;
            case "jeu" :
                if (((String) valeur).equals(this.jeu.getPremier().getNomJeu())) return;
                else setJeu((String) valeur); break;
            default :
                throw new IllegalArgumentException("Ce parametre n'exite pas");
        }
    }

    public HashMap<String, Object> getParametresSalle() {
        HashMap<String, Object> mapParametresSalle = new HashMap<String, Object>();
        mapParametresSalle.put("nombre_joueur", this.nombreJoueurSalle);
        mapParametresSalle.put("mot_de_passe", (this.motDePasse == null) ? "" : this.motDePasse);
        mapParametresSalle.put("chat", this.chat);
        mapParametresSalle.put("publique", this.publique);
        mapParametresSalle.put("jeu", this.jeu.getPremier().getNomJeu());
        mapParametresSalle.put("pret", salleAttente.verifierNombreJoueur(salleAttente.getMapJoueurs().size()));
        return mapParametresSalle;
    }

    public void changerParametreJeu(String nom, Object valeur) throws ClassCastException, IllegalArgumentException {
        this.jeu.getSecond().changerParametre(nom, valeur);
    }

    public HashMap<String, Object> getParametresJeu() {
        return jeu.getSecond().getMapParametres();
    }

    /*
     * Getters et setters
     */

    public void setMotDePasse(String motDePasse) {
        this.motDePasse = motDePasse;
    }

    public String getMotDePasse() {
        return motDePasse;
    }

    public void setPublique(boolean publique) {
        this.publique = publique;
    }

    public boolean isPublique() {
        return publique;
    }

    public void setChat(boolean chat) {
        this.chat = chat;
    }

    public boolean isChat() {
        return chat;
    }

    public Paire<JeuxEnum, Parametres> getJeu() {
        return jeu;
    }

    private void setJeu(String nomJeu) {
        JeuxEnum jeu = JeuxEnum.fromNomJeu(nomJeu);
        this.jeu = new Paire<JeuxEnum, Parametres>(jeu, Parametres.getParametres(jeu));
    }

    private void setNombreJoueurSalle(int nombre_joueur_salle) throws IllegalArgumentException {
        if(nombre_joueur_salle < JOUEUR_MIN_SALLE || JOUEUR_MAX_SALLE < nombre_joueur_salle)
            throw new IllegalArgumentException("Une salle d'attente ne peut pas contenir ce nombre de joueur.");
        if(nombre_joueur_salle < salleAttente.getMapJoueurs().size())
            throw new IllegalArgumentException("Vous devez exclure certains joueurs afin de reduire la taille.");
        this.nombreJoueurSalle = nombre_joueur_salle;
    }
    public int getNombreJoueurSalle() {
        return nombreJoueurSalle;
    }

}
