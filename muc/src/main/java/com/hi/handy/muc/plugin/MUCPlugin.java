package com.hi.handy.muc.plugin;

import com.hi.handy.muc.dao.MUCDao;
import com.hi.handy.muc.entity.HdUserPropertyEntity;
import com.hi.handy.muc.handler.MUCPersistenceHandler;
import com.hi.handy.muc.listener.CustomMUCEventListener;
import com.hi.handy.muc.service.GroupService;
import lombok.extern.slf4j.Slf4j;
import org.jivesoftware.openfire.IQRouter;
import org.jivesoftware.openfire.PacketRouter;
import org.jivesoftware.openfire.XMPPServer;
import org.jivesoftware.openfire.container.Plugin;
import org.jivesoftware.openfire.container.PluginManager;
import org.jivesoftware.openfire.group.Group;
import org.jivesoftware.openfire.group.GroupManager;
import org.jivesoftware.openfire.group.GroupNotFoundException;
import org.jivesoftware.openfire.handler.IQHandler;
import org.jivesoftware.openfire.interceptor.InterceptorManager;
import org.jivesoftware.openfire.interceptor.PacketInterceptor;
import org.jivesoftware.openfire.interceptor.PacketRejectedException;
import org.jivesoftware.openfire.muc.MUCEventDispatcher;
import org.jivesoftware.openfire.session.Session;
import org.xmpp.packet.Message;
import org.xmpp.packet.Packet;

import java.io.File;
import java.util.List;

/**
 * @author huangxiutao
 * @mail xiutao.huang@tinklabs.com
 * @create 2019-05-30 17:16
 * @Description
 */
@Slf4j
public class MUCPlugin implements Plugin,PacketInterceptor {

    private InterceptorManager interceptorManager;

    public void initializePlugin(PluginManager pluginManager, File file) {
        log.info("loading muc plugin>>>>>");
        IQHandler handler = new MUCPersistenceHandler();
        IQRouter iqRouter = XMPPServer.getInstance().getIQRouter();
        iqRouter.addHandler(handler);

        //持久化聊天室成员
        MUCEventDispatcher.addListener(new CustomMUCEventListener());
        //将当前消息拦截器加入
        interceptorManager = InterceptorManager.getInstance();
        interceptorManager.addInterceptor(this);
    }

    public void destroyPlugin() {
        log.info("destroy muc plugin>>>>>");
        interceptorManager.removeInterceptor(this);
    }


