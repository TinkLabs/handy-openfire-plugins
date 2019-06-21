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
url:http://handy-internal-openfire-dev.handytravel.tech:9090/plugins/messageapi/api
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
            "hotelName": "3",
            "roomNum": "hotel3",
            "unRead": 0,
            "content": "<\message to=\"room-vip#1-3-hotel3-303#guest-tinklabs.com@conference.handy-internal-openfire-dev.handytravel.tech\" id=\"8oAgQ-393\" type=\"groupchat\" from=\"agentdave-tinklabs.com@handy-internal-openfire-dev.handytravel.tech/Spark 2.8.3.579\" chatroom=\"room-vip#1-3-hotel3-303#guest-tinklabs.com@conference.handy-internal-openfire-dev.handytravel.tech\"><\body>gggggggggggggggggggggggg</\body><\x xmlns=\"jabber:x:event\"><\offline/><\delivered/><\displayed/><\composing/><\/x><\/message>",
            "createDate": 1561102414000
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
