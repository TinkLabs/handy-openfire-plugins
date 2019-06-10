package com.hi.handy.messageapi.plugin.domain.agentmessagerecord;

import com.hi.handy.messageapi.plugin.domain.BaseEntity;

import java.sql.Timestamp;

public class AgentMessageRecordEntity extends BaseEntity {

  private String userName;
  private Long hotelId;
  private String hotelName;
  private String roomNum;
  private Long readCount;
  private Timestamp updateDate;

  public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
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

  public Timestamp getUpdateDate() {
    return updateDate;
  }

  public void setUpdateDate(Timestamp updateDate) {
    this.updateDate = updateDate;
  }

  public Long getReadCount() {
    return readCount;
  }

  public void setReadCount(Long readCount) {
    this.readCount = readCount;
  }
}
