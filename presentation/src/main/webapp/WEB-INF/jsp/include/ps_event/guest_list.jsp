<%@include file="/WEB-INF/jsp/include/page_header.jspf" %>
<head>
    <%@include file="/WEB-INF/jsp/include/ps_event/ps_event_header.jspf" %>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <title>Event</title>
    <link rel="stylesheet" href="${bb:libraryResource('/library/css/my_n.css')}" type="text/css" media="screen, projection" />
    <link href="${bb:libraryResource('/library/css/gue_lis.css')}" rel="stylesheet" type="text/css" />
    <link href="${bb:libraryResource('/library/css/psevent.css')}" rel="stylesheet" type="text/css" />

    <style>

    #guest_list_opts label {
        color: #999;
    }
    #guest_list_opts textarea {
        color: #999;
    }


    </style>
    <script type="text/javascript">
        $(document).ready(
            function () {
                $("#email_dialog").hide();
                $("#btn_add_email_guests").click(
                    function () {

                       collect_email_guests();

                    }
                );
            }
        );

        $(function(){
            $("#select_all").click(function(){
                $("#select_all img").attr("src", "/library/images/psevent/sel_allbg1.jpg")
                $("#select_none img").attr("src", "/library/images/psevent/sel_nonbg2.jpg")
                $(".friend_item").attr("checked", true)
            });
            $("#select_none").click(function(){
                $("#select_all img").attr("src", "/library/images/psevent/sel_allbg2.jpg")
                $("#select_none img").attr("src", "/library/images/psevent/sel_nonbg1.jpg")
                $(".friend_item").attr("checked", false)
            });
        });

        var collect_email_guests = function () {

                $("#email_dialog").show();
                $('#email_dialog').bbDialog({
                    buttons: {
                        "Ok": function() {
                            processEmailGuests();
                            $("#email_dialog").bbDialog("close");
                        },
                        "Cancel": function() {
                            $("#email_dialog").bbDialog("close");
                        }
                    },
                    title:"Add Email Guests",
                    modal:true

                });

                
        }

        var processEmailGuests = function () {
            var csv = $("#csv_text").val();
            alert(csv);
        }

        var last_step=function(){
            window.location.href="<s:url beanclass='com.blackbox.presentation.action.psevent.PSConfigEventActionBean'/>;jsessionid=<%=session.getId()%>?_eventName=member";
        }
        var next_step=function(){
            var bbUserStr=""
            var emailUserStr=""
            $(".bbUser[checked=true]").each(function(){
                bbUserStr=bbUserStr+this.value+",";
            });
            $(".emailUser[checked=true]").each(function(){
                emailUserStr=emailUserStr+this.value+",";
            })
            bbUserStr=bbUserStr.substr(0, bbUserStr.length-1);
            emailUserStr=emailUserStr.substr(0, emailUserStr.length-1);
            var receiverList={
                bboxReceiverGuidList:bbUserStr,
                emailReceiverStr:emailUserStr
            }
            var optionArray=$("#option_form").serializeArray();
            for(var index in optionArray){
                var option=optionArray[index]
                receiverList[option.name]=option.value;
            }
            $.post("<s:url event='confirmReceiver' beanclass='com.blackbox.presentation.action.psevent.PSAjaxEventActionBean'/>"
            , receiverList
            ,function(data){
                window.location.href="<s:url beanclass='com.blackbox.presentation.action.psevent.PSConfigEventActionBean'/>;jsessionid=<%=session.getId()%>?_eventName=webDetail";
            });
        }


        var guestList = [];
        var displaySelections=function(selections) {
                $("#big_add_button").hide();
                var entries = "";

                for (var i = 0; i < selections.length; i++) {

                    var userobj = selections[i];
                    if("null" != userobj) {
                        //entries += "<tr><td width='35' height='45'><img height='30' width='30' class='up-avatar' src='" + userobj.avatarUrl + "' alt='" + userobj.name + "'></td><td>" + userobj.name + "</td><td align='right'><input type='checkbox' name='list_action' value='" + userobj.guid + "' /></td></tr>";
                        entries = "<div class='add-guest-list-entry'><div class='add-guest-list-source'>Blackbox Republic</div><div class='add-guest-list-avatar'><img height='30' width='30' class='up-avatar' src='" + userobj.avatarUrl + "' alt='" + userobj.name + "'></div><div class='add-guest-list-name'>" + userobj.name + "</div><div class='add-guest-list-status'>Not responded</div><div class='add-guest-list-delete'>x</div></div>";
                        $('#guest_list_container').append(entries);
                    }
                    //add to the submission
                    guestList.push(userobj.guid);
                }

        }



