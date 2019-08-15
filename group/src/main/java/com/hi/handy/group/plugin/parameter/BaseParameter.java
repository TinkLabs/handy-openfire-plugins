package com.hi.handy.group.plugin.parameter;

public class BaseParameter {

  public enum ApiType {
    AGENT_DELETE,
    RELATION_DELETE
  }

  private ApiType apiType;

  public ApiType getApiType() {
    return apiType;
  }

  public void setApiType(ApiType apiType) {
    this.apiType = apiType;
  }
}
