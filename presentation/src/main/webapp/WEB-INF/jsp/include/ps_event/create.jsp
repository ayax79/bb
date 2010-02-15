<head>
	<title>Event</title>
	<%@include file="/WEB-INF/jsp/include/ps_event/ps_event_header.jspf" %>
	<script type="text/javascript">
		var sessionValue = {image:{guid:null,location:null,name:null,type:null},video:null,name:null,catalog:null};
		var saveSession = function() {
			sessionValue.name = $("#event_name_value").val();
			var nameLength = sessionValue.name.length;
			if (sessionValue.name != null && sessionValue.name != "" && nameLength <= 20) {
				$.post("<s:url beanclass='com.blackbox.presentation.action.psevent.PSAjaxEventActionBean'/>;jsessionid=<%=session.getId()%>",
				{eventName:sessionValue.name,
					catalog:sessionValue.catalog,
					imageGUID:sessionValue.image.guid,
					imageType:sessionValue.image.type,
					vedio:sessionValue.video,
					'_eventName' : 'startEventConfig'},
						function() {
							window.location.href = "<s:url beanclass='com.blackbox.presentation.action.psevent.PSConfigEventActionBean'/>;jsessionid=<%=session.getId()%>"
						})
			} else {
				if (nameLength > 20) {
					$.bbDialog.alert("Event name length cannot be bigger than 20", {
						onclose:function() {
							$("#event_name_value").focus();
						}
					});
				} else {
					$.bbDialog.alert("Event Name is must", {
						onclose:function() {
							$("#event_name_value").focus();
						}
					});
				}

			}
		};
		$(document).ready(function() {
		<%--initScetionForDialog();--%>
			$("#event_navigation").tabs({selected:0,select:function(e, ui) {
				var tab_header = $(ui.tab).parent();
				$(".inv_pre").each(function() {
					$(this).addClass("inv_pre1");
				})
				$(tab_header).removeClass("inv_pre1");
			}});
			focusSelected("image");
			
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
		})

		var loadEventImageTab = function(fn) {
			$("#event_image").load('<s:url beanclass="com.blackbox.presentation.action.psevent.PSAjaxEventActionBean" />;jsessionid=<%=session.getId()%>', {'_eventName' : 'images'}, function() {
				focusSelected("image");
				if (fn != null) {
					fn.call(this);
				}
			})
		}
		var focusSelected = function(type) {
			var render = function(e) {
				$(".event_media").removeClass("event_activity")
				var target = $(e).children(".event_media")
				target.addClass("event_activity")
				var alert_text = target[0].alt
				$("#event_theme_alert").text(alert_text)
			}
			if (type == "image") {
				$($(".event_media").parent()).each(function() {
					if (sessionValue.image.guid != null && sessionValue.image.guid == $(this).children(".event_media")[0].id) {
						render(this);
					}
				});
				$($(".event_media").parent()).click(function() {
					sessionValue.image.guid = $(this).children(".event_media")[0].id;
					sessionValue.image.location = $(this).children(".event_media")[0].src;
					sessionValue.image.name = $(this).children(".event_media")[0].alt;
					sessionValue.image.type = $(this).attr("class");
					render(this);
				})
			}
		}
	</script>
	<style type="text/css">
		.event_activity {
			border: 5px solid rgb(2, 191, 246) !important;
		}
	</style>
</head>
<c:set value='yes' var='noStream' scope='request'/>
<body>
<div style="display:none;" id="events-message">
	<p>Events are nearly done. Feel free to play and give us feedback.<br/>
	We'll let you know when it's time to use the feature to throw your bashes.</p>
	<p>But please feel free to play for now. :)</p>
</div>
<div class="container container-top"></div>
<div class="container darken">
	<div class="clea_tit">Create an event</div>
	<div class="clea_eve">
		<div class="clea_eve_lef"></div>
		<div class="clea_eve_mid">
			<div class="eve_inp_tit">Name your event</div>
			<div class="eve_inp_con">
				<div class="edit_inp_lef"></div>
				<div class="edit_inp_mid1"><input id="event_name_value" type="text" name="textfield" value="${occasion.name}"/></div>
				<div class="edit_inp_rig"></div>
				<div class="clear"></div>
			</div>
		</div>
		<div class="clea_eve_rig"></div>
		<div class="clear"></div>
	</div>
	<div class="hight_12"></div>
	<div class="cre_con">
		<div class="inv_con">
			<div class="inv_conlef"></div>
			<div class="inv_conmid">Invitation Design</div>
			<div class="inv_conrig"></div>
			<div class="clear"></div>
		</div>
		<div id="event_navigation" class="inv_men">
			<ul style="margin: 0pt; padding: 0pt; position: relative; list-style-type: none;">
				<li class="inv_pre"><a href="#event_premade">Premade Invitations<br/>
					<span class="pre_men_tex">All of these are free to use</span>
				</a>
				</li>
				<%--<li class="inv_pre inv_pre1"><a href="#event_image" onclick="javascript:loadEventImageTab();">Your images<br />
											<span class="pre_men_tex1">Select or upload your own images</span>
										</a>
									</li>
									<li class="inv_pre inv_pre1"><a href="#event_video">Video<br />
											<span class="pre_men_tex1">Spice things up and add video.</span>
										</a>
									</li>--%>
			</ul>

			<div class="clear"></div>
		</div>
		<div id="event_premade">
			<%@include file="/WEB-INF/jsp/include/ps_event/event_premade.jspf"%>
		</div>

		<%--<div id="event_image">
						<div id="event_image_main" class="upl_con">
							<div class="vid_rec"><a href="javascript:create_dailog_for_upload();"><img src="${bb:libraryResource('/library/images/psevent/cre_upl.jpg')}"/></a></div>
							<div class="ima_fro">or add images from:</div>
							<div class="ima_ico">
								<a id="event_flickr" href="javascript:create_dailog_for_link('Upload an image','dd','image');"><img src="${bb:libraryResource('/library/images/psevent/ima_icon_01.jpg')}"/></a>
								<a href="javascript:create_dailog_for_link('Upload an image','dd','image');"><img src="${bb:libraryResource('/library/images/psevent/ima_icon_03.jpg')}"/></a>
							</div>
						</div>
						<div id="" class="inv_main">
						</div>
					</div>--%>

		<%--<div id="event_video">
						<%@include file="/WEB-INF/jsp/include/ps_event/event_video.jspf"%>
					</div>--%>
		<div class="inv_bot">
			<div class="inv_bot_lef"></div>
			<div class="inv_bot_mid"><%--Current selection:&nbsp; <span id="event_theme_alert">Red Glass Event</span>--%></div>
			<div class="inv_bot_rig"></div>
			<div class="clear"></div>
		</div>
		<div class="eve_con1">
			<div class="eve_con1_top"></div>
			<div class="eve_con1_mid">
				<div class="eve_con1_mid_lef">
					<div class="eve_con1_tex_tit">Build it and they will come</div>
					<div class="eve_con1_tex_bod">&nbsp;&nbsp;&nbsp;&nbsp;</div>
				</div>
				<div class="eve_con1_mid_rig">
					<div style="padding-top:20px;">
						<div class="but_gre8">

							<div class="but_gre8_rig">
								<a id="config for layout" class="nav_btn" href="javascript:saveSession(this);" style="display:block;">
									Next
								</a>
							</div>
						</div>
					</div>
				</div>
				<div class="clear"></div>
			</div>
			<div class="eve_con1_bot"></div>
		</div>
	</div>
</div>
<div class="container container-bottom"></div>

</body>


