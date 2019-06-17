package com.hi.handy.messageapi.plugin.domain.hduserproperty;

import com.hi.handy.messageapi.plugin.domain.BaseEntity;

import java.sql.Timestamp;

public class HdUserPropertyEntity extends BaseEntity {

    private String userName;
    private String displayName;
    private String password;
    private Long zoneId;
    private String zoneName;
    private Long hotelId;
    private String hotelName;
    private String roomNum;
    private String type;
    private Timestamp creationDate;
    private Timestamp modificationDamodificationDate;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Long getZoneId() {
        return zoneId;
    }

    public void setZoneId(Long zoneId) {
        this.zoneId = zoneId;
    }

    public String getZoneName() {
        return zoneName;
    }

    public void setZoneName(String zoneName) {
        this.zoneName = zoneName;
    }

    public Long getHotelId() {
        return hotelId;
    }

    public void setHotelId(Long hotelId) {
        this.hotelId = hotelId;
    }

    public String getHotelName() {
        return hotelName;
    }

    public void setHotelName(String hotelName) {
        this.hotelName = hotelName;
    }

    public String getRoomNum() {
        return roomNum;
    }

    public void setRoomNum(String roomNum) {
        this.roomNum = roomNum;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Timestamp getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Timestamp creationDate) {
        this.creationDate = creationDate;
    }

    public Timestamp getModificationDamodificationDate() {
        return modificationDamodificationDate;
    }

    public void setModificationDamodificationDate(Timestamp modificationDamodificationDate) {
        this.modificationDamodificationDate = modificationDamodificationDate;
    }
}
