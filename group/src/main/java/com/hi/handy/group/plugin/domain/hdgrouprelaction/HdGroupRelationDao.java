package com.hi.handy.group.plugin.domain.hdgrouprelaction;

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

public class HdGroupRelationDao extends BaseDao {
  private static final Logger LOGGER = LoggerFactory.getLogger(HdGroupRelationDao.class);

  private HdGroupRelationDao() {}
  private static final HdGroupRelationDao INSTANCE = new HdGroupRelationDao();
  public static HdGroupRelationDao getInstance() {
    return INSTANCE;
  }

  private static final String SEARCH_SQL = "SELECT * FROM hdGroupRelation";
  private static final String INSERT_SQL = "INSERT INTO hdGroupRelation (id, groupId, type, relationId, name, createDate) VALUES(?,?,?,?,?,?)";
  private static final String DELETE_SQL = "DELETE FROM hdGroupRelation WHERE id = ?";

  public List<HdGroupRelationEntity> searchAll() {
    List<HdGroupRelationEntity> groupRelationEntityList = new ArrayList();
    Connection con = null;
    PreparedStatement pstmt = null;
    ResultSet rs = null;
    try {
      con = DbConnectionManager.getConnection();
      pstmt = con.prepareStatement(SEARCH_SQL);
      rs = pstmt.executeQuery();
      while (rs.next()) {
        HdGroupRelationEntity hdGroupRelationEntity = new HdGroupRelationEntity();
        hdGroupRelationEntity.setId(rs.getString(1));
        hdGroupRelationEntity.setGroupId(rs.getString(2));
        hdGroupRelationEntity.setType(rs.getString(3));
        hdGroupRelationEntity.setRelationId(rs.getString(4));
        hdGroupRelationEntity.setRelationName(rs.getString(5));
        groupRelationEntityList.add(hdGroupRelationEntity);
      }
    } catch (Exception e) {
      e.printStackTrace();
      LOGGER.error("searchAll error", e);
      throw new BusinessException(ExceptionConst.DB_ERROR, e.getMessage());
    } finally {
      DbConnectionManager.closeConnection(rs, pstmt, con);
    }
    return groupRelationEntityList;
  }

  public boolean createGroupRelation(HdGroupRelationEntity hdGroupRelation) {
    Connection con = null;
    PreparedStatement pstmt = null;
    boolean result = false;
    try {
      con = DbConnectionManager.getConnection();
      pstmt = con.prepareStatement(INSERT_SQL);
      pstmt.setString(1, hdGroupRelation.getId());
      pstmt.setString(2, hdGroupRelation.getGroupId());
      pstmt.setString(3, hdGroupRelation.getType());
      pstmt.setString(4, hdGroupRelation.getRelationId());
      pstmt.setString(5, hdGroupRelation.getRelationName());
      pstmt.setTimestamp(6, hdGroupRelation.getCreateDate());
      if(pstmt.executeUpdate()>0){
        result =  true;
      }
    } catch (Exception e) {
      e.printStackTrace();
      LOGGER.error("createGroupRelation error", e);
      throw new BusinessException(ExceptionConst.DB_ERROR, e.getMessage());
    } finally {
      DbConnectionManager.closeConnection(pstmt, con);
    }
    return result;
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
}
