package com.hi.handy.messageapi.plugin.parameter;

public class BaseParameter {

  public enum UserType {
    AGENT,
    GUEST
  }

  private UserType userType;

  public UserType getUserType() {
    return userType;
  }

  public void setUserType(UserType userType) {
    this.userType = userType;
  }
}
