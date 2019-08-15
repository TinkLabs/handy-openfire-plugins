package com.hi.handy.group.plugin.model;

import com.hi.handy.group.plugin.domain.hdgroup.HdGroupEntity;
import com.hi.handy.group.plugin.domain.hdgroupagent.HdGroupAgentEntity;
import com.hi.handy.group.plugin.domain.hdgrouprelaction.HdGroupRelationEntity;

import java.util.List;

public class GroupModel {

    private HdGroupEntity group;
    private List<HdGroupRelationEntity> relations;
    private List<HdGroupAgentEntity> agents;

    public HdGroupEntity getGroup() {
        return group;
    }

    public void setGroup(HdGroupEntity group) {
        this.group = group;
    }

    public List<HdGroupRelationEntity> getRelations() {
        return relations;
    }

    public void setRelations(List<HdGroupRelationEntity> relations) {
        this.relations = relations;
    }

    public List<HdGroupAgentEntity> getAgents() {
        return agents;
    }

    public void setAgents(List<HdGroupAgentEntity> agents) {
        this.agents = agents;
    }
}
