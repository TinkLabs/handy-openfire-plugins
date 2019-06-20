package com.hi.handy.authapi.plugin;

import org.jivesoftware.openfire.event.GroupEventListener;
import org.jivesoftware.openfire.group.Group;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;


public class AuthApiEventListener implements GroupEventListener {
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthApiEventListener.class);
    @Override
    public void groupCreated(Group group, Map map) {

    }

    @Override
    public void groupDeleting(Group group, Map map) {

    }

    @Override
    public void groupModified(Group group, Map map) {

    }

    @Override
    public void memberAdded(Group group, Map map) {
        LOGGER.info("group",group);
        LOGGER.info("map",map);
    }

    @Override
    public void memberRemoved(Group group, Map map) {

    }

    @Override
    public void adminAdded(Group group, Map map) {

    }

    @Override
    public void adminRemoved(Group group, Map map) {

    }
}
