package com.hi.handy.authapi.plugin.dao;

import com.hi.handy.authapi.plugin.entity.HdGroupEntity;
import com.hi.handy.authapi.plugin.exception.BusinessException;
import com.hi.handy.authapi.plugin.exception.ExceptionConst;
import org.jivesoftware.database.DbConnectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class HdGroupDao extends BaseDao {
  private static final Logger LOGGER = LoggerFactory.getLogger(HdGroupDao.class);
  private HdGroupDao() {
  }

  public static final HdGroupDao INSTANCE = new HdGroupDao();

  public static HdGroupDao getInstance() {
    return INSTANCE;
  }

  private static final String ALL_COLUMN = "id, name, icon, type, welcomeMessage, displayName, createDate";
  private static final String SEARCH_BY_ID_SQL =  "SELECT "+ALL_COLUMN+" FROM hdGroup WHERE id = ?";
  private static final String SEARCH_BY_GROUPNAME_SQL =  "SELECT "+ALL_COLUMN+" FROM hdGroup WHERE name = ?";
  private static final String SEARCH_COUNT_SQL =  "SELECT count(1) FROM hdGroup WHERE id = ?";
  private static final String INSERT_SQL =  "INSERT INTO hdGroup ("+ALL_COLUMN+") VALUES(?,?,?,?,?,?,?)";

  public HdGroupEntity searchById(String id) {
    HdGroupEntity hdGroupEntity = new HdGroupEntity();
    Connection con = null;
    PreparedStatement pstmt = null;
    ResultSet rs = null;
    try {
      con = DbConnectionManager.getConnection();
      pstmt = con.prepareStatement(SEARCH_BY_ID_SQL);
      pstmt.setString(1, id);
      rs = pstmt.executeQuery();
      while (rs.next()) {
        hdGroupEntity.setId(rs.getString(1));
        hdGroupEntity.setName(rs.getString(2));
        hdGroupEntity.setIcon(rs.getString(3));
        hdGroupEntity.setType(rs.getString(4));
        hdGroupEntity.setWelcomeMessage(rs.getString(5));
        hdGroupEntity.setDisplayName(rs.getString(6));
        hdGroupEntity.setCreateDate(rs.getTimestamp(7));
      }
    } catch (Exception e) {
      e.printStackTrace();
      LOGGER.error("searchAgentByGroupId error", e);
      throw new BusinessException(ExceptionConst.DB_ERROR, e.getMessage());
    } finally {
      DbConnectionManager.closeConnection(rs, pstmt, con);
    }
    return hdGroupEntity;
  }

  public long searchCountById(String id){
    Connection con = null;
    PreparedStatement pstmt = null;
    ResultSet rs = null;
    long count = 0;
    try {
      con = DbConnectionManager.getConnection();
      pstmt = con.prepareStatement(SEARCH_COUNT_SQL);
      pstmt.setString(1, id);
      rs = pstmt.executeQuery();
      if(rs.next()){
        count  = rs.getLong(1);
      }
    } catch (Exception e) {
      e.printStackTrace();
      LOGGER.error("isGroupExist error", e);
      throw new BusinessException(ExceptionConst.DB_ERROR, e.getMessage());
    } finally {
      DbConnectionManager.closeConnection(rs, pstmt, con);
    }
    return count;
  }

  public boolean createGroup(HdGroupEntity hdGroupEntity) {
    Connection con = null;
    PreparedStatement pstmt = null;
    boolean result = false;
    try {
      con = DbConnectionManager.getConnection();
      pstmt = con.prepareStatement(INSERT_SQL);
      pstmt.setString(1, hdGroupEntity.getId());
      pstmt.setString(2, hdGroupEntity.getName());
      pstmt.setString(3, hdGroupEntity.getIcon());
      pstmt.setString(4, hdGroupEntity.getType());
      pstmt.setString(5, hdGroupEntity.getWelcomeMessage());
      pstmt.setString(6, hdGroupEntity.getDisplayName());
      pstmt.setTimestamp(7, hdGroupEntity.getCreateDate());
      if(pstmt.executeUpdate()>0){
        result =  true;
      }
    } catch (Exception e) {
      e.printStackTrace();
      LOGGER.error("createGroup error", e);
      throw new BusinessException(ExceptionConst.DB_ERROR, e.getMessage());
    } finally {
      DbConnectionManager.closeConnection(pstmt, con);
    }
    return result;
  }

  public HdGroupEntity searchByGroupName(String groupName) {
    HdGroupEntity hdGroupEntity = new HdGroupEntity();
    Connection con = null;
    PreparedStatement pstmt = null;
    ResultSet rs = null;
    try {
      con = DbConnectionManager.getConnection();
      pstmt = con.prepareStatement(SEARCH_BY_GROUPNAME_SQL);
      pstmt.setString(1, groupName);
      rs = pstmt.executeQuery();
      while (rs.next()) {
        hdGroupEntity.setId(rs.getString(1));
        hdGroupEntity.setName(rs.getString(2));
        hdGroupEntity.setIcon(rs.getString(3));
        hdGroupEntity.setType(rs.getString(4));
        hdGroupEntity.setWelcomeMessage(rs.getString(5));
        hdGroupEntity.setDisplayName(rs.getString(6));
        hdGroupEntity.setCreateDate(rs.getTimestamp(7));
      }
    } catch (Exception e) {
      e.printStackTrace();
      LOGGER.error("searchByGroupName error", e);
      throw new BusinessException(ExceptionConst.DB_ERROR, e.getMessage());
    } finally {
      DbConnectionManager.closeConnection(rs, pstmt, con);
    }
    return hdGroupEntity;
  }
}
