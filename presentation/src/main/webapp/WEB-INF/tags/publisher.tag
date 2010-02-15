<%@ tag language="java" %>
<%@ attribute name="id" required="true" %>
<%@ attribute name="className" required="true" %>
<%@ attribute name="config" required="true" %>
<%@ attribute name="recipientIdentifier" required="false" %>
<%@ attribute name="parentGuid" required="false" %>
<%@ attribute name="sourceUser" required="false" type="com.blackbox.user.User" %>
<%@ include file="/WEB-INF/jsp/include/taglibs.jspf" %>

<c:choose>
    <c:when test="${config == 'gift'}">
        <c:set var="beanClass" value="com.blackbox.presentation.action.media.VirtualGiftActionBean"/>
        <c:set var="textMessageEvent" value="sendMessage"/>
        <c:set var="videoWebcamEvent" value=""/>
        <c:set var="videoUploadEvent" value=""/>
        <c:set var="pictureWebcamEvent" value="sendBase64"/>
        <c:set var="pictureUploadEvent" value="sendMedia"/>
        <c:set var="pictureUploadView" value="json"/>
    </c:when>
    <c:otherwise>
        <c:set var="beanClass" value="com.blackbox.presentation.action.media.PublishActionBean"/>
        <c:set var="textMessageEvent" value=""/>
        <c:set var="videoWebcamEvent" value=""/>
        <c:set var="videoUploadEvent" value="publishMedia"/>
        <c:set var="pictureWebcamEvent" value="publishBase64Media"/>
        <c:set var="pictureUploadEvent" value="publishMedia"/>
        <c:set var="pictureUploadView" value=""/>

    </c:otherwise>
</c:choose>

