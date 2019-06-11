package com.hi.handy.messageapi.plugin.domain.hduserproperty;

import com.hi.handy.messageapi.plugin.domain.BaseDao;
import com.hi.handy.messageapi.plugin.exception.BusinessException;
import com.hi.handy.messageapi.plugin.exception.ExceptionConst;
import com.hi.handy.messageapi.plugin.model.HotelIdAndRoomNum;
import org.jivesoftware.database.DbConnectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class HdUserPropertyDao extends BaseDao {
  private static final Logger LOGGER = LoggerFactory.getLogger(HdUserPropertyDao.class);
  private HdUserPropertyDao() {
  }

  public static final HdUserPropertyDao INSTANCE = new HdUserPropertyDao();

  public static HdUserPropertyDao getInstance() {
    return INSTANCE;
  }

  private static final String SEARCH_BY_NAME_SQL =
          "SELECT * FROM hdUserProperty WHERE userName = ?";
  private static final String SEARCH_HOTELID_AND_ROOMNUM_BY_AGENTNAME_SQL =
          "SELECT roomNum,hotelId FROM hdUserProperty WHERE userName = ? OR parentZoneId = ?  GROUP BY roomNum,hotelId limit ?,?";

  public HdUserPropertyEntity searchByName(String userName) {
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
        result.setZoneId(rs.getLong(2));
        result.setZoneName(rs.getString(3));
        result.setHotelId(rs.getLong(4));
        result.setHotelName(rs.getString(5));
        result.setRoomNum(rs.getString(6));
        result.setType(rs.getString(7));
        result.setRoomAmount(rs.getLong(8));
        result.setCreationDate(rs.getTimestamp(9));
        result.setModificationDamodificationDate(rs.getTimestamp(10));
      }
    } catch (Exception ex) {
      ex.printStackTrace();
      LOGGER.error("searchByName error", ex);
      throw new BusinessException(ExceptionConst.DB_ERROR, ex.getMessage());
    } finally {
      DbConnectionManager.closeConnection(rs, pstmt, con);
    }
    return result;
  }

  public List<HotelIdAndRoomNum> searchHotelIdAndRoomNumByAgentName(String userName,Long zoneId,Integer pageIndex,Integer pageSize){
    List<HotelIdAndRoomNum> result = new ArrayList<HotelIdAndRoomNum>();
    Connection con = null;
    PreparedStatement pstmt = null;
    ResultSet rs = null;
    try {
      con = DbConnectionManager.getConnection();
      pstmt = con.prepareStatement(SEARCH_HOTELID_AND_ROOMNUM_BY_AGENTNAME_SQL);
      pstmt.setString(1, userName);
      pstmt.setLong(2, zoneId);
      pstmt.setInt(3, pageIndex-1);
      pstmt.setInt(4, pageSize);
      rs = pstmt.executeQuery();
      while (rs.next()) {
        HotelIdAndRoomNum hotelIdAndRoomNum = new HotelIdAndRoomNum();
        hotelIdAndRoomNum.setRoomNum(rs.getString(1));
        hotelIdAndRoomNum.setHotelId(rs.getLong(2));
        result.add(hotelIdAndRoomNum);
      }
    } catch (Exception ex) {
      ex.printStackTrace();
      LOGGER.error("searchHotelIdAndRoomNumByAgentName error", ex);
      throw new BusinessException(ExceptionConst.DB_ERROR, ex.getMessage());
    } finally {
      DbConnectionManager.closeConnection(rs, pstmt, con);
    }
    return result;
  }
}