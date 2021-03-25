package modele.implementation.amis.chat;

import java.io.Serializable;

public abstract class Message implements Serializable {

    private final String de;
    private final String contenu;
    private final String pour;

    protected Message(String de, String pour, String contenu) {
        this.de = de;
        this.pour = pour;
        this.contenu = contenu;
    }

    public String getDe() {
        return de;
    }

    public String getContenu() {
        return contenu;
    }

    public String getPour() {
        return pour;
    }
}
