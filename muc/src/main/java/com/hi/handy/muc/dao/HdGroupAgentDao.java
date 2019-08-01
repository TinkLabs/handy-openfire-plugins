package com.hi.handy.muc.dao;

import lombok.extern.slf4j.Slf4j;
import org.jivesoftware.database.DbConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class HdGroupAgentDao extends BaseDao {

  private HdGroupAgentDao() {}
  private static HdGroupAgentDao INSTANCE = new HdGroupAgentDao();
  public static HdGroupAgentDao getInstance() {
    return INSTANCE;
  }

  public List<String> searchGroupNameByAgentName(String agentNames) {
    String SEARCH_GROUP_NAME_BY_AGENT_NAMES_SQL = "SELECT * FROM hdGroupAgent WHERE userName in ("+agentNames+")";
    List<String> result = new ArrayList();
    Connection con = null;
    PreparedStatement pstmt = null;
    ResultSet rs = null;
    try {
      con = DbConnectionManager.getConnection();
      pstmt = con.prepareStatement(SEARCH_GROUP_NAME_BY_AGENT_NAMES_SQL);
      rs = pstmt.executeQuery();
      while (rs.next()) {
        result.add(rs.getString(1));
      }
    } catch (Exception e) {
      e.printStackTrace();
      log.error("searchGroupNameByAgentName error ",e);
    } finally {
      DbConnectionManager.closeConnection(rs, pstmt, con);
    }
    return result;
  }
}
