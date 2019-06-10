<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="java.util.*,
                 org.jivesoftware.openfire.XMPPServer,
                 com.hi.handy.message.plugin.MessagePlugin,
                 org.jivesoftware.openfire.user.*,
                 org.jivesoftware.util.*"
%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%
    // Get parameters
    boolean save = request.getParameter("save") != null;
    boolean success = request.getParameter("success") != null;
    boolean searchEnabled = ParamUtils.getBooleanParameter(request, "searchEnabled");
    MessagePlugin plugin = (MessagePlugin) XMPPServer.getInstance().getPluginManager().getPlugin("message");
    // Handle a save
    Map<String, String> errors = new HashMap<String, String>();
    if (save) {
        if (errors.size() == 0) {
            plugin.setServiceEnabled(searchEnabled);
            response.sendRedirect("message-props-edit-form?success=true");
            return;
        }
    }
    searchEnabled = plugin.getServiceEnabled();
%>

<html>
<head>
    <title>Handy Message</title>
    <meta name="pageID" content="message-props-edit-form"/>
</head>
<body>
<p>
    handy message setting
</p>
<% if (success) { %>
<div class="jive-success">
    <table cellpadding="0" cellspacing="0" border="0">
        <tbody>
        <tr>
            <td class="jive-icon"><img src="images/success-16x16.gif" width="16" height="16" border="0" alt=""></td>
            <td class="jive-icon-label">
                Service properties edited successfully.
            </td>
        </tr>
        </tbody>
    </table>
</div>
<br>
<% } else if (errors.size() > 0) { %>
<div class="jive-error">
    <table cellpadding="0" cellspacing="0" border="0">
        <tbody>
        <tr>
            <td class="jive-icon"><img src="images/error-16x16.gif" width="16" height="16" border="0" alt=""></td>
            <td class="jive-icon-label">
                Error setting the service name.
            </td>
        </tr>
        </tbody>
    </table>
</div>
<br>
<% } %>
<form action="message-props-edit-form.jsp?save" method="post">

    <div class="jive-contentBoxHeader">Service Enabled</div>
    <div class="jive-contentBox">
        <p>
            You can choose to enable or disable the message record
        </p>
        <table cellpadding="3" cellspacing="0" border="0" width="100%">
            <tbody>
            <tr>
                <td width="1%">
                    <input type="radio" name="searchEnabled" value="true" id="rb01"
                        <%= ((searchEnabled) ? "checked" : "") %>>
                </td>
                <td width="99%">
                    <label for="rb01"><b>Enabled</b></label> - the message will write to local db
                </td>
            </tr>
            <tr>
                <td width="1%">
                    <input type="radio" name="searchEnabled" value="false" id="rb02"
                        <%= ((!searchEnabled) ? "checked" : "") %>>
                </td>
                <td width="99%">
                    <label for="rb02"><b>Disabled</b></label> - the message will not write to local db
                </td>
            </tr>
            </tbody>
        </table>
    </div>

    <br>

    <input type="submit" value="Save Properties">
</form>
</body>
</html>
