package com.hi.handy.group.plugin.domain.hdgroup;

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

public class HdGroupDao extends BaseDao {
  private static final Logger LOGGER = LoggerFactory.getLogger(HdGroupDao.class);

  private HdGroupDao() {}
  private static final HdGroupDao INSTANCE = new HdGroupDao();
  public static HdGroupDao getInstance() {
    return INSTANCE;
  }

  private static final String SEARCH_SQL =  "SELECT * FROM hdGroup ORDER BY createDate DESC";
  private static final String INSERT_SQL =  "INSERT INTO hdGroup (id,name,icon,type,welcomeMessage,displayName,createDate) VALUES(?,?,?,?,?,?,?)";
  private static final String SEARCH_BY_ID_SQL =  "SELECT * FROM hdGroup WHERE id = ?";

  public List<HdGroupEntity> searchAll() {
    List<HdGroupEntity> groupEntityList = new ArrayList();
    Connection con = null;
    PreparedStatement pstmt = null;
    ResultSet rs = null;
    try {
      con = DbConnectionManager.getConnection();
      pstmt = con.prepareStatement(SEARCH_SQL);
      rs = pstmt.executeQuery();
      while (rs.next()) {
        HdGroupEntity hdGroupEntity = new HdGroupEntity();
        hdGroupEntity.setId(rs.getString(1));
        hdGroupEntity.setName(rs.getString(2));
        hdGroupEntity.setIcon(rs.getString(3));
        hdGroupEntity.setType(rs.getString(4));
        hdGroupEntity.setWelcomeMessage(rs.getString(5));
        hdGroupEntity.setDisplayName(rs.getString(6));
        hdGroupEntity.setCreateDate(rs.getTimestamp(7));
        groupEntityList.add(hdGroupEntity);
      }
    } catch (Exception e) {
      e.printStackTrace();
      LOGGER.error("searchAll error", e);
      throw new BusinessException(ExceptionConst.DB_ERROR, e.getMessage());
    } finally {
      DbConnectionManager.closeConnection(rs, pstmt, con);
    }
    return groupEntityList;
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

  public HdGroupEntity searchById(String id) {
    HdGroupEntity hdGroupEntity = null;
    Connection con = null;
    PreparedStatement pstmt = null;
    ResultSet rs = null;
    try {
      con = DbConnectionManager.getConnection();
      pstmt = con.prepareStatement(SEARCH_BY_ID_SQL);
      pstmt.setString(1, id);
      rs = pstmt.executeQuery();
      while (rs.next()) {
        hdGroupEntity = new HdGroupEntity();
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

}
