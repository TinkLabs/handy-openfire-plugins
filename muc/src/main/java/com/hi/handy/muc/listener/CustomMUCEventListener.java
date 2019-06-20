package com.hi.handy.muc.listener;

import com.hi.handy.muc.dao.MUCDao;
import lombok.extern.slf4j.Slf4j;
import org.jivesoftware.openfire.XMPPServer;
import org.jivesoftware.openfire.muc.MUCEventListener;
import org.jivesoftware.openfire.muc.MUCRoom;
import org.jivesoftware.openfire.muc.MultiUserChatService;
import org.jivesoftware.openfire.muc.NotAllowedException;
import org.xmpp.packet.JID;
import org.xmpp.packet.Message;

/**
 * @author huangxiutao
 * @mail xiutao.huang@tinklabs.com
 * @create 2019-05-30 19:03
 * @Description
 */
@Slf4j
public class CustomMUCEventListener implements MUCEventListener {
    public void roomCreated(JID jid) {

    }

    public void roomDestroyed(JID jid) {

    }

    public void occupantJoined(JID roomJid, JID userJid, String s) {

        log.info("occupantJoined: nickName:"+s);
        XMPPServer server = XMPPServer.getInstance();
        MultiUserChatService multiUserChatService = server.getMultiUserChatManager().getMultiUserChatService(roomJid);
        if(multiUserChatService != null){
            String roomName = roomJid.getNode();
            try {
                MUCRoom room = multiUserChatService.getChatRoom(roomName,userJid);
                boolean isRoomPersistent = room.isPersistent();
                boolean hasJoinedRoom = MUCDao.hasJoinedRoom(room,userJid);
                log.info("room persistent? "+isRoomPersistent);
                log.info("user hasJoined? "+hasJoinedRoom);
                if(isRoomPersistent && !hasJoinedRoom){
                    MUCDao.saveMember(room,userJid,s);
                }
            } catch (NotAllowedException e) {
                log.error("MUCPlugin>>CustomMUCEventListener error"+e.getMessage(),e);
            }
        }

    }

    public void occupantLeft(JID jid, JID jid1) {

    }

    public void nicknameChanged(JID jid, JID jid1, String s, String s1) {

    }

    public void messageReceived(JID jid, JID jid1, String s, Message message) {

    }

    public void privateMessageRecieved(JID jid, JID jid1, Message message) {

    }

    public void roomSubjectChanged(JID jid, JID jid1, String s) {

    }
}
