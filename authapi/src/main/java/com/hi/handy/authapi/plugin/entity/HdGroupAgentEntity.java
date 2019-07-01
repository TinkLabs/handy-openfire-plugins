package com.hi.handy.authapi.plugin.entity;

import java.sql.Timestamp;

/**
 * @author huangxiutao
 * @mail xiutao.huang@tinklabs.com
 * @create 2019-06-27 20:02
 * @Description
 */
public class HdGroupAgentEntity {
    private String id;
    private String groupId;
    private String userName;
    private Timestamp createDate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Timestamp getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Timestamp createDate) {
        this.createDate = createDate;
    }
}
