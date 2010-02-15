<head>
    <title>Event</title>
    <%@include file="/WEB-INF/jsp/include/ps_event/ps_event_header.jspf" %>
    <%--@elvariable id="actionBean" type="com.blackbox.presentation.action.psevent.PSAjaxEventActionBean"--%>
    
    
    <script type="text/javascript">
         var showPermissionDlg = function () {

                $("#permission_dialog").show();
                $('#permission_dialog').bbDialog({
                    buttons: {
                        "Bummer": function() {
                            $("#permission_dialog").bbDialog("close");
                            window.location="/community";
                        }
                        <%--
                        ,
                        "Upgrade": function() {
                            $("#permission_dialog").bbDialog("close");
                            window.location="/community";
                        }
                        --%>
                    },
                    title:"Upgrade your account!",
                    modal:true

                });


        }

        function todayStr() {
            <%@ page import="org.joda.time.DateTime" %>
            <c:set var="date" value="<%=new DateTime()%>"/>
            return "${bb:formatDate(date, "M/dd/yyyy")}";
            
        }
        var event_detail={privacy:null};
        $(document).ready(function(){
            if("LIMITED" == "${actionBean.currentUser.type}") {
                showPermissionDlg();
            }

            if($("#event_name").val() == "") {
                $("#event_name").focus();
            }

            <%--
             $("#events-message").bbDialog({
                onopen: function() {$(this).show();$(this).bbDialog('center');},
                autoCenterX:true,
                autoCenterY:true,
                modal:true,
                draggable:false,
                buttons: {
                    'OK':function() {this.close();}
                },
                title:'Hey there camper!'
            });
            --%>

            $("#event_title").focus();

            //var start = ${bb:formatDate(actionBean.occasion.eventTime,'h:mm')};
            //alert(start);

            /* determine and set the privacy levl */
            selectPrivacyLevel('${actionBean.occasion.occasionType}');

            // set some restrictions on form field inputs
            $('#event_zip').numeric();
            $('#event_start_time').numeric({allow:":"});
            $('#event_end_time').numeric({allow:":"});
            $('#event_phone_number').numeric({allow: "().- "});


            if($("#event_date").val()==null||$("#event_date").val()==""){
                $("#event_date").val(todayStr());
            }
       
                var curYear = new Date().getFullYear();
                var yearRange = curYear + ":" + (curYear + 1);
			    $("#event_date").datepicker({
				    changeMonth: true,
				    changeYear: true,
				    dateFormat:'mm/dd/yy',
				    yearRange: yearRange
			    });

                // pulled by rhayes because it doesn't look good, or make much sense
                //$("#google_map_checker").click(function(){
                //    if(this.checked){
                //        create_dialog_for_google_map();
                //    }
                //});

                $(".event_privacy").click(function(){
                    event_detail.privacy=$(this).val();
                });
                $("#event_time").val("${bb:formatDate(occasion.eventTime,'h:mm')}");
                $("#am_pm").val("${bb:formatDate(occasion.eventTime,'a')}");
            });
            var getAddress=function(){
                return $("#event_street1").val()+" "
                    +$("#city").val()+" "
                    +$("#state").val()+" "
                    +"USA";
            };
            var getLocation=function(){
                return $("#location").val();
            };



            var save_detail=function(cb,re){

                
                var not_save='null';
                if(re=="true"){
                    not_save="true";
                }

                // validate form data.  This should use the stanard validation, however the java is currently bypassing
                // validation for events - and - we may be changing how we handle the errors as I don't believe that
                // our current method will work within the event form

                var validationFail = false;

                if($("#event_name").val().length < 1) {
                    $("#event_name").addClass("bb-input-error");
                    validationFail = true;
                }

                if($("#event_date").val().length < 1) {
                    $("#event_date").addClass("bb-input-error");
                    validationFail = true;
                }

                if($("#event_host").val().length < 1) {
                    $("#event_host").addClass("bb-input-error");
                    validationFail = true;
                }

                if($("#event_start_time").val().length < 1) {
                    $("#event_start_time").addClass("bb-input-error");
                    validationFail = true;
                }

                if(validationFail) {
                    $.bbDialog.info("Please fill in all required fields");
                    return false;
                }

                
                // rhayes confirm fields with AJ
                // validation passed, so submit the form data
                $.post("<s:url beanclass='com.blackbox.presentation.action.psevent.PSAjaxEventActionBean'/>;jsessionid=<%=session.getId()%>",
                    {
                        "occasion.name": $("#event_name").val(),
                        "occasion.occasionType": $("#privacy_field").val(),
                        "occasion.location": $("#event_location").val(),
                        "occasion.hostBy": $("#event_host").val(),
                        "occasion.address.address1": $("#event_street1").val(),
                        "occasion.address.address2": $("#event_street2").val(),
                        "occasion.address.city": $("#event_city").val(),
                        "occasion.address.state": $("#event_state").val(),
                        "occasion.address.country": "UNITED_STATES",
                        "occasion.address.zipCode": $("#event_zip").val(),
                        "occasion.phoneNumber": $("#event_phone_number").val(),
                        "occasion.phoneExtension": $("#event_phone_extension").val(),
                        "occasion.eventUrl": $("#event_website").val(),
                        "occasion.email": $("#event_email").val(),
                        "occasion.withGoogleMap": $("#google_map_checkbox").attr("checked"),
                        "occasion.description": $("#event_description").val(),
                        "date": $("#event_date").val(),
                        "startTime": $("#event_start_time").val(),
                        "startAmpm": $("#event_start_ampm").val(),
                        "endTime": $("#event_end_time").val(),
                        "endAmpm": $("#event_end_ampm").val(),
                        "_eventName": "saveDetails"
                    },

                    function(data){
                          if("success" == data) {
                            window.location.href="<s:url beanclass='com.blackbox.presentation.action.psevent.PSConfigEventActionBean'/>;jsessionid=<%=session.getId()%>?_eventName=config";
                          } else { 
                              //alert(data);
                          }
                    });
                };
            
                        
        function nextStep(){
            save_detail(function(){
                window.location.href="<s:url beanclass='com.blackbox.presentation.action.psevent.PSConfigEventActionBean'/>;jsessionid=<%=session.getId()%>?_eventName=config";
            });
        };
        function prevStep(){
            save_detail(function(){
                window.location.href="<s:url beanclass='com.blackbox.presentation.action.psevent.PSConfigEventActionBean'/>;jsessionid=<%=session.getId()%>?_eventName=member";
            },'true');
        };
        var finished_step=function(){
            save_detail(function(){
                window.location.href="/event/show/${actionBean.occasion.eventUrl}";
            });
        };


        function toggleMap(showMap) {

            if(showMap) {
                $("#event_map_preview").show();
            } else {
                $("#event_map_preview").hide();
            }
            
        }

        function selectPrivacyLevel(privacyopt) {

            //alert(privacyopt);

            if("OPEN" == privacyopt) {
                $("#privacy_edge_left").removeClass('privacy-left-edge-off');
                $("#privacy_edge_left").addClass('privacy-left-edge-on');
                $("#privacy_edge_right").removeClass('privacy-right-edge-on');
                $("#privacy_edge_right").addClass('privacy-right-edge-off');

                $("#privacy_open").removeClass('privacy-box-off');
                $("#privacy_open").addClass('privacy-box-on');
                $("#privacy_invite").removeClass('privacy-box-on');
                $("#privacy_invite").addClass('privacy-box-off');
                $("#privacy_private").removeClass('privacy-box-on');
                $("#privacy_private").addClass('privacy-box-off');

                $("#privacy_opt_open_radio").attr('src', '${bb:libraryResource("/library/images/events/fakeradio_on.png")}');
                $("#privacy_opt_invite_radio").attr('src', '${bb:libraryResource("/library/images/events/fakeradio_off.png")}');
                $("#privacy_opt_private_radio").attr('src', '${bb:libraryResource("/library/images/events/fakeradio_off.png")}');
                $("#privacy_field").val("OPEN");

            }

            if("INVITE_ONLY" == privacyopt) {
                $("#privacy_edge_left").removeClass('privacy-left-edge-on');
                $("#privacy_edge_left").addClass('privacy-left-edge-off');
                $("#privacy_edge_right").removeClass('privacy-right-edge-on');
                $("#privacy_edge_right").addClass('privacy-right-edge-off');

                $("#privacy_open").removeClass('privacy-box-on');
                $("#privacy_open").addClass('privacy-box-off');
                $("#privacy_invite").removeClass('privacy-box-off');
                $("#privacy_invite").addClass('privacy-box-on');
                $("#privacy_private").removeClass('privacy-box-on');
                $("#privacy_private").addClass('privacy-box-off');

                $("#privacy_opt_open_radio").attr('src', '${bb:libraryResource("/library/images/events/fakeradio_off.png")}');
                $("#privacy_opt_invite_radio").attr('src', '${bb:libraryResource("/library/images/events/fakeradio_on.png")}');
                $("#privacy_opt_private_radio").attr('src', '${bb:libraryResource("/library/images/events/fakeradio_off.png")}');
                $("#privacy_field").val("INVITE_ONLY");

            }

            if("PRIVATE" == privacyopt) {
                $("#privacy_edge_left").removeClass('privacy-left-edge-on');
                $("#privacy_edge_left").addClass('privacy-left-edge-off');
                $("#privacy_edge_right").removeClass('privacy-right-edge-off');
                $("#privacy_edge_right").addClass('privacy-right-edge-on');

                $("#privacy_open").removeClass('privacy-box-on');
                $("#privacy_open").addClass('privacy-box-off');
                $("#privacy_invite").removeClass('privacy-box-on');
                $("#privacy_invite").addClass('privacy-box-off');
                $("#privacy_private").removeClass('privacy-box-off');
                $("#privacy_private").addClass('privacy-box-on');

                $("#privacy_opt_open_radio").attr('src', '${bb:libraryResource("/library/images/events/fakeradio_off.png")}');
                $("#privacy_opt_invite_radio").attr('src', '${bb:libraryResource("/library/images/events/fakeradio_off.png")}');
                $("#privacy_opt_private_radio").attr('src', '${bb:libraryResource("/library/images/events/fakeradio_on.png")}');
                $("#privacy_field").val("PRIVATE");

            }

        }


		$(function() {
	
			<c:if test="${actionBean.firstTime}">
		    $.prettyPhoto.open('http://cdn.episodic.com/player/EpisodicPlayer.swf?width=480&height=360&config=http%3A%2F%2Fcdn.episodic.com%2Fshows%2Fob0pj9jq17up%2Fob1k92falblz%2Fconfig.xml');
		    </c:if>
		
		    $("#intro").click(function() {
		       $.prettyPhoto.open('http://cdn.episodic.com/player/EpisodicPlayer.swf?width=480&height=360&config=http%3A%2F%2Fcdn.episodic.com%2Fshows%2Fob0pj9jq17up%2Fob1k92falblz%2Fconfig.xml');
		    })

		});

    </script>


