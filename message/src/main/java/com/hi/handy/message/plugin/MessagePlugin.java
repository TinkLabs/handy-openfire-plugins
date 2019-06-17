package com.hi.handy.message.plugin;

import com.hi.handy.message.plugin.service.MessageService;
import org.apache.commons.lang3.StringUtils;
import org.jivesoftware.openfire.XMPPServer;
import org.jivesoftware.openfire.container.Plugin;
import org.jivesoftware.openfire.container.PluginManager;
import org.jivesoftware.openfire.interceptor.InterceptorManager;
import org.jivesoftware.openfire.interceptor.PacketInterceptor;
import org.jivesoftware.openfire.session.Session;
import org.jivesoftware.openfire.user.UserManager;
import org.jivesoftware.util.JiveGlobals;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xmpp.packet.JID;
import org.xmpp.packet.Message;
import org.xmpp.packet.Packet;

import java.io.File;
import java.util.concurrent.CompletableFuture;

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
    LOGGER.info("MessagePlugin init============");
    interceptorManager = InterceptorManager.getInstance();
    interceptorManager.addInterceptor(this);
  }

  public void destroyPlugin() {
    // 当插件被卸载的时候，主要通过openfire管理控制台卸载插件时，被调用。注意interceptorManager的addInterceptor和removeInterceptor需要成对调用。
    LOGGER.info("MessagePlugin destory============");
    interceptorManager.removeInterceptor(this);
  }

  @Override
  public void interceptPacket(Packet packet, Session session, boolean incoming, boolean processed) {
    if(serviceEnabled) {
      LOGGER.info("MessagePlugin is enabled============");
      // incoming表示本条消息刚进入openFire。processed为false，表示本条消息没有被openFire处理过
      if (incoming && processed == false && packet instanceof Message) {
        LOGGER.info("MessagePlugin interceptPacket packet:" + packet);
        CompletableFuture.runAsync(() -> recordMessage(packet));
      }
    }else{
      LOGGER.info("MessagePlugin is disabled============");
    }
  }

  private void recordMessage(Packet packet) {
      Message message = (Message) packet;
      LOGGER.info("MessagePlugin interceptPacket message:" + message);
      if (!shouldStoreMessage(message)) {
        return;
      }
      LOGGER.info("MessagePlugin interceptPacket shouldStoreMessage pass");
      MessageService.getInstance().save(packet);
  }

  private boolean shouldStoreMessage(Message message) {
    if (message.getType() != Message.Type.groupchat) {
      return false;
    }
    if (StringUtils.isBlank(message.getBody())) {
      return false;
    }
    JID recipient = message.getTo();
    String username = recipient.getNode();

    if (username == null || !UserManager.getInstance().isRegisteredUser(recipient)) {
      return false;
    }
    JID sender = message.getTo();
    String senderUser = sender.getNode();

    if (senderUser == null || !UserManager.getInstance().isRegisteredUser(sender)) {
      return false;
    }

    if (!XMPPServer.getInstance().getServerInfo().getXMPPDomain().equals(recipient.getDomain())) {
      return false;
    }
    return true;
  }
}
