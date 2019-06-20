# openfire-plugins
plugins for openfire(Concierge Chat 4.x backend services）


这里我摘的是PresenceRouter里面的消息处理函数，我们可以看到消息在被处理之前和处理之后都被拦截了一次，因此在处理之前processed=false,处理之后就为processed=true;由于这是服务器收消息，因此incoming都为true，同样在消息往客户端传送的时候也会被拦截两次，incoming=false。最后通过InterceptorManager.getInstance().addInterceptor将拦截器加到openfire中，就可以生效了

# How To
- download project
- package your module(jar filename format:`xxx.jar`.eg:`auth.jar`) 
- upload your plugin jar(eg:`auth.jar`) in openfire plugins page

>  - [Openfire Documentation](https://www.igniterealtime.org/projects/openfire/documentation.jsp)
>  - [Plugin Developer Guide](http://download.igniterealtime.org/openfire/docs/latest/documentation/plugin-dev-guide.html)