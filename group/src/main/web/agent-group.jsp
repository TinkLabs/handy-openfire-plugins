<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="java.util.*,
                 org.jivesoftware.openfire.XMPPServer,
                 com.hi.handy.group.plugin.GroupPlugin,
                 org.jivesoftware.openfire.user.*,
                 org.jivesoftware.util.*"
%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<html>
<head>
    <title>Handy Agent Group</title>
    <meta name="pageID" content="message-props-edit-form"/>
    <style>
        label {
            margin-right: 5px;
        }
    </style>
</head>
<body>
<div class="jive-success" style="display: none;" id="successMsgDiv">
    <table cellpadding="0" cellspacing="0" border="0">
        <tbody>
        <tr>
            <td class="jive-icon"><img src="images/success-16x16.gif" width="16" height="16" border="0" alt=""></td>
            <td class="jive-icon-label" id="successMsg">

            </td>
        </tr>
        </tbody>
    </table>
</div>
<br>
<div class="jive-error" style="display: none;" id="failedMsgDiv">
    <table cellpadding="0" cellspacing="0" border="0">
        <tbody>
        <tr>
            <td class="jive-icon"><img src="images/error-16x16.gif" width="16" height="16" border="0" alt=""></td>
            <td class="jive-icon-label" id="failedMsg">

            </td>
        </tr>
        </tbody>
    </table>
</div>
<br>
<div class="jive-contentBoxHeader">edit</div>
<div class="jive-contentBox">
    <table cellspacing="0" border="0" width="100%">
        <tbody>
        <tr style="vertical-align: top;">
            <td width="30%">
                <fieldset>
                    <legend>group</legend>
                    <div>
                        <table cellspacing="0" border="0" width="100%">
                            <tbody>
                            <tr>
                                <td width="10%">
                                    Icon:
                                </td>
                                <td width="90%">
                                    <img id ="group_icon" style="width: 180px;height: 180px;border-radius: 50%;background: #dddddd;">
                                    <br/>
                                    <p id="group_iconpath"></p><br/>
                                    <input type="file" id="group_fileChooser" accept="image/*">
                                    <button id="group_upload">Upload</button>
                                </td>
                            </tr>
                            <tr>
                                <td width="10%">
                                    Name:
                                </td>
                                <td width="90%">
                                    <input type="text" id="group_name" style="width:350px;">
                                </td>
                            </tr>
                            <tr>
                                <td width="10%">
                                    Type:
                                </td>
                                <td width="90%">
                                    <input type="radio" name="group_type" value="VIP" id="vip" class="group-type" checked><label for="vip">VIP</label>
                                    <input type="radio" name="group_type" value="HOTEL" id="hotel" class="group-type"><label for="hotel">HOTEL</label>
                                </td>
                            </tr>
                            <tr>
                                <td width="10%">
                                    Welcome Message:
                                </td>
                                <td width="90%">
                                    <textarea name="welcomeMessage" id="group_welcomeMessage" cols="30" rows="10" style="width:350px;height:100px;"></textarea>
                                </td>
                            </tr>
                            <tr>
                                <td width="10%">
                                    Retlation:
                                </td>
                                <td width="90%">
                                    <textarea name="relations" id="group_relations" cols="30" rows="10" style="width:350px;height:100px;" placeholder="zoneid:zonename;zoneid:zonename"></textarea>
                                </td>
                            </tr>
                            </tbody>
                            <tfoot>
                            <tr>
                                <td width="10%">
                                </td>
                                <td width="90%">
                                    <button type="button" id="group_save">save group</button>
                                </td>
                            </tr>
                            </tfoot>
                        </table>
                    </div>
                </fieldset>
            </td>
            <td width="30%">
                <fieldset>
                    <legend>zone/hotel</legend>
                    <div>
                        <table cellspacing="0" border="0" width="100%">
                            <tbody>
                            <tr>
                                <td width="30%">
                                    zone/hotel id
                                </td>
                                <td width="70%">
                                    <input type="text" id="zone_or_hotel_id" value="" required>
                                </td>
                            </tr>
                            <tr>
                                <td width="30%">
                                    zone/hotel name
                                </td>
                                <td width="70%">
                                    <input type="text" id="zone_or_hotel_name" value="" required>
                                </td>
                            </tr>
                            </tbody>
                            <tfoot>
                            <tr>
                                <td>
                                    <input type="button" value="save" id="zone_or_hotel_save">
                                </td>
                            </tr>
                            </tfoot>
                        </table>
                    </div>
                </fieldset>
            </td>
            <td width="30%">
                <fieldset>
                    <legend>agent</legend>
                    <div>
                        <table cellspacing="0" border="0" width="100%">
                            <tbody>
                            <tr>
                                <td width="30%">
                                    email
                                </td>
                                <td width="70%">
                                    <input type="text" id="agent_email" value="" required>
                                </td>
                            </tr>
                            <tr>
                                <td width="30%">
                                    displayname
                                </td>
                                <td width="70%">
                                    <input type="text" id="agent_displayname" value="" required>
                                </td>
                            </tr>
                            <tr>
                                <td width="30%">
                                    password
                                </td>
                                <td width="70%">
                                    <input type="text" id="agent_password" value="" required>
                                </td>
                            </tr>
                            <tr>
                                <td width="10%">
                                    type
                                </td>
                                <td width="90%">
                                    <input type="radio" name="agent_type" value="ADMIN" id="agent_type_admin" class="group-type"><label for="agent_type_admin">admin</label>
                                </td>
                            </tr>
                            </tbody>
                            <tfoot>
                            <tr>
                                <td>
                                    <input type="button" value="save" id="agent_save">
                                </td>
                            </tr>
                            </tfoot>
                        </table>
                    </div>
                </fieldset>
            </td>
        </tr>
        </tbody>
    </table>
