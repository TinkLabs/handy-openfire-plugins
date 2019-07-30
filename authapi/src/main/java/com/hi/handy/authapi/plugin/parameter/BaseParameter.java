package com.hi.handy.authapi.plugin.parameter;

public class BaseParameter {

  private Long hotelId;
  private String hotelName;
  private String roomId;
  private String roomNum;
  private String displayName;
  private String password;
  private String email;
  private Long zoneId;
  private String zoneName;
  private String deviceUserId;
  private String groupName;

  public enum AuthType {
    GUEST_ENTRY_HOTELCHATROOM,
    GUEST_LOGIN,
    AGENT_LOGIN,
    AGENT_LOGOUT,
    AGENT_REGISTER,
    GUEST_LEAVECHAT,
    MESSAGE_DELETE
  }

  public enum UserType {
    GUEST,AGENT
  }

  private AuthType AuthType;

  private UserType userType;


  public BaseParameter.AuthType getAuthType() {
    return AuthType;
  }

  public void setAuthType(BaseParameter.AuthType authType) {
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

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
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

  public String getDeviceUserId() {
    return deviceUserId;
  }

  public void setDeviceUserId(String deviceUserId) {
    this.deviceUserId = deviceUserId;
  }

  public String getGroupName() {
    return groupName;
  }

  public void setGroupName(String groupName) {
    this.groupName = groupName;
  }
}
