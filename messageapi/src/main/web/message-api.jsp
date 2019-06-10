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
    <meta name="pageID" content="message-props-edit-form"/>
</head>
<body>
<p>
    get message data
</p>

<div class="jive-contentBoxHeader">1 get history message list</div>
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
    "userName":"user1",
    "pageIndex":1,
    "pageSize":10,
    "messageType":"HISTORY"
}
result:
{
    "success": true,
    "code": null,
    "message": null,
    "data": {
        "readCount": 0,
        "messageStanzaAndCreationDates": [
            {
                "creationDate": "1559635741231",
                "stanza": "<\message to=\"user1@192.168.100.66/Spark\" id=\"5Um83-775\" type=\"groupchat\" from=\"user2-tinklabs.com-room-guest@conference.192.168.100.66/user1\"><\body>message<\/body><\x xmlns=\"jabber:x:event\"><\offline/><\delivered/><\displayed/><\composing/><\/x><\stanza-id xmlns=\"urn:xmpp:sid:0\" id=\"61bde1c2-b9b3-4936-8bbf-b455087e3f58\" by=\"user2-tinklabs.com-room-guest@conference.192.168.100.66\"/><\/message>"
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

<div class="jive-contentBoxHeader">2 get chat message list</div>
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
    "userName":"user1",
    "pageIndex":1,
    "pageSize":10,
    "messageType":"LIST"
}
result:
{
    "success": true,
    "code": null,
    "message": null,
    "data": [
        {
            "hotelName": "酒店1",
            "roomNum": "301",
            "count": 1,
            "content": "",
            "createDate": 1559741831000
        }
    ]
}
                </pre>
            </td>
        </tr>
        </tbody>
    </table>
</div>

</body>
</html>
