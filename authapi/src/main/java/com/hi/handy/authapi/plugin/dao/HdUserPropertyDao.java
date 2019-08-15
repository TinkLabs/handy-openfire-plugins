package com.hi.handy.authapi.plugin.dao;

import com.hi.handy.authapi.plugin.entity.AgentStatus;
import com.hi.handy.authapi.plugin.entity.HdUserPropertyEntity;
import com.hi.handy.authapi.plugin.exception.BusinessException;
import com.hi.handy.authapi.plugin.exception.ExceptionConst;
import com.hi.handy.authapi.plugin.parameter.AuthParameter;
import org.apache.commons.lang3.StringUtils;
import org.jivesoftware.database.DbConnectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;

public class HdUserPropertyDao extends BaseDao {
  private static final Logger LOGGER = LoggerFactory.getLogger(HdUserPropertyDao.class);

  private HdUserPropertyDao() {}
  private static final HdUserPropertyDao INSTANCE = new HdUserPropertyDao();
  public static HdUserPropertyDao getInstance() {
    return INSTANCE;
  }

  private static final String COUNT_SQL                   = "SELECT count(1) FROM hdUserProperty WHERE userName = ?";
  private static final String CREATE_SQL                  = "INSERT INTO hdUserProperty (userName, displayName, password, zoneId, zoneName, hotelId, hotelName, roomNum, type, creationDate) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
  private static final String UPDATE_ALL_SQL              = "UPDATE hdUserProperty t SET zoneId = ?, zoneName = ?, hotelId = ?, hotelName = ?, roomNum = ?,displayName = ? WHERE userName = ?";
  private static final String SEARCH_BY_NAME_SQL          = "SELECT * FROM hdUserProperty WHERE userName = ?";
  private static final String UPDATE_STATUS_SQL           = "UPDATE hdUserProperty t SET status = ? WHERE userName = ?";

  public Long countByUserName(String userName) {
    Connection con = null;
    PreparedStatement pstmt = null;
    ResultSet rs = null;
    Long count = null;
    try {
      con = DbConnectionManager.getConnection();
      pstmt = con.prepareStatement(COUNT_SQL);
      pstmt.setString(1, userName);
      rs = pstmt.executeQuery();
      if (rs.next()) {
        count = rs.getLong(1);
      }
    } catch (Exception e) {
      e.printStackTrace();
      LOGGER.error("countByUserName error", e);
      throw new BusinessException(ExceptionConst.DB_ERROR,"Unable to count UserProperty for userName " + userName, e);
    } finally {
      DbConnectionManager.closeConnection(rs, pstmt, con);
    }
    return count;
  }

  public void createUserProperty(String userName, AuthParameter parameter) {
    Connection con = null;
    PreparedStatement pstmt = null;
    try {
      int i = 1;
      con = DbConnectionManager.getConnection();
      pstmt = con.prepareStatement(CREATE_SQL);
      if(StringUtils.isNoneBlank(userName)){
        pstmt.setString(i++, userName);
      }else{
        pstmt.setNull(i++, Types.VARCHAR);
      }
      if(StringUtils.isNoneBlank(parameter.getDisplayName())){
        pstmt.setString(i++, parameter.getDisplayName());
      }else{
        pstmt.setNull(i++, Types.VARCHAR);
      }
      if(StringUtils.isNoneBlank(parameter.getPassword())){
        pstmt.setString(i++, parameter.getPassword());
      }else{
        pstmt.setNull(i++, Types.VARCHAR);
      }
      if (parameter.getZoneId() != null) {
        pstmt.setLong(i++, parameter.getZoneId());
      }else{
        pstmt.setNull(i++, Types.INTEGER);
      }
      if (StringUtils.isNoneBlank(parameter.getZoneName())) {
        pstmt.setString(i++, parameter.getZoneName());
      } else {
        pstmt.setNull(i++, Types.VARCHAR);
      }
      if (parameter.getHotelId() != null) {
        pstmt.setLong(i++, parameter.getHotelId());
      } else {
        pstmt.setNull(i++, Types.INTEGER);
      }
      if (StringUtils.isNoneBlank(parameter.getHotelName())) {
        pstmt.setString(i++, parameter.getHotelName());
      } else {
        pstmt.setNull(i++, Types.VARCHAR);
      }
      if (StringUtils.isNoneBlank(parameter.getRoomNum())) {
        pstmt.setString(i++, parameter.getRoomNum());
      } else {
        pstmt.setNull(i++, Types.VARCHAR);
      }
      pstmt.setString(i++, parameter.getUserType().name());
      pstmt.setTimestamp(i++, new Timestamp(System.currentTimeMillis()));
      pstmt.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
      LOGGER.error("createUserProperty error", e);
      throw new BusinessException(ExceptionConst.DB_ERROR, e.getMessage(), e);
    } finally {
      DbConnectionManager.closeConnection(pstmt, con);
    }
  }

