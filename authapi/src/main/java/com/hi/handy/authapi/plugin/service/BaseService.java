package com.hi.handy.authapi.plugin.service;

public class BaseService {

    protected static final String LINE_THROUGH = "-";
    protected static final String AT_SYMBOL = "@";
    protected static final String GUEST_PASSWORD_SUFFIX = "_openfire";
    protected static final String DEFAULT_SERVICE_NAME = "conference";
    protected static final String VIP_CHAT_ROOM_SUFFIX = "room-vip#";
    protected static final String HOTEL_CHAT_ROOM_SUFFIX = "room-hotel#";
    protected static final String EMAIL_SUFFIX = "@tinklabs.com";
    protected static final String ROOMINFO_SUFFIX = "#";
    protected static final String HOTEL_CHAT_ROOM_GUEST_DEFAULT_DISPLAY_NAME = "guest";
    // TODO how to generate password
    protected String generatePassword(String code) {
        return code + GUEST_PASSWORD_SUFFIX;
    }
}
