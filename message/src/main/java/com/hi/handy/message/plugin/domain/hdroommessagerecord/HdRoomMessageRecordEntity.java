package com.hi.handy.message.plugin.domain.hdroommessagerecord;

import com.hi.handy.message.plugin.domain.BaseEntity;

import java.sql.Timestamp;

public class HdRoomMessageRecordEntity extends BaseEntity {

    private String id;
    private String roomName;
    private Long amount;
    private Timestamp updateDate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public Timestamp getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Timestamp updateDate) {
        this.updateDate = updateDate;
    }
}
