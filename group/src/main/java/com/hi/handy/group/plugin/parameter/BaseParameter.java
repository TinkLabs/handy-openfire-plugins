package com.hi.handy.group.plugin.parameter;

public class BaseParameter {

  public enum ApiType {
    GROUP_ADD,
    ZONE_ADD,
    AGENT_ADD,
    DELETE
  }

  private ApiType apiType;

  public ApiType getApiType() {
    return apiType;
  }

  public void setApiType(ApiType apiType) {
    this.apiType = apiType;
  }
}
