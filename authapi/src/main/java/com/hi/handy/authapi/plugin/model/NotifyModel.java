package com.hi.handy.authapi.plugin.model;

public class NotifyModel {
    private String id;
    private NotifyType notifyType;

    public NotifyModel(){}
    public NotifyModel(NotifyType notifyType,String id) {
        this.notifyType = notifyType;
        this.id = id;
    }

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public NotifyType getNotifyType() {
        return notifyType;
    }
    public void setNotifyType(NotifyType notifyType) {
        this.notifyType = notifyType;
    }
}
