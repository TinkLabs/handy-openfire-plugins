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
    <title>Handy Message API</title>
    <meta name="pageID" content="message-api"/>
</head>
<body>
<p>
    message-api
</p>

<div class="jive-contentBoxHeader">1 get chat message list</div>
<div class="jive-contentBox">
    <table cellpadding="3" cellspacing="0" border="0" width="100%">
        <tbody>
        <tr>
            <td>
                <pre>
method:post
url:http://127.0.0.1:9090/plugins/messageapi/api
paramter:
{
	"userName":"agent-tinklabs.com",
	"pageIndex":0,
	"pageSize":10
}
result:
{
    "success": true,
    "code": null,
    "message": null,
    "data": [
        {
            "hotelName": "2",
            "roomNum": "酒店2",
            "count": 0,
            "content": "<\message to=\"4352346234523@192.168.100.66/Spark 2.8.3.579\" id=\"n4ZD9-204\" type=\"groupchat\" from=\"room-hotel#1-2-酒店2-302#4352346234523@conference.192.168.100.66/4352346234523\"><\body>黑山<\/body><\x xmlns=\"jabber:x:event\"><\offline/><\delivered/><\displayed/><\composing/><\/x><\stanza-id xmlns=\"urn:xmpp:sid:0\" id=\"e1910bb1-1012-45da-9f3b-895d6ffb63e7\" by=\"room-hotel#1-2-酒店2-302#4352346234523@conference.192.168.100.66\"/><\/message>",
            "createDate": 1560761442000
        }
    ]
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
