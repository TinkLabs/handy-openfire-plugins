package com.hi.handy.auth.plugin;

import java.io.File;
import org.jivesoftware.openfire.container.Plugin;
import org.jivesoftware.openfire.container.PluginManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AuthPlugin implements Plugin {

  private static final Logger LOGGER = LoggerFactory.getLogger(AuthPlugin.class);

  //B: 消息拦截器
//  private InterceptorManager interceptorManager;


  //C: 插件初始化函数
  public void initializePlugin(PluginManager manager, File pluginDirectory) {
    System.out.println("MessageFilter init============");
    // 将当前插件加入到消息拦截管理器（interceptorManager ）中，当消息到来或者发送出去的时候，会触发本插件的interceptPacket方法。
//    interceptorManager = InterceptorManager.getInstance();
//    interceptorManager.addInterceptor(this);
  }

  //D: 插件销毁函数
  public void destroyPlugin() {
    System.out.println("MessageFilter destory============");
    // 当插件被卸载的时候，主要通过openfire管理控制台卸载插件时，被调用。注意interceptorManager的addInterceptor和removeInterceptor需要成对调用。
//    interceptorManager.removeInterceptor(this);

  }

  //E 插件拦截处理函数


}