</head>

<body>
	<div style="display:none;" id="events-message">
	<p>Events are nearly done. Feel free to play and give us feedback.<br/>
	We'll let you know when it's time to use the feature to throw your bashes.</p>
	<p>But please feel free to play for now. :)</p>
</div>
    <div class="container container-top"></div>
    <div class="container darken">
        <form name="create_event_form" autocomplete="off" onsubmit="return false;">

			<h1 class="white event">Event Details</h1>

        <div class="widget-top" style='margin-left: 11px; margin-right: 11px;'><div class="widget-top-right"></div></div>
        <div id='event_body' class="event-body clearfix">
            <div id="event-workflow1">

            </div>




            <div id="event_form_div" class="clearfix" style="margin-top: 13px; margin-left: 4px;">
                <div id="event_form_title_left"></div>
                    <div id="event_title_container">

                        <div style="padding-top: 20px;">
                            <div style="float: left; font-weight: bold; font-size: 18px; line-height: 30px; margin-right: 8px;">Name your event</div>
                            <div class="input-left"></div>
                            <div class="input-mid" style='width: 717px;'><input type='text' id='event_name' name='event_name' value='${actionBean.occasion.name}' style="width: 717px;" /></div>
                            <div class="input-right"></div>
                        </div>
                        
                    </div>
                <div id="event_form_title_right"></div>
                <div style='clear: both;'></div>

                <%-- left side of event creation form --%>
                <div style="width: 560px; height: 500px; float: left;">

                    <%-- labels --%>
                    <div>
                        <label for="event_date" class="input-label" style="width: 260px;">Date *</label>
                        <label for="event_start_time" class="input-label" style='width: 147px;'>Start Time *</label>
                        <label for="event_end_time" class="input-label" style='width: 125px;'>End Time</label>
                    </div>

                    <%-- inputs --%>
                    <div style='clear: both;'>

                        <div class="input-left"></div>
                        <div class="input-mid" style='width: 244px;'><input type='text' id='event_date' name='event_date' maxlength="255" value='<c:if test="${not empty actionBean.occasion.eventTime}">${bb:formatDate(actionBean.occasion.eventTime, "M/dd/YYYY")}</c:if>' style="width: 240px;" /></div>
                        <div class="input-right"></div>

                        <div class="input-left" style='margin-left: 5px;'></div>
                        <div class="input-mid"><input id="event_start_time" name='event_start_time' type="text" maxlength="255" value="${bb:formatDate(actionBean.occasion.eventTime, "h:mm")}" style="width: 70px;"></div>
                        <div class="input-right"></div>

                        <div style='float: left; margin-left: 5px; padding-top: 2px;'>
                            <select id="event_start_ampm" name="event_start_ampm" style="font-size: 18px;">
                                <option value="AM">AM</option>     <c:if test="${bb:formatDate(actionBean.occasion.eventTime, 'a') eq 'AM'}" >poop</c:if>
                                <option value="PM" selected="">PM</option>
                            </select>
                        </div>

                        <div class="input-left" style="float: left; margin-left: 5px;"></div>
                        <div class="input-mid"><input id='event_end_time' name='event_end_time' type="text" maxlength="255" value="${bb:formatDate(actionBean.occasion.eventEndTime, "h:mm")}" style="width: 70px;"></div>
                        <div class="input-right"></div>

                        <div style='float: left; margin-left: 5px; padding-top: 2px;'>
                            <select id="event_end_ampm" name="event_end_ampm" style="font-size: 18px;">
                                <option value="AM">AM</option>
                                <option value="PM" selected="">PM</option>
                            </select>
                        </div>
                    </div>

                    <%-- labels --%>
                    <div class="clearfix" style="display:block;width:270px;">
                        <div class="input-label" style='width: 270px;'>Location Name</div>
                    </div>

                    <%-- inputs --%>
                    <div class="clearfix">
                        <div class="input-left"></div>
                        <div class="input-mid" style='width: 550px;'><input type='text' id="event_location" name='event_location' maxlength="255" value='${actionBean.occasion.location}' style="width: 550px;" /></div>
                        <div class="input-right"></div>
                    </div>

                    <%-- labels --%>
                    <div class="clearfix">
                        <div class="input-label" style="width: 446px;">Street</div>
                        <div class="input-label">Ste/Apt</div>
                    </div>

                    <%-- inputs --%>
                    <div class="clearfix">
                        <div class="input-left"></div>
                        <div class="input-mid" style='width: 430px;'><input type='text' id="event_street1" name='event_street1' maxlength="255" value='${actionBean.occasion.address.address1}' style="width: 430px;" /></div>
                        <div class="input-right"></div>

                        <div class="input-left" style='margin-left: 5px;'></div>
                        <div class="input-mid" style='width: 104px;'><input type='text' id="event_street2" name='event_street2' maxlength="255" value='${actionBean.occasion.address.address2}' style="width:104px;" /></div>
                        <div class="input-right"></div>
                    </div>

                    <%-- labels --%>
                    <div class="clearfix">
                        <div class="input-label" style="width: 362px;">City</div>
                        <div class="input-label" style="width: 61px;">State</div>
                        <div class="input-label">Zip/Postal Code</div>
                    </div>


                    <%-- inputs --%>
                    <div class="clearfix">
                        <div class="input-left"></div>
                        <div class="input-mid" style='width: 342px;'><input type='text' id="event_city" name='event_city' maxlength="255" value='${actionBean.occasion.address.city}' style="width: 342px;" /></div>
                        <div class="input-right"></div>


                        <%-- ${occasion.address.state} --%>
                        <div style="float: left; margin-left: 5px; padding-top: 2px;">
                            <select id="event_state" name="event_state" style="font-size: 18px;">
                                <option value="0"></option>
                                <option value="Alabama">AL</option>
                                <option value="Alaska">AK</option>
                                <option value="Arizona">AZ</option>
                                <option value="Arkansas">AR</option>
                                <option value="California">CA</option>
                                <option value="Colorado">CO</option>
                                <option value="Connecticut">CT</option>
                                <option value="Deleware">DE</option>
                                <option value="Florida">FL</option>
                                <option value="Georga">GA</option>
                                <option value="Hawaii">HI</option>
                                <option value="Idaho">ID</option>
                                <option value="Illinois">IL</option>
                                <option value="Indiana">IN</option>
                                <option value="Iowa">IA</option>
                                <option value="Kansas">KS</option>
                                <option value="Kentucky">KY</option>
                                <option value="Louisiana">LA</option>
                                <option value="Maine">ME</option>
                                <option value="Maryland">MD</option>
                                <option value="Massachusetts">MA</option>
                                <option value="Michigan">MI</option>
                                <option value="Minnesota">MN</option>
                                <option value="Mississippi">MS</option>
                                <option value="Missouri">MO</option>
                                <option value="Montana">MT</option>
                                <option value="Nebraska">NE</option>
                                <option value="Nevada">NV</option>
                                <option value="New Hampshire">NH</option>
                                <option value="New Jersy">NJ</option>
                                <option value="New Mexio">NM</option>
                                <option value="New York">NY</option>
                                <option value="North Carolina">NC</option>
                                <option value="North Dakota">ND</option>
                                <option value="Ohio">OH</option>
                                <option value="Oklahoma">OK</option>
                                <option value="Oregon">OR</option>
                                <option value="Pennsylvania">PA</option>
                                <option value="Rhode Island">RI</option>
                                <option value="South Carolina">SC</option>
                                <option value="South Dakota">SD</option>
                                <option value="Tennessee">TN</option>
                                <option value="Texas">TX</option>
                                <option value="Utah">UT</option>
                                <option value="Vermont">VT</option>
                                <option value="Virginia">VA</option>
                                <option value="Washington">WA</option>
                                <option value="West Virginia">WV</option>
                                <option value="Wisconsin">WI</option>
                                <option value="Wyoming">WY</option>
                            </select>
                        </div>

                        <div class="input-left" style='margin-left: 5px;'></div>
                        <div class="input-mid" style='width: 123px;'><input type='text' id="event_zip" name='event_zip' maxlength="20" value='${actionBean.occasion.address.zipCode}' style="width: 123px;" /></div>
                        <div class="input-right"></div>
                    </div>

                    <%-- labels --%>
                    <div class="clearfix">
                        <div class="input-label" style='width: 270px;'>Describe your event</div>
                    </div>

                    <%-- inputs --%>
                    <div class="clearfix">
                        <div class="textarea-left"></div>
                        <textarea class="bb-textarea" id="event_description" name='event_description' style="width: 550px;">${actionBean.occasion.description}</textarea>
                        <div class="textarea-right"></div>
                    </div>

                </div>
                <div style="margin-left: 17px; border: 0px solid blue; width: 344px; height: 500px; float: left">

                    <%-- labels --%>
                    <div class="clearfix">
                        <div class="input-label">Host *</div>
                    </div>

                    <%-- inputs --%>
                    <div class="clearfix">
                        <div class="input-left"></div>
                        <div class="input-mid" style='width: 334px;'><input type='text' id="event_host" name='event_host' maxlength="255" value='${actionBean.occasion.hostBy}' style="width: 334px;" /></div>
                        <div class="input-right"></div>
                    </div>

                    <%-- labels --%>
                    <div class="clearfix">
                        <div class="input-label" style="width: 241px;">Phone</div>
                        <div class="input-label">Ext</div>
                    </div>

                    <%-- inputs --%>
                    <div class="clearfix">
                        <div class="input-left"></div>
                        <div class="input-mid" style='width: 210px;'><input type='text' id="event_phone_number" name='event_phone_number' maxlength="20" value='${actionBean.occasion.phoneNumber}' style="width: 210px;" /></div>
                        <div class="input-right"></div>

                        <div class="input-left" style="margin-left: 20px;"></div>
                        <div class="input-mid" style='width: 93px;'><input type='text' id="event_phone_extension" name='event_phone_extension'  maxlength="20" value='${actionBean.occasion.phoneExtension}' style="width: 93px;" /></div>
                        <div class="input-right"></div>
                    </div>

                    <%-- labels --%>
                    <div class="clearfix">
                        <div class="input-label">Email</div>
                    </div>
                    <%-- inputs --%>
                    <div class="clearfix">
                        <div class="input-left"></div>
                        <div class="input-mid" style='width: 334px;'><input type='text' id="event_email" name='event_email' maxlength="255" value='${actionBean.occasion.email}' style="width: 334px;" /></div>
                        <div class="input-right"></div>
                    </div>


                    <%-- labels --%>
                    <div class="clearfix">
                        <div class="input-label">Website</div>
                    </div>
                    <%-- inputs --%>
                    <div class="clearfix">
                        <div class="input-left"></div>
                        <div class="input-mid" style='width: 334px;'><input type='text' id="event_website" name='event_website' maxlength="255" value='${actionBean.occasion.eventUrl}' style="width: 300px;" /></div>
                        <div class="input-right"></div>
                    </div>

                    <%-- labels --%>
                    <div class="clearfix">
                        <div style="padding-top: 10px; padding-bottom: 2px;">
                            <input type="checkbox" id="google_map_checkbox" name="google_map_checkbox" checked="${actionBean.occasion.withGoogleMap}" onchange="toggleMap(this.checked);"> <span style="font-weight: bold;">Add Google Maps to your invitation</span>
                        </div>
                        <div id="event_map_preview" style="width: 343px; height: 207px; border: 1px solid #ccc;"><img src='${bb:libraryResource('/library/images/events/samplemap.png')}' /></div>
                    </div>


                    
                </div>

                <div class="clearfix"></div>
                
                
                
            </div>

            <div id="privacy_selectors" style="margin: 0px 2px 0px 4px; height: 114px;">

                <div id="privacy_edge_left" class="privacy-left-edge-off"></div>
                <div id="privacy_open" class="privacy-box-off" style='border-right: 1px solid #c3e3f0;' onclick="selectPrivacyLevel('OPEN');">
                    <div class='privacy-opt-radio'><img id="privacy_opt_open_radio" src="${bb:libraryResource('/library/images/events/fakeradio_off.png')}" height="12" width="12"></div>
                    <div class='privacy-opt-desc'><strong>Open Event</strong><br />Open to anyone who wishes to come.<br />Members have your consent to forward on to others.</div>
                </div>
                <div id="privacy_invite" class="privacy-box-off" style='border-left: 1px solid #fff; border-right: 1px solid #c3e3f0;' onclick="selectPrivacyLevel('INVITE_ONLY');">
                    <div class='privacy-opt-radio'><img id="privacy_opt_invite_radio" src="${bb:libraryResource('/library/images/events/fakeradio_off.png')}" height="12" width="12"></div>
                    <div class='privacy-opt-desc'><strong>Invite Only</strong><br />Event is visible to all, allowing members to request invitations that would be authorized by you.</div>
                </div>
                <div id="privacy_private" class="privacy-box-off" style='border-left: 1px solid #fff;' onclick="selectPrivacyLevel('PRIVATE');">
                    <div class='privacy-opt-radio'><img id="privacy_opt_private_radio" src="${bb:libraryResource('/library/images/events/fakeradio_off.png')}" height="12" width="12"></div>
                    <div class='privacy-opt-desc'><strong>Private Event</strong><br />Event is visible to only those invited by you.</div>
                </div>
                <div id="privacy_edge_right" class="privacy-right-edge-off"></div>

            </div>

            <div style="clear:both; padding-top: 15px;">
                <div style="float: right; cursor: pointer;"><img src='${bb:libraryResource('/library/images/events/design_btn.png')}' onclick="nextStep();"></div>
            </div>


        </div>
        <input type="hidden" name="privacy_field" id="privacy_field" value="${actionBean.occasion.occasionType}">
        </form>
    
    </div>
 <div id="permission_dialog">
    Sorry, you must be an unlimited member in order to create events.
</div>
</body>
