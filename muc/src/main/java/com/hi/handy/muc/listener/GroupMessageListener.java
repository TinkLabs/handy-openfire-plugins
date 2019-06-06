package com.hi.handy.muc.listener;

import lombok.extern.slf4j.Slf4j;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.packet.Message;
import org.jxmpp.jid.Jid;

/**
 * @author huangxiutao
 * @mail xiutao.huang@tinklabs.com
 * @create 2019-05-31 17:19
 * @Description
 */
@Slf4j
public class GroupMessageListener implements MessageListener {
    public void processMessage(Message message) {
        Jid from = message.getFrom();
        String messageBody = message.getBody();
        System.out.println("GroupChat>>>>>from:"+from+" message body:"+messageBody);
        log.info("GroupChat>>>>>from:"+from+" message body:"+messageBody);
    }
}
