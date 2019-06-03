package com.hi.handy.messageapi.plugin.model;

import java.util.List;

public class MessageHistoryModel {
    private Long readCount;

    public List<MessageStanzaAndCreationDate> getMessageStanzaAndCreationDates() {
        return messageStanzaAndCreationDates;
    }

    public void setMessageStanzaAndCreationDates(List<MessageStanzaAndCreationDate> messageStanzaAndCreationDates) {
        this.messageStanzaAndCreationDates = messageStanzaAndCreationDates;
    }

    private List<MessageStanzaAndCreationDate> messageStanzaAndCreationDates;

    public Long getReadCount() {
        return readCount;
    }

    public void setReadCount(Long readCount) {
        this.readCount = readCount;
    }
}