</div>

<br>

<div class="jive-contentBoxHeader">list</div>
<div class="jive-contentBox">
    <table class="jive-table" cellpadding="3" cellspacing="0" border="0" width="100%">
        <thead>
            <tr>
                <th>
                    choice
                </th>
                <th>
                    group
                </th>
                <th>
                    zone/hotel
                </th>
                <th>
                    agent
                </th>
            </tr>
        </thead>
        <tbody id="list">
        </tbody>
    </table>
</div>

<br>
<input type="hidden" id="h_group_id">
<script src="jquery-3.4.1.min.js" type="text/javascript"></script>
<script src="aws-sdk-2.502.0.min.js" type="text/javascript"></script>
<script type="text/javascript">
    $(function(){
        getGroupList();
        $("#group_save").on("click",function(){
            var groupObject = {};
            groupObject.icon = $("#group_iconpath").html();
            groupObject.name = $("#group_name").val();
            groupObject.type = $("input[name='group_type']:checked").val();
            groupObject.welcomeMessage = $("#group_welcomeMessage").val();
            groupObject.relations  = $("#group_relations").val();
            if(inputObjectHasEmpty(groupObject)){
                return;
            }
            save("register",groupObject);
        });

        function inputObjectHasEmpty(inputObject){
            var hasEmpty = false;
            for(var key in inputObject) {
                if(isEmptyOrEmpty(inputObject[key])){
                    hasEmpty = true;
                    alert(key + "is empty");
                    return;
                }
            }
            return hasEmpty;
        }

        function isEmptyOrEmpty(obj){
            if(typeof obj == "undefined" || obj == null || obj == ""){
                return true;
            }else{
                return false;
            }
        }

        function save(apiurl,dataObject){
            $.ajax({
                url:apiurl,
                type:"POST",
                contentType:"application/json",
                dataType:"json",
                data:JSON.stringify(dataObject),
                success:function (result) {
                    if(result.success){
                        var message = "<span>save success</span><br/><span>name:"+JSON.stringify(result.data)+"</span>";
                        $("#successMsgDiv").show();
                        $("#successMsg").html("").html(message);
                        getGroupList();
                    }else{
                        $("#failedMsgDiv").show();
                        $("#failedMsg").html("").html("save fail!"+result.message);
                    }
                }
            });
        }

        $("#group_upload").on("click",function(){
            AWS_FILEUPLAOD();
        });

        function AWS_FILEUPLAOD() {
            var _accessKeyId = 'AKIAS7EXJMYWUZJLRIAM';
            var _secretAccessKey = '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';
            var bucketRegion = 'ap-southeast-1';
            var albumBucketName = 'handy-concierge-chat';

            AWS.config.update({
                region: bucketRegion,
                credentials: {
                    accessKeyId: _accessKeyId,
                    secretAccessKey: _secretAccessKey
                }
            });

            var bucketObject = new AWS.S3({
                params: {Bucket: albumBucketName}
            });

            return addPhoto(bucketObject,"staging/","group_fileChooser");
        }

        function addPhoto(bucketObject,bucketFilePath,fileInputId) {
            var files = document.getElementById(fileInputId).files;
            if (!files.length) {
                return alert('Please choose a file to upload first.');
            }
            var file = files[0];
            var fileName = file.name;

            bucketObject.upload({
                Key: bucketFilePath + fileName,
                Body: file,
            }, function(err, result) {
                if (err) {
                    return alert('There was an error uploading your photo: ', err.message);
                }
                var iconPath = result["Location"];
                $("#group_icon").attr('src',iconPath);
                $("#group_iconpath").html("").html(iconPath);
            });
        }

        //hotel_or_zone_save
        $("#zone_or_hotel_save").on("click",function(){
            var groupRelationObject={};
            groupRelationObject.id=$("#zone_or_hotel_id").val();
            groupRelationObject.name=$("#zone_or_hotel_name").val();
            groupRelationObject.groupId=$("#h_group_id").val();
            if(isEmptyOrEmpty(groupRelationObject.groupId)){
                alert("choice a group first");
                return;
            }
            if(inputObjectHasEmpty(groupRelationObject)){
                return;
            }
            save("addrelation",groupRelationObject);
        });

        //agent_save
        $("#agent_save").on("click",function(){
            var agentObject={};
            agentObject.name=$("#agent_email").val();
            agentObject.displayName=$("#agent_displayname").val();
            agentObject.password=$("#agent_password").val();
            agentObject.groupId=$("#h_group_id").val();;
            agentObject.type= isEmptyOrEmpty($("input[name='agent_type']:checked").val())?"AGENT":"ADMIN"
            if(isEmptyOrEmpty(agentObject.groupId)){
                alert("choice a group first");
                return;
            }
            if(inputObjectHasEmpty(agentObject)){
                return;
            }
            save("addagent",agentObject);
        });

        $("#list").on("click",'input[name="group_choice"]',function(){
            $("#h_group_id").val($(this).val());
        });

        $("#list").on("click","a",function(){
            var deleteObject={};
            deleteObject.id=$(this).data("id");
            deleteObject.apiType= $(this).data("type")=="relation"?1:0;
            if(confirm('确定要删除吗')==true){
                save("delete",deleteObject);
            }
        });

        function getGroupList() {
            $.ajax({
                type: "post",
                url: 'list',
                contentType: "application/json; charset=utf-8",
                dataType: "json",
                data: null,
                success: function(result) {
                    if(result.success){
                        var groups = result.data;
                        var html_trs = '';
                        $.each(groups,function(i,group){
                            var html_tr = "<tr>";
                            var html_group_td =
                            '<td width="10%">'+ '<input type="radio" name="group_choice" value="'+group.group.id+'"></td>'+
                            '<td width="40%">' +
                                '<p><label style="background:green;color:white">icon:</label>'+group.group.icon+"</p>" +
                                "<p><label style='background:green;color:white'>name:</label>"+group.group.name+"</p>" +
                                "<p><label style='background:green;color:white'>displayName:</label>"+group.group.displayName+"</p>" +
                                "<p><label style='background:green;color:white'>type:</label>"+group.group.type+'</p>' +
                                '<p><label style="background:green;color:white">welcomemessage:</label>'+group.group.welcomeMessage+'</p>' +
                                '</td>';

                            var zone_or_hotel_td = '<td width="20%">';
                            $.each(group.relations,function(i,relation) {
                                    var relationhtml = '<p>id:' + relation.relationId + "-name:" + relation.relationName +
                                        '<a href="javascript:void()" data-id="'+relation.id+'" data-type="relation" title="删除"><img src="images/delete-16x16.gif" width="16" height="16" border="0" alt="删除 Content Filter"></a>';
                                zone_or_hotel_td = zone_or_hotel_td + relationhtml;
                            });
                            zone_or_hotel_td = zone_or_hotel_td + "</td>";

                            var agent_td='<td width="20%">';
                            $.each(group.agents,function(i,agent) {
                                var agenthtml = '<p>' + agent.userName +
                                    '<a href="javascript:void()" data-id="'+agent.id+'" data-type="agent" title="删除"><img src="images/delete-16x16.gif" width="16" height="16" border="0" alt="删除 Content Filter"></a>' +
                                '</p>';
                                agent_td = agent_td + agenthtml;
                            });
                            agent_td = agent_td + "</td>";

                            html_tr = html_tr+html_group_td+zone_or_hotel_td+agent_td+"</tr>";
                            html_trs=html_trs+html_tr;
                        })
                        $("#list").html('').append($(html_trs));
                    }
                }
            });
        };
    });
</script>
</body>
</html>
