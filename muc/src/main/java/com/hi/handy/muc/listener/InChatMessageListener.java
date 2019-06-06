package com.hi.handy.muc.listener;

import lombok.extern.slf4j.Slf4j;
import org.jivesoftware.smack.chat2.Chat;
import org.jivesoftware.smack.chat2.IncomingChatMessageListener;
import org.jivesoftware.smack.packet.Message;
import org.jxmpp.jid.EntityBareJid;

/**
 * @author huangxiutao
 * @mail xiutao.huang@tinklabs.com
 * @create 2019-05-31 17:20
 * @Description
 */
@Slf4j
public class InChatMessageListener implements IncomingChatMessageListener {
    public void newIncomingMessage(EntityBareJid from, Message message, Chat chat) {
        System.out.println("single chat>>>>>from: "+from.toString()+" message body:"+message.getBody());
        log.info("single chat>>>>>from: "+from.toString()+" message body:"+message.getBody());
    }
}
