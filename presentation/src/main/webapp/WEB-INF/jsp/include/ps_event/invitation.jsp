<%@include file="/WEB-INF/jsp/include/page_header.jspf" %>
<%--@elvariable id="actionBean" type="com.blackbox.presentation.action.psevent.PSShowEventActionBean"--%>
<c:set var="newLine" value="
"/>
<c:set var="occasion" value="${actionBean.occasion}"/>
<c:set var="layout" value="${occasion.layout}"/>
<c:set var="address" value="${occasion.address}"/>
<c:set var="attendees" value="${occasion.attendees}"/>
<c:set var="attending" value="${actionBean.attending}"/>
<c:set var="maybe" value="${actionBean.maybeAttending}"/>
<c:set var="canot" value="${actionBean.canot}"/>

<c:set var="attendingUser" value="${actionBean.attendingUser}"/>
<c:set var="maybeUser" value="${actionBean.maybeAttendingUser}"/>
<c:set var="canotUser" value="${actionBean.canotUser}"/>


<c:choose>
	<c:when test="${layout.textAlign == 'event_text_align_left'}">
		<c:set var="text_align" value="left"/>
	</c:when>
	<c:when test="${layout.textAlign == 'event_text_align_right'}">
		<c:set var="text_align" value="right"/>
	</c:when>
	<c:when test="${layout.textAlign == 'event_text_align_center'}">
		<c:set var="text_align" value="center"/>
	</c:when>
</c:choose>

