<%--@elvariable id="actionBean" type="com.blackbox.presentation.action.persona.PersonaActionBean"--%>
<%@ page import="java.util.ArrayList" %>
<%@ page import="com.blackbox.presentation.action.persona.PersonaActionBean" %>
<%@ page import="java.util.List" %>

<%@include file="/WEB-INF/jsp/include/page_header.jspf" %>

<%--
There is a time check and flushing in DefaultPersonaHelper#containsPersonaPageCache
don't modify this cache key without first modifying that method.
--%>

<%
	List<String> lookingFor = new ArrayList<String>();
	PersonaActionBean ab = (PersonaActionBean) request.getAttribute("actionBean");
	if (ab.getProfile().getLookingFor().isDates()) {
		lookingFor.add("social dates");
	}
	if (ab.getProfile().getLookingFor().isDonkeySex()) {
		lookingFor.add("pillow fights");
	}
	if (ab.getProfile().getLookingFor().isFriends()) {
		lookingFor.add("real friends");
	}
	if (ab.getProfile().getLookingFor().isHookup()) {
		lookingFor.add("hookups");
	}
	if (ab.getProfile().getLookingFor().isLove()) {
		lookingFor.add("love");
	}
	if (ab.getProfile().getLookingFor().isSnuggling()) {
		lookingFor.add("parties");
	}
	pageContext.setAttribute("lookingFor", lookingFor);

%>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<title><fmt:message key="pageTitlePrefix"/> : ${bb:displayName(actionBean.user)}</title>

<link rel="stylesheet" href="${bb:libraryResource('/library/css/my_n.css')}" type="text/css" media="screen, projection"/>
<link rel="stylesheet" href="${bb:libraryResource('/library/css/persona.css')}" type="text/css" media="screen, projection"/>
<link rel="stylesheet" href="${bb:libraryResource('/library/css/jquery.lightbox-0.5.css')}" type="text/css"/>
<link rel="stylesheet" href="${bb:libraryResource('/library/uploadify/uploadify.css')}" type="text/css"/>
<link rel="stylesheet" href="${bb:libraryResource('/library/css/vid_pop.css')}" type="text/css" media="screen, projection"/>

<script src="${bb:libraryResource("/library/js/jquery.lightbox-0.5.js")}" type="text/javascript"></script>
<script src="${bb:libraryResource("/library/js/publisher.js")}" type="text/javascript"></script>
<script src="${bb:libraryResource("/library/uploadify/swfobject.js")}" type="text/javascript"></script>
<script src="${bb:libraryResource("/library/uploadify/jquery.uploadify.js")}" type="text/javascript"></script>
<script src="${bb:libraryResource('/library/js/jquery.tokeninput.js')}" type="text/javascript"></script>

<script type="text/javascript">
//<![CDATA[
	// This is used throughout persona.js for user identifiers and display
	var currentPersonaUser = {
		identifier: '${actionBean.user.username}',
		guid: '${actionBean.user.guid}',
		name: '${bb:displayName(actionBean.user)}'
	};

	var currentUserVouched = ('${bb:isVouched(actionBean.currentUser.guid)}' == 'true');
	var disablePersonaTabs = (('${actionBean.owner}' != 'true') && !currentUserVouched);

//]]>
</script>
<script src="${bb:libraryResource("/library/js/persona.js")}" type="text/javascript"></script>
<%@ include file="/WEB-INF/jsp/include/jcrop.jspf" %>

<script type="text/javascript">
//<![CDATA[
$(function() {

	window.myPictureTab = new picturesTab($("#tab_Content_Pictures"));
	$("#main-albums").photoAlbum({
		size:6,
		itemSelector:".photo-album"
	});
	myPictureTab.bindAlbumCovers($("#main-albums"));

	<%--<c:if test="${actionBean.firstTime}">--%>
    <%--$.prettyPhoto.open('http://cdn.episodic.com/player/EpisodicPlayer.swf?width=480&height=360&config=http%3A%2F%2Fcdn.episodic.com%2Fshows%2Fob0pj9jq17up%2Fob0tc2cv668i%2Fconfig.xml');--%>
    <%--</c:if>--%>

    $("#intro").click(function() {
       $.prettyPhoto.open('http://cdn.episodic.com/player/EpisodicPlayer.swf?width=480&height=360&config=http%3A%2F%2Fcdn.episodic.com%2Fshows%2Fob0pj9jq17up%2Fob0tc2cv668i%2Fconfig.xml');
    });

	$("#menu_bot_id").fadeIn();

});

