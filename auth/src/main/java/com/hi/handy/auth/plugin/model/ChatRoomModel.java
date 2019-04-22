package com.hi.handy.auth.plugin.model;

public class ChatRoomModel {

  public enum RoomType {
    AGENT,
    HOTEL
  }

  private String roomName;
  private String hotelId;
  private String hotelName;
  private String hotelRoomNum;
  private RoomType roomType = RoomType.AGENT;

  public String getRoomName() {
    return roomName;
  }

  public void setRoomName(String roomName) {
    this.roomName = roomName;
  }

  public String getHotelId() {
    return hotelId;
  }

  public void setHotelId(String hotelId) {
    this.hotelId = hotelId;
  }

  public String getHotelName() {
    return hotelName;
  }

  public void setHotelName(String hotelName) {
    this.hotelName = hotelName;
  }

  public String getHotelRoomNum() {
    return hotelRoomNum;
  }

  public void setHotelRoomNum(String hotelRoomNum) {
    this.hotelRoomNum = hotelRoomNum;
  }

  public RoomType getRoomType() {
    return roomType;
  }

  public void setRoomType(RoomType roomType) {
    this.roomType = roomType;
  }
}
