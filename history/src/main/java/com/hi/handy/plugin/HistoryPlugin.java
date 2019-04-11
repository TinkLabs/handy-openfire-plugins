package com.hi.handy.plugin;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import org.apache.commons.lang3.StringUtils;
import org.jivesoftware.database.DbConnectionManager;
import org.jivesoftware.openfire.XMPPServer;
import org.jivesoftware.openfire.container.Plugin;
import org.jivesoftware.openfire.container.PluginManager;
import org.jivesoftware.openfire.interceptor.InterceptorManager;
import org.jivesoftware.openfire.interceptor.PacketInterceptor;
import org.jivesoftware.openfire.session.Session;
import org.jivesoftware.openfire.user.UserManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xmpp.packet.JID;
import org.xmpp.packet.Message;
import org.xmpp.packet.Message.Type;
import org.xmpp.packet.Packet;

public class HistoryPlugin implements Plugin, PacketInterceptor {

  private static final Logger LOGGER = LoggerFactory.getLogger(HistoryPlugin.class);

  private static final String CREATE_HISTORY_SQL =
      "INSERT INTO hdHistory (fromUser, toUser, fromJID, toJID, creationDate, stanza) " +
          "VALUES (?, ?, ?, ?, ?, ?)";

  //B: 消息拦截器
  private InterceptorManager interceptorManager;


  class HistoryMessage {

    private Long messageID;
    private String fromUser;
    private String toUser;
    private String fromJID;
    private String toJID;
    private String creationDate;
    private String stanza;

    public Long getMessageID() {
      return messageID;
    }

    public void setMessageID(Long messageID) {
      this.messageID = messageID;
    }

    public String getFromUser() {
      return fromUser;
    }

    public void setFromUser(String fromUser) {
      this.fromUser = fromUser;
    }

    public String getToUser() {
      return toUser;
    }

    public void setToUser(String toUser) {
      this.toUser = toUser;
    }

    public String getFromJID() {
      return fromJID;
    }

    public void setFromJID(String fromJID) {
      this.fromJID = fromJID;
    }

    public String getToJID() {
      return toJID;
    }

    public void setToJID(String toJID) {
      this.toJID = toJID;
    }

    public String getCreationDate() {
      return creationDate;
    }

    public void setCreationDate(String creationDate) {
      this.creationDate = creationDate;
    }

    public String getStanza() {
      return stanza;
    }

    public void setStanza(String stanza) {
      this.stanza = stanza;
    }
  }


  //C: 插件初始化函数
  public void initializePlugin(PluginManager manager, File pluginDirectory) {

    LOGGER.info("MessageFilter init============");
    // 将当前插件加入到消息拦截管理器（interceptorManager ）中，当消息到来或者发送出去的时候，会触发本插件的interceptPacket方法。
    interceptorManager = InterceptorManager.getInstance();
    interceptorManager.addInterceptor(this);
  }

  //D: 插件销毁函数
  public void destroyPlugin() {
    LOGGER.info("MessageFilter destory============");
    // 当插件被卸载的时候，主要通过openfire管理控制台卸载插件时，被调用。注意interceptorManager的addInterceptor和removeInterceptor需要成对调用。
    interceptorManager.removeInterceptor(this);
  }

  //E 插件拦截处理函数
  public void interceptPacket(Packet packet, Session session, boolean incoming, boolean processed) {
    // incoming表示本条消息刚进入openFire。processed为false，表示本条消息没有被openFire处理过
    if (incoming && processed && packet instanceof Message) {
      Message message = (Message) packet;
      if (!shouldStoreMessage(message)) {
        return;
      }
      HistoryMessage historyMessage = new HistoryMessage();
      historyMessage.setFromUser(packet.getFrom().getNode());
      historyMessage.setFromJID(packet.getFrom().toBareJID());
      historyMessage.setToUser(packet.getTo().getNode());
      historyMessage.setToJID(packet.getTo().toBareJID());
      historyMessage.setCreationDate(String.valueOf(System.currentTimeMillis()));
      historyMessage.setStanza(message.getElement().asXML());
      saveHistoryMessage(historyMessage);
    }
  }

  private boolean shouldStoreMessage(Message message) {
    if (message.getType() != Type.chat) {
      return false;
    }

    if (StringUtils.isBlank(message.getBody())) {
      return false;
    }

    JID recipient = message.getTo();
    String username = recipient.getNode();
    // If the username is null (such as when an anonymous user), don't store.
    if (username == null || !UserManager.getInstance().isRegisteredUser(recipient)) {
      return false;
    }

    JID sender = message.getFrom();
    String senderUser = sender.getNode();
    // If the username is null (such as when an anonymous user), don't store.
    if (senderUser == null || !UserManager.getInstance().isRegisteredUser(sender)) {
      return false;
    }

    // Do not store messages sent to users of remote servers
    if (!XMPPServer.getInstance().getServerInfo().getXMPPDomain().equals(recipient.getDomain())) {
      return false;
    }
    return true;
  }

  private void saveHistoryMessage(HistoryMessage historyMessage) {

    Connection con = null;
    PreparedStatement statement = null;
    try {
      con = DbConnectionManager.getConnection();
      statement = con.prepareStatement(CREATE_HISTORY_SQL);
      statement.setString(1, historyMessage.getFromUser());
      statement.setString(2, historyMessage.getToUser());
      statement.setString(3, historyMessage.getFromJID());
      statement.setString(4, historyMessage.getToJID());
      statement.setString(5, historyMessage.getCreationDate());
      statement.setString(6, historyMessage.getStanza());
      statement.executeUpdate();

    } catch (Exception ex) {
      ex.printStackTrace();
      LOGGER.error("history save error", ex);
    } finally {
      DbConnectionManager.closeConnection(statement, con);
    }
  }


}
