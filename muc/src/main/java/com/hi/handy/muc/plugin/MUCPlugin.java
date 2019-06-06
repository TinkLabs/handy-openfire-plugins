package com.hi.handy.muc.plugin;

import com.hi.handy.muc.handler.MUCPersistenceHandler;
import com.hi.handy.muc.listener.CustomMUCEventListener;
import lombok.extern.slf4j.Slf4j;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.jivesoftware.openfire.IQRouter;
import org.jivesoftware.openfire.XMPPServer;
import org.jivesoftware.openfire.container.Plugin;
import org.jivesoftware.openfire.container.PluginManager;
import org.jivesoftware.openfire.handler.IQHandler;
import org.jivesoftware.openfire.handler.IQRosterHandler;
import org.jivesoftware.openfire.interceptor.InterceptorManager;
import org.jivesoftware.openfire.interceptor.PacketInterceptor;
import org.jivesoftware.openfire.interceptor.PacketRejectedException;
import org.jivesoftware.openfire.muc.MUCEventDispatcher;
import org.jivesoftware.openfire.muc.MUCEventListener;
import org.jivesoftware.openfire.session.Session;
import org.xmpp.packet.Message;
import org.xmpp.packet.Packet;
import org.xmpp.packet.PacketExtension;

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
            log.info("收到聊天消息,消息类型： "+message.getType()+" 消息内容："+message.getBody()+" 消息类型(文/语/图)："+message.getElement().attributeValue("messageType")+" From:"+message.getFrom().toString()+" to:"+message.getTo().toString());
        }
    }
}
