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
    <meta name="pageID" content="hdGuest-operate"/>
    <script src="jquery-3.4.1.min.js"></script>
</head>
<body>
<div class="jive-contentBoxHeader">guest entry into Hotel chat</div>
<div class="jive-contentBox">
    <table cellpadding="3" cellspacing="0" border="0" width="100%">
        <tbody>
        <tr>
            <td>
                <div id="hotel-guestInfo-id">
                    <table cellspacing="0" border="0" width="100%">
                        <tbody>
                            <tr>
                                <td width="10%">
                                    Device User ID:
                                </td>
                                <td width="90%">
                                    <input type="text" name="deviceUserId" id="hotel-deviceUserId-id">
                                </td>
                            </tr>
                            <tr>
                                <td width="10%">
                                    Hotel ID:
                                </td>
                                <td width="90%">
                                    <input type="text" name="hotelId" id="hotel-hotelId-id" >
                                </td>
                            </tr>
                            <tr>
                                <td width="10%">
                                    Hotel Name:
                                </td>
                                <td width="90%">
                                    <input type="text" name="hotelName" id="hotel-hotelName-id">
                                </td>
                            </tr>
                            <tr>
                                <td width="10%">
                                    Room Number:
                                </td>
                                <td width="90%">
                                    <input type="text" name="roomNum" id="hotel-roomNum-id">
                                </td>
                            </tr>
                            <tr>
                                <td width="10%">
                                    Zone ID:
                                </td>
                                <td width="90%">
                                    <input type="text" name="zoneId" id="hotel-zoneId-id">
                                </td>
                            </tr>
                            <tr>
                                <td width="10%">
                                    Zone Name:
                                </td>
                                <td width="90%">
                                    <input type="text" name="zoneName" id="hotel-zoneName-id">
                                </td>
                            </tr>
                        </tbody>
                        <tfoot>
                            <tr>
                                <td>
                                    <button type="button" id="save-hotel-btn-id">save hotel guest</button>
                                </td>
                            </tr>
                        </tfoot>
                    </table>
                    <div id="hotel-msg"></div>
                </div>
            </td>
        </tr>
        </tbody>
    </table>
</div>
<div class="jive-contentBoxHeader">guest entry into VIP chat</div>
<div class="jive-contentBox">
    <table cellpadding="3" cellspacing="0" border="0" width="100%">
        <tbody>
        <tr>
            <td>
                <div id="vip-guestInfo-id">
                    <table cellspacing="0" border="0" width="100%">
                        <tbody>
                            <tr>
                                <td width="10%">
                                    Display Name:
                                </td>
                                <td width="90%">
                                    <input type="text" name="displayName" id="vip-displayName-id">
                                </td>
                            </tr>
                            <tr>
                                <td width="10%">
                                    Email:
                                </td>
                                <td width="90%">
                                    <input type="text" name="email" id="vip-email-id">
                                </td>
                            </tr>
                            <tr>
                                <td width="10%">
                                    Device User ID:
                                </td>
                                <td width="90%">
                                    <input type="text" name="v-deviceUserId" id="vip-deviceUserId-id">
                                </td>
                            </tr>
                            <tr>
                                <td width="10%">
                                    Hotel ID:
                                </td>
                                <td width="90%">
                                    <input type="text" name="v-hotelId" id="vip-hotelId-id" >
                                </td>
                            </tr>
                            <tr>
                                <td width="10%">
                                    Hotel Name:
                                </td>
                                <td width="90%">
                                    <input type="text" name="v-hotelName" id="vip-hotelName-id">
                                </td>
                            </tr>
                            <tr>
                                <td width="10%">
                                    Room Number:
                                </td>
                                <td width="90%">
                                    <input type="text" name="v-roomNum" id="vip-roomNum-id">
                                </td>
                            </tr>
                            <tr>
                                <td width="10%">
                                    Zone ID:
                                </td>
                                <td width="90%">
                                    <input type="text" name="v-zoneId" id="vip-zoneId-id">
                                </td>
                            </tr>
                            <tr>
                                <td width="10%">
                                    Zone Name:
                                </td>
                                <td width="90%">
                                    <input type="text" name="v-zoneName" id="vip-zoneName-id">
                                </td>
                            </tr>
                        </tbody>
                        <tfoot>
                        <tr>
                            <td>
                                <button type="button" id="save-vip-btn-id">save hotel guest</button>
                            </td>
                        </tr>
                        </tfoot>
                    </table>
                    <div id="vip-msg"></div>
                </div>
            </td>
        </tr>
        </tbody>
    </table>
