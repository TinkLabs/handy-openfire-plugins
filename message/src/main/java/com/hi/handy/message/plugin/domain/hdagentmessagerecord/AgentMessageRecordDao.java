package com.hi.handy.message.plugin.domain.hdagentmessagerecord;

import com.hi.handy.message.plugin.domain.BaseDao;
import com.hi.handy.message.plugin.domain.hdmessage.HdMessageDao;
import org.jivesoftware.database.DbConnectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class AgentMessageRecordDao extends BaseDao {
  private static final Logger LOGGER = LoggerFactory.getLogger(HdMessageDao.class);
  private AgentMessageRecordDao() {
  }

  public static final AgentMessageRecordDao INSTANCE = new AgentMessageRecordDao();

  public static AgentMessageRecordDao getInstance() {
    return INSTANCE;
  }

  private static final String UPDATE_SQL = "UPDATE hdAgentMessageRecord t SET unReadCount = 0 WHERE userName = ? AND roomName = ? ";

  public void updateByUserNameAndRoomName(String userName, String roomName){
    Connection con = null;
    PreparedStatement pstmt = null;
    try {
      int i = 1;
      con = DbConnectionManager.getConnection();
      pstmt = con.prepareStatement(UPDATE_SQL);
      pstmt.setString(i++, userName);
      pstmt.setString(i++, roomName);
      pstmt.executeUpdate();
    } catch (Exception ex) {
      ex.printStackTrace();
      LOGGER.error("updateById error", ex);
    } finally {
      DbConnectionManager.closeConnection(pstmt, con);
    }
  }
}
