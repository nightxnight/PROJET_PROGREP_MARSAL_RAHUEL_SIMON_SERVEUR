package serveur.modele.gestionnaire;

import serveur.modele.client.stub.salleattente.ListenerSalleAttenteIF;
import serveur.modele.implementation.salleattente.SalleAttente;

import java.rmi.RemoteException;
import java.util.HashMap;

public class GestionnaireSalleAttente {

    /*
     * Singleton Design Pattern
     */
    private static GestionnaireSalleAttente instance = null;
    public static GestionnaireSalleAttente getInstance() {
        if (instance == null) instance = new GestionnaireSalleAttente();
        return instance;
    }

    private HashMap<String, SalleAttente> mapSalleAttente;

    private GestionnaireSalleAttente() {
        this.mapSalleAttente = new HashMap<String, SalleAttente>();
    }

    public SalleAttente enregistrerSalleAttente(String pseudoProprietaire, String nomSalle, String motDePasse, boolean publique)
            throws RemoteException, IllegalArgumentException, IllegalAccessException {
        if(nomSalle.equals("")) throw new IllegalArgumentException("Le nom de la salle doit etre preciser");
        if(mapSalleAttente.containsKey(nomSalle)) throw new IllegalArgumentException("Une salle de ce nom existe deja.");
        SalleAttente salleAttente = new SalleAttente(pseudoProprietaire, nomSalle, motDePasse, publique);
        mapSalleAttente.put(nomSalle, salleAttente);
        return salleAttente;
    }

    public SalleAttente rejoindreSalleAttente(String pseudo, ListenerSalleAttenteIF clientListener, String nomSalle, String motDePasse) throws RemoteException, IllegalArgumentException {
        return mapSalleAttente.get(nomSalle).entrer(pseudo, clientListener, motDePasse);
    }

    public void detruireSalleAttente(String nomSalle) {
        mapSalleAttente.remove(nomSalle);
    }

    public HashMap<String, SalleAttente> getMapSalleAttente() {
        return mapSalleAttente;
    }
}
