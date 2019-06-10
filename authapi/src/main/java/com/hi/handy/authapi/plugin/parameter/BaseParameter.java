package com.hi.handy.authapi.plugin.parameter;

public class BaseParameter {

  public enum AuthType {
    AGENT_LOGIN,
    AGENT_REGISTER,
    AGENT_CHAT_ROOM,
    AGENT_MODIFY,
    GUEST_LOGIN,
    GUEST_REGISTER
  }

  public enum UserType {
    AGENT,
    AGENT_ADMIN,
    HOTEL,
    GUEST
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
}
