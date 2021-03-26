package modele.implementation.amis;

import modele.client.stub.amis.AmisListenerIF;
import modele.gestionnaire.GestionnaireSession;
import modele.implementation.amis.chat.InvitationMessage;
import modele.implementation.amis.chat.SimpleMessage;
import modele.implementation.connexion.joueur.Joueur;
import modele.implementation.connexion.joueur.JoueurProxy;
import modele.implementation.amis.chat.Message;
import modele.gestionnaire.GestionnaireJoueur;
import modele.serveur.stub.amis.PortailAmisIF;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;

public class PortailAmis extends UnicastRemoteObject implements PortailAmisIF {

    private static PortailAmis instance = null;
    public static PortailAmis getInstance() throws RemoteException {
        if (instance == null) instance = new PortailAmis();
        return instance;
    }

    private HashMap<String, AmisListenerIF> mapJoueur;

    /*
     * Le portail des amis c'est l'interface
     * qui permet aux clients d'interargir avec
     * toute la partie amis de l'application
     */
    private PortailAmis() throws RemoteException {
        super();
        this.mapJoueur = new HashMap<String, AmisListenerIF>();
    }

    public void seConnecter(String pseudo, AmisListenerIF listener) throws IllegalArgumentException {
        if (mapJoueur.containsKey(pseudo)) throw new IllegalArgumentException("Erreur vous etes deja connecte");
        else {
            mapJoueur.put(pseudo, listener);
            HashMap<String, Joueur> mapAmi = GestionnaireJoueur.getInstance().getJoueurFromPseudo(pseudo).getAmis();
            for(String pseudoAmi : mapAmi.keySet()) {
                if (mapJoueur.containsKey(pseudoAmi))
                    try { mapJoueur.get(pseudoAmi).actualiserEtat(pseudo, true); } catch (RemoteException re) { /* ne rien faire */ }
            }
        }
    }

    public void seDeconnecter(String pseudo) throws IllegalArgumentException {
        if (!mapJoueur.containsKey(pseudo)) throw new IllegalArgumentException("Vous n'etes pas connecte");
        else {
            mapJoueur.remove(pseudo);
            HashMap<String, Joueur> mapAmi = GestionnaireJoueur.getInstance().getJoueurFromPseudo(pseudo).getAmis();
            for(String pseudoAmi : mapAmi.keySet()) {
                if (mapJoueur.containsKey(pseudoAmi))
                    try { mapJoueur.get(pseudoAmi).actualiserEtat(pseudo, false); } catch (RemoteException re) { /* ne rien faire */ }
            }
        }
    }

    @Override
    public void demandeAmis(String pseudo, String pseudoDemande) throws RemoteException, IllegalArgumentException {
        if (pseudo.equals(pseudoDemande)) throw new IllegalArgumentException("Vous ne pouvez pas vous ajouter vous meme en ami.");
        GestionnaireJoueur.getInstance().getJoueurFromPseudo(pseudoDemande).ajoutDemandeAmis(GestionnaireJoueur.getInstance().getJoueurFromPseudo(pseudo));
        if(mapJoueur.containsKey(pseudoDemande)) {
            try { mapJoueur.get(pseudoDemande).recupererDemande(new JoueurProxy(pseudo, true));} catch (RemoteException re) { /* ne rien faire */ }
        }
        GestionnaireJoueur.getInstance().sauvegarderChangements();
    }

    @Override
    public void accepterDemande(String pseudo, String pseudoAccepte, boolean accepter) throws RemoteException, IllegalArgumentException {
        GestionnaireJoueur.getInstance().getJoueurFromPseudo(pseudo).accepterDemandeAmis(GestionnaireJoueur.getInstance().getJoueurFromPseudo(pseudoAccepte), accepter);
        if(!accepter) return;
        try { mapJoueur.get(pseudo).recupererAmis(new JoueurProxy(pseudoAccepte, true)); } catch (RemoteException re) { /* ne rien faire */ }
        if(mapJoueur.containsKey(pseudoAccepte))
            try { mapJoueur.get(pseudoAccepte).recupererAmis(new JoueurProxy(pseudo, true)); } catch (RemoteException re) { /* ne rien faire */ }
        GestionnaireJoueur.getInstance().sauvegarderChangements();
    }