<head>
	<%@include file="/WEB-INF/jsp/include/ps_event/ps_event_header.jspf" %>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
	<title>Blackbox Republic Event | ${occasion.name} - ${address.city}, ${address.state} - ${bb:formatDate(occasion.eventTime, "M/dd/yyyy")}</title>
	<link href="${bb:libraryResource('/library/css/psevent.css')}" rel="stylesheet" type="text/css"/>
	<link href="${bb:libraryResource('/library/css/my_n.css')}" rel="stylesheet" type="text/css"/>
	<link href="${bb:libraryResource('/library/css/ps_dialog.css')}" rel="stylesheet" type="text/css"/>
	<link rel="stylesheet" href="${bb:libraryResource('/library/css/jquery.lightbox-0.5.css')}" type="text/css"/>

	<script type="text/javascript" src="${bb:libraryResource('/library/js/jquery.Jcrop.min.js')}"></script>
	<script type="text/javascript" src='${bb:libraryResource('/library/js/colorpicker.js')}'></script>
	<script type="text/javascript" src="${bb:libraryResource('/library/js/psevent.js')}"></script>
	<script src="${bb:libraryResource('/library/js/publisher.js')}" type="text/javascript"></script>
	<script src="${bb:libraryResource('/library/js/swfobject.js')}" type="text/javascript"></script>
	<link rel="stylesheet" href="${bb:libraryResource('/library/css/jquery.Jcrop.css')}" type="text/css"/>
	<link rel="stylesheet" href='${bb:libraryResource('/library/css/jquery.colorpicker/css/colorpicker.css')}' type="text/css"/>
	<script src="http://maps.google.com/maps?file=api&amp;v=2&amp;sensor=false&amp;key=ABQIAAAAp9yvzzCMVz8SKrPHBMtKBhRoR84c2uF6PsISXXf8g1VBgaojsRSd3xfJ2bYFyKbTkZ4c9b7tStQrFw" type="text/javascript"></script>


	<script type="text/javascript">

		$(document).ready(function() {
            function bindSliders(scope) {

            if (scope && scope.is(".private")) {

                scope.css({"position" : "relative"});
                scope.append("<div class='private-icon'></div>");

            }
            $.each($(".slider_container", scope), function() {
                var $slider_container = $(this);
                var min = $(this).find(".slider-val-min");
                var max = $(this).find(".slider-val-max");
                var nom = $(this).find(".slider-val");
                var range = null;
                var values = null;
                var change = null;
                if (min.length && max.length) {
                    range = true;
                    min.val((min.val() != "" && min.val() !== undefined) ? min.val() : -2);
                    max.val((max.val() != "" && min.val() !== undefined) ? max.val() : 2);
                    values = [min.val(), max.val()];
                    change = function(event, ui) {
                        min.val(ui.values[0]);
                        max.val(ui.values[1]);
                    };
                } else if (nom.length) {
                    range = false;
                    nom.val((nom.val()) ? nom.val() : 0);
                    values = [nom.val()];

                    change = function(event, ui) {
                        nom.val(ui.value);
                    };
                }
                $slider_container.find(".bbslider").slider({
                    min:-2,
                    max:2,
                    range:range,
                    values:values,
                    change: change
                });
                $slider_container.find(".bbslider-disable-slider").click(function() {
                    $slider_container.find(".bbslider").slider((($(this).is(":checked")) ? 'enable' : 'disable'));
                    var isVisible = ($(this).is(":checked")) ? true : false;
                    if (isVisible) {
                        $slider_container.find(".ui-slider-range").show();
                        $slider_container.find(".ui-slider-handle").show();
                        if (min.length && max.length) {
                            min.val($slider_container.find(".bbslider").slider('values')[0]);
                            max.val($slider_container.find(".bbslider").slider('values')[1]);
                        } else if (nom.length) {
                            nom.val($slider_container.find(".bbslider").slider('value'));
                        }
                    } else {
                        $slider_container.find(".ui-slider-range").hide();
                        $slider_container.find(".ui-slider-handle").hide();
                        if (min.length && max.length) {
                            min.val("");
                            max.val("");
                        } else if (nom.length) {
                            nom.val("");
                        }
                    }
                });
            });
        }

        bindSliders();
            
			if ("false" == "${occasion.withGoogleMap}") {

				$("#event_layout_right").hide();
				$("#event_layout_left").css('width', '100%');
			} else {
				create_gmap();
			}

			$("#guest_tabs").tabs();

		});

		var create_gmap = function() {

			try {
				var map = new GMap2(document.getElementById("map_preview"));
				var geocoder = new GClientGeocoder();
				var address = "${address.address1},${address.city},${address.state},USA";
				geocoder.getLatLng(
						address,
						function(point) {
							if (point) {
								map.setCenter(point, 13);
								var marker = new GMarker(point);
								map.addOverlay(marker);
							}
						}
						);
			} catch(e) {
				alert(e);
			}
			;

		}

		var finalSave = function() {

			$.post("<s:url beanclass='com.blackbox.presentation.action.psevent.PSAjaxEventActionBean'/>;jsessionid=<%=session.getId()%>", { "_eventName": "finalSave" },
					function(data) {
						if ("success" == data) {
							window.location.href = "<s:url beanclass='com.blackbox.presentation.action.psevent.PSShowEventActionBean'><s:param name="eventParam" value="${occasion.guid}" /></s:url>";
						} else {
							//alert(data);
						}
					});
		};

		var setRSVP = function (evtguid, userguid, attendingstatus) {
			//alert('setRSVP(' + evtguid + ', ' + userguid + ', ' + attendingstatus + ')');
			// send the RSVP
			$.post('<s:url beanclass="com.blackbox.presentation.action.occasion.RsvpActionBean" />', {'_eventName': 'attend', 'guid': evtguid, 'status' : attendingstatus },
					function(data) {
						window.location.reload();
					});

		}

		var populateRSVP = function (attendingstatus) {

		}


		var delete_event = function(guid) {
            $.bbDialog.confirm("Do you want to remove this event?", function() {
				$.get('<s:url beanclass="com.blackbox.presentation.action.psevent.PSEventsActionBean"/>', {"_eventName":"delete","eventGuid":guid}, function(r) {
                    document.location = bb.urls.user.home;                    
				}


            )}, function() {
				this.close();
			})
		}

	</script>
</head>
<body>
<div class="container container-top"></div>
<div class="container darken">

