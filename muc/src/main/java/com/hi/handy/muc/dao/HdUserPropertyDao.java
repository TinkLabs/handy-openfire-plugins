package com.hi.handy.muc.dao;

import lombok.extern.slf4j.Slf4j;
import org.jivesoftware.database.DbConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class HdUserPropertyDao extends BaseDao {

  private HdUserPropertyDao() {}
  public static final HdUserPropertyDao INSTANCE = new HdUserPropertyDao();
  public static HdUserPropertyDao getInstance() {
    return INSTANCE;
  }

  public List<String> searchAdminAgentName() {
    String SEARCH_ADMIN_AGENT_NAME_SQL = "SELECT userName FROM hdUserProperty WHERE userName type = 'ADMIN'";
    List<String> result = new ArrayList();
    Connection con = null;
    PreparedStatement pstmt = null;
    ResultSet rs = null;
    try {
      con = DbConnectionManager.getConnection();
      pstmt = con.prepareStatement(SEARCH_ADMIN_AGENT_NAME_SQL);
      rs = pstmt.executeQuery();
      while (rs.next()) {
        result.add(rs.getString(1));
      }
    } catch (Exception e) {
      e.printStackTrace();
      log.error("searchAdminAgentName error ",e);
    } finally {
      DbConnectionManager.closeConnection(rs, pstmt, con);
    }
    return result;
  }
}