    /**
     *
     * @param packet Packet有4种类型：Message、IQ、Presence和Route
     * @param session 表示服务器与某个客户端之间的连接，或者服务器与服务器之间的连接。连接的那方就是消息过来的那方，或者消息将要发出去的那方。session这个参数非常有用，它里面存储了很多信息，例如用户的连接、用户的用户名等
     * @param incoming 表示这条消息是从服务器进来还是从服务器出去
     * @param processed 表示这个包是否被openfire处理了
     * @throws PacketRejectedException
     */
    public void interceptPacket(Packet packet, Session session, boolean incoming,boolean processed) throws PacketRejectedException {

        //对刚进入(incoming=true)和未被处理的(processed=false)的聊天消息（packet instanceof Message）进行处理
        if (incoming && !processed && packet instanceof Message) {
            Message message = (Message) packet;
            if (null!=message.getBody()){
                //只处理有内容的消息
                log.info("session:"+(session!=null?session.toString():"")+" 消息类型："+message.getType()+" 消息内容："+message.toXML()+" 消息类型(文/语/图)："+message.getElement().attributeValue("messageType")+" From:"+message.getFrom().toString()+" to:"+message.getTo().toString());
                GroupManager groupManager = GroupManager.getInstance();
                if(message.getTo().equals(message.getFrom())){
                    PacketRejectedException rejectedException =  new PacketRejectedException();
                    log.info("self message");
                    rejectedException.setRejectionMessage("self message...");
                    throw rejectedException;
                }
                if(null!=session&&session.getAddress().equals(message.getFrom())){
                    log.info("session address: "+session.getAddress()+" session streamId:"+session.getStreamID());
                    String domain =message.getFrom().getDomain();
                    String fromUserName = message.getFrom().getNode();
                    String chatRoom = null;
                    Group group = null;

                    if(Message.Type.groupchat ==(message.getType())){
                        message.getElement().addAttribute("chatroom",message.getTo().toString());
                        chatRoom = message.getTo().getNode();
                    }
                    HdUserPropertyEntity user = MUCDao.getHdUserByUsername(fromUserName);
                    if(null!=user){
                        String roomType = getRoomType(chatRoom);
                        String groupName = null;
                        boolean check = null != roomType;
                        // 判断是 消息是来自VIP concierge or Hotel concierge
                        Long zoneId = null;
                        Long hotelId = null;


                        try {
                            String[] roomInfoArray = chatRoom.split("#");
                            String[] zoneHotelInfo = roomInfoArray[1].split("-");
                            zoneId = Long.valueOf(zoneHotelInfo[0]);
                            hotelId = Long.valueOf(zoneHotelInfo[1]);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        if(check && "room-vip".equals(roomType)){
                            //根据zone id找agent group
                            //zoneId = user.getZoneId();

                            //TODO 此处通过chatRoom来获取zoneid和hotelid，这样就不会存在agent发消息组内agent收不到消息的问题
                            if(null != zoneId){
                                log.info(">>>>>>>>> group name:"+groupName);
                                groupName = MUCDao.getGroupNameByTypeAndRelationId("VIP",zoneId.toString());
                            }
                        }else if(check && "room-hotel".equals(roomType)){
                            //根据hotel id找agent group
                            //hotelId = user.getHotelId();
                            if(null != hotelId){
                                log.info(">>>>>>>>> group name:"+groupName);
                                groupName = MUCDao.getGroupNameByTypeAndRelationId("HOTEL",hotelId.toString());
                            }
                        }
                        try {
                            //已经确认：一个agent group可能会管理多个zone，但是一个zone只会有一个agent group 管理。
                            if(null != groupName){
                                group = groupManager.getGroup(groupName,true);
                            }
                        } catch (GroupNotFoundException e) {
                            // do nothing
                        }
                        if(null!=group){
                            notifyGroup(message, domain, fromUserName, groupName);
                            notifyAdminGroup(message, domain, fromUserName);
                        }
                    }
                }
            }
        }
    }

    private String getRoomType(String roomInfo){
        try {
            String[] infoArray = roomInfo.split("#");
            return infoArray[0];
        } catch (Exception e) {
            return null;
        }
    }

    private void notifyGroup(Message message, String domain, String fromUserName, String groupName) {
        log.info("notifyGroup");
        notify(message, domain, fromUserName, groupName);
    }

    private void notifyAdminGroup(Message message, String domain, String fromUserName) {
        log.info("notifyAdminGroup");
        List<String> groupNames = GroupService.getInstance().searchGroupNameByAgentName();
        if(!(null!=groupNames && groupNames.size()>0)) return;
        for(String groupName:groupNames){
            notify(message, domain, fromUserName, groupName);
        }
    }

    private void notify(Message message, String domain, String fromUserName,String groupName) {
        log.info("notify"+message.toXML());
        XMPPServer xmppServer = XMPPServer.getInstance();
        PacketRouter packetRouter = xmppServer.getPacketRouter();
        Message broadCast = new Message();
        broadCast.setFrom(fromUserName+"@"+domain);
        broadCast.setTo(groupName+"@broadcast."+domain);
        broadCast.setBody(message.getBody());
        if(Message.Type.groupchat ==(message.getType())){
            broadCast.getElement().addAttribute("chatroom",message.getTo().toString());
        }
        packetRouter.route(broadCast);
    }

}