//]]>
</script>

</head>
<body id="persona-page">

<%@include file="/WEB-INF/jsp/include/stream-templates.jspf" %>

<div class="container container-top"></div>
<div class="container darken">

<ui:roundedBox className="black40 persona-header">
	<div style="float:left;">
		<div id="show_id">
			<div class="con_onl_lef">${actionBean.user.username}</div>
			<c:if test="${actionBean.online}"><div class="con_online">Online</div></c:if>
			<div class="clear"></div>
			<c:set var="lastSec" value="false"/>
			<div class="con_onl_bot">
				<c:if test="${actionBean.owner or bb:isFriend(user)}">
					${bb:userFullName(actionBean.user)}
					<c:set var="lastSec" value="true"/>
				</c:if>
				<span id="city_span_id">
					<c:if test="${!empty actionBean.profile.location.city}">
						<c:if test="${lastSec == 'true'}"> <span class="con_onl_bot ">|</span> </c:if>
						<c:if test="${!empty actionBean.profile.location.city}">${actionBean.profile.location.city}</c:if>
						<c:if test="${!empty bb:stateAbv(actionBean.profile.location.state)}">, ${bb:stateAbv(actionBean.profile.location.state)}</c:if>
					</c:if>
				</span>
				<c:if test="${not empty actionBean.profile.birthday}">
					<c:if test="${actionBean.owner == 'true' || actionBean.profile.birthdayInVisible == 'false'}">
						<c:if test="${lastSec == 'true' }"> | </c:if>
						<span id="age_span_id">
							${bb:age(actionBean.profile.birthday)}
							<c:choose>
								<c:when test="${actionBean.profile.sex == 'MALE'}">M</c:when>
								<c:when test="${actionBean.profile.sex == 'FEMALE'}">F</c:when>
							</c:choose>
							<c:set var="lastSec" value="true"/>
						</span>
					</c:if>
				</c:if>
				<c:if test="${not empty actionBean.profile.currentAddress.city}">
					| Currently in ${actionBean.profile.currentAddress.city}
				</c:if>
				<span class="tex_11"></span>
			</div>
		</div>
	</div>
	<c:if test="${actionBean.owner == 'false'}">
		<c:set var="wishUrl"><s:url beanclass="com.blackbox.presentation.action.persona.PersonaActionBean" event="wish"><s:param name="identifier" value="${actionBean.user.username}"/><s:param name="returnPage" value="persona"/></s:url></c:set>
		<c:set var="unwishUrl"><s:url beanclass="com.blackbox.presentation.action.persona.PersonaActionBean" event="unWish"><s:param name="identifier" value="${actionBean.user.username}"/><s:param name="returnPage" value="persona"/></s:url></c:set>
		<div id="menu_bot_id" style="display:none;">
            <c:if test="${not bb:isLimited(actionBean.currentUser) or actionBean.owner}">
			<ui:relationshipsButton user="${actionBean.user}" relationshipType="friend" beanclass="com.blackbox.presentation.action.persona.PersonaActionBean" event="friend" isBlocked="${actionBean.blocksCurrentUser}" isVouched="${bb:isVouched(actionBean.currentUser.guid)}"/>
            </c:if>
			<c:if test="${bb:isAllowedToFollow(actionBean.currentUser, actionBean.user.guid)}"><ui:relationshipsButton user="${actionBean.user}" relationshipType="follow" beanclass="com.blackbox.presentation.action.persona.PersonaActionBean" event="follow" isBlocked="${actionBean.blocksCurrentUser}" isVouched="${bb:isVouched(actionBean.currentUser.guid)}"/></c:if>
			<ui:relationshipsButton user="${actionBean.user}" relationshipType="donkey" beanclass="com.blackbox.presentation.action.persona.PersonaActionBean" event="block" isVouched="${bb:isVouched(actionBean.currentUser.guid)}"/>
		</div>
		<br style="clear:right;"/>
		<div class="clear"></div>
	</c:if>

	<div class="clear"></div>
</ui:roundedBox>

