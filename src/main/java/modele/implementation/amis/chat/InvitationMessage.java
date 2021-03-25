package modele.implementation.amis.chat;

public class InvitationMessage extends Message{

    private final String nomSalle;
    private final String motDePasseSalle;

    public InvitationMessage(String de, String pour, String nomSalle, String motDePasseSalle) {
        super(de, pour, "t'as invite a rejoindre " + nomSalle);
        this.nomSalle = nomSalle;
        this.motDePasseSalle = motDePasseSalle;
    }

    public String getNomSalle() {
        return nomSalle;
    }

    public String getMotDePasseSalle() {
        return motDePasseSalle;
    }
}
