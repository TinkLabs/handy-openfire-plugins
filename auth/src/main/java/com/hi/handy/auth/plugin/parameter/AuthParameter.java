package com.hi.handy.auth.plugin.parameter;

import java.io.Serializable;

public class AuthParameter implements Serializable {

  private Long hotelId;
  private String hotelName;
  private String roomId;
  private String roomNum;
  private String userName;
  private String password;
  private String email;
  private Long zoneId;
  private String zoneName;
  private String barcode;

  public enum AuthType {
    AGENT_CHAT_ROOM,
    AGENT_REGISTER,
    AGENT_MODIFY,
    GUEST
  }

  public enum UserType {
    AGENT,
    AGENT_ADMIN,
    HOTEL,
    GUEST
  }

  private AuthType AuthType;

  private UserType userType;


  public AuthType getAuthType() {
    return AuthType;
  }

  public void setAuthType(AuthType authType) {
    AuthType = authType;
  }

  public UserType getUserType() {
    return userType;
  }

  public void setUserType(UserType userType) {
    this.userType = userType;
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

  public String getRoomId() {
    return roomId;
  }

  public void setRoomId(String roomId) {
    this.roomId = roomId;
  }

  public String getRoomNum() {
    return roomNum;
  }

  public void setRoomNum(String roomNum) {
    this.roomNum = roomNum;
  }

  public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getBarcode() {
    return barcode;
  }

  public void setBarcode(String barcode) {
    this.barcode = barcode;
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
}
