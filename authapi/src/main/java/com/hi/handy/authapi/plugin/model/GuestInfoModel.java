package com.hi.handy.authapi.plugin.model;

public class GuestInfoModel extends AuthModel {
    private String groupId;
    private String groupName;
    private String groupIcon;
    private ChatRoomModel chatRoomModel;

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getGroupIcon() {
        return groupIcon;
    }

    public void setGroupIcon(String groupIcon) {
        this.groupIcon = groupIcon;
    }

    public ChatRoomModel getChatRoomModel() {
        return chatRoomModel;
    }

    public void setChatRoomModel(ChatRoomModel chatRoomModel) {
        this.chatRoomModel = chatRoomModel;
    }
}
