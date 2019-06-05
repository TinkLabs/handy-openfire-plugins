package com.hi.handy.messageapi.plugin.parameter;

public class BaseParameter {

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

  public enum MessageType {
    HISTORY,
    LIST
  }

  private AuthType AuthType;

  private UserType userType;

  private MessageType messageType;


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

  public MessageType getMessageType() {
    return messageType;
  }

  public void setMessageType(MessageType messageType) {
    this.messageType = messageType;
  }
}
