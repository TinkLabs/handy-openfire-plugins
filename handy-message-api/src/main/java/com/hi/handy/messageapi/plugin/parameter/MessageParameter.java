package com.hi.handy.messageapi.plugin.parameter;

public class MessageParameter extends BaseParameter {

    private Integer pageIndex;
    private Integer pageSize;
    private String userName;

    public Integer getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(Integer pageIndex) {
        this.pageIndex = pageIndex;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Override
    public String toString() {
        return "MessageParameter{" +
                "messageType=" + getMessageType() +
                ", pageIndex=" + pageIndex +
                ", pageSize=" + pageSize +
                ", userName='" + userName + '\'' +
                '}';
    }
}
