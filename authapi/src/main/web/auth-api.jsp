<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="java.util.*,
                 org.jivesoftware.openfire.XMPPServer,
                 org.jivesoftware.openfire.user.*,
                 org.jivesoftware.util.*"
%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<html>
<head>
    <title>Handy Auth API</title>
    <meta name="pageID" content="auth-api"/>
</head>
<body>
<p>
    auth-api
</p>

<div class="jive-contentBoxHeader">类型说明</div>
<div class="jive-contentBox">
    <table cellpadding="3" cellspacing="0" border="0" width="100%">
        <tbody>
        <tr>
            <td>
                <pre>
public enum AuthType {
    AGENT_LOGIN,     // 0
    AGENT_REGISTER,  // 1
    AGENT_CHAT_ROOM, // 2
    AGENT_MODIFY,    // 3
    GUEST_LOGIN,     // 4
    GUEST_REGISTER   // 5
}

public enum UserType {
    AGENT,        // 0
    AGENT_ADMIN,  // 1
    HOTEL,        // 2
    GUEST         // 3
  }
                </pre>
            </td>
        </tr>
        </tbody>
    </table>
</div>

<br>

<div class="jive-contentBoxHeader">测试步骤</div>
<div class="jive-contentBox">
    <table cellpadding="3" cellspacing="0" border="0" width="100%">
        <tbody>
        <tr>
            <td>
                <pre>
1 agent 登录  ——> 2 guest 注册 ——> 3 guest 登录 ——> 4 agent 获取房间列表
                </pre>
            </td>
        </tr>
        </tbody>
    </table>
</div>

<br>

<div class="jive-contentBoxHeader">1 guest register</div>
<div class="jive-contentBox">
    <table cellpadding="3" cellspacing="0" border="0" width="100%">
        <tbody>
        <tr>
            <td>
                <pre>
method:post
url:http://127.0.0.1:9090/plugins/authapi/api
paramter:
{
	"authType":5,
	"email":"xiaoming2000@tinklabs.com",
	"password":"12345678",
	"userType":3
}
result:
{
    "success": true,
    "code": null,
    "message": null,
    "data": {
        "userName": null,
        "password": null,
        "email": "xiaoming6@tinklabs.com",
        "agent": null,
        "agentJID": null,
        "chatRooms": null
    }
}
                </pre>
            </td>
        </tr>
        </tbody>
    </table>
</div>

<br>

<div class="jive-contentBoxHeader">2 guest login</div>
<div class="jive-contentBox">
    <table cellpadding="3" cellspacing="0" border="0" width="100%">
        <tbody>
        <tr>
            <td>
                <pre>
method:post
url:http://127.0.0.1:9090/plugins/authapi/api
paramter:
{
	"authType":4,
	"email":"xiaoming1000@tinklabs.com",
	"password":"12345678",
	"hotelId":1,
	"hotelName":"酒店1",
	"roomNum": "305",
	"zoneId": 1
}
result:
{
    "success": true,
    "code": null,
    "message": null,
    "data": {
        "userName": "xiaoming1000-tinklabs.com",
        "password": "xiaoming1000-tinklabs.com_openfire",
        "email": "xiaoming1000@tinklabs.com",
        "agent": "agent2000-tinklabs.com",
        "agentJID": "agent2000-tinklabs.com@conference.192.168.100.66",
        "chatRooms": [
            {
                "roomName": "xiaoming1000-tinklabs.com-room-guest",
                "roomJID": null,
                "hotelId": null,
                "hotelName": null,
                "hotelRoomNum": null,
                "roomType": "AGENT"
            }
        ]
    }
}
                </pre>
            </td>
        </tr>
        </tbody>
    </table>
</div>

<br>

<div class="jive-contentBoxHeader">3 agent login</div>
<div class="jive-contentBox">
    <table cellpadding="3" cellspacing="0" border="0" width="100%">
        <tbody>
        <tr>
            <td>
                <pre>
method:post
url:http://127.0.0.1:9090/plugins/authapi/api
paramter:
{
	"authType":0,
	"email":"agent2000@tinklabs.com",
	"password":"12345678",
	"userType": 0,
	"zoneId":1
}
result:
{
    "success": true,
    "code": null,
    "message": null,
    "data": {
        "userName": "agent2000-tinklabs.com",
        "password": null,
        "email": "agent2000@tinklabs.com",
        "agent": null,
        "agentJID": null,
        "chatRooms": null
    }
}
                </pre>
            </td>
        </tr>
        </tbody>
    </table>
</div>

<br>

<div class="jive-contentBoxHeader">4 agent chat room</div>
<div class="jive-contentBox">
    <table cellpadding="3" cellspacing="0" border="0" width="100%">
        <tbody>
        <tr>
            <td>
                <pre>
method:post
url:http://127.0.0.1:9090/plugins/authapi/api
paramter:
{
	"authType":2,
	"email":"agent2000@tinklabs.com",
	"zoneId":1
}
result:
{
    "success": true,
    "code": null,
    "message": null,
    "data": {
        "userName": null,
        "password": null,
        "email": null,
        "agent": null,
        "agentJID": null,
        "chatRooms": [
            {
                "roomName": "xiaoming1000-tinklabs.com-room-guest",
                "roomJID": "xiaoming1000-tinklabs.com-room-guest@conference.192.168.100.66",
                "hotelId": null,
                "hotelName": null,
                "hotelRoomNum": null,
                "roomType": "AGENT"
            }
        ]
    }
}
                </pre>
            </td>
        </tr>
        </tbody>
    </table>
</div>

<br>

</body>
</html>
