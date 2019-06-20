package com.hi.handy.message.plugin.domain.hdagentmessagerecord;

import com.hi.handy.message.plugin.domain.BaseDao;
import com.hi.handy.message.plugin.domain.hdmessage.HdMessageDao;
import org.jivesoftware.database.DbConnectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;

public class AgentMessageRecordDao extends BaseDao {
  private static final Logger LOGGER = LoggerFactory.getLogger(HdMessageDao.class);
  private AgentMessageRecordDao() {
  }

  public static final AgentMessageRecordDao INSTANCE = new AgentMessageRecordDao();

  public static AgentMessageRecordDao getInstance() {
    return INSTANCE;
  }

  private static final String UPDATE_SQL = "UPDATE hdAgentMessageRecord t SET readCount = ?,updateDate = ? WHERE userName = ? AND roomName = ? ";
  private static final String CREATE_SQL = "INSERT INTO hdAgentMessageRecord (id, userName, roomName, readCount, updateDate) VALUES (?, ?, ?, ?, ?)";
  private static final String SEARCH_BY_ROOMNAME_SQL = "SELECT * FROM hdAgentMessageRecord WHERE userName = ? AND roomName = ? ";

  public void updateByUserNameAndRoomName(String userName, String roomName,Long readCount){
    Connection con = null;
    PreparedStatement pstmt = null;
    try {
      int i = 1;
      con = DbConnectionManager.getConnection();
      pstmt = con.prepareStatement(UPDATE_SQL);
      pstmt.setLong(i++, readCount);
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

  public void create(String id, String userName, String roomName, Long readCount, Timestamp updateDate){
    Connection con = null;
    PreparedStatement pstmt = null;
    try {
      int i = 1;
      con = DbConnectionManager.getConnection();
      pstmt = con.prepareStatement(CREATE_SQL);
      pstmt.setString(i++, id);
      pstmt.setString(i++, userName);
      pstmt.setString(i++, roomName);
      pstmt.setLong(i++, readCount);
      pstmt.setTimestamp(i++, updateDate);
      pstmt.executeUpdate();
    } catch (Exception ex) {
      ex.printStackTrace();
      LOGGER.error("updateById error", ex);
    } finally {
      DbConnectionManager.closeConnection(pstmt, con);
    }
  }

  public AgentMessageRecordEntity findByAgentNameAndRoomName(String agentName,String roomName){
    AgentMessageRecordEntity result = null;
    Connection con = null;
    PreparedStatement pstmt = null;
    ResultSet rs = null;
    try {
      con = DbConnectionManager.getConnection();
      pstmt = con.prepareStatement(SEARCH_BY_ROOMNAME_SQL);
      pstmt.setString(1, roomName);
      rs = pstmt.executeQuery();
      while (rs.next()) {
        result = new AgentMessageRecordEntity();
        result.setId(rs.getString(1));
//        result.set(rs.getString(2));
//        result.setAmount(rs.getLong(3));
        result.setUpdateDate(rs.getTimestamp(4));
      }
    } catch (Exception ex) {
      ex.printStackTrace();
      LOGGER.error("findByRoomName error", ex);
    } finally {
      DbConnectionManager.closeConnection(rs, pstmt, con);
    }
    return result;
  }

}
