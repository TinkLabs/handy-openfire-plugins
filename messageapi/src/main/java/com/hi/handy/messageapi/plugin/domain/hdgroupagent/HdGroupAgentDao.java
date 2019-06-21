package com.hi.handy.messageapi.plugin.domain.hdgroupagent;

import com.hi.handy.messageapi.plugin.domain.BaseDao;
import com.hi.handy.messageapi.plugin.exception.BusinessException;
import com.hi.handy.messageapi.plugin.exception.ExceptionConst;
import org.jivesoftware.database.DbConnectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class HdGroupAgentDao extends BaseDao {
  private static final Logger LOGGER = LoggerFactory.getLogger(HdGroupAgentDao.class);
  private HdGroupAgentDao() {
  }

  public static final HdGroupAgentDao INSTANCE = new HdGroupAgentDao();

  public static HdGroupAgentDao getInstance() {
    return INSTANCE;
  }

  private static final String SEARCH_BY_USERNAME_SQL = "SELECT groupId FROM hdGroupAgent WHERE userName = ?";

  public String searchByUserName(String userName) {
    String groupId = null;
    Connection con = null;
    PreparedStatement pstmt = null;
    ResultSet rs = null;
    try {
      con = DbConnectionManager.getConnection();
      pstmt = con.prepareStatement(SEARCH_BY_USERNAME_SQL);
      pstmt.setString(1, userName);
      rs = pstmt.executeQuery();
      while (rs.next()) {
        groupId = rs.getString(1);
      }
    } catch (Exception ex) {
      ex.printStackTrace();
      LOGGER.error("searchByUserName error", ex);
      throw new BusinessException(ExceptionConst.DB_ERROR, ex.getMessage());
    } finally {
      DbConnectionManager.closeConnection(rs, pstmt, con);
    }
    return groupId;
  }
}