  public void updateAllUserProperty(String userName, AuthParameter parameter) {

    Connection con = null;
    PreparedStatement pstmt = null;
    try {
      int i = 1;
      con = DbConnectionManager.getConnection();
      pstmt = con.prepareStatement(UPDATE_ALL_SQL);
      pstmt.setLong(i++, parameter.getZoneId());
      pstmt.setString(i++, parameter.getZoneName());
      pstmt.setLong(i++, parameter.getHotelId());
      pstmt.setString(i++, parameter.getHotelName());
      pstmt.setString(i++, parameter.getRoomNum());
      pstmt.setString(i++, parameter.getDisplayName());
      pstmt.setString(i++, userName);
      pstmt.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
      LOGGER.error("updateAllUserProperty error", e);
      throw new BusinessException(ExceptionConst.DB_ERROR, e.getMessage(), e);
    } finally {
      DbConnectionManager.closeConnection(pstmt, con);
    }
  }

  public HdUserPropertyEntity searchByUserName(String userName) {
    HdUserPropertyEntity result = null;
    Connection con = null;
    PreparedStatement pstmt = null;
    ResultSet rs = null;
    try {
      con = DbConnectionManager.getConnection();
      pstmt = con.prepareStatement(SEARCH_BY_NAME_SQL);
      pstmt.setString(1, userName);
      rs = pstmt.executeQuery();
      while (rs.next()) {
        result = new HdUserPropertyEntity();
        result.setUserName(rs.getString(1));
        result.setDisplayName(rs.getString(2));
        result.setPassword(rs.getString(3));
        result.setZoneId(rs.getLong(4));
        result.setZoneName(rs.getString(5));
        result.setHotelId(rs.getLong(6));
        result.setHotelName(rs.getString(7));
        result.setRoomNum(rs.getString(8));
        result.setType(rs.getString(9));
        result.setCreationDate(rs.getTimestamp(10));
        result.setModificationDamodificationDate(rs.getTimestamp(11));
        result.setStatus(rs.getString(12));
      }
    } catch (Exception e) {
      e.printStackTrace();
      LOGGER.error("searchByUserName error", e);
      throw new BusinessException(ExceptionConst.DB_ERROR, e.getMessage());
    } finally {
      DbConnectionManager.closeConnection(rs, pstmt, con);
    }
    return result;
  }

  public void updateStatus(String userName, String status) {

    Connection con = null;
    PreparedStatement pstmt = null;
    try {
      int i = 1;
      con = DbConnectionManager.getConnection();
      pstmt = con.prepareStatement(UPDATE_STATUS_SQL);
      pstmt.setString(i++, status);
      pstmt.setString(i++, userName);
      pstmt.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
      LOGGER.error("updateStatus error", e);
      throw new BusinessException(ExceptionConst.DB_ERROR, e.getMessage(), e);
    } finally {
      DbConnectionManager.closeConnection(pstmt, con);
    }
  }

  public Long searchLoginCount(String userNames, AgentStatus agentStatus){
    String sql = "SELECT count(1) FROM hdUserProperty WHERE userName in (" + userNames + ") AND status = '"+agentStatus.name()+"'";
    Connection con = null;
    PreparedStatement pstmt = null;
    ResultSet rs = null;
    Long count = null;
    try {
      con = DbConnectionManager.getConnection();
      pstmt = con.prepareStatement(sql);
      System.out.println(pstmt);
      rs = pstmt.executeQuery();
      if (rs.next()) {
        count = rs.getLong(1);
      }
    } catch (Exception e) {
      e.printStackTrace();
      LOGGER.error("searchLoginCount error", e);
      throw new BusinessException(ExceptionConst.DB_ERROR, e.getMessage(), e);
    } finally {
      DbConnectionManager.closeConnection(rs, pstmt, con);
    }
    return count;
  }
}