<form name="create_event_form" autocomplete="off" onsubmit="return false;">
<div class="rounded-box black40 persona-header">
	<div class="rb-header">
		<div class="rb-header-r">&nbsp;</div>
	</div>
	<div class="rb-content">
		<div class="rb-content-r">

			<div style="float:left;">
				<div id="show_id">
					<div class="con_onl_lef">
						${occasion.name}
					</div>
					<div class="clear"></div>

					<div class="con_onl_bot">


						<c:if test="${occasion.occasionType == 'OPEN'}">
							a <span class="blue-text">public</span>
						</c:if>
						<c:if test="${occasion.occasionType == 'INVITE_ONLY'}">
							an <span class="blue-text">invitation only</span>
						</c:if>
						<c:if test="${occasion.occasionType == 'PRIVATE'}">
							a <span class="blue-text">private</span>
						</c:if>

						event by

                            <span class="blue-text">
                                <c:if test="${not empty occasion.owner.name}">${occasion.owner.name}</c:if>
                            </span>

					</div>
				</div>
			</div>
			<div style="float:right;">
				<c:set var="showRsvpOpts" value="false"/>
				<c:if test="${occasion.occasionType == 'OPEN' && actionBean.currentUser.guid != null}">
					<c:set var="showRsvpOpts" value="true"/>
				</c:if>
				<c:if test="${actionBean.viewerOwner}">
					<c:set var="showRsvpOpts" value="false"/>
				</c:if>

				<c:if test="${showRsvpOpts}">

					<div class="mem_men_bot">
						<ul>
							<li id="rsvp_cant" status="NOT_ATTENDING" class="mem_men_bot_bg2 rsvp_btn"><a href="javascript:setRSVP('${occasion.guid}', '${actionBean.currentUser.guid}', 'NOT_ATTENDING');">Can't</a></li>
							<li id="rsvp_tenative" status="TENATIVE" class="mem_men_bot_bg1 rsvp_btn"><a href="javascript:setRSVP('${occasion.guid}', '${actionBean.currentUser.guid}', 'TENATIVE');">Maybe</a></li>
							<li id="rsvp_attending" status="ATTENDING" class="mem_men_bot_bg1 rsvp_btn"><a href="javascript:setRSVP('${occasion.guid}', '${actionBean.currentUser.guid}', 'ATTENDING');">Yes</a></li>
							<li style="font-size:14px; font-weight:bold; color:#fff;margin-top:12px;">RSVP:</li>
						</ul>
					</div>
				</c:if>
			</div>


			<div class="clear"></div>

		</div>
	</div>
	<div class="rb-footer">
		<div class="rb-footer-r">&nbsp;</div>
	</div>
</div>


<div id='event_body' class="event-body clearfix">

<c:if test="${actionBean.unsaved}">
<div id="event-workflow5"></div>
</c:if>


<div id="invite_right">

<c:if test="${actionBean.unsaved}">
	<div id="preview_promote_header">

		<div class="evtborder evtpadding">
				<%-- <span class='preview-headline'>Guest List</span><span id='add_more_span'><a href="#">add more</a></span> --%>
		</div>
		<div class="evtborder">
			<div class="evtpadding">
				<table cellpadding="0" cellspacing="0" border="0" width="100%">
					<tr>
						<td align="center" width="50%">
							<span class="blue-counts">${f:length(attendees)}</span>
							<br/>
							<span style="font-weight: bold;">Blackbox Members</span>
						</td>
						<td class='c-vert-line'></td>
						<td align="center" width="50%">
							<span class="blue-counts">0</span>
							<br/>
							<span style="font-weight: bold;">Other Guests</span>
						</td>
					</tr>
				</table>
			</div>
		</div>

		<div class="preview-promote-rule"></div>

		<div class="evtborder evtpadding">
			<span class='preview-headline'>Share your invitation</span>
		</div>


		<div class="evtborder">
			<div class="evtpadding clearfix">
				<div id="invite_link_glyph"><img src="${bb:libraryResource('/library/images/events/link_glyph.png')}"></div>
				<div class="invite-field-label">Copy URL</div>
				<div style="float:right;">
					<div class="input-left" style='margin-left: 5px;'></div>
					<div class="input-mid" style='width: 150px;'><input type='text' id="share_url" name='share_url'
																		value='${bb:getProperty('presentation.url')}<s:url beanclass="com.blackbox.presentation.action.psevent.PSShowEventActionBean"><s:param name="eventParam" value="${occasion.guid}"/></s:url>' style="width: 150px;"/></div>
					<div class="input-right"></div>
				</div>
			</div>

			<div class="evtpadding clearfix">
				<div id='invite_image_glyph'><img src="${bb:libraryResource('/library/images/events/img_glyph.png')}"></div>
				<div class="invite-field-label">Copy Image</div>
				<div style="float:right;">
					<div class="input-left" style='margin-left: 5px;'></div>
					<div class="input-mid" style='width: 150px;'><input type='text' id="share_img" name='share_img' value='${actionBean.occasion.layout.transiantImage.location}' style="width: 150px;"/></div>
					<div class="input-right"></div>
				</div>
			</div>

		</div>
	</div>
</c:if>

<c:if test="${!actionBean.unsaved}">

<c:if test="${actionBean.currentUser.guid != null}">

	<div id="guest_tabs" class="esub-tab-container">
		<ul class="esub-tab-container-right esub-tabs">
			<li><a href="#frag-1"><span>Attending (${f:length(attending)})</span></a></li>
			<li><a href="#frag-2"><span>Maybe (${f:length(maybe)})</span></a></li>
			<li><a href="#frag-3"><span>Not Attending (${f:length(canot)})</span></a></li>
		</ul>
		<div id="frag-1" class="esub-tabs-frag">
			<c:forEach var="attendee" varStatus="index" items="${attending}">

				<c:set var="user" value="${attendingUser[index.index]}"/>
				<table cellpadding="3" cellspacing="0" border="0">
					<tr>
						<td>
							<ui:profileImage guid="${user.guid}" size="small" color="white" linkToProfile="true" showMiniProfile="true" showUserType="true"/>
						</td>
						<td valign="top">
							<ui:entitylink entity="${user}">${bb:displayName(user)}</ui:entitylink>
							<br>

							<c:if test="${not user.profile.birthdayInVisible and not empty user.profile.birthday}">
								${bb:age(user.profile.birthday)} |
							</c:if>
							<c:choose>
								<c:when test="${user.profile.sex == 'MALE'}">M</c:when>
								<c:when test="${user.profile.sex == 'FEMALE'}">F</c:when>
							</c:choose>
							<c:if test="${not empty user.profile.location.city}">
								&nbsp; ${user.profile.location.city}
								<c:set var="prevToken" value="true"/>
							</c:if>
							<c:if test="${not empty user.profile.location.state}">
								<c:if test="${prevToken}">, </c:if>
								${bb:stateAbv(user.profile.location.state)}
							</c:if>
						</td>

					</tr>
				</table>

				<hr class="clearfix">
				<c:if test="${attendee.bbxUserGuid == actionBean.currentUser.guid}">
					<script type="text/javascript">
						populateRSVP("ATTENDING");
					</script>
				</c:if>

			</c:forEach>
		</div>
		<div id="frag-2" class="esub-tabs-frag">
			<c:forEach var="attendee" varStatus="index" items="${maybe}">

				<c:set var="user" value="${maybeUser[index.index]}"/>
				<table cellpadding="3" cellspacing="0" border="0">
					<tr>
						<td>
							<ui:profileImage guid="${user.guid}" size="small" color="white" linkToProfile="true" showMiniProfile="true" showUserType="true"/>
						</td>
						<td valign="top">
							<ui:entitylink entity="${user}">${bb:displayName(user)}</ui:entitylink>
							<br>

							<c:if test="${not user.profile.birthdayInVisible and not empty user.profile.birthday}">
								${bb:age(user.profile.birthday)} |
							</c:if>
							<c:choose>
								<c:when test="${user.profile.sex == 'MALE'}">M</c:when>
								<c:when test="${user.profile.sex == 'FEMALE'}">M</c:when>
							</c:choose>
							<c:if test="${not empty user.profile.location.city}">
								&nbsp; ${user.profile.location.city}
								<c:set var="prevToken" value="true"/>
							</c:if>
							<c:if test="${not empty user.profile.location.state}">
								<c:if test="${prevToken}">, </c:if>
								${bb:stateAbv(user.profile.location.state)}
							</c:if>
						</td>

					</tr>
				</table>

				<hr class="clearfix">
				<c:if test="${attendee.bbxUserGuid == actionBean.currentUser.guid}">
					<script type="text/javascript">
						populateRSVP("TENATIVE");
					</script>
				</c:if>

			</c:forEach>
		</div>
		<div id="frag-3" class="esub-tabs-frag">
			<c:forEach var="attendee" varStatus="index" items="${canot}">

				<c:set var="user" value="${canotUser[index.index]}"/>
				<table cellpadding="3" cellspacing="0" border="0">
					<tr>
						<td>
							<ui:profileImage guid="${user.guid}" size="small" color="white" linkToProfile="true" showMiniProfile="true" showUserType="true"/>
						</td>
						<td valign="top">
							<ui:entitylink entity="${user}">${bb:displayName(user)}</ui:entitylink>
							<br>

							<c:if test="${not user.profile.birthdayInVisible and not empty user.profile.birthday}">
								${bb:age(user.profile.birthday)} |
							</c:if>
							<c:choose>
								<c:when test="${user.profile.sex == 'MALE'}">M</c:when>
								<c:when test="${user.profile.sex == 'FEMALE'}">M</c:when>
							</c:choose>
							<c:if test="${not empty user.profile.location.city}">
								&nbsp; ${user.profile.location.city}
								<c:set var="prevToken" value="true"/>
							</c:if>
							<c:if test="${not empty user.profile.location.state}">
								<c:if test="${prevToken}">, </c:if>
								${bb:stateAbv(user.profile.location.state)}
							</c:if>
						</td>

					</tr>
				</table>

				<hr class="clearfix">
				<c:if test="${attendee.bbxUserGuid == actionBean.currentUser.guid}">
					<script type="text/javascript">
						populateRSVP("NOT_ATTENDING");
					</script>
				</c:if>

			</c:forEach>
		</div>
	</div>
