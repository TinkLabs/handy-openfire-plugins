package com.hi.handy.message.plugin.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hi.handy.message.plugin.BaseTest;
import org.junit.Test;
import org.xmpp.packet.Packet;

public class MessageServiceTest extends BaseTest {

    @Test
    public void save_Test() throws JsonProcessingException {
        Packet packet= new Packet() {
            @Override
            public Packet createCopy() {
                return null;
            }
        };
        MessageService.getInstance().save(packet);
    }
}
