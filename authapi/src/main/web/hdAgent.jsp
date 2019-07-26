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
    <title>Handy Agent Operate</title>
    <meta name="pageID" content="hdAgent-operate"/>
    <script src="jquery-3.4.1.min.js"></script>
</head>
<body>
<div class="jive-contentBox">
    <table cellpadding="3" cellspacing="0" border="0" width="100%">
        <tbody>
        <tr>
            <td>
                <div id="agentInfo-id">
                    <table cellspacing="0" border="0" width="100%">
                        <tbody>
                            <tr>
                                <td width="10%">
                                    Agent Email:
                                </td>
                                <td width="90%">
                                  <input type="text" name="email" id="email-id">
                                </td>
                            </tr>
                            <tr>
                                <td width="10%">
                                    Agent Password:
                                </td>
                                <td width="90%">
                                    <input type="password" name="password" id="password-id" >
                                </td>
                            </tr>
                            <tr>
                                <td width="10%">
                                    Agent GroupName:
                                </td>
                                <td width="90%">
                                    <input type="text" name="groupName" id="groupName-id">
                                </td>
                            </tr>
                        </tbody>
                        <tfoot>
                            <tr>
                                <td>
                                    <button type="button" id="save-btn-id">save agent</button>
                                </td>
                            </tr>
                        </tfoot>
                    </table>
                    <div id="msg"></div>
                </div>
            </td>
        </tr>
        </tbody>
    </table>
</div>

<br>
<script type="text/javascript">
    $(document).ready(function () {
        $("#save-btn-id").click(function(){
            if(!$("input[name='email']").val()){
                alert("please input agent email");
                $("input[name='email']").focus();
                return false;
            }
            if(!$("input[name='password']").val()){
                alert("please input agent password");
                $("input[name='password']").focus();
                return false;
            }
            if(!$("input[name='groupName']").val()){
                alert("please input agent groupName");
                $("input[name='groupName']").focus();
                return false;
            }
            $.ajax({
                url:"api",
                type:"POST",
                contentType:"application/json",
                dataType:"json",
                data:JSON.stringify({
                    authType:4,
                    email:$("input[name='email']").val(),
                    password:$("input[name='password']").val(),
                    groupName:$("input[name='groupName']").val()
                }),
                success:function (resultData) {
                    if(resultData.success){
                        $("#msg").html("<span style='color: green'>save success</span>");
                    }else{
                        $("#msg").html("<span style='color: red'>save fail!</span>");
                    }
                }
            });
        });
    });
</script>

</body>
</html>
