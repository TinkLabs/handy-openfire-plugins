package com.hi.handy.auth.plugin.entity;

public class UserRoomEntity extends BaseEntity {

  private Long id;
  private Long zoneId;
  private String userName;
  private String roomName;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getZoneId() {
    return zoneId;
  }

  public void setZoneId(Long zoneId) {
    this.zoneId = zoneId;
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
}
