package com.hi.handy.auth.plugin.parameter;

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
