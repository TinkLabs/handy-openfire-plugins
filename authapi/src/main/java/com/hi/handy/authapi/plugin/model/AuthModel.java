package com.hi.handy.authapi.plugin.model;

public class AuthModel {

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public ChatRoomModel getChatRoom() {
    return chatRoom;
  }

  public void setChatRoom(ChatRoomModel chatRoom) {
    this.chatRoom = chatRoom;
  }

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

  private String userName;
  private String displayName;
  private String email;
  private ChatRoomModel chatRoom;
}