function confirmReceiver(){

     $.post("<s:url beanclass='com.blackbox.presentation.action.psevent.PSAjaxEventActionBean'/>;jsessionid=<%=session.getId()%>",
        {
            "bboxReceiverGuidList": guestList,
            "occasion.occasionNotifyOption.reminderSendViaEmail": $("#reminderSendViaEmail").attr("checked"),
            "occasion.occasionNotifyOption.reminderSendToBbxInbox": $("#reminderSendToBbxInbox").attr("checked"),
            "occasion.occasionNotifyOption.reminderSend7DaysBeforeWORsvp": $("#reminderSend7DaysBeforeWORsvp").attr("checked"),
            "occasion.occasionNotifyOption.reminderSend1DayBeforeWRsvp": $("#sreminderSend1DayBeforeWORsvp").attr("checked"), // typo on name?
            "occasion.occasionNotifyOption.thankNotesSendViaEmail": $("#thankNotesSendViaEmail").attr("checked"),
            "occasion.occasionNotifyOption.thankNotesSendViaBbx": $("#thankNotesSendViaBbx").attr("checked"),
//            "occasion.occasionNotifyOption.thankCustomNotes": $("#thankCustomNotes").val(),
//            "occasion.occasionNotifyOption.guestMaxNbr": $("#guestMaxNbr").val(),
//            "occasion.occasionNotifyOption.guestBringFriendNbr": $("#guestBringFriendNbr").val(),
            "occasion.occasionNotifyOption.guestCanForward": $("#guestCanForward").attr("checked"),
            // "occasion.occasionNotifyOption.requireRsvpDays": $("#requireRsvp").attr("checked"),
            // "occasion.occasionNotifyOption.requireRsvpDays": $("#requireRsvpDays").val(), // as EVERY_RSVP or DAILY_ROLLUP
            // "occasion.occasionNotifyOption.sendRsvpNotification": $("#sendRsvpNotification").val(), // as EVERY_RSVP or DAILY_ROLLUP
            "_eventName": "confirmReceiver"
        },

        function(data){
              if("success" == data) {
                window.location.href="<s:url beanclass='com.blackbox.presentation.action.psevent.PSConfigEventActionBean'/>;jsessionid=<%=session.getId()%>?_eventName=webDetail";
              }
        });
}

        function bbrReminderSwitch(pstate) {
            var cstate = "";
            if(pstate) {
                cstate = false;
            } else {
                cstate = true;
            }

            $("#reminderSend7DaysBeforeWORsvp").attr("disabled", cstate);
            $("#reminderSend1DayBeforeWRsvp").attr("disabled", cstate);
        }

        function bbRsvpSwitch(pstate) {
            var cstate = "";
            if(pstate) {
                cstate = false;
            } else {
                cstate = true;
            }

            $("#sendRsvpNotification").attr("disabled", cstate);
        }

        function bbBringFriendSwitch(pstate) {
            var cstate = "";
            if(pstate) {
                cstate = false;
            } else {
                cstate = true;
            }

            $("#guestBringFriendNbr").attr("disabled", cstate);
        }

        function bbEventCapacitySwitch(pstate) {
            var cstate = "";
            if(pstate) {
                cstate = false;
            } else {
                cstate = true;
            }

            $("#guestMaxNbr").attr("disabled", cstate);
        }

        function bbRequireRsvpSwitch(pstate) {
            var cstate = "";
            if(pstate) {
                cstate = false;
            } else {
                cstate = true;
            }

            $("#rsvp_days_switch").attr("disabled", cstate);
            $("#requireRsvpDays").attr("disabled", cstate);
        }


    </script>

</head>

<body>