<div class="hight_6"></div>
<div class="main_con">
	<div class="main_con_bg clearfix">
		<div id="activity" class="con_rig_menu_t">
			<div id="stream-tabs_t" class="rsub-tab-container clearfix">
				<ul id="persona-nav" class="sub-tabs">
					<li>
						<a href="#tab_Content_Info" onclick="<%--<c:if test="${actionBean.owner}">javascript:showInfo();</c:if>--%>">Info</a>
					</li>
                    <c:if test="${bb:isAllowedToViewPersona(actionBean.currentUser, actionBean.user.guid)}">
					<li>
						<a href="#tab_Content_Pictures">Pictures<span id="all_pics_count" class="font_8"></span></a>
						<div class="notify-number"><span id="today_pics_count" class="count" style="display:none"></span></div>
					</li>
					<li>
						<a href="#tab_Content_Activity" onclick="//showActivity();">Activity</a>
					</li>
					<li>
						<a href="#tab_Content_Vouches">Vouches</a>
					</li>
                    </c:if>
				</ul>
				<c:if test="${not actionBean.owner}">
					<div id="persona-icons">
						<ul>
                            <c:if test="${not bb:isLimited(actionBean.currentUser) or actionBean.owner}">
							<li class="vouch"><a id="vouch_btn" title="Vouch" href="#" class="<c:if test="${actionBean.vouched || not bb:isVouched(actionBean.currentUser.guid) || actionBean.blocksCurrentUser}"> disabled </c:if><c:if test="${not bb:isVouched(actionBean.currentUser.guid)}"> unvouched </c:if><c:if test="${actionBean.blocksCurrentUser}"> blocked </c:if>">
                                <c:choose>
                                    <c:when test = "${actionBean.totalVouches < 1}">
                                        <span id="unvouched" class="vouch-icon">0</span>
                                    </c:when>
                                    <c:otherwise>
                                        <span id="vouched" class="vouch-icon">${actionBean.totalVouches}</span>
                                    </c:otherwise>
							    </c:choose>
                                <c:choose>
                                    <c:when test = "${actionBean.vouched}">
										<span class="vouch-label">Vouches</span>
                                    </c:when>

                                    <c:otherwise>
										<span class="vouch-label">Vouch Me</span>
                                    </c:otherwise>
							    </c:choose>
							</a></li>
							</c:if>

							<li class="wish">
								<a id="wish_btn" title="Wish" href="#" class="<c:if test="${'WISHED' == s:enumName(actionBean.persona.wishStatus) || 'MUTUAL' == s:enumName(actionBean.persona.wishStatus) || not bb:isVouched(actionBean.currentUser.guid) || actionBean.blocksCurrentUser}">disabled</c:if> <c:if test="${not bb:isVouched(actionBean.currentUser.guid)}"> unvouched </c:if><c:if test="${actionBean.blocksCurrentUser}"> blocked </c:if>">
								<c:set var="wishClass" value="${actionBean.persona.wishStatus}"/>
								<span id="wished-icon" class="wish-icon ${wishClass}"></span>
								<span id="WISHED_LABEL" class="wish-label <c:if test="${'WISHED' == s:enumName(actionBean.persona.wishStatus)}">active</c:if>">Wished</span>
							    <span id="WISHED_BY_LABEL" class="wish-label <c:if test="${'WISHED_BY' == s:enumName(actionBean.persona.wishStatus)}">active</c:if>">Wished Me</span>
							    <span id="MUTUAL_LABEL" class="wish-label <c:if test="${'MUTUAL' == s:enumName(actionBean.persona.wishStatus)}">active</c:if>">Mutual wish</span>
							    <span id="NEITHER_LABEL" class="wish-label <c:if test="${'NEITHER' == s:enumName(actionBean.persona.wishStatus)}">active</c:if>">Wish Me</span>
								</a>
							</li>
							<c:if test="${bb:isAllowedToGift(actionBean.currentUser, actionBean.user.guid) and not actionBean.owner}">
                                <li class="gift"><a href="#" title="Gift" id="gift-button" class="<c:if test="${not bb:isVouched(actionBean.currentUser.guid) || actionBean.blocksCurrentUser}"> disabled </c:if><c:if test="${not bb:isVouched(actionBean.currentUser.guid)}"> unvouched </c:if><c:if test="${actionBean.blocksCurrentUser}"> blocked </c:if>"><span class="gift-icon"></span><span>Send Gift</span></a></li>
                            </c:if>
							<c:if test="${bb:isAllowedToPrivateMessage(actionBean.currentUser, actionBean.user.guid)}"><li class="message last"><a href="#${actionBean.user.guid}" class="compose-link <c:if test="${not bb:isVouched(actionBean.currentUser.guid) || actionBean.blocksCurrentUser}"> disabled </c:if><c:if test="${not bb:isVouched(actionBean.currentUser.guid)}"> unvouched </c:if><c:if test="${actionBean.blocksCurrentUser}"> blocked </c:if>"><span class="msg-icon"></span><span>Send Message</span></a></li></c:if>
						</ul>
					</div>
				</c:if>
			</div>
			<div class="clear"></div>
		</div>
		<div class="main_con_rigbg" style="">
            <div id="tab_Content_Info">
				<jsp:include page="/WEB-INF/jsp/include/persona_info.jspf"/>
			</div>
            <%-- Hide these sections from limited users --%>
            <c:if test="${(bb:isAllowedToViewPersona(actionBean.currentUser, actionBean.user.guid) && bb:isVouched(actionBean.currentUser.guid)) or actionBean.owner}">
                <div id="tab_Content_Pictures">
                    <jsp:include page="/WEB-INF/jsp/include/persona_albums.jspf"/>
                </div>

                <div id="tab_Content_Activity">
                    <%@ include file="/WEB-INF/jsp/include/persona_activity.jspf"%>
                </div>
                <div id="tab_Content_Vouches" class="user-list-container">
                    <jsp:include page="/WEB-INF/jsp/include/persona_vouches.jspf"/>
                </div>
            </c:if>

		</div>
	</div>
