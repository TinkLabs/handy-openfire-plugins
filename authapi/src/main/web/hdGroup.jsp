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
    <script src="aws-sdk-2.502.0.min.js"></script>
    <style>
        table tr td:first-child{
            text-align: right;
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
<div class="jive-contentBox">
    <div id="message"></div>
    <table cellpadding="3" cellspacing="0" border="0" width="100%">
        <tbody>
            <tr>
                <td>
                    <div id="groupInfo-id">
                        <table cellspacing="0" border="0" width="100%">
                            <tbody>
                                <tr>
                                    <td width="10%">
                                        Icon:
                                    </td>
                                    <td width="90%">
                                        <img id ="icon" style="width: 180px;height: 180px;border-radius: 50%;background: #dddddd;">
                                        <br/>
                                        <p id="iconpath"></p><br/>
                                        <input type="file" id="fileChooser" accept="image/*">
                                        <button id="upload">Upload</button>
                                    </td>
                                </tr>
                                <tr>
                                    <td width="10%">
                                        Name:
                                    </td>
                                    <td width="90%">
                                        <input type="text" id="name" style="width:350px;">
                                    </td>
                                </tr>
                                <tr>
                                    <td width="10%">
                                        Type:
                                    </td>
                                    <td width="90%">
                                        <input type="radio" name="type" value="VIP" id="vip" class="group-type" checked><label for="vip">VIP</label>
                                        <input type="radio" name="type" value="HOTEL" id="hotel" class="group-type"><label for="hotel">HOTEL</label>
                                    </td>
                                </tr>
                                <tr>
                                    <td width="10%">
                                        Welcome Message:
                                    </td>
                                    <td width="90%">
                                        <textarea name="welcomeMessage" id="welcomeMessage" cols="30" rows="10" style="width:350px;height:100px;"></textarea>
                                    </td>
                                </tr>
                                <tr>
                                    <td width="10%">
                                        Retlation:
                                    </td>
                                    <td width="90%">
                                        <textarea name="relations" id="relations" cols="30" rows="10" style="width:350px;height:100px;" placeholder="zoneid:zonename;zoneid:zonename"></textarea>
                                    </td>
                                </tr>
                            </tbody>
                            <tfoot>
                            <tr>
                                <td width="10%">
                                </td>
                                <td width="90%">
                                    <button type="button" id="save">save group</button>
                                </td>
                            </tr>
                            </tfoot>
                        </table>
                    </div>
                </td>
            </tr>
        </tbody>
    </table>
</div>

<br>
<script type="text/javascript">
    $(function(){

        $("#save").on("click",function(){
            var groupObject = getData();
            if(isEmptyOrEmpty(groupObject)){
                return;
            }

            $.ajax({
                url:"groupapi",
                type:"POST",
                contentType:"application/json",
                dataType:"json",
                data:JSON.stringify(groupObject),
                success:function (result) {
                    if(result.success){
                        var message = "<span>save success</span><br/><span>name:"+result.data["groupName"]+"</span>";
                        $("#successMsgDiv").show();
                        $("#successMsg").html("").html(message);
                    }else{
                        $("#failedMsgDiv").show().html("").html("save fail!"+result.message);
                        $("#failedMsg").html("").html("save fail!"+result.message);
                    }
                }
            });
        });

        function getData(){
            var groupObject = {};
            groupObject.icon = $("#iconpath").html();
            groupObject.name = $("#name").val();
            groupObject.type = $("input[name='type']:checked").val();
            groupObject.welcomeMessage = $("#welcomeMessage").val();
            groupObject.relations  = $("#relations").val();

            var hasEmpty = false;
            for(var key in groupObject) {
                if(isEmptyOrEmpty(groupObject[key])){
                    hasEmpty = true;
                    alert(key + "is empty");
                    return;
                }
            }
            return hasEmpty?null:groupObject;
        }

        $("#upload").on("click",function(){
            AWS_FILEUPLAOD();
        });

        function isEmptyOrEmpty(obj){
            if(typeof obj == "undefined" || obj == null || obj == ""){
                return true;
            }else{
                return false;
            }
        }

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

            return addPhoto(bucketObject,"staging/","fileChooser");
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
                $("#icon").attr('src',iconPath);
                $("#iconpath").html("").html(iconPath);
            });
        }
    });
</script>

</body>
</html>
