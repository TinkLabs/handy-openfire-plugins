package com.hi.handy.authapi.plugin.dao;

import com.hi.handy.authapi.plugin.entity.HdGroupAgentEntity;
import com.hi.handy.authapi.plugin.exception.BusinessException;
import com.hi.handy.authapi.plugin.exception.ExceptionConst;
import org.jivesoftware.database.DbConnectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class HdGroupAgentDao extends BaseDao {
  private static final Logger LOGGER = LoggerFactory.getLogger(HdGroupAgentDao.class);
  private HdGroupAgentDao() {
  }

  public static final HdGroupAgentDao INSTANCE = new HdGroupAgentDao();

  public static HdGroupAgentDao getInstance() {
    return INSTANCE;
  }

  private static final String SEARCH_BY_USERNAME_SQL = "SELECT groupId FROM hdGroupAgent WHERE userName = ?";
  private static final String INSERT_SQL = "INSERT INTO hdGroupAgent (id, groupId, userName, createDate) VALUES(?,?,?,?)";
  private static final String SEARCH_BY_GROUP_SQL = "SELECT userName FROM hdGroupAgent WHERE groupId = ?";

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
    } catch (Exception e) {
      e.printStackTrace();
      LOGGER.error("searchByUserName error", e);
      throw new BusinessException(ExceptionConst.DB_ERROR, e.getMessage());
    } finally {
      DbConnectionManager.closeConnection(rs, pstmt, con);
    }
    return groupId;
  }

  public List<String> searchAgentByGroupId(String groupId) {
    List<String> result = new ArrayList();
    Connection con = null;
    PreparedStatement pstmt = null;
    ResultSet rs = null;
    try {
      con = DbConnectionManager.getConnection();
      pstmt = con.prepareStatement(SEARCH_BY_GROUP_SQL);
      pstmt.setString(1, groupId);
      rs = pstmt.executeQuery();
      while (rs.next()) {
        result.add(rs.getString(1));
      }
    } catch (Exception e) {
      e.printStackTrace();
      LOGGER.error("searchAgentByGroupId error", e);
      throw new BusinessException(ExceptionConst.DB_ERROR, e.getMessage());
    } finally {
      DbConnectionManager.closeConnection(rs, pstmt, con);
    }
    return result;
  }

  public boolean createGroupAgent(HdGroupAgentEntity hdGroupAgent) {
    Connection con = null;
    PreparedStatement pstmt = null;
    int rs = 0;
    boolean r = false;
    try {
      con = DbConnectionManager.getConnection();
      pstmt = con.prepareStatement(INSERT_SQL);
      pstmt.setString(1, hdGroupAgent.getId());
      pstmt.setString(2, hdGroupAgent.getGroupId());
      pstmt.setString(3, hdGroupAgent.getUserName());
      pstmt.setTimestamp(4, hdGroupAgent.getCreateDate());
      rs = pstmt.executeUpdate();
      if(rs>0){
        r =  true;
      }else{
        r = false;
      }
    } catch (Exception e) {
      e.printStackTrace();
      LOGGER.error("createGroupAgent error", e);
      throw new BusinessException(ExceptionConst.DB_ERROR, e.getMessage());
    } finally {
      DbConnectionManager.closeConnection(pstmt, con);
    }
    return r;
  }
}