    @Override
    public void supprimerAmis(String pseudo, String pseudoSupprime) throws RemoteException, IllegalArgumentException {
        GestionnaireJoueur.getInstance().getJoueurFromPseudo(pseudo).supprimerAmis(GestionnaireJoueur.getInstance().getJoueurFromPseudo(pseudoSupprime));
        try { mapJoueur.get(pseudo).supprimerAmis(new JoueurProxy(pseudoSupprime, true)); } catch (RemoteException re) { /* ne rien faire */ }
        if(mapJoueur.containsKey(pseudoSupprime))
            try { mapJoueur.get(pseudoSupprime).supprimerAmis(new JoueurProxy(pseudo, true)); } catch (RemoteException re) { /* ne rien faire */ }
        GestionnaireJoueur.getInstance().sauvegarderChangements();
    }

    @Override
    public ArrayList<JoueurProxy> getListeAmis(String pseudo) throws RemoteException, IllegalArgumentException {
        HashMap<String, Joueur> mapAmis = GestionnaireJoueur.getInstance().getJoueurFromPseudo(pseudo).getAmis();
        ArrayList<JoueurProxy> listeAmis = new ArrayList<JoueurProxy>();
        for(String pseudoAmi : mapAmis.keySet())
            listeAmis.add(new JoueurProxy(pseudoAmi, GestionnaireSession.getInstance().isSessionEnLigne(pseudoAmi)));
        return listeAmis;
    }

    @Override
    public ArrayList<JoueurProxy> getListeDemande(String pseudo) throws RemoteException, IllegalArgumentException {
        HashMap<String, Joueur> mapDemande = GestionnaireJoueur.getInstance().getJoueurFromPseudo(pseudo).getDemandeAmis();
        ArrayList<JoueurProxy> listeDemande = new ArrayList<JoueurProxy>();
        for(String pseudoAmi : mapDemande.keySet())
            listeDemande.add(new JoueurProxy(pseudoAmi, false));
        return listeDemande;
    }

    @Override
    public ArrayList<Message> getConversation(String pseudoDemandeur, String pseudoConversation) throws RemoteException, IllegalArgumentException {
        if(!GestionnaireJoueur.getInstance().getJoueurFromPseudo(pseudoDemandeur).getAmis().containsKey(pseudoConversation))
            throw new IllegalArgumentException("Vous n'etes pas amis avec ce joueur.");
        else {
            if(!GestionnaireJoueur.getInstance().getJoueurFromPseudo(pseudoDemandeur).getConversations()
                    .containsKey(GestionnaireJoueur.getInstance().getJoueurFromPseudo(pseudoConversation))) {
                return new ArrayList<Message>();
            } else {
                return GestionnaireJoueur.getInstance().getJoueurFromPseudo(pseudoDemandeur).getConversations().get(GestionnaireJoueur.getInstance().getJoueurFromPseudo(pseudoConversation));
            }
        }
    }

    @Override
    public void envoyerMessage(String de, String pour, SimpleMessage message) throws RemoteException, IllegalArgumentException {
        if(!GestionnaireJoueur.getInstance().getJoueurFromPseudo(pour).getAmis().containsKey(de))
            throw new IllegalArgumentException("Vous n'etes pas amis avec ce joueur");
        else {
            GestionnaireJoueur.getInstance().getJoueurFromPseudo(de).enregistrerMessage(pour, message);
            GestionnaireJoueur.getInstance().getJoueurFromPseudo(pour).enregistrerMessage(de, message);
            GestionnaireJoueur.getInstance().sauvegarderChangements();
            try { mapJoueur.get(de).recupererMessage(pour, message);} catch (RemoteException re) { /* ne rien faire */ }
            if(mapJoueur.containsKey(pour))
                try { mapJoueur.get(pour).recupererMessage(de, message);} catch (RemoteException re) { /* ne rien faire */}
        }
    }

    public void envoyerInvitationSalle(String de, String pour, String nomSalle, String motDePasse) throws IllegalArgumentException {
        if(!GestionnaireJoueur.getInstance().getJoueurFromPseudo(de).getAmis().containsKey(pour))
            throw new IllegalArgumentException("Vous n'etes pas amis avec ce joueur");
        else if(!mapJoueur.containsKey(pour))
            throw new IllegalArgumentException("Ce joueur n'est pas en ligne");
        else {
            InvitationMessage invitation = new InvitationMessage(de, pour, nomSalle, motDePasse);
            try { mapJoueur.get(pour).recupererMessage(de, invitation); } catch (RemoteException re) { /* ne rien faire */ }
        }

    }
}
