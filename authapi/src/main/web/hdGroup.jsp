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
    <title>Handy Agent Group Operate</title>
    <meta name="pageID" content="hdGroup-operate"/>
    <script src="jquery-3.4.1.min.js"></script>
</head>
<body>
<div class="jive-contentBox">
    <table cellpadding="3" cellspacing="0" border="0" width="100%">
        <tbody>
        <tr>
            <td>
                <div id="groupInfo-id">
                    <form action="groupapi" method="post" enctype="multipart/form-data">
                        <div>
                            AgentGroup Name: <input type="text" name="displayName" id="displayName-id">
                        </div>
                        <div>AgentGroup Icon: <input type="file" name="icon" id="icon-id" accept="image/*" onchange="iconPreview(this)"><span id="avarimg"></span></div>
                        <div>AgentGroup Type: <input type="radio" name="type" value="VIP" id="VIP-id" class="group-type" checked><label for="VIP-id">VIP
                            AgentGroup</label>
                            <input type="radio" name="type" value="HOTEL" id="HOTEL-id" class="group-type"><label for="HOTEL-id">HOTEL
                                AgentGroup</label>
                        </div>
                        <div style="display:flex;">AgentGroup Welcome Message: <textarea name="welcomeMessage" id="welcomeMessage-id"
                                                                                         cols="30" rows="10" style="width:350px;height:100px;"></textarea>
                        </div>
                        <div style="display:flex;">AgentGroup Retlation: <textarea name="relations" id="relations-id"
                                                                                   cols="30" rows="10" style="width:350px;height:100px;" placeholder="zoneid:zonename;zoneid:zonename"></textarea>
                        </div>
                        <button type="submit" id="save-group-btn-id">save group</button>
                    </form>
                </div>
            </td>
        </tr>
        </tbody>
    </table>
</div>

<br>
<script type="text/javascript">
    $(document).ready(function () {
        $("form").submit(function(event){
            console.log();
            if(!$("input[name='displayName']").val()){
                alert("please input group name");
                $("input[name='displayName']").focus();
                return false;
            }
            if(!$("input[name='type']").val()){
                alert("please select group type");
                $("input[name='type']").focus();
                return false;
            }
            if(!$("textarea[name='welcomeMessage']").val()){
                alert("please input welcome message");
                $("textarea[name='welcomeMessage']").focus();
                return false;
            }
            if(!$("textarea[name='relations']").val()){
                alert("please input relations");
                $("textarea[name='relations']").focus();
                return false;
            }
        });
    });

    if (typeof FileReader == 'undefined') {
        document.getElementById("groupInfo-id").InnerHTML = "<h1>当前浏览器不支持FileReader接口</h1>";
        document.getElementById("icon-id").setAttribute("disabled", "disabled");
    }
    function iconPreview(obj) {
        var file = obj.files[0];
        console.log(obj); console.log(file);
        console.log("file.size = " + file.size);
        var reader = new FileReader();
        reader.onloadstart = function (e) {
            console.log("开始读取....");
        }
        reader.onprogress = function (e) {
            console.log("正在读取中....");
        }
        reader.onabort = function (e) {
            console.log("中断读取....");
        }
        reader.onerror = function (e) {
            console.log("读取异常....");
        }
        reader.onload = function (e) {
            console.log("成功读取....");
            var img = document.getElementById("avarimg");
            img.innerHTML = '<img src="'+e.target.result+'" alt="" width="50px" height="50px">';
        }
        reader.readAsDataURL(file)
    }
</script>

</body>
</html>
