package com.hi.handy.auth.plugin.parameter;

public class BaseParameter {

  public enum AuthType {
    AGENT,
    GUEST
  }

  private AuthType AuthType;

  public AuthType getAuthType() {
    return AuthType;
  }

  public void setAuthType(AuthType authType) {
    AuthType = authType;
  }
}