</div>

<br>
<script type="text/javascript">
    $(document).ready(function () {
        $("#save-hotel-btn-id").click(function(){
            if(!$("input[name='deviceUserId']").val()){
                alert("please input deviceUserId");
                $("input[name='deviceUserId']").focus();
                return false;
            }
            if(!$("input[name='hotelId']").val()){
                alert("please input hotelId");
                $("input[name='hotelId']").focus();
                return false;
            }
            if(!$("input[name='hotelName']").val()){
                alert("please input hotelName");
                $("input[name='hotelName']").focus();
                return false;
            }
            if(!$("input[name='roomNum']").val()){
                alert("please input roomNum");
                $("input[name='roomNum']").focus();
                return false;
            }
            if(!$("input[name='zoneId']").val()){
                alert("please input zoneId");
                $("input[name='zoneId']").focus();
                return false;
            }
            if(!$("input[name='zoneName']").val()){
                alert("please input zoneName");
                $("input[name='zoneName']").focus();
                return false;
            }
            $.ajax({
                url:"api",
                type:"POST",
                contentType:"application/json",
                dataType:"json",
                data:JSON.stringify({
                    authType:0,
                    deviceUserId:$("input[name='deviceUserId']").val(),
                    hotelId:$("input[name='hotelId']").val(),
                    hotelName:$("input[name='hotelName']").val(),
                    roomNum:$("input[name='roomNum']").val(),
                    zoneId:$("input[name='zoneId']").val(),
                    zoneName:$("input[name='zoneName']").val()
                }),
                success:function (resultData) {
                    if(resultData.success){
                        $("#hotel-msg").html("<span style='color: green'>save success</span>");
                    }else{
                        $("#hotel-msg").html("<span style='color: red'>save fail!</span>");
                    }
                }
            });
        });
        $("#save-vip-btn-id").click(function(){
            if(!$("input[name='displayName']").val()){
                alert("please input displayName");
                $("input[name='displayName']").focus();
                return false;
            }
            if(!$("input[name='email']").val()){
                alert("please input email");
                $("input[name='email']").focus();
                return false;
            }
            if(!$("input[name='v-deviceUserId']").val()){
                alert("please input deviceUserId");
                $("input[name='v-deviceUserId']").focus();
                return false;
            }
            if(!$("input[name='v-hotelId']").val()){
                alert("please input hotelId");
                $("input[name='v-hotelId']").focus();
                return false;
            }
            if(!$("input[name='v-hotelName']").val()){
                alert("please input hotelName");
                $("input[name='v-hotelName']").focus();
                return false;
            }
            if(!$("input[name='v-roomNum']").val()){
                alert("please input roomNum");
                $("input[name='v-roomNum']").focus();
                return false;
            }
            if(!$("input[name='v-zoneId']").val()){
                alert("please input zoneId");
                $("input[name='v-zoneId']").focus();
                return false;
            }
            if(!$("input[name='v-zoneName']").val()){
                alert("please input zoneName");
                $("input[name='v-zoneName']").focus();
                return false;
            }
            $.ajax({
                url:"api",
                type:"POST",
                contentType:"application/json",
                dataType:"json",
                data:JSON.stringify({
                    authType:1,
                    displayName:$("input[name='displayName']").val(),
                    email:$("input[name='email']").val(),
                    deviceUserId:$("input[name='v-deviceUserId']").val(),
                    hotelId:$("input[name='v-hotelId']").val(),
                    hotelName:$("input[name='v-hotelName']").val(),
                    roomNum:$("input[name='v-roomNum']").val(),
                    zoneId:$("input[name='v-zoneId']").val(),
                    zoneName:$("input[name='v-zoneName']").val()
                }),
                success:function (resultData) {
                    if(resultData.success){
                        $("#vip-msg").html("<span style='color: green'>save success</span>");
                    }else{
                        $("#vip-msg").html("<span style='color: red'>save fail!</span>");
                    }
                }
            });
        });
    });
</script>

</body>
</html>
