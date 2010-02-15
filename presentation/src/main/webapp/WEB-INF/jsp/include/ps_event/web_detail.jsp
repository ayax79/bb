<%@include file="/WEB-INF/jsp/include/page_header.jspf" %>
<head>
    <%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
    <%@include file="/WEB-INF/jsp/include/ps_event/ps_event_header.jspf" %>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <title>Event</title>
    <link rel="stylesheet" href="${bb:libraryResource('/library/css/my_n.css')}" type="text/css" media="screen, projection" />
    <link href="${bb:libraryResource('/library/css/psevent.css')}" rel="stylesheet" type="text/css" />
	<script type="text/javascript" src="http://static.ak.connect.facebook.com/js/api_lib/v0.4/FeatureLoader.js.php"></script>
    <script type="text/javascript">
	//<![CDATA[
		$(function() {

			$("#promote_form").bind("submit", function() {
				if($("#publish_to_facebook_checkbox").is(":checked")) {
					if($("#facebookCategory option:selected").is(":disabled")) {
						$.bbDialog.alert("Please select a category");
						return false;
					}
					if($("#facebookSubCategory option:selected").is(":disabled")) {
						$.bbDialog.alert("Please select a subcategory");
						return false;
					}
				}
				return true;
			});

			$("#publish_to_twitter_checkbox").click(function() {
				$("#twitter_creds").toggle();
				if($(this).is(":checked")) {
					$.getJSON("/community/media/Publish.action?twitterCreds=", function(data) {
						$("#twitterUsername").val(data.username);
						$("#twitterPassword").val(data.password);
					});
				}
			});

			$("#publish_to_facebook_checkbox").click(function() {
				$("#facebook_creds").toggle();
				if($(this).is(":checked")) {
					try{
						bb.facebook.callInit(function() {
							bb.facebook.updateFBConnect();
							$("#facebook_event_fields").show();
							$("#guest_list_opts_alpha").find("*[name=facebookSessionId]").val(FB.Facebook.apiClient.get_session().session_key);
						});
					} catch(e) {
						//
					}
				} else {
					$("#facebook_event_fields").hide();
				}
			});
		});
	//]]>
    </script>
</head>

