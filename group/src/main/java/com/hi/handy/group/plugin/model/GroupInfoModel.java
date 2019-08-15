package com.hi.handy.group.plugin.model;

public class GroupInfoModel{

    private String id;
    private String groupName;

    public GroupInfoModel(String id, String groupName) {
        this.id = id;
        this.groupName = groupName;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
