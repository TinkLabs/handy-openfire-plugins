package com.hi.handy.authapi.plugin.dao;

import com.hi.handy.authapi.plugin.exception.BusinessException;
import com.hi.handy.authapi.plugin.exception.ExceptionConst;
import org.jivesoftware.database.DbConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class HdGroupAgentDao extends BaseDao {

  private HdGroupAgentDao() {
  }

  public static final HdGroupAgentDao INSTANCE = new HdGroupAgentDao();

  public static HdGroupAgentDao getInstance() {
    return INSTANCE;
  }

  private static final String SEARCH_BY_USERNAME_SQL = "SELECT groupId FROM hdGroupAgent WHERE userName = ?";

  private static final String SEARCH_BY_GROUP_SQL = "SELECT userName FROM hdGroupAgent WHERE groupId = ?";

  public List<String> searchByUserName(String userName) {
    List<String> result = new ArrayList();
    Connection con = null;
    PreparedStatement pstmt = null;
    ResultSet rs = null;
    try {
      con = DbConnectionManager.getConnection();
      pstmt = con.prepareStatement(SEARCH_BY_USERNAME_SQL);
      pstmt.setString(1, userName);
      rs = pstmt.executeQuery();
      while (rs.next()) {
        result.add(rs.getString(1));
      }
    } catch (Exception e) {
      throw new BusinessException(ExceptionConst.DB_ERROR, e.getMessage());
    } finally {
      DbConnectionManager.closeConnection(rs, pstmt, con);
    }
    return result;
  }

  public List<String> searchAgentByGroupId(String userName) {
    List<String> result = new ArrayList();
    Connection con = null;
    PreparedStatement pstmt = null;
    ResultSet rs = null;
    try {
      con = DbConnectionManager.getConnection();
      pstmt = con.prepareStatement(SEARCH_BY_GROUP_SQL);
      pstmt.setString(1, userName);
      rs = pstmt.executeQuery();
      while (rs.next()) {
        result.add(rs.getString(1));
      }
    } catch (Exception e) {
      throw new BusinessException(ExceptionConst.DB_ERROR, e.getMessage());
    } finally {
      DbConnectionManager.closeConnection(rs, pstmt, con);
    }
    return result;
  }
}