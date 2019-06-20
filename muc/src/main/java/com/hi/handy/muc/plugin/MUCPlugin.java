package com.hi.handy.muc.plugin;

import com.hi.handy.muc.dao.MUCDao;
import com.hi.handy.muc.handler.MUCPersistenceHandler;
import com.hi.handy.muc.listener.CustomMUCEventListener;
import lombok.extern.slf4j.Slf4j;
import org.jivesoftware.openfire.IQRouter;
import org.jivesoftware.openfire.OfflineMessageStore;
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
                String fromUserName = message.getFrom().getNode();
                Long zoneId = MUCDao.getUserZoneId(fromUserName);
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
                    Group group = null;

                    if(Message.Type.groupchat ==(message.getType())){
                        message.getElement().addAttribute("chatroom",message.getTo().toString());
                    }

                    if(null!=zoneId){
                        String groupName = "zone-"+zoneId;
                        try {
                            //已经确认：一个agent group可能会对应多个zone，但是一个zone只会有一个agent group 管理。
                            //TODO group 的名字
                            group = groupManager.getGroup(groupName,true);
                        } catch (GroupNotFoundException e) {
                            e.printStackTrace();
                        }
                        if(null!=group){
                            //TODO 处理的时候如果是来自groupchat。转发的时候要自定义加一个chatroom="xxx@con.10.xxxx"
                            XMPPServer xmppServer = XMPPServer.getInstance();
                            PacketRouter packetRouter = xmppServer.getPacketRouter();
                            Message broadCast = new Message();
                            broadCast.setFrom(fromUserName+"@"+domain);
                            broadCast.setTo(groupName+"@broadcast."+domain);
                            broadCast.setBody(message.getBody());
                            if(Message.Type.groupchat ==(message.getType())){
                                broadCast.getElement().addAttribute("chatroom",message.getTo().toString());
//                                broadCast.setFrom(message.getTo());
                            }
                            packetRouter.route(broadCast);
                        }
                        log.info("有body 的消息："+ message.toXML());
                    }
                }
//                OfflineMessageStore offlineMessageStore = new OfflineMessageStore();
//                offlineMessageStore.addMessage(message);
            }
        }
    }
}
