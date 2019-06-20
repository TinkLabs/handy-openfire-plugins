package com.hi.handy.message.plugin.domain.hdagentmessagerecord;

import com.hi.handy.message.plugin.domain.BaseEntity;

import java.sql.Timestamp;

public class AgentMessageRecordEntity extends BaseEntity {

  private String id;
  private String userName;
  private String roomName;
  private Long readCount;
  private Timestamp updateDate;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

  public String getRoomName() {
    return roomName;
  }

  public void setRoomName(String roomName) {
    this.roomName = roomName;
  }

  public Long getReadCount() {
    return readCount;
  }

  public void setReadCount(Long readCount) {
    this.readCount = readCount;
  }

  public Timestamp getUpdateDate() {
    return updateDate;
  }

  public void setUpdateDate(Timestamp updateDate) {
    this.updateDate = updateDate;
  }
}