</c:if>

<c:if test="${actionBean.currentUser.guid == null}">

	<div id="right-col">
		<div class="top">
			<h2>Not yet a member?</h2>

			<p>Sign up for Blackbox Republic and get in on the action.<br/>
				<span>Get started by moving the sliders below.</span></p>
			<hr/>
		</div>
		<div class="body">

			<img src="${bb:libraryResource('/library/images/events/today-im.png')}" alt="Today I'm"/>
			<!-- begin slider div -->
			<s:form action="empty">
				<div class="info-sliders mini-slider">
					<ul>
						<c:if test="${not empty actionBean.context.validationErrors}">
							<div class="login-errors">
								<ui:roundedBox className="rounded-comment xplr-results-container">
									<s:errors/>
									<s:messages/>
								</ui:roundedBox>
							</div>
						</c:if>

						<!-- Mood sliders -->
						<li class="slider_container clearfix">
							<s:hidden name="user.profile.mood.orientation" class="slider-val"
									  value="0"/>
							<label><span><fmt:message key="settings.field.orientation"/></span></label>
							<ui:slider containerClass="sliderz" labelLeft="Gay" labelCenter="Bi" labelRight="Hetero"
									   showQuarterTicks="true"/>
						</li>
						<li class="slider_container clearfix">
							<s:hidden name="user.profile.mood.relationshipStatus" class="slider-val"
									  value="0"/>
							<label><span><fmt:message key="settings.field.status"/></span></label>
							<ui:slider containerClass="sliderz" labelLeft="Unattached" labelCenter="It's complicated"
									   labelRight="Attached" showQuarterTicks="true"/>
						</li>
						<li class="slider_container clearfix">
							<s:hidden name="user.profile.mood.polyStatus" class="slider-val" value="0"/>
							<label><span><fmt:message key="settings.field.addPartners"/></span></label>
							<ui:slider containerClass="sliderz" labelLeft="Room for more" labelCenter="It fluctuates"
									   labelRight="Not right now" showQuarterTicks="true"/>
						</li>
						<li class="slider_container clearfix">
							<s:hidden name="user.profile.mood.interestLevel" class="slider-val" value="0"/>
							<label><span><fmt:message key="settings.field.lovin"/></span></label>
							<ui:slider containerClass="sliderz" labelLeft="Going slow" labelCenter="If it happens, it happens"
									   labelRight="Gotta have it" showQuarterTicks="true"/>
						</li>
						<li class="slider_container clearfix">
							<s:hidden name="user.profile.mood.energyLevel" class="slider-val" value="0"/>
							<label><span><fmt:message key="settings.field.vibe"/></span></label>
							<ui:slider containerClass="sliderz" labelLeft="Party mode" labelCenter="Let's hang" labelRight="Lying low"
									   showQuarterTicks="true"/>
						</li>
					</ul>
				</div>
			</s:form>

			<!-- end slider div -->

			<img src="${bb:libraryResource('/library/images/events/join-people.jpg')}" alt="Join the Republic"/>

		</div>
		<div class="bottom">
		</div>
	</div>