</div>

<div class="main_con_bot"></div>
<div class="clear"></div>

<c:if test="${bb:isAllowedToViewPersona(actionBean.currentUser, actionBean.user.guid)}">
<div class="widget-container corkboard">
	<h2><span>My Corkboard</span></h2>

	<div class="widget-body-container">
		<div id="cor_con" class="widget-body">
			<jsp:include page="/WEB-INF/jsp/include/persona_corkboard.jspf">
				<jsp:param name="corkboard" value="${corkboard}"/>
			</jsp:include>
		</div>
	</div>
	<div class="widget-footer"><span>&nbsp;</span></div>
</div>

<div class="widget-container widget-collapsible wordcloud">
	<h2><span>My Activity Cloud</span></h2>
	<div class="widget-body-container">
		<div class="widget-body">
			<jsp:include page="/WEB-INF/jsp/include/persona_tagcloud.jspf"/>
		</div>
	</div>
	<div class="widget-footer"><span>&nbsp;</span></div>
</div>

<div class="widget-container gift-shelf">
	<h2><span>My Gifts</span></h2>
	<div class="widget-body-container">
		<div class="widget-body">
			<jsp:include page="/WEB-INF/jsp/include/persona_giftshelf.jspf"/>
		</div>
	</div>
	<div class="widget-footer"><span>&nbsp;</span></div>
</div>
</c:if>
</div>

<div class="container container-bottom"></div>

<div id="gift-publisher-container">
	<ui:mainPublisher inputId="gift-publisher" defaultText="Type a message (e.g. a poem, story or joke)"/>
	<ui:publisher id="giftPublisher" className="gift" pubConfig="gift" recipientIdentifier="${actionBean.user.guid}"/>
</div>
<div id="vouchForm">
	<s:form beanclass="com.blackbox.presentation.action.persona.PersonaActionBean" class="bbform">
		<ul>
			<li>
				<s:hidden name="identifier" value="${actionBean.user.username}"/>
				<s:label for="description">Enter a brief message <span>240 characters max</span></s:label>
				<s:textarea name="vouch.description"/>
			</li>
		</ul>
	</s:form>
</div>
<div id="wishForm">
	<s:form beanclass="com.blackbox.presentation.action.persona.PersonaActionBean" class="bbform">
		<ul>
			<li>
				<s:hidden name="identifier" value="${actionBean.user.username}"/>
				<label for="wish-description">Enter a brief message <span>240 characters max</span></label>
                <s:textarea id="wish-description" name="description"/>
			</li>
		</ul>
	</s:form>
</div>
</body>
</html>