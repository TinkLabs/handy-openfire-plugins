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
    GUEST         // 1
  }
                </pre>
            </td>
        </tr>
        </tbody>
    </table>
</div>

<br>

<div class="jive-contentBoxHeader">1 guest entry into hotel chat room</div>
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
	"barcode": "4352346234523",
	"hotelId":2,
	"hotelName":"酒店2",
	"roomNum": "302",
	"zoneId": 1,
	"zoneName": "香港"
}
result:
{
    "success": true,
    "code": null,
    "message": null,
    "data": {
        "userName": "4352346234523",
        "displayName": "guest",
        "email": "4352346234523@tinklabs.com",
        "chatRoom": {
            "name": null,
            "status": false,
            "icon": null,
            "roomName": "room-hotel#1-2-酒店2-302#4352346234523",
            "roomJID": null,
            "roomType": "HOTEL"
        }
    }
}
                </pre>
            </td>
        </tr>
        </tbody>
    </table>
</div>

<br>

<div class="jive-contentBoxHeader">2 guest login in to vip chat room</div>
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
	"authType":1,
	"displayName":"dave",
	"email":"dave@tinklabs.com",
	"barcode": "999999999",
	"hotelId":3,
	"hotelName":"酒店3",
	"roomNum": "303",
	"zoneId": 3,
	"zoneName": "新加坡"
}
result:
{
    "success": true,
    "code": null,
    "message": null,
    "data": {
        "userName": "dave-tinklabs.com",
        "displayName": "dave",
        "email": "dave@tinklabs.com",
        "chatRoom": {
            "name": null,
            "status": false,
            "icon": null,
            "roomName": "room-vip#3-3-酒店3-303#dave-tinklabs.com",
            "roomJID": null,
            "roomType": "VIP"
        }
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
	"authType":2,
	"email":"agent@tinklabs.com",
	"password":"12345678"
}
result:
{
    "success": true,
    "code": null,
    "message": null,
    "data": {
        "userName": "agent-tinklabs.com",
        "displayName": "",
        "email": "agent@tinklabs.com",
        "chatRoom": null
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
