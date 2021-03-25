package modele.implementation.salleattente.connecteur;

import modele.client.stub.salleattente.ListenerSalleAttenteIF;
import modele.gestionnaire.GestionnaireSalleAttente;
import modele.serveur.stub.salleattente.SalleAttenteIF;
import modele.serveur.stub.salleattente.SalleAttenteProprietaireIF;
import modele.implementation.salleattente.SalleAttenteProxy;
import modele.serveur.stub.salleattente.connecteur.ConnecteurSalleAttenteIF;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class ConnecteurSalleAttente extends UnicastRemoteObject implements ConnecteurSalleAttenteIF {

    /*
     * Singleton Design Pattern
     */
    private static ConnecteurSalleAttente instance = null;
    public static ConnecteurSalleAttente getInstance() throws RemoteException {
        if (instance == null) instance = new ConnecteurSalleAttente();
        return instance;
    }

    private ConnecteurSalleAttente() throws RemoteException {
        super();
    }

    @Override
    public SalleAttenteProprietaireIF creer(String pseudoProprietaire,  String nomSalle, String motDePasse, boolean publique) throws RemoteException, IllegalArgumentException, IllegalAccessException {
      return (SalleAttenteProprietaireIF) GestionnaireSalleAttente.getInstance().enregistrerSalleAttente(pseudoProprietaire, nomSalle, motDePasse, publique);

    }

    @Override
    public SalleAttenteIF rejoindre(String pseudoPropietaire, ListenerSalleAttenteIF clientListener, String nomSalle, String motDePasseSalle) throws RemoteException, IllegalArgumentException {
        return (SalleAttenteIF) GestionnaireSalleAttente.getInstance().rejoindreSalleAttente(pseudoPropietaire, clientListener, nomSalle, motDePasseSalle);
    }

    @Override
    public ArrayList<SalleAttenteProxy> getListeSallesAttentes() throws RemoteException {
        ArrayList<SalleAttenteProxy> listeSallesAttentes = new ArrayList<SalleAttenteProxy>();
        for(String nomSalle : GestionnaireSalleAttente.getInstance().getMapSalleAttente().keySet()) {
            listeSallesAttentes.add(new SalleAttenteProxy(GestionnaireSalleAttente.getInstance().getMapSalleAttente().get(nomSalle)));
        }
        return listeSallesAttentes.stream().filter(SalleAttenteProxy::isPublique).collect(Collectors.toCollection(ArrayList::new));
    }

    @Override
    public ArrayList<SalleAttenteProxy> rechercher(String nomJeu, boolean masquerPleine, boolean masquerMdp) throws RemoteException {
        ArrayList<SalleAttenteProxy> listeSallesAttentes = getListeSallesAttentes();
        if(masquerMdp)
            listeSallesAttentes = listeSallesAttentes.stream().filter(salle -> !salle.isBesoinMdp()).collect(Collectors.toCollection(ArrayList::new));
        if(masquerPleine)
            listeSallesAttentes = listeSallesAttentes.stream().filter(salle -> salle.getNbJoueursConnecte() != salle.getNbJoueursMax()).collect(Collectors.toCollection(ArrayList::new));
        if(!nomJeu.equals("Non selectionne"))
            listeSallesAttentes = listeSallesAttentes.stream().filter(salle -> salle.getNomJeu().equals(nomJeu)).collect(Collectors.toCollection(ArrayList::new));
        return listeSallesAttentes;
    }
}
