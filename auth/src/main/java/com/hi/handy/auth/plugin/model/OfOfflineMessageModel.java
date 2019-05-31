package com.hi.handy.auth.plugin.model;

import com.hi.handy.auth.plugin.entity.BaseEntity;

import java.sql.Timestamp;

public class OfOfflineMessageModel extends BaseEntity{
    private String hotelName;
    private String userName;
    private Long messageID;
    private String creationDate;
    private Integer messageSize;
    private String stanza;

    public String getHotelName() {
        return hotelName;
    }

    public void setHotelName(String hotelName) {
        this.hotelName = hotelName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Long getMessageID() {
        return messageID;
    }

    public void setMessageID(Long messageID) {
        this.messageID = messageID;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public Integer getMessageSize() {
        return messageSize;
    }

    public void setMessageSize(Integer messageSize) {
        this.messageSize = messageSize;
    }

    public String getStanza() {
        return stanza;
    }

    public void setStanza(String stanza) {
        this.stanza = stanza;
    }
}
