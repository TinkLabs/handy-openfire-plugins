package com.hi.handy.messageapi.plugin.model;

public class MessageStanzaAndCreationDate {
    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public String getStanza() {
        return stanza;
    }

    public void setStanza(String stanza) {
        this.stanza = stanza;
    }

    private String creationDate;
    private String stanza;
}
