<%@include file="/WEB-INF/jsp/include/page_header.jspf" %>
<div class="eve_con_rig_mar">
	<div id="layout_preview" class="event_background_color_white" style="background-color:#${occasion.layout.backgroundColor};color:#${occasion.layout.textColor};">
		<div class="event_media">
			<div class="event_media_video">
				<%--                <c:choose>
													<c:when test="${occasion.layout.transiantVideo != null}">
														<img src="${occasion.layout.video.transiantVideo}" onerror='Activity.Utils.imgPoll(event, this);' />
													</c:when>
													<c:otherwise>
														<img src="${defaultVideoLocation}" onerror='Activity.Utils.imgPoll(event, this);' />
													</c:otherwise>
												</c:choose>--%>
			</div>
			<div class="event_media_pic" style="position:relative;">

				<c:choose>
					<c:when test="${not empty event_image_location}">
						<img id="big_event_image_p" src="${event_image_location}"/>
					</c:when>
					<c:when test="${not empty com.blackbox.presentation.SESSION_IMAGE}">
						<img id="big_event_image_p" src="<s:url beanclass="com.blackbox.presentation.action.media.SessionImageActionBean"/>/<%=new java.util.Date()%>"/>
					</c:when>
					<c:otherwise>
						<img id="big_event_image_p" src="${defaultImageLocation}"/>
					</c:otherwise>
				</c:choose>
			</div>
		</div>
		<div class="event_content event_content_left" style="width:300px">
			<div class="field">
				<div class="name">${occasion.name}</div>
				<div class="txt">description</div>
			</div>
			<div class="field">
				<div class="title">Date</div>
				<div class="txt">
					Event Time
				</div>
			</div>
			<div class="field">
				<div class="title">Location</div>
				<div class="txt">
					Event Location
				</div>
			</div>
			<div class="field">
				<div class="title">Address</div>
				<div class="map">
					<div class="txt">Address Line<br/>
						City<br/>
						State<br/>
						Country<br/>
						Zip/Postal Code
					</div>
					<div class="clear"></div>
				</div>
			</div>
		</div>
		<div class="event_content_right">
			<c:choose>
				<c:when test="${userType eq 'PROMOTER'}">
					<div style="visibility:hidden;background-image:url(${bb:libraryResource('/library/images/psevent/map_mock.jpg')}); width:161px; height:128px; margin:244px 20px 20px 0;float:right;"></div>
				</c:when>
				<c:otherwise>
					<div style="visibility:hidden;background-image:url(${bb:libraryResource('/library/images/defaultplaceholder.jpg')}); width:241px; height:188px; margin:20px 20px 0 0; color:#FF3C00; font-size:14px; text-align:center; font-weight:bold; line-height:188px;">Select a video to use
						here
					</div>
					<div style="background-image:url(${bb:libraryResource('/library/images/psevent/map_mock.jpg')}); width:161px; height:128px; margin:40px 20px 10px 0;float:right;"></div>
				</c:otherwise>
			</c:choose>
		</div>
		<div class="clear"></div>
	</div>
</div>

