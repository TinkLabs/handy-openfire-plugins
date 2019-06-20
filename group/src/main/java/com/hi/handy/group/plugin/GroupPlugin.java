package com.hi.handy.group.plugin;

import org.jivesoftware.openfire.container.Plugin;
import org.jivesoftware.openfire.container.PluginManager;
import org.jivesoftware.openfire.interceptor.InterceptorManager;
import org.jivesoftware.util.JiveGlobals;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

public class GroupPlugin implements Plugin {

    public static final String SERVICEENABLED = "plugin.message.serviceEnabled";

    private boolean serviceEnabled;

    public void setServiceEnabled(boolean enabled) {
        serviceEnabled = enabled;
        JiveGlobals.setProperty(SERVICEENABLED, enabled ? "true" : "false");
    }

    public boolean getServiceEnabled() {
        return serviceEnabled;
    }

    public GroupPlugin() {
        serviceEnabled = true;
        serviceEnabled = JiveGlobals.getBooleanProperty(SERVICEENABLED, true);
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(GroupPlugin.class);

    private InterceptorManager interceptorManager;

    public void initializePlugin(PluginManager manager, File pluginDirectory) {
        // 将当前插件加入到消息拦截管理器（interceptorManager ）中，当消息到来或者发送出去的时候，会触发本插件的interceptPacket方法。
        LOGGER.info("GroupPlugin init============");
        interceptorManager = InterceptorManager.getInstance();
    }

    public void destroyPlugin() {
        // 当插件被卸载的时候，主要通过openfire管理控制台卸载插件时，被调用。注意interceptorManager的addInterceptor和removeInterceptor需要成对调用。
        LOGGER.info("GroupPlugin destory============");
    }
}
