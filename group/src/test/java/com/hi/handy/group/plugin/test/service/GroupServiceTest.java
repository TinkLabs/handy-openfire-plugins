package com.hi.handy.group.plugin.test.service;

import com.hi.handy.group.plugin.parameter.GroupRelationParameter;
import com.hi.handy.group.plugin.service.GroupService;
import com.hi.handy.group.plugin.test.BaseTest;
import org.junit.Test;

public class GroupServiceTest extends BaseTest {

    @Test
    public void addGroupRelationTest() {
        GroupRelationParameter groupRelationParameter = new GroupRelationParameter();
        groupRelationParameter.setGroupId("e2c36aaa451c48a5a751fb21d456fc1d");
        groupRelationParameter.setId("1");
        groupRelationParameter.setName("F");
        GroupService.getInstance().addGroupRelation(groupRelationParameter);
    }
}
