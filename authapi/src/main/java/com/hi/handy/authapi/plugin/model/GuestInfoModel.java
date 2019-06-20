package com.hi.handy.authapi.plugin.model;

public class GuestInfoModel extends AuthModel {
    public ChatRoomModel getChatRoom() {
        return chatRoom;
    }

    public void setChatRoom(ChatRoomModel chatRoom) {
        this.chatRoom = chatRoom;
    }

    private ChatRoomModel chatRoom;
}