<body>
    <div class="container container-top"></div>
    <div class="container darken">

        <form id="promote_form" name="promote_form" action="<s:url event='promote' beanclass='com.blackbox.presentation.action.psevent.PSConfigEventActionBean'/>" autocomplete="off">
            <input type="hidden" name="_eventName" value="promote">
			<h1 class="white event">Promote Your Event</h1>

        <div class="widget-top" style='margin-left: 11px; margin-right: 11px;'><div class="widget-top-right"></div></div>
        <div id='event_body' class="event-body clearfix">
            <div id="event-workflow4">

            </div>


            <div style="padding: 20px 50px 20px 50px; line-height: 18px;">
                <span style="color: #55b4ed; font-family: cambria,palatino,georgia,times new roman,serif; font-size: 19px; font-style: italic;">Spread the love.</span><br>
                Now that you've created your event, we make it easy to share the news with all of your friends, followers, and cyberspace
                cronies.  Simply fill out the fields below and your event will be posted to each of the respective networks after you finish
                the next step.  You also have the option of copying the link to your public event to post on your webpage or blog.
            </div>

            <div id="guest_list_opts" class="clearfix">
                    <%--<div id="guest_list_opts_left"></div>--%>
                    <div id="guest_list_opts_alpha" class="clearfix">
                        <div class="promote_field_icons"><img src="${bb:libraryResource('/library/images/events/fb_icon.jpg')}"></div>
                        <div class='promote_field_container'>

                            <input type="checkbox" name="publishToFacebook" id="publish_to_facebook_checkbox" disabled="disabled"> <label for="publish_to_facebook_checkbox" style="font-weight: bold;">Post to your wall.</label><br />
                            <textarea name="facebook_text" id="facebook_text" disabled="disabled">Coming soon!${actionBean.occasion.description}</textarea><br/>

							<div id="facebook_event_fields">
							<select name="facebookCategory" id="facebookCategory">
								<option disabled="disabled">Select a category</option>
								<option value="1">Party</option>
								<option value="2">Causes</option>
								<option value="3">Education</option>
								<option value="4">Meetings</option>
								<option value="5">Music/Arts</option>
								<option value="6">Sports</option>
								<option value="7">Trips</option>
								<option value="8">Other</option>
							</select><br/>

							<select name="facebookSubCategory" id="facebookSubCategory">
								<option disabled="disabled">Select a subcategory</option>
								<option value="1">Birthday Party</option>
								<option value="2">Cocktail Party</option>
								<option value="3">Club Party</option>
								<option value="4">Concert</option>
								<option value="5">Fraternity/Sorority Party</option>
								<option value="6">Business Meeting</option>
								<option value="7">Barbecue</option>
								<option value="8">Card Night</option>
								<option value="9">Dinner Party</option>
								<option value="10">Holiday Party</option>
								<option value="11">Night of Mayhem</option>
								<option value="12">Movie/TV Night</option>
								<option value="13">Drinking Games</option>
								<option value="14">Bar Night</option>
								<option value="15">LAN Party</option>
								<option value="16">Study Group</option>
								<option value="17">Mixer</option>
								<option value="18">Slumber Party</option>
								<option value="19">Erotic Party</option>
								<option value="20">Benefit</option>
								<option value="21">Goodbye Party</option>
								<option value="22">House Party</option>
								<option value="23">Reunion</option>
								<option value="24">Fundraiser</option>
								<option value="25">Protest</option>
								<option value="26">Rally</option>
								<option value="27">Class</option>
								<option value="28">Lecture</option>
								<option value="29">Office Hours</option>
								<option value="30">Workshop</option>
								<option value="31">Club/Group Meeting</option>
								<option value="32">Convention</option>
								<option value="33">Dorm/House Meeting</option>
								<option value="34">Informational Meeting</option>
								<option value="35">Audition</option>
								<option value="36">Exhibit</option>
								<option value="37">Jam Session</option>
								<option value="38">Listening Party</option>
								<option value="39">Opening</option>
								<option value="40">Performance</option>
								<option value="41">Preview</option>
								<option value="42">Recital</option>
								<option value="43">Rehearsal</option>
								<option value="44">Pep Rally</option>
								<option value="45">Pick-Up</option>
								<option value="46">Sporting Event</option>
								<option value="47">Sports Practice</option>
								<option value="48">Tournament</option>
								<option value="49">Camping Trip</option>
								<option value="50">Daytrip</option>
								<option value="51">Group Trip</option>
								<option value="52">Roadtrip</option>
								<option value="53">Carnival</option>
								<option value="54">Ceremony</option>
								<option value="55">Festival</option>
								<option value="56">Flea Market</option>
								<option value="57">Retail</option>
								<option value="58">Wedding</option>
							</select>
							</div>
							<div id="facebook_creds">
								<div id="facebook_connect_status_container" class="clearfix">
									<fb:login-button v="2" size="medium">Connect with Facebook</fb:login-button>
								</div>
								<input type="hidden" name="facebookSessionId" value=""/>
							</div>

                        </div>
                    </div>
                    <div id="guest_list_opts_bravo" class="clearfix">
                        <div class="promote_field_icons"><img src="${bb:libraryResource('/library/images/events/twitter_icon.jpg')}"></div>
                        <div class='promote_field_container'>
                            <input type="checkbox" name="publishToTwitter" id="publish_to_twitter_checkbox" <c:if test="${actionBean.occasion.publishToTwitter == 'TRUE'}"> checked='checked'</c:if>> <label for="publish_to_twitter_checkbox" style="font-weight: bold;">Tweet the deets.</label>
                            <br />
                            <textarea name="twitterDescription" id="twitter_text">${fn:substring(actionBean.occasion.description, 0, 120)}</textarea>
                            <div id="twitter_creds" style="display: none; font-size: 11px;">
                                Username: <input type="text" name="twitterUsername" id="twitterUsername" size="6">
                                Password: <input type="password" name="twitterPassword" id="twitterPassword" size="6">
                            </div>
                        </div>
                    </div>
                    <div id="guest_list_opts_charlie" class="clearfix">
                        <div><img src="${bb:libraryResource('/library/images/events/BBR_logo.png')}"> &nbsp; <span style="font-weight: bold;">Get rid of the middle man.</span></div>
                        <div>
                            <div class="clearfix" style='padding-top: 5px;'>
                                <div id="invite_link_glyph"><img src="${bb:libraryResource('/library/images/events/link_glyph.png')}"></div>
                                <div class="invite-field-label">Copy URL</div>
                                <div style="float:right;">
                                    <div class="input-left" style='margin-left: 5px;'></div>
                                    <div class="input-mid" style='width: 170px;'>
										<input type='text' id="share_url" name='share_url' value='${bb:getProperty('presentation.url')}<s:url beanclass="com.blackbox.presentation.action.psevent.PSShowEventActionBean"><s:param name="eventParam" value="${actionBean.occasion.guid}"/></s:url>' style="width: 150px;"/></div>
                                    <div class="input-right"></div>
                                </div>
                            </div>

                            <div class="clearfix" style='padding-top: 5px;'>
                                <div id='invite_image_glyph'><img src="${bb:libraryResource('/library/images/events/img_glyph.png')}"></div>
                                <div class="invite-field-label">Copy Image</div>
                                <div style="float:right;">
                                    <div class="input-left" style='margin-left: 5px;'></div>
                                    <div class="input-mid" style='width: 170px;'><input type='text' id="share_img" name='share_img' value='${actionBean.occasion.layout.transiantImage.location}' style="width: 150px;"/></div>
                                    <div class="input-right"></div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <%--<div id="guest_list_opts_right"></div>--%>
                </div>

            <%--${actionBean.occasion}--%>
            <%--
            <br /><br />
            <img src="${bb:libraryResource('/library/images/events/promote_placeholder.jpg')}">
            <br /><br />
            --%>

            <div style="clear:both; padding-top: 15px;">
                <%--
                <div style="float: right; cursor: pointer;"><img src='${bb:libraryResource('/library/images/events/design_btn5.png')}' onclick="window.location.href='<s:url beanclass='com.blackbox.presentation.action.psevent.PSShowEventActionBean'><s:param name="eventParam" value="${actionBean.occasion.guid}" /></s:url>';"></div>

                <div style="float: right; cursor: pointer;"><img src='${bb:libraryResource('/library/images/events/design_btn5.png')}' onclick="promote_save()" /></div>
                --%>
                <div style="float: right; cursor: pointer;"><input type="image" src='${bb:libraryResource('/library/images/events/design_btn5.png')}' /></div>
            </div>


        </div>

    </form>
    </div>

</body>
