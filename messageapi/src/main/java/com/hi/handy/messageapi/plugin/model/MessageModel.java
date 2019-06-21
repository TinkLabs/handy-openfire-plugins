package com.hi.handy.messageapi.plugin.model;

import java.sql.Timestamp;

public class MessageModel{

    private String hotelName;
    private String roomNum;
    private Long unRead;
    private String content;
    private Timestamp createDate;

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

    public Long getUnRead() {
        return unRead;
    }

    public void setUnRead(Long unRead) {
        this.unRead = unRead;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Timestamp getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Timestamp createDate) {
        this.createDate = createDate;
    }
}
