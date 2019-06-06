package com.hi.handy.muc.handler;

import com.hi.handy.muc.dao.MUCDao;
import lombok.extern.slf4j.Slf4j;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Namespace;
import org.jivesoftware.openfire.IQHandlerInfo;
import org.jivesoftware.openfire.XMPPServer;
import org.jivesoftware.openfire.auth.UnauthorizedException;
import org.jivesoftware.openfire.handler.IQHandler;
import org.jivesoftware.openfire.muc.spi.LocalMUCRoom;
import org.jivesoftware.openfire.muc.spi.MultiUserChatServiceImpl;
import org.xmpp.packet.IQ;
import org.xmpp.packet.JID;

import java.util.List;
import java.util.Map;

/**
 * @author huangxiutao
 * @mail xiutao.huang@tinklabs.com
 * @create 2019-05-30 17:24
 * @Description
 */
@Slf4j
public class MUCPersistenceHandler extends IQHandler {
    private IQHandlerInfo handlerInfo;
    private XMPPServer server;

    public MUCPersistenceHandler() {
        super("Group Roster Handler");
        server = XMPPServer.getInstance();
        handlerInfo = new IQHandlerInfo("query","im:iq:group");
    }

    @Override
    public IQ handleIQ(IQ iq) throws UnauthorizedException {
        JID userJID = iq.getFrom();
        log.info("MUCPersistenceHandler>handleIQ");

        server =XMPPServer.getInstance();

        List<Map<String, String>> data = MUCDao.getMUCInfo(userJID.toBareJID());

        if (data == null || data.isEmpty()) {
            return null;
        }
        Map<String, String> map = null;


        Document document = DocumentHelper.createDocument();
        Element iqElement = document.addElement("iq");
        iqElement.addAttribute("type","result");
        iqElement.addAttribute("to",userJID.toFullJID());
        iqElement.addAttribute("id",iq.getID());
        Namespace namespace = new Namespace("",handlerInfo.getNamespace());
        Element muc = iqElement.addElement(handlerInfo.getName());
        muc.add(namespace);

        for (int i = 0, len = data.size(); i < len; i++) {
            map = data.get(i);

            String serviceID = map.get("serviceID");
            MultiUserChatServiceImpl mucService = (MultiUserChatServiceImpl) server.getMultiUserChatManager().getMultiUserChatService(Long.parseLong(serviceID));
            String roomName = map.get("name");
            LocalMUCRoom room = (LocalMUCRoom) mucService.getChatRoom(roomName);

            // 增加room和account信息
            Element roome = muc.addElement("room");
            roome.setText(map.get("description"));
            roome.addAttribute("nickname", map.get("nickname"));
            roome.addAttribute("id", room.getJID().toBareJID());
            roome.addAttribute("naturalName", map.get("naturalName"));
        }

        IQ reply = new IQ(iqElement);

        log.info("MUCPersistenceHandler reply XML:"+reply.toXML());

        return reply;
    }
    @Override
    public IQHandlerInfo getInfo() {
        return handlerInfo;
    }


}
