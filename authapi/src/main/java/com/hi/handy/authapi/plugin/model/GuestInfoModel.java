package com.hi.handy.authapi.plugin.model;

public class GuestInfoModel extends AuthModel {
    private String groupId;
    private String groupIcon;
    private String groupName;

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getGroupIcon() {
        return groupIcon;
    }

    public void setGroupIcon(String groupIcon) {
        this.groupIcon = groupIcon;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public ChatRoomModel getChatRoom() {
        return chatRoom;
    }

    public void setChatRoom(ChatRoomModel chatRoom) {
        this.chatRoom = chatRoom;
    }

    private ChatRoomModel chatRoom;
}
