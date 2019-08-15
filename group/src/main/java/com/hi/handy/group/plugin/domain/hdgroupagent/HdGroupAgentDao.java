package com.hi.handy.group.plugin.domain.hdgroupagent;

import com.hi.handy.group.plugin.domain.BaseDao;
import com.hi.handy.group.plugin.exception.BusinessException;
import com.hi.handy.group.plugin.exception.ExceptionConst;
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

  private static final String SEARCH_SQL = "SELECT * FROM hdGroupAgent";
  private static final String DELETE_SQL = "DELETE FROM hdGroupAgent WHERE id = ?";
  private static final String INSERT_SQL = "INSERT INTO hdGroupAgent (id, groupId, userName, createDate) VALUES(?,?,?,?)";
  private static final String COUNT_BY_AGENTNAME_SQL = "SELECT count(1) FROM hdGroupAgent WHERE userName = ?";
  private static final String SEARCH_BY_ID_SQL =  "SELECT * FROM hdGroupAgent WHERE id = ?";

  public int countByUserName(String userName) {
    int count = 0;
    Connection con = null;
    PreparedStatement pstmt = null;
    ResultSet rs = null;
    try {
      con = DbConnectionManager.getConnection();
      pstmt = con.prepareStatement(COUNT_BY_AGENTNAME_SQL);
      pstmt.setString(1, userName);
      rs = pstmt.executeQuery();
      while (rs.next()) {
        count = rs.getInt(1);
      }
    } catch (Exception e) {
      e.printStackTrace();
      LOGGER.error("countByUserName error", e);
      throw new BusinessException(ExceptionConst.DB_ERROR, e.getMessage());
    } finally {
      DbConnectionManager.closeConnection(rs, pstmt, con);
    }
    return count;
  }

  public List<HdGroupAgentEntity> searchAll() {
    List<HdGroupAgentEntity> groupAgentEntityList = new ArrayList();
    Connection con = null;
    PreparedStatement pstmt = null;
    ResultSet rs = null;
    try {
      con = DbConnectionManager.getConnection();
      pstmt = con.prepareStatement(SEARCH_SQL);
      rs = pstmt.executeQuery();
      while (rs.next()) {
        HdGroupAgentEntity hdGroupAgentEntity = new HdGroupAgentEntity();
        hdGroupAgentEntity.setId(rs.getString(1));
        hdGroupAgentEntity.setGroupId(rs.getString(2));
        hdGroupAgentEntity.setUserName(rs.getString(3));
        hdGroupAgentEntity.setCreateDate(rs.getTimestamp(4));
        groupAgentEntityList.add(hdGroupAgentEntity);
      }
    } catch (Exception e) {
      e.printStackTrace();
      LOGGER.error("searchAll error", e);
      throw new BusinessException(ExceptionConst.DB_ERROR, e.getMessage());
    } finally {
      DbConnectionManager.closeConnection(rs, pstmt, con);
    }
    return groupAgentEntityList;
  }

  public boolean delete(String id) {
    Connection con = null;
    PreparedStatement pstmt = null;
    boolean result = false;
    try {
      con = DbConnectionManager.getConnection();
      pstmt = con.prepareStatement(DELETE_SQL);
      pstmt.setString(1, id);
      if(pstmt.executeUpdate()>0){
        result =  true;
      }
    } catch (Exception e) {
      e.printStackTrace();
      LOGGER.error("delete error", e);
      throw new BusinessException(ExceptionConst.DB_ERROR, e.getMessage());
    } finally {
      DbConnectionManager.closeConnection(pstmt, con);
    }
    return result;
  }

  public boolean createGroupAgent(HdGroupAgentEntity hdGroupAgent) {
    Connection con = null;
    PreparedStatement pstmt = null;
    boolean result = false;
    try {
      con = DbConnectionManager.getConnection();
      pstmt = con.prepareStatement(INSERT_SQL);
      pstmt.setString(1, hdGroupAgent.getId());
      pstmt.setString(2, hdGroupAgent.getGroupId());
      pstmt.setString(3, hdGroupAgent.getUserName());
      pstmt.setTimestamp(4, hdGroupAgent.getCreateDate());
      if(pstmt.executeUpdate() > 0){
        result =  true;
      }
    } catch (Exception e) {
      e.printStackTrace();
      LOGGER.error("createGroupAgent error", e);
      throw new BusinessException(ExceptionConst.DB_ERROR, e.getMessage());
    } finally {
      DbConnectionManager.closeConnection(pstmt, con);
    }
    return result;
  }

  public HdGroupAgentEntity searchGroupIdById(String id) {
    Connection con = null;
    PreparedStatement pstmt = null;
    ResultSet rs = null;
    HdGroupAgentEntity hdGroupAgentEntity = null;
    try {
      con = DbConnectionManager.getConnection();
      pstmt = con.prepareStatement(SEARCH_BY_ID_SQL);
      pstmt.setString(1, id);
      rs = pstmt.executeQuery();
      while (rs.next()) {
        hdGroupAgentEntity = new HdGroupAgentEntity();
        hdGroupAgentEntity.setId(rs.getString(1));
        hdGroupAgentEntity.setGroupId(rs.getString(2));
        hdGroupAgentEntity.setUserName(rs.getString(3));
        hdGroupAgentEntity.setCreateDate(rs.getTimestamp(4));
      }
    } catch (Exception e) {
      e.printStackTrace();
      LOGGER.error("searchGroupIdById error", e);
      throw new BusinessException(ExceptionConst.DB_ERROR, e.getMessage());
    } finally {
      DbConnectionManager.closeConnection(rs, pstmt, con);
    }
    return hdGroupAgentEntity;
  }
}
