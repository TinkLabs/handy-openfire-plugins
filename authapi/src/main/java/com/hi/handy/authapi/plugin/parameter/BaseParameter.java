package com.hi.handy.authapi.plugin.parameter;

public class BaseParameter {

  public enum AuthType {
    GUEST_ENTRY_HOTELCHATROOM,
    GUEST_LOGIN,
    AGENT_LOGIN,
    AGENT_LOGOUT,
    AGENT_REGISTER
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
}
