package com.hi.handy.auth.plugin.dao;

import com.hi.handy.auth.plugin.entity.UserRoomEntity;
import com.hi.handy.auth.plugin.exception.BusinessException;
import com.hi.handy.auth.plugin.exception.ExceptionConst;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.jivesoftware.database.DbConnectionManager;

public class HdUserRoomDao extends BaseDao {

  private HdUserRoomDao() {
  }

  public static final HdUserRoomDao INSTANCE = new HdUserRoomDao();

  public static HdUserRoomDao getInstance() {
    return INSTANCE;
  }

  private static final String CREATE_SQL =
      "INSERT INTO hdUserRoom (zoneId, userName, roomName) VALUES (?, ?, ?)";
  private static final String SEARCH_BY_ROOM_SQL =
      "SELECT * FROM hdUserRoom WHERE roomName = ?";
  private static final String SEARCH_BY_ZONE_AND_ROOM_SQL =
      "SELECT * FROM hdUserRoom WHERE zoneId = ? AND userName = ?";
  private static final String COUNT_SQL =
      "SELECT count(1) FROM hdUserRoom WHERE userName = ?";
  private static final String DELETE_SQL = "DELETE FROM hdUserRoom WHERE roomName = ?";

  public List<UserRoomEntity> searchByRoomName(String roomName) {
    List<UserRoomEntity> result = new ArrayList<UserRoomEntity>(2);
    Connection con = null;
    PreparedStatement pstmt = null;
    ResultSet rs = null;
    try {
      con = DbConnectionManager.getConnection();
      pstmt = con.prepareStatement(SEARCH_BY_ROOM_SQL);
      pstmt.setString(1, roomName);
      rs = pstmt.executeQuery();
      while (rs.next()) {
        UserRoomEntity userRoom = new UserRoomEntity();
        userRoom.setId(rs.getLong(1));
        userRoom.setZoneId(rs.getLong(2));
        userRoom.setUserName(rs.getString(3));
        userRoom.setRoomName(rs.getString(4));
        result.add(userRoom);
      }
    } catch (Exception e) {
      throw new BusinessException(ExceptionConst.DB_ERROR, e.getMessage());
    } finally {
      DbConnectionManager.closeConnection(rs, pstmt, con);
    }
    return result;
  }

  public List<UserRoomEntity> searchByZoneAndRoomName(Long zoneId, String roomName) {
    List<UserRoomEntity> result = new ArrayList<UserRoomEntity>();
    Connection con = null;
    PreparedStatement pstmt = null;
    ResultSet rs = null;
    try {
      con = DbConnectionManager.getConnection();
      pstmt = con.prepareStatement(SEARCH_BY_ZONE_AND_ROOM_SQL);
      pstmt.setLong(1, zoneId);
      pstmt.setString(2, roomName);
      rs = pstmt.executeQuery();
      while (rs.next()) {
        UserRoomEntity userRoom = new UserRoomEntity();
        userRoom.setId(rs.getLong(1));
        userRoom.setZoneId(rs.getLong(2));
        userRoom.setUserName(rs.getString(3));
        userRoom.setRoomName(rs.getString(4));
        result.add(userRoom);
      }
    } catch (Exception e) {
      throw new BusinessException(ExceptionConst.DB_ERROR, e.getMessage());
    } finally {
      DbConnectionManager.closeConnection(rs, pstmt, con);
    }
    return result;
  }

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

  public void create(Long zoneId, String userName, String roomName) {
    Connection con = null;
    PreparedStatement pstmt = null;
    try {
      int i = 1;
      con = DbConnectionManager.getConnection();
      pstmt = con.prepareStatement(CREATE_SQL);
      pstmt.setLong(i++, zoneId);
      pstmt.setString(i++, userName);
      pstmt.setString(i++, roomName);
      pstmt.executeUpdate();
    } catch (SQLException e) {
      throw new BusinessException(ExceptionConst.DB_ERROR, e.getMessage(), e);
    } finally {
      DbConnectionManager.closeConnection(pstmt, con);
    }
  }

  public void deleteByRoomName(String roomName) {
    Connection con = null;
    PreparedStatement pstmt = null;
    try {
      con = DbConnectionManager.getConnection();
      pstmt = con.prepareStatement(DELETE_SQL);
      pstmt.setString(1, roomName);
      pstmt.executeUpdate();
    } catch (SQLException e) {
      throw new BusinessException(ExceptionConst.DB_ERROR, e.getMessage(), e);
    } finally {
      DbConnectionManager.closeConnection(pstmt, con);
    }
  }
}
