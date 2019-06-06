package com.hi.handy.muc.dao;

import lombok.extern.slf4j.Slf4j;
import org.jivesoftware.database.DbConnectionManager;
import org.jivesoftware.openfire.muc.MUCRoom;
import org.xmpp.packet.JID;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author huangxiutao
 * @mail xiutao.huang@tinklabs.com
 * @create 2019-05-30 18:04
 * @Description
 */
@Slf4j
public class MUCDao {

    /**
     * 根据JId 查询用户
     * @param jid
     * @return
     */
    public static List<Map<String,String>> getMUCInfo(String jid){
        log.info("getMUCInfo Jid:"+jid);
        List<Map<String, String>> list = new ArrayList<Map<String, String>>();
        Connection dbConnection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        Map<String,String> map = null;
        try {
            dbConnection = DbConnectionManager.getConnection();
            String sql = "SELECT ofMucRoom.serviceID, ofMucRoom.name, ofMucRoom.roomid, ofMucRoom.naturalName, ofMucRoom.description ,ofMucMember.nickname "
                    + "FROM ofMucRoom JOIN ofMucMember ON ofMucRoom.roomID = ofMucMember.roomID AND ofMucMember.jid = ?";
            statement = dbConnection.prepareStatement(sql);
            statement.setString(1,jid);
            resultSet = statement.executeQuery();
            while (resultSet.next()){
                map = new HashMap<String, String>();
                map.put("serviceID", resultSet.getString(1));
                map.put("name", resultSet.getString(2));
                map.put("roomid", resultSet.getString(3));
                map.put("naturalName", resultSet.getString(4));
                map.put("description", resultSet.getString(5));
                map.put("nickname", resultSet.getString(6));
                list.add(map);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            DbConnectionManager.closeConnection(resultSet,statement,dbConnection);
        }
        return list;
    }

    /**
     * 查询数据库，判断用户是否加入了群聊
     * @param room
     * @param userJid
     * @return
     */
    public static boolean hasJoinedRoom(MUCRoom room, JID userJid) {
        Connection dbConnection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            dbConnection = DbConnectionManager.getConnection();
            String sql = "SELECT roomID, jid FROM ofMucMember WHERE roomID=? AND jid=?";
            statement = dbConnection.prepareStatement(sql);
            statement.setLong(1,room.getID());
            statement.setString(2,userJid.toBareJID());
            resultSet = statement.executeQuery();
            if(resultSet.next()){
                log.info("hasJoinedRoom");
                return true;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            DbConnectionManager.closeConnection(resultSet,statement,dbConnection);
        }
        log.info("notJoinedRoom");
        return false;
    }

    /**
     * 将用户保存到聊天室的成员表中
     * @param room
     * @param userJid
     * @param nickName
     */
    public static void saveMember(MUCRoom room, JID userJid, String nickName) {
        log.info("saveMember roomName: "+nickName);
        Connection dbConnection = null;
        PreparedStatement statement = null;
        try {
            dbConnection = DbConnectionManager.getConnection();
            String sql = "INSERT INTO ofMucMember (roomID,jid,nickname) VALUES (?,?,?)";
            statement = dbConnection.prepareStatement(sql);
            statement.setLong(1,room.getID());
            statement.setString(2,userJid.toBareJID());
            statement.setString(3,nickName);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            DbConnectionManager.closeConnection(statement,dbConnection);
        }
    }
}