</c:if>
</c:if>

<div style="display: block;">

	<c:if test="${actionBean.viewerOwner && !actionBean.unsaved}">
		<a href="<s:url beanclass='com.blackbox.presentation.action.psevent.PSConfigEventActionBean'/>;jsessionid=<%=session.getId()%>?_eventName=edit&guid=${occasion.guid}">Edit this event</a>
		<br/>
		<a href="javascript:delete_event('${occasion.guid}');">Delete this event</a><br>
	</c:if>
	<c:if test="${actionBean.unsaved}">
		<div id="final_save_btn"><img src="${bb:libraryResource('/library/images/events/design_btn_final.png')}" onclick="finalSave();"></div>
	</c:if>
    
</div>

</div>


<div id="event_preview_container" style="background-color: #${layout.backgroundColor};">
	<div id='photo_background'>
		<div id="image_zone" class="event_media_pic">
            <c:choose>
                <c:when test="${not empty layout.transiantImage.location}">
                    <img id="big_event_image" src="${layout.transiantImage.location}" width="589" onload="slideBtn();" />
                </c:when>
                <c:otherwise>
                    <img id="big_event_image" src="${bb:libraryResource('/library/images/events/event_default_photo.png')}" width="589" onload="slideBtn();" />
                </c:otherwise>
            </c:choose>
        </div>
	</div>
	<div id="photo_shadow"></div>
	<div id="event_layout_box" style="color: #${layout.textColor}; background-color: #${layout.boxcolor}; text-align: ${text_align};">
		<div id="event_layout_left">
			<div id='event_layout_title'>${occasion.name}</div>
			<div id="event_layout_body">
				<div id='event_layout_sep' style="border-bottom: 1px solid #${layout.textColor}">
				    ${f:replace(occasion.description, newLine, "<br/>")}
                </div>

				<table cellpadding="0" cellspacing="10" border="0" width="100%">
					<tr>
						<td align="right" valign='top'>
							<span class="event_layout_label">When</span>
						</td>
						<td valign='top' width="100%">
							${bb:formatDate(occasion.eventTime, "M/dd/yyyy")}
							<br/>
							${bb:formatDate(occasion.eventTime, "h:mm a")} <%-- to ${bb:formatDate(actionBean.occasion.eventEndTime, "H:mm a")} --%>
						</td>
					</tr>
					<tr>
						<td align="right" valign="top">
							<span class="event_layout_label">Where</span>
						</td>
						<td>
							${address.address1}
							<br/>
							${address.address2}
							<br/>
							${address.city}, ${address.state} &nbsp; ${address.zipCode}
						</td>
					</tr>
					<tr>
						<td align="right" valign="top">
							<span class="event_layout_label">Host</span>
						</td>
						<td valign="top">
							${occasion.hostBy}
							<c:if test="${not empty occasion.phoneNumber}">
								<br/>
								${occasion.phoneNumber}
							</c:if>
							<c:if test="${not empty occasion.email}">
								<br/>
								<a style='color:#${layout.textColor}' href='mailto:${occasion.email}'>${actionBean.occasion.email}</a>
							</c:if>
							<c:if test="${not empty occasion.eventUrl}">
								<br/>
								<a style='color:#${layout.textColor}' href="${occasion.eventUrl}"> ${occasion.eventUrl}</a>
							</c:if>
						</td>
					</tr>
				</table>
			</div>
		</div>
		<div id="event_layout_right">
			<div id="map_preview" style="border: 5px solid #${layout.textColor}">
			</div>
		</div>
	</div>
	<div class="clearfix"></div>
</div>
</form>
</div>

