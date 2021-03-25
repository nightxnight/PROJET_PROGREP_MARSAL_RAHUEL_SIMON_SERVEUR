package modele.implementation.connexion.joueur;

import modele.implementation.amis.chat.Message;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;

public class Joueur implements Serializable {

    private String pseudo;
    private String mail;
    private String anniversaire;
    private String inscription;
    private String motDePasse;

    private HashMap<Joueur, ArrayList<Message>> conversations;
    private HashMap<String, Joueur> amis;
    private HashMap<String, Joueur> demandeAmis;

    private final static transient DateTimeFormatter formateur = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public Joueur(String pseudo, String motDePasse, String mail, LocalDate anniversaire) {
        this.pseudo = pseudo;
        this.motDePasse = motDePasse;
        this.mail = mail;
        this.anniversaire = formateur.format(anniversaire);
        this.inscription = formateur.format(LocalDate.now());
        this.amis = new HashMap<String, Joueur>();
        this.demandeAmis = new HashMap<String, Joueur>();
        this.conversations = new HashMap<Joueur, ArrayList<Message>>();
    }

    public void modifierInformation(String ancienMotDePasse, String nouveauMotDePasse, String mail, LocalDate anniv) {
        if(!this.motDePasseEqual(ancienMotDePasse)) throw new IllegalArgumentException("L'ancien mot de passe n'est pas valide.");
        else {
            this.motDePasse = nouveauMotDePasse;
            this.mail = mail;
            this.anniversaire = formateur.format(anniv);
        }
    }

    public boolean motDePasseEqual(String motDePasse) {
        return this.motDePasse.equals(motDePasse);
    }

    public String getPseudo() {
        return pseudo;
    }

    public String getAnniversaire() {
        return anniversaire;
    }

    public String getMail() {
        return mail;
    }

    public String getInscription() {
        return inscription;
    }

    /*
     * Amis
     */

    public void ajoutDemandeAmis(Joueur joueur) throws IllegalArgumentException {
        if(amis.containsKey(joueur.getPseudo())) throw new IllegalArgumentException("Vous etes deja amis avec " + this.pseudo + ".");
        else if (demandeAmis.containsKey(joueur.getPseudo())) throw new IllegalArgumentException("Vous avez deja demande " + this.pseudo + " en amis.");
        else if(joueur.getDemandeAmis().containsKey(this.pseudo)) accepterDemandeAmis(joueur, true);
        demandeAmis.put(joueur.getPseudo(), joueur);
    }

    public void accepterDemandeAmis(Joueur joueur, boolean accepte) {
        if(!demandeAmis.containsKey(joueur.getPseudo())) throw new IllegalArgumentException(joueur.getPseudo() + " ne vous a pas demande en amis");
        else if(!accepte) this.demandeAmis.remove(joueur.getPseudo());
        else if (amis.containsKey(joueur.getPseudo())) {
            this.demandeAmis.remove(joueur.getPseudo());
            throw new IllegalArgumentException("Vous etes deja amis avec " + joueur.getPseudo());
        }
        amis.put(joueur.getPseudo(), joueur);
        joueur.getAmis().put(this.pseudo, this);
        demandeAmis.remove(joueur.getPseudo());
    }

    public void supprimerAmis(Joueur joueur) {
        if(!amis.containsKey(joueur.getPseudo())) throw new IllegalArgumentException("Vous n'etes pas amis avec ce joueur.");
        else {
            this.amis.remove(joueur.getPseudo());
            joueur.getAmis().remove(this.pseudo);
        }
    }

    public void enregistrerMessage(String pseudoAvec, Message message) throws IllegalArgumentException {
        if(!amis.containsKey(pseudoAvec)) throw new IllegalArgumentException("Ce joueur ne fait pas partie de vos amis.");
        else if (!conversations.containsKey(amis.get(pseudoAvec))) conversations.put(amis.get(pseudoAvec), new ArrayList<Message>());
        conversations.get(amis.get(pseudoAvec)).add(message);
    }

    public HashMap<String, Joueur> getAmis() {
        return amis;
    }

    public HashMap<String, Joueur> getDemandeAmis() {
        return demandeAmis;
    }

    public HashMap<Joueur, ArrayList<Message>> getConversations() {
        return conversations;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Joueur joueur = (Joueur) o;
        return getPseudo().equals(joueur.getPseudo());
    }
}
