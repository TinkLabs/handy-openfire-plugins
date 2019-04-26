package com.hi.handy.auth.plugin.model;

import java.util.List;

public class AuthModel {

  private String userName;
  private String password;
  private String email;
  private String agent;
  private String agentJID;
  private List<ChatRoomModel> chatRooms;

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


  public List<ChatRoomModel> getChatRooms() {
    return chatRooms;
  }

  public void setChatRooms(List<ChatRoomModel> chatRooms) {
    this.chatRooms = chatRooms;
  }

  public String getAgent() {
    return agent;
  }

  public void setAgent(String agent) {
    this.agent = agent;
  }

  public String getAgentJID() {
    return agentJID;
  }

  public void setAgentJID(String agentJID) {
    this.agentJID = agentJID;
  }
}
