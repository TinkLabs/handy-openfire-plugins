package com.hi.handy.auth.plugin.dao;

import com.hi.handy.auth.plugin.exception.BusinessException;
import com.hi.handy.auth.plugin.exception.ExceptionConst;
import com.hi.handy.auth.plugin.parameter.AuthParameter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.jivesoftware.database.DbConnectionManager;

public class HdUserPropertyDao extends BaseDao {

  private HdUserPropertyDao() {
  }

  public static final HdUserPropertyDao INSTANCE = new HdUserPropertyDao();

  public static HdUserPropertyDao getInstance() {
    return INSTANCE;
  }

  private static final String CREATE_SQL =
      "INSERT INTO hdUserProperty (userName, zoneId, zoneName, hotelId, hotelName, roomNum, type, "
          + "roomAmount, creationDate) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
  private static final String UPDATE_SQL =
      "UPDATE hdUserProperty t SET zoneId = ?, zoneName = ? "
          + "WHERE userName = ?";
  private static final String UPDATE_ALL_SQL =
      "UPDATE hdUserProperty t SET zoneId = ?, zoneName = ?, hotelId = ?, hotelName = ?, "
          + " roomNum = ? WHERE userName = ?";
  private static final String ROOM_AMOUNT_PLUS_ONE_SQL =
      "UPDATE hdUserProperty set roomAmount = roomAmount+1 where userName = ?";
  private static final String COUNT_SQL =
      "SELECT count(1) FROM hdUserProperty WHERE userName = ?";
  private static final String SEARCH_AGENT_SQL =
      "SELECT p.userName FROM hdUserProperty p, ofUser u WHERE p.userName = u.userName "
          + "AND p.type = 'AGENT' AND p.zoneId = ?";


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
      throw new BusinessException(ExceptionConst.DB_ERROR,
          "Unable to count UserProperty for userName " + userName, e);
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
      pstmt.setString(i++, userName);
      pstmt.setLong(i++, parameter.getZoneId());
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
      pstmt.setLong(i++, 0L);
      pstmt.setTimestamp(i++, new java.sql.Timestamp(System.currentTimeMillis()));
      pstmt.executeUpdate();
    } catch (SQLException e) {
      throw new BusinessException(ExceptionConst.DB_ERROR, e.getMessage(), e);
    } finally {
      DbConnectionManager.closeConnection(pstmt, con);
    }
  }

  public void updateUserProperty(String agentUserName, AuthParameter parameter) {

    Connection con = null;
    PreparedStatement pstmt = null;
    try {
      int i = 1;
      con = DbConnectionManager.getConnection();
      pstmt = con.prepareStatement(UPDATE_SQL);
      pstmt.setLong(i++, parameter.getZoneId());
      pstmt.setString(i++, parameter.getZoneName());
      pstmt.setString(i++, agentUserName);
      pstmt.executeUpdate();
    } catch (SQLException e) {
      throw new BusinessException(ExceptionConst.DB_ERROR, e.getMessage(), e);
    } finally {
      DbConnectionManager.closeConnection(pstmt, con);
    }
  }

  public void roomAmountPlusOne(String userName) {
    Connection con = null;
    PreparedStatement pstmt = null;
    try {
      con = DbConnectionManager.getConnection();
      pstmt = con.prepareStatement(ROOM_AMOUNT_PLUS_ONE_SQL);
      pstmt.setString(1, userName);
      pstmt.executeUpdate();
    } catch (SQLException e) {
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
      pstmt.setString(i++, userName);
      pstmt.executeUpdate();
    } catch (SQLException e) {
      throw new BusinessException(ExceptionConst.DB_ERROR, e.getMessage(), e);
    } finally {
      DbConnectionManager.closeConnection(pstmt, con);
    }
  }

  public List<String> searchByZone(Long zoneId) {
    List<String> result = new ArrayList<String>();
    Connection con = null;
    PreparedStatement pstmt = null;
    ResultSet rs = null;
    try {
      con = DbConnectionManager.getConnection();
      pstmt = con.prepareStatement(SEARCH_AGENT_SQL);
      pstmt.setLong(1, zoneId);
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


  public String searchMinRoomAmountUserName(List<String> users) {
    String result = null;
    StringBuilder in_sql = new StringBuilder();
    for (int i = 0; i < users.size(); i++) {
      in_sql.append("'" + users.get(i) + "'");
      if (i < users.size() - 1) {
        in_sql.append(",");
      }
    }

    String sql = new StringBuilder("SELECT userName FROM hdUserProperty WHERE userName in (")
        .append(in_sql).append(") order by roomAmount ASC LIMIT 1").toString();

    Connection con = null;
    PreparedStatement pstmt = null;
    ResultSet rs = null;
    try {
      con = DbConnectionManager.getConnection();
      pstmt = con.prepareStatement(sql);
      rs = pstmt.executeQuery();
      while (rs.next()) {
        result = rs.getString(1);
        return result;
      }
    } catch (Exception e) {
      throw new BusinessException(ExceptionConst.DB_ERROR, e.getMessage());
    } finally {
      DbConnectionManager.closeConnection(rs, pstmt, con);
    }
    return result;
  }
}
