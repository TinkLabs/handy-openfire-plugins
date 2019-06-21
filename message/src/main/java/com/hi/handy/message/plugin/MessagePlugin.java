package com.hi.handy.message.plugin;

import com.hi.handy.message.plugin.service.AgentJoinChatRoomService;
import com.hi.handy.message.plugin.service.MessageService;
import org.jivesoftware.openfire.container.Plugin;
import org.jivesoftware.openfire.container.PluginManager;
import org.jivesoftware.openfire.interceptor.InterceptorManager;
import org.jivesoftware.openfire.interceptor.PacketInterceptor;
import org.jivesoftware.openfire.session.Session;
import org.jivesoftware.util.JiveGlobals;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xmpp.packet.Message;
import org.xmpp.packet.Packet;
import org.xmpp.packet.Presence;

import java.io.File;

public class MessagePlugin implements Plugin, PacketInterceptor {

    public static final String SERVICEENABLED = "plugin.message.serviceEnabled";

    private boolean serviceEnabled;

    public void setServiceEnabled(boolean enabled) {
        serviceEnabled = enabled;
        JiveGlobals.setProperty(SERVICEENABLED, enabled ? "true" : "false");
    }

    public boolean getServiceEnabled() {
        return serviceEnabled;
    }

    public MessagePlugin() {
        serviceEnabled = true;
        serviceEnabled = JiveGlobals.getBooleanProperty(SERVICEENABLED, true);
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(MessagePlugin.class);

    private InterceptorManager interceptorManager;

    public void initializePlugin(PluginManager manager, File pluginDirectory) {
        // 将当前插件加入到消息拦截管理器（interceptorManager ）中，当消息到来或者发送出去的时候，会触发本插件的interceptPacket方法。
        LOGGER.debug("MessagePlugin init============");
        interceptorManager = InterceptorManager.getInstance();
        interceptorManager.addInterceptor(this);
    }

    public void destroyPlugin() {
        // 当插件被卸载的时候，主要通过openfire管理控制台卸载插件时，被调用。注意interceptorManager的addInterceptor和removeInterceptor需要成对调用。
        LOGGER.debug("MessagePlugin destory============");
        interceptorManager.removeInterceptor(this);
    }

    @Override
    public void interceptPacket(Packet packet, Session session, boolean incoming, boolean processed) {
        if (!incoming && processed && packet instanceof Presence) {
            LOGGER.debug("agent join chat room save packet" + packet);
            // CompletableFuture.runAsync(() -> AgentJoinChatRoomService.getInstance().setRoomAllMessageIsRead(packet));
            AgentJoinChatRoomService.getInstance().setRoomAllMessageIsRead(packet);
        }

        if (serviceEnabled) {
            LOGGER.debug("message save ============");
            if (incoming && !processed && packet instanceof Message) {
                LOGGER.info("message save packet" + packet);
                // CompletableFuture.runAsync(() -> MessageService.getInstance().recordMessage(packet));
                MessageService.getInstance().recordMessage(packet);
            }
        } else {
            LOGGER.debug("message save");
        }
    }
}
