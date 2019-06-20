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
</head>
<body>
<p>
    handy agent group setting
</p>
<p>
    just for add and delete,not edit
</p>
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
                                    name
                                </td>
                                <td width="90%">
                                    <input type="text" id="group_name" value="" required>
                                </td>
                            </tr>
                            <tr>
                                <td width="10%">
                                    icon
                                </td>
                                <td width="90%">
                                    <input type="text" id="group_icon" value="" required>
                                </td>
                            </tr>
                            <tr>
                                <td width="10%">
                                    type
                                </td>
                                <td width="90%">
                                    <input type="radio" name="group_type" value="VIP"> <label><b>vip chat room</b></label>
                                    <input type="radio" name="group_type" value="HOTEL"> <label><b>hotel chat room</b></label>
                                </td>
                            </tr>
                            </tbody>
                            <tfoot>
                            <tr>
                                <td>
                                    <input type="button" value="save" id="group_save">
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
                                    user_zone_id
                                </td>
                                <td width="70%">
                                    <input type="text" id="user_zone_id" value="" required>
                                </td>
                            </tr>
                            <tr>
                                <td width="30%">
                                    user_zone_name
                                </td>
                                <td width="70%">
                                    <input type="text" id="user_zone_name" value="" required>
                                </td>
                            </tr>
                            <tr>
                                <td width="30%">
                                    user_email
                                </td>
                                <td width="70%">
                                    <input type="text" id="user_email" value="" required>
                                </td>
                            </tr>
                            <tr>
                                <td width="30%">
                                    user_display_name
                                </td>
                                <td width="70%">
                                    <input type="text" id="user_displayname" value="" required>
                                </td>
                            </tr>
                            <tr>
                                <td width="30%">
                                    user_password
                                </td>
                                <td width="70%">
                                    <input type="text" id="user_password" value="" required>
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
<script src="jquery-3.4.1.min.js"></script>
<script>
    $(document).ready(function(){
        init();
        //group_save
        $("#group_save").on("click",function(){
            var group={};
            group.name=$("#group_name").val();
            group.icon=$("#group_icon").val();
            group.type=$('input[name="group_type"]:checked').val();
            alert(JSON.stringify(group));
            // save(group);
        });

        //hotel_or_zone_save
        $("#zone_or_hotel_save").on("click",function(){
            var group_id = $("#h_group_id").val();
            if(group_id == "" || group_id == null){
                alert("choice a group first");
                return;
            }
            var hotel_or_zone={};
            hotel_or_zone.id=$("#zone_or_hotel_id").val();
            hotel_or_zone.name=$("#zone_or_hotel_name").val();
            hotel_or_zone.groupId=group_id;
            alert(JSON.stringify(hotel_or_zone));
            // save(hotel_or_zone);
        });

        //agent_save
        $("#agent_save").on("click",function(){
            var group_id = $("#h_group_id").val();
            if(group_id == "" || group_id == null){
                alert("choice a group first");
                return;
            }
            var agent={};
            agent.zoneId=$("#user_zone_id").val();
            agent.zoneName=$("#user_zone_name").val();
            agent.email=$("#user_email").val();
            agent.displayName=$("#user_displayname").val();
            agent.password=$("#user_password").val();
            agent.groupId=group_id;
            alert(JSON.stringify(agent));
            // save(agent);
        });

        $("#list").on("click",'input[name="group_choice"]',function(){
            $("#h_group_id").val($(this).val());
        });

        $("#list").on("click","a",function(){
            var id=$(this).data("id");
            var type=$(this).data("type");
            alert(id+type);
        });

        function init() {
            var groups=[
                {
                    id:'1',
                    type:"VIP",
                    icon:"http://4532545324523",
                    name:"group1",
                    relations:[
                        {
                            id:"1",
                            name:"香港"
                        },
                        {
                            id:"2",
                            name:"北京"
                        }
                    ],
                    agents:[
                        {
                            id:"fdads",
                            email:"h@tinks.com",
                            displayname:"h"
                        }
                    ]
                },
                {
                    id:'2',
                    type:"HOTEL",
                    icon:"http://4532545324523",
                    name:"group2",
                    relations:[
                        {
                            id:"1",
                            name:"酒店1"
                        },
                        {
                            id:"2",
                            name:"酒店2"
                        }
                    ],
                    agents:[
                        {
                            id:"fdads",
                            email:"h@tinks.com",
                            displayname:"h"
                        }
                    ]
                },
            ];
            // $.ajax({
            //     type: "post",
            //     url: 'add-post-json.php',
            //     contentType: "application/json; charset=utf-8",
            //     dataType: "json",
            //     data: JSON.stringify(object),
            //     success: function(result) {
            //         if(result.success){
            //             var groups = result.data;
                        var html_trs = '';
                        $.each(groups,function(i,group){
                            var html_tr = "<tr>";
                            var html_group_td =
                            '<td width="10%">'+ '<input type="radio" name="group_choice" value="'+group.id+'"></td>'+
                            '<td width="40%"><label><b>'+group.icon+"-"+group.name+"-"+group.type+'</b></label></td>';

                            var zone_or_hotel_td = '<td width="20%">';
                            $.each(group.relations,function(i,relation) {
                                    var relationhtml = '<p>' + relation.id + "-" + relation.name +
                                        '<a href="javascript:void()" data-id="'+relation.id+'" data-type="relation" title="删除"><img src="images/delete-16x16.gif" width="16" height="16" border="0" alt="删除 Content Filter"></a>' +
                                        '</p>';
                                zone_or_hotel_td = zone_or_hotel_td + relationhtml;
                            });
                            zone_or_hotel_td = zone_or_hotel_td + "</td>";

                            var agent_td='<td width="20%">';
                            $.each(group.agents,function(i,agent) {
                                var agenthtml = '<p>' + agent.email + "-" + agent.displayname +
                                    '<a href="javascript:void()" data-id="'+agent.id+'" data-type="agent" title="删除"><img src="images/delete-16x16.gif" width="16" height="16" border="0" alt="删除 Content Filter"></a>' +
                                '</p>';
                                agent_td = agent_td + agenthtml;
                            });
                            agent_td = agent_td + "</td>";

                            html_tr = html_tr+html_group_td+zone_or_hotel_td+agent_td+"</tr>";
                            html_trs=html_trs+html_tr;
                        })
                    // }
            //     }
            // });
            $("#list").html('').append($(html_trs));
        };

        function save(object) {
            $.ajax({
                type: "post",
                url: 'add-post-json.php',
                contentType: "application/json; charset=utf-8",
                dataType: "json",
                data: JSON.stringify(object),
                success: function(data) {

                }
            });
        }
    });
</script>
</body>
</html>
