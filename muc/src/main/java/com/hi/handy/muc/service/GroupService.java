package com.hi.handy.muc.service;

import com.hi.handy.muc.dao.HdGroupAgentDao;
import com.hi.handy.muc.dao.HdGroupDao;
import com.hi.handy.muc.dao.HdUserPropertyDao;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class GroupService extends BaseService{

    private GroupService(){}
    private static GroupService INSTANCE = new GroupService();
    public static GroupService getInstance() {
        return INSTANCE;
    }

    public List<String> searchGroupNameByAgentName() {
        log.info("searchGroupNameByAgentName");
        List<String> adminAgentNameList = HdUserPropertyDao.getInstance().searchAdminAgentName();
        String adminAgentNamesString = joinListForInSqlString(adminAgentNameList);
        List<String> groupIdList = HdGroupAgentDao.getInstance().searchGroupIdsByAgentName(adminAgentNamesString);
        String groupIdsString = joinListForInSqlString(groupIdList);
        return HdGroupDao.getInstance().searchNamesByIds(groupIdsString);
    }
}
