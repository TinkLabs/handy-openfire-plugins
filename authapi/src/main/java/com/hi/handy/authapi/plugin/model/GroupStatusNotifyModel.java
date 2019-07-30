package com.hi.handy.authapi.plugin.model;

public class GroupStatusNotifyModel extends NotifyModel{
    private Boolean isOnline;

    public GroupStatusNotifyModel(NotifyType notifyType, String id, Boolean isOnline) {
        super(notifyType, id);
        this.isOnline = isOnline;
    }

    public Boolean getOnline() {
        return isOnline;
    }

    public void setOnline(Boolean online) {
        isOnline = online;
    }
}