<div class="container container-top"></div>
    <div class="container darken">
        <form name="ccustomize_event_form" autocomplete="off" onsubmit="return false;">

			<h1 class="white event">Invite Guests</h1>

        <div id='event-body' class="bb-4px-corner-all event-body clearfix">
            <div id="event-workflow3">
                <div id="event-workflow-box1"></div>
                <div id="event-workflow-box2"></div>
            </div>

            <div id="guest_list_header">

               <img src="${bb:libraryResource('/library/images/events/btn_add_guests_to_list.png')}" alt="Add guests to list" class='user-chooser-link' style="position: relative; top: 15px; left: 20px; cursor: pointer;">
               <img src="${bb:libraryResource('/library/images/events/btn_remove_from_list.png')}" alt="Remove guests from list" style="position: relative; top: 15px; left: 17px; cursor: pointer;">
               <div style="float: right; padding-top: 20px; font-weight: bold; cursor: pointer; color: #dd3b26;">
                    <span id="btn_add_email_guests" style="display:none;">Add guests via email.</span>
               </div>
            </div>
            <div id="guest_list_blue_stripe">
            </div>
            <div>

            <img id='big_add_button' src='${bb:libraryResource('/library/images/events/add_guests_btn.png')}' class="user-chooser-link">


            </div>

            <div style="padding-top: 20px; display: none;">${actionBean.occasion.attendees}</div>

            <div class="gue_lis" style='margin-top: 8px; width: 928px;'>

                <div id="guest_list_container">

                <c:forEach var="attendee" items="${actionBean.occasion.attendees}">
                        <c:set var="source" value="Blackbox Republic" />
                        <div class="add-guest-list-entry">
                            <div class="add-guest-list-source">
                                ${source}    
                            </div>
                            <div class="add-guest-list-avatar">
                                <ui:profileImage guid="${attendee.bbxUserGuid}" size="small" color="white" linkToProfile="true" showMiniProfile="true" showUserType="true"/>
                            </div>
                            <div class="add-guest-list-name">
                                ${attendee.bbxUserName}
                            </div>
                            <div class="add-guest-list-status">
                                <c:if test="${attendee.attendingStatus  == 'NOT_RESPONDED'}">
                                    Not responded
                                    <c:set var="not_responded" scope="page" value="${not_responded + 1}" />
                                </c:if>
                                <c:if test="${attendee.attendingStatus == 'ATTENDING'}">
                                    Attending
                                    <c:set var="attending" scope="page" value="${attending + 1}" />
                                </c:if>
                                <c:if test="${attendee.attendingStatus == 'NOT_ATTENDING'}">
                                    Attending
                                    <c:set var="not_attending" scope="page" value="${not_attending + 1}" />
                                </c:if>
                                <c:if test="${attendee.attendingStatus == 'TENATIVE'}">
                                    Tentative
                                    <c:set var="tenative" scope="page" value="${tenative + 1}" />
                                </c:if>
                            </div>
                            <div class="add-guest-list-delete">

                            </div>
                        </div>
                    </c:forEach>

                    </div>

                


                <div id="guest_list_opts">
                    <div id="guest_list_opts_left"></div>
                    <div id="guest_list_opts_alpha">
                        <span style="font-weight: bold;">Notifications</span>
                        <br />
                        <input disabled="disabled" type="checkbox" id="reminderSendViaEmail" name="reminderSendViaEmail" /> <label for="reminderSendViaEmail" class="">Send via email</label>
                        <br />
                        <input disabled="disabled" type="checkbox" id="reminderSendToBbxInbox" name="reminderSendToBbxInbox" onclick='bbrReminderSwitch(this.checked);' /> <label for="reminderSendToBbxInbox">Send to Blackbox members' inbox</label>
                        <br />
                        <input disabled="disabled" class="ml20" type="checkbox" id="reminderSend7DaysBeforeWORsvp" name="reminderSend7DaysBeforeWORsvp" /> <label for="reminderSend7DaysBeforeWORsvp">Send one week before event with no response</label>
                        <br />
                        <input disabled="disabled" class="ml20 "type="checkbox" id="reminderSend1DayBeforeWRsvp" name="reminderSend1DayBeforeWRsvp" /> <label for="reminderSend1DayBeforeWRsvp">Send one day before event with no response</label>
                        <br />
                        <input disabled="disabled" type="checkbox" id="rsvp_switch" name="rsvp_switch" onclick="bbRsvpSwitch(this.checked)" /> <label for="rsvp_switch">Send RSVP notification</label>
                        <select id="sendRsvpNotification" name="sendRsvpNotification" disabled="disabled" />
                            <option value="EVERY_RSVP">Every RSVP</option>
                            <option value="DAILY_ROLLUP">One Email Per Day</option>
                        </select>
                    </div>
                    <div id="guest_list_opts_bravo">
                        <span style="font-weight: bold;">Thank You Notes</span>
                        <br />
                        <input disabled="disabled" type="checkbox" id="thankNotesSendViaEmail" name="thankNotesSendViaEmail" /> <label for="thankNotesSendViaEmail"> Send via email</label>
                        <br />
                        <input disabled="disabled" type="checkbox" id="thankNotesSendViaBbx" name="thankNotesSendViaBbx" /> <label for="thankNotesSendViaBbx">Send to Blackbox members' inbox</label>
                        <br />
                        <textarea disabled="disabled" style='margin-top: 3px; height: 60px; width: 280px;' onfocus="if($(this).html()=='Write an optional thank you message.'){$(this).html('')}">Write an optional thank you message.</textarea>
                    </div>
                    <div id="guest_list_opts_charlie">
                        <span style="font-weight: bold;">Permissions</span>
                        <br />
                        <input disabled="disabled" type="checkbox" id="guestCanForward" name="guestCanForward" onclick="bbBringFriendSwitch(this.checked)" /> <label for="guestCanForward">Members can bring</label>
                            <select id="guestBringFriendNbr" name="guestBringFriendNbr" disabled="disabled">
                                <option value="1">1</option>
                                <option value="2">2</option>
                                <option value="3">3</option>
                                <option value="4">4</option>
                                <option value="5">5</option>
                                <option value="6">6</option>
                                <option value="7">7</option>
                                <option value="8">8</option>
                                <option value="9">9</option>
                                <option value="10">10</option>
                            </select>
                            <label for="guestCanForward">guest(s)</label>
                        <br />
                        <input disabled="disabled" type="checkbox" id="event_capacity_switch" onclick="bbEventCapacitySwitch(this.checked);" /> <label for="event_capacity_switch">Set capacity at</label> <input type="text" size="5" id="guestMaxNbr" name="guestMaxNbr" disabled="disabled" /> <label for="event_capacity_switch">people</label>
                        <br />
                        <input disabled="disabled" type="checkbox" id="requireRsvp" name="requreRsvp" onclick="bbRequireRsvpSwitch(this.checked);" /> <label for="requireRsvp">Require RSVP</label>
                        <br />
                        <input disabled="disabled" type="checkbox" id="rsvp_days_switch" name="rsvp_days_switch" style="margin-left: 20px;" /> <input type="text" size="5" id="requireRsvpDays" name="requireRsvpDays" disabled="disabled" /> <label for="requireRsvpDays">days in advance</label>


                    </div>
                    <div id="guest_list_opts_right"></div>
                </div>

            <div class="clearfix">
                <div id="guest_list_button_container"><img src='${bb:libraryResource('/library/images/events/design_btn4.png')}' onclick="confirmReceiver();"></div>
            </div>

            </div>



        </div>
    </form>
    </div>

<div id="email_dialog" style="width: 400px; height: 300px;">
     Enter CSV email addresses.
    <textarea id="csv_text" style="width: 395px; height: 270px; resize: none;"></textarea>

</div>
<script type="text/javascript" language="javascript">
//<![CDATA[
    $(".user-chooser-link").click(function() {

        //Open the picker sending the allowMultiple option, and assign a reference
        //to the picker that's returned. Without the open, you can only make one
        //selection at a time, and the return object is different

        var userPicker = bb.userPicker.open({allowMultiple:true});

        //Bind a function to the selectUsers event which is invoked when the user
        //clicks the "Add users" button. The selections parameter in the handler
        //contains a list of objects containing the user name, the avatar URL and
        //the guid for that user.

        userPicker.bind("selectUsers", function(event, selections) {
            //Do something with the selections returned...
            displaySelections(selections);
            //And then close the picker.
            userPicker.userChooser('close');
        });
    });
//]]>
</script>

</body>
</html>