<script type="text/javascript">

	$(function() {
		var eventStream = new Activity.Stream("#event_target");
		eventStream.addEventTypes([
			Activity.Event.MESSAGE_EVENT,
			Activity.Event.MEDIA_EVENT,
			Activity.Event.OCCASION_EVENT
		], "event");
		eventStream.activate();
		bb.widgets.createMiniPublisher();
		bb.pageInit.bindArticleEvents($("#event_container"));
		bb.pageInit.bindDefaultTextBoxes($("#event_container"));
		bb.widgets.components.miniPublisher = new Publisher("#miniPublisher", "invitation");
	});

</script>


<div style="background-color:#fff; padding:10px;margin:10px;">

	<c:choose>

	<%-------------------------%>
	<%--| USER IS LOGGED IN |--%>
	<%-------------------------%>
	<c:when test="${not empty actionBean.currentUser}">
		<%@include file="/WEB-INF/jsp/include/stream-templates.jspf" %>
		<c:set var="hackfuckingtastic" scope="request" value="hellsyeah"/>
	<div id="event_container" class="stream-container">
		<div id="event_target" class="stream-content articles">
			<div class="article  article_${actionBean.occasion.guid}">
				<div class="article-comments">
					<div class="comments-container comments_${activity.parent.guid}">
						<c:forEach var="childActivity" items="${actionBean.comments}">
							<c:set var="childActivity" scope="request" value="${childActivity}"/>
							<c:choose>
								<c:when test="${childActivity.activityType == 'MESSAGE'}">
									<%@include file="/WEB-INF/jsp/include/templates/message-comment.jspf" %>
								</c:when>
								<c:when test="${childActivity.activityType == 'MEDIA'}">
									<%@include file="/WEB-INF/jsp/include/templates/media-comment.jspf" %>
								</c:when>
							</c:choose>
						</c:forEach>
					</div>
					<%@include file="/WEB-INF/jsp/include/templates/add-comment.jspf" %>
				</div>
			</div>
		</div>
	</div>
		<ui:publisher id="miniPublisher" className="mini" pubConfig="invitation" parentGuid="${actionBean.occasion.guid}"/>
	</c:when>

	<%-----------------------------%>
	<%--| USER IS NOT LOGGED IN |--%>
	<%-----------------------------%>
	<c:otherwise>
	<div id="event_container" class="stream-container">
		<div id="event_target" class="stream-content articles">
			<div class="article  article_${actionBean.occasion.guid}">
				<div class="article-comments">
					<div class="comments-container comments_${activity.parent.guid}">
						<c:forEach var="childActivity" items="${actionBean.comments}">
							<c:set var="childActivity" scope="request" value="${childActivity}"/>
							<div class="article-comment">
								<div class="article-comment-pict">
									<div class="profile-image-container s_small">
										<img src='<c:out value="${childActivity.senderProfile.senderAvatarImage}" default="${bb:libraryResource('/library/images/icons/profile_person.png')}"/>' class="profile-image"/>
									</div>
								</div>
								<div class="article-comment-body">
									<div class="article-comment-header">
										<div class="article-comment-meta">
									<span class="article-comment-user">
										${childActivity.senderProfile.senderDisplayName}
									</span>
											<span class="collapsed-message">posted a reply</span>
											<span class="article-comment-date"><abbr class="timeago" title="${childActivity.formattedPostDate}">${childActivity.formattedPostDate}</abbr></span>
										</div>
										<div class="clear"></div>
									</div>
									<div class="article-comment-meat">
										<div class="comment-meat">
											<c:choose>
											<c:when test="${childActivity.activityType == 'MESSAGE'}">
												${f:replace(childActivity.body, newLine, "<br/>")}
											</c:when>
											<c:when test="${childActivity.activityType == 'MEDIA'}">
												${f:replace(childActivity.comment, newLine, "<br/>")}
											<div class="article-media">
												<c:if test="${not empty childActivity.location}">
													<c:choose>
														<c:when test="${childActivity.mimeType == 'application/octet-stream'}">
															VIDEO NOT SUPPORTED YET
														</c:when>
														<c:otherwise>
															<ui:streamImage msgObject="${childActivity}"/>
														</c:otherwise>
													</c:choose>
												</c:if>
												</c:when>
												</c:choose>
											</div>
										</div>
									</div>
								</div>
							</div>
						</c:forEach>
					</div>
				</div>
			</div>
		</div>
		</c:otherwise>
		</c:choose>

	</div>


</body>
