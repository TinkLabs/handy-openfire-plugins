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
    GUEST_ENTRY_HOTELCHATROOM, // 0
    GUEST_LOGIN,               // 1
    AGENT_LOGIN                // 2
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
url:http://handy-internal-openfire-dev.handytravel.tech:9090/plugins/authapi/api
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
        "uid": "4352346234523",
        "name": "guest",
        "token": "MDNjY2Y3NDg4ZjU1OTlmMzkwYjMyMTc5NTE3MWNlOGU=",
        "domain": "192.168.100.66",
        "chatRoom": {
            "id": "sdfasdfadf",
            "name": "hotel-chat-room",
            "status": false,
            "icon": null,
            "roomJID": {
                "node": "room-hotel#1-2-酒店2-302#4352346234523",
                "domain": "conference.192.168.100.66",
                "resource": null
            },
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
url:http://handy-internal-openfire-dev.handytravel.tech:9090/plugins/authapi/api
paramter:
{
	"authType":1,
	"displayName":"guest",
	"email":"guest@tinklabs.com",
	"barcode": "999999999",
	"hotelId":3,
	"hotelName":"酒店3",
	"roomNum": "303",
	"zoneId": 1,
	"zoneName": "新加坡"
}
result:
{
    "success": true,
    "code": null,
    "message": null,
    "data": {
        "uid": "guest-tinklabs.com",
        "name": "guest",
        "token": "MTQwNTAyNmJjMmVkMzRmMWM2ZWRmOTAzYjZjYmRhYTQ=",
        "domain": "192.168.100.66",
        "chatRoom": {
            "id": "rqwereqer345afdf",
            "name": "vip-chat-room",
            "status": false,
            "icon": null,
            "roomJID": {
                "node": "room-vip#1-3-酒店3-303#guest-tinklabs.com",
                "domain": "conference.192.168.100.66",
                "resource": null
            },
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
url:http://handy-internal-openfire-dev.handytravel.tech:9090/plugins/authapi/api
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
        "uid": "agent-tinklabs.com",
        "name": "",
        "token": "MjVkNTVhZDI4M2FhNDAwYWY0NjRjNzZkNzEzYzA3YWQ=",
        "domain": "192.168.100.66",
        "groupIcon": null,
        "groupName": "vip-chat-room",
        "welcomeMessage": null
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