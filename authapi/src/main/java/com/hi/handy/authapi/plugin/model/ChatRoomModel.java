package com.hi.handy.authapi.plugin.model;

public class ChatRoomModel {

  public enum RoomType {
    VIP,
    HOTEL
  }

  private String id;
  private String name;
  private Boolean status;
  private String icon;
  private String roomName;
  private String roomJID;
  private RoomType roomType = RoomType.VIP;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public RoomType getRoomType() {
    return roomType;
  }

  public void setRoomType(RoomType roomType) {
    this.roomType = roomType;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Boolean getStatus() {
    return status;
  }

  public void setStatus(Boolean status) {
    this.status = status;
  }

  public String getIcon() {
    return icon;
  }

  public void setIcon(String icon) {
    this.icon = icon;
  }

  public String getRoomName() {
    return roomName;
  }

  public void setRoomName(String roomName) {
    this.roomName = roomName;
  }

  public String getRoomJID() {
    return roomJID;
  }

  public void setRoomJID(String roomJID) {
    this.roomJID = roomJID;
  }
}
