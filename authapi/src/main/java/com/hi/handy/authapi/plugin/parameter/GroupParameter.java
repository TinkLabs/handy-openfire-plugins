package com.hi.handy.authapi.plugin.parameter;

import java.sql.Timestamp;
import java.util.List;

/**
 * @author huangxiutao
 * @mail xiutao.huang@tinklabs.com
 * @create 2019-06-27 14:34
 * @Description
 */
public class GroupParameter{
    private String id;
    private String name;
    private String icon;
    private String type;
    private List<Relation> relations;
    private String welcomeMessage;
    private String displayName;
    private Timestamp createDate;

    @Override
    public String toString() {
        String s = "";
        for (Relation relation : relations) {
            s+=("id:"+relation.getId()+" name:"+relation.getName());
        }
        return "GroupParameter{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", icon='" + icon + '\'' +
                ", type='" + type + '\'' +
                ", relations=" + s +
                ", welcomeMessage='" + welcomeMessage + '\'' +
                ", displayName='" + displayName + '\'' +
                ", createDate=" + createDate +
                '}';
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getWelcomeMessage() {
        return welcomeMessage;
    }

    public void setWelcomeMessage(String welcomeMessage) {
        this.welcomeMessage = welcomeMessage;
    }

    public List<Relation> getRelations() {
        return relations;
    }

    public void setRelations(List<Relation> relations) {
        this.relations = relations;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public Timestamp getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Timestamp createDate) {
        this.createDate = createDate;
    }
}
