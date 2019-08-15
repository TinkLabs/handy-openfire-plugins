package com.hi.handy.group.plugin.domain.hduserproperty;

import com.hi.handy.group.plugin.domain.BaseDao;
import com.hi.handy.group.plugin.exception.BusinessException;
import com.hi.handy.group.plugin.exception.ExceptionConst;
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

  private static final String CREATE_SQL                  = "INSERT INTO hdUserProperty (userName, displayName, password, zoneId, zoneName, hotelId, hotelName, roomNum, type, creationDate) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
  private static final String COUNT_SQL                   = "SELECT count(1) FROM hdUserProperty WHERE userName = ?";

  public boolean createUserProperty(HdUserPropertyEntity hdUserPropertyEntity) {
    Connection con = null;
    PreparedStatement pstmt = null;
    boolean result = false;
    try {
      int i = 1;
      con = DbConnectionManager.getConnection();
      pstmt = con.prepareStatement(CREATE_SQL);
      if(StringUtils.isNoneBlank(hdUserPropertyEntity.getUserName())){
        pstmt.setString(i++, hdUserPropertyEntity.getUserName());
      }else{
        pstmt.setNull(i++, Types.VARCHAR);
      }
      if(StringUtils.isNoneBlank(hdUserPropertyEntity.getDisplayName())){
        pstmt.setString(i++, hdUserPropertyEntity.getDisplayName());
      }else{
        pstmt.setNull(i++, Types.VARCHAR);
      }
      if(StringUtils.isNoneBlank(hdUserPropertyEntity.getPassword())){
        pstmt.setString(i++, hdUserPropertyEntity.getPassword());
      }else{
        pstmt.setNull(i++, Types.VARCHAR);
      }
      if (hdUserPropertyEntity.getZoneId() != null) {
        pstmt.setLong(i++, hdUserPropertyEntity.getZoneId());
      }else{
        pstmt.setNull(i++, Types.INTEGER);
      }
      if (StringUtils.isNoneBlank(hdUserPropertyEntity.getZoneName())) {
        pstmt.setString(i++, hdUserPropertyEntity.getZoneName());
      } else {
        pstmt.setNull(i++, Types.VARCHAR);
      }
      if (hdUserPropertyEntity.getHotelId() != null) {
        pstmt.setLong(i++, hdUserPropertyEntity.getHotelId());
      } else {
        pstmt.setNull(i++, Types.INTEGER);
      }
      if (StringUtils.isNoneBlank(hdUserPropertyEntity.getHotelName())) {
        pstmt.setString(i++, hdUserPropertyEntity.getHotelName());
      } else {
        pstmt.setNull(i++, Types.VARCHAR);
      }
      if (StringUtils.isNoneBlank(hdUserPropertyEntity.getRoomNum())) {
        pstmt.setString(i++, hdUserPropertyEntity.getRoomNum());
      } else {
        pstmt.setNull(i++, Types.VARCHAR);
      }
      pstmt.setString(i++, hdUserPropertyEntity.getType());
      pstmt.setTimestamp(i++, new Timestamp(System.currentTimeMillis()));
      if(pstmt.executeUpdate()>0){
        result = true;
      }
    } catch (SQLException e) {
      e.printStackTrace();
      LOGGER.error("createUserProperty error", e);
      throw new BusinessException(ExceptionConst.DB_ERROR, e.getMessage(), e);
    } finally {
      DbConnectionManager.closeConnection(pstmt, con);
    }
    return result;
  }
  public int countByUserName(String userName) {
    Connection con = null;
    PreparedStatement pstmt = null;
    ResultSet rs = null;
    int count = 0;
    try {
      con = DbConnectionManager.getConnection();
      pstmt = con.prepareStatement(COUNT_SQL);
      pstmt.setString(1, userName);
      rs = pstmt.executeQuery();
      if (rs.next()) {
        count = rs.getInt(1);
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

}
