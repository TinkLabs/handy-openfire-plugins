package com.hi.handy.muc.entity;

import lombok.Data;

import java.sql.Timestamp;

/**
 * @author huangxiutao
 * @mail xiutao.huang@tinklabs.com
 * @create 2019-06-21 15:57
 * @Description
 */
@Data
public class HdUserPropertyEntity {

    private String userName;
    private String displayName;
    private String password;
    private Long zoneId;
    private String zoneName;
    private Long hotelId;
    private String hotelName;
    private String roomNum;
    private String type;
    private Timestamp creationDate;
    private Timestamp modificationDamodificationDate;
    private String status;
}