<div id="${id}" class="${className}">

    <div class="publisher-contents">

        <div class="media-type-inputs">

            <%--------| VIDEO WEBCAM |--------%>
            <div class="video-webcam-record media-source">
                <s:form beanclass="${beanClass}" method="post" class="video_webcam"></s:form>
                <div id="${id}_videoWebcamSWF">You need flash!</div>
            </div>

            <%--------| VIDEO UPLOAD |--------%>
            <div class="video-upload media-source file-upload">
                <s:form beanclass="${beanClass}" enctype="multipart/form-data" method="post" class="video_upload">
                    <div class="media-form">
                        <s:hidden name="_eventName" value="${videoUploadEvent}"/>
                        <s:file name="fileData"/>
                    </div>
                </s:form>
            </div>

            <%--------| PICTURE WEBCAM |--------%>
            <div class="picture-webcam-record media-source">
                <div id="${id}_pictureWebcamSWF">You need flash!</div>
                <s:form beanclass="${beanClass}" enctype="multipart/form-data" method="post" class="picture_webcam">
                    <div class="media-form">
                        <s:hidden name="_eventName" value="${pictureWebcamEvent}"/>
                        <s:hidden name="view" value="json"/>
                    </div>
                </s:form>
            </div>

        
            <div class="picture-upload media-source file-upload">
                <s:form beanclass="${beanClass}" enctype="multipart/form-data" method="post" class="picture_upload">
                    <div class="media-form">
                        <s:hidden name="_eventName" value="${pictureUploadEvent}"/>
                        <s:hidden name="view" value="${pictureUploadView}"/>
                        <s:file name="fileData"/>
                    </div>
                </s:form>
            </div>

            <%--------| TEXT MESSAGE |--------%>
            <s:form beanclass="${beanClass}" method="post" class="message_post media-source">
                <div class="media-form">
                    <s:hidden name="view" value="json"/>
                    <s:hidden name="_eventName" value="${textMessageEvent}"/>
                </div>
            </s:form>

            <div style="position:absolute;left:-9999px;top:-9999px;width:1px;height:1px;">
                <iframe src="<%=request.getContextPath()%>/uploadTarget.html" type="text/html" class="publisher_target" name="publisher_target"></iframe>
            </div>
        </div>

        <div class="publisher-controls clearfix">
            <div class="publisher-button-bar clearfix">

                <div class="publish-media-button">
                    <img src="${bb:libraryResource('/library/images/spacer.gif')}" alt="" class="small-icon small-images-icon publisher-media-icon"/>
                    <p>Add a photo</p>
                    <ul>
                        <li><a href="#" class="uploadPicture">Upload from computer</a></li>
                        <li class="last"><a href="#" class="webcamPicture">Use webcam</a></li>
                    </ul>
                </div>

                <div class="publish-media-button last">
                    <img src="${bb:libraryResource('/library/images/spacer.gif')}" alt="" class="small-icon small-video-icon publisher-media-icon"/>
                    <p>Add a video</p>
                    <ul>
                        <li><a href="#" class="uploadVideo">Upload from computer</a></li>
                        <li class="last"><a href="#" class="webcamVideo">Use webcam</a></li>
                    </ul>
                </div>

            </div>

            <form action="" method="post" id="${id}_form" class="recipients_form">
                <c:choose>
                    <c:when test="${config == 'main'}">
                        <div class="post-to">
                            <label>Post to:</label>
							<c:set var="allowRecipientDepth" value="true"/>
							<c:if test="${not empty sourceUser and not bb:isVouched(sourceUser.guid)}">
								<c:set var="allowRecipientDepth" value="false"/>
								<c:set var="recipientDepthValue" value="ALL_MEMEBERS"/>
							</c:if>

                            <input type="hidden" name="recipientDepth" value="${recipientDepthValue}"/>

                            <c:if test="${allowRecipientDepth}"><button class="bbbutton post-to-button WORLD" title="Post" value="WORLD"><span>Public Life</span></button></c:if>
                            <button class="bbbutton post-to-button ALL_MEMBERS" title="Post" value="ALL_MEMBERS"><span>All Members</span></button>
                            <c:if test="${allowRecipientDepth}"><button class="bbbutton post-to-button FRIENDS" title="Post" value="FRIENDS"><span>Friends</span></button></c:if>
                        </div>
                    </c:when>

					<c:when test="${config == 'invitation'}">
						<input type="hidden" name="recipientDepth" value="WORLD" />
						<input type="hidden" name="parent.guid" value="${parentGuid}" />
						<input type="hidden" name="parent.ownerType" value="OCCASION" />
						<button class="bbbutton post-to-button" title="Post"><span>Post message</span></button>
					</c:when>

                    <c:when test="${config == 'legends'}">
                        <input type="hidden" name="recipientDepth" value="WORLD"/>
                    </c:when>

                    <c:when test="${config == 'mini'}">
                        <button class="bbbutton post-to-button" title="Post"><span>Post reply</span></button>
                    </c:when>

                    <c:when test="${config == 'gift'}">
                        <button class="bbbutton post-to-button" title="Post"><span>Send Gift</span></button>
                        <input type="hidden" name="recipientDepth" value="ALL_MEMBERS"/>
                        <input type="hidden" name="recipientIdentifier" value="${recipientIdentifier}"/>
                    </c:when>

                </c:choose>
            </form>

            <a href="#" class="publisher-controls-close"><span>Cancel</span></a>

        </div>

    </div>

	<c:if test="${config == 'main'}">

	<div class="post-to-public-confirm">
		<h2>You are about to post this to the entire world.</h2>
		<p>By publishing this publicly you agree not to reference other members unless you have their consent. All public posts must adhere to the BBR <a href="http://www.blackboxrepublic.com/tos" target="_blank">Terms of Service</a>.</p>
		
		<div class="public-post-form">
			
			<form action="" method="post" onsubmit="return false;">

				<div class="post-to-wrap">

					<div class="post-to-check-row">
						<input type="checkbox" id="${id}publishToBlackbox" name="publishToBlackbox" value="true" checked="checked" disabled="disabled"/>
						<label for="${id}publishToBlackbox" class="post-to-label">
							<span>POST TO</span>
							<img src="${bb:libraryResource('/library/images/branding/bbr.png')}" alt="Post to Twitter" />
						</label>
					</div>
				</div>

				<div class="post-to-wrap">

					<div class="post-to-check-row">
						<input type="checkbox" id="${id}publishToTwitter" name="publishToTwitter" value="true"/>
						<label for="${id}publishToTwitter" class="post-to-label">
							<span>POST TO</span>
							<img src="${bb:libraryResource('/library/images/branding/twitter.png')}" alt="Post to Twitter" />
						</label>
					</div>

					<div class="post-to-creds twitter-box">
						<label for="${id}twitterMessageBody" style="display:none;">Twitter Message</label>
						<input type="text" id="${id}twitterMessageBody" value="Twitter message" name="twitterMessageBody" maxlength="125"/>
						<%-- + http://vb.ly/... --%>
						<span class="chars-remaining"><span class="char-count">140</span> characters remaining</span>
						<div id="twitterAuth">
							<label for="${id}twitterUsername">Username</label>
							<input type="text" id="${id}twitterUsername" style="margin-right: 20px;" name="twitterUsername"/>
							<label for="${id}twitterPassword">Password</label>
							<input type="password" id="${id}twitterPassword" name="twitterPassword"/>
							<input type="checkbox" id="${id}twitterRemember" name="twitterRemember" value="true"/><label for="${id}twitterRemember">Remember login</label>
						</div>
					</div>
				</div>

				<div class="post-to-wrap">

					<div class="post-to-check-row">
						<input type="checkbox" id="${id}publishToFacebook" name="publishToFacebook" value="true" disabled="disabled"/>
						<label for="${id}publishToFacebook" class="post-to-label">
							<span>POST TO</span>
							<img src="${bb:libraryResource('/library/images/branding/facebook.png')}" alt="Post to Facebook" />
						</label>
					</div>

					<div class="post-to-creds facebook-box">
						<%--  TODO: update to not use FBML --%>
						<div id="facebook_connect_status_container" class="clearfix">
							<fb:login-button v="2" size="medium">Connect with Facebook</fb:login-button>
						</div>
						<input type="hidden" name="facebookSessionId" value=""/>
					</div>

				</div>

			</form>

		</div>

	</div>
	</c:if>

</div>


