package com.hi.handy.messageapi.plugin.domain.hdagentmessagerecord;

import com.hi.handy.messageapi.plugin.domain.BaseDao;
import com.hi.handy.messageapi.plugin.exception.BusinessException;
import com.hi.handy.messageapi.plugin.exception.ExceptionConst;
import org.jivesoftware.database.DbConnectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class AgentMessageRecordDao extends BaseDao {
  private static final Logger LOGGER = LoggerFactory.getLogger(AgentMessageRecordDao.class);
  private AgentMessageRecordDao() {
  }

  public static final AgentMessageRecordDao INSTANCE = new AgentMessageRecordDao();

  public static AgentMessageRecordDao getInstance() {
    return INSTANCE;
  }

  private static final String SEARCH_BY_ROOMNAME_SQL = "SELECT readCount FROM hdAgentMessageRecord WHERE userName = ? AND roomName = ? ";

  public Long findByAgentNameAndRoomName(String agentName,String roomName){
    Long readCount = 0l;
    Connection con = null;
    PreparedStatement pstmt = null;
    ResultSet rs = null;
    try {
      con = DbConnectionManager.getConnection();
      pstmt = con.prepareStatement(SEARCH_BY_ROOMNAME_SQL);
      pstmt.setString(1, agentName);
      pstmt.setString(2, roomName);
      rs = pstmt.executeQuery();
      while (rs.next()) {
        readCount = rs.getLong(1);
      }
    } catch (Exception ex) {
      ex.printStackTrace();
      LOGGER.error("findByAgentNameAndRoomName error", ex);
      throw new BusinessException(ExceptionConst.DB_ERROR, ex.getMessage());
    } finally {
      DbConnectionManager.closeConnection(rs, pstmt, con);
    }
    return readCount;
  }
}
