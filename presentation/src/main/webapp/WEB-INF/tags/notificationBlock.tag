 <%@ tag language="java" %>
<%@ attribute name="type" required="true" %>
<%@ attribute name="includeCheck" required="false" %>
<%@ attribute name="notification" type="com.blackbox.foundation.notification.Notification" required="false" %>
<%@include file="/WEB-INF/jsp/include/taglibs.jspf" %>
<c:set var="type" value="${f:toLowerCase(type)}"/>

<div class="notification-item user notification_${notification.guid} clearfix">
	<c:if test="${includeCheck == 'true'}"><input type="checkbox" class="notification-checkbox" value="${notification.guid}_${notification.fromEntityGuid}"/></c:if>
	<c:choose>
		<%-- FRIENDS --%>
		<c:when test="${type eq 'friend'}">
            <ui:profileImage guid="${notification.fromEntityGuid}" size="small" color="white" linkToProfile="true" showMiniProfile="true" showUserType="true"/>
			<p><s:link beanclass="com.blackbox.presentation.action.persona.PersonaActionBean"> <s:param name="identifier" value="${notification.username}"/>${notification.displayName}</s:link> wants to be your friend</p>
			<ul>
				<li><a href="#${notification.guid}_${notification.fromEntityGuid}" class="accept-friend-link">Accept</a></li>
				<li class="noborder"><a href="#${notification.guid}_${notification.fromEntityGuid}" class="ignore-friend-link">Ignore</a></li>
			</ul>
		</c:when>
		<%-- FOLLOWING --%>
		<c:when test="${type eq 'follow'}">
            <ui:profileImage guid="${notification.fromEntityGuid}" size="small" color="white" linkToProfile="true" showMiniProfile="true" showUserType="true"/>
			<p><s:link beanclass="com.blackbox.presentation.action.persona.PersonaActionBean"> <s:param name="identifier" value="${notification.username}"/>${notification.displayName}</s:link> is now following you</p>
			<ul>
				<li><a href="#${notification.guid}_${notification.fromEntityGuid}" class="acknowledge-follow-link">OK</a></li>
				<c:choose>
					<c:when test="${notification.following}">
						<li class="noborder"><span>Following</span></li>
					</c:when>
					<c:otherwise>
						<li class="noborder"><a href="#${notification.guid}_${notification.fromEntityGuid}" class="follow-back-link">Follow back</a></li>
					</c:otherwise>
				</c:choose>
			</ul>
		</c:when>
		<%-- WISHES --%>
		<c:when test="${type eq 'wish'}">
            <ui:profileImage guid="${notification.fromEntityGuid}" size="small" color="white" linkToProfile="true" showMiniProfile="true" showUserType="true"/>
			<p><s:link beanclass="com.blackbox.presentation.action.persona.PersonaActionBean"> <s:param name="identifier" value="${notification.username}"/>${notification.displayName}</s:link> has wished you</p>
			<ul>
				<li><a href="#${notification.guid}_${notification.fromEntityGuid}" class="accept-wish-link">Accept</a></li>
				<li class="noborder"><a href="#${notification.guid}_${notification.fromEntityGuid}" class="deny-wish-link">Ignore</a></li>
			</ul>
		</c:when>
		<%-- VOUCHES --%>
		<c:when test="${type eq 'vouch'}">
            <ui:profileImage guid="${notification.fromEntityGuid}" size="small" color="white" linkToProfile="true" showMiniProfile="true" showUserType="true"/>
			<p><s:link beanclass="com.blackbox.presentation.action.persona.PersonaActionBean"> <s:param name="identifier" value="${notification.username}"/>${notification.displayName}</s:link> has vouched you</p>
			<ul>
				<li>
					<a href="#${notification.guid}_${notification.fromEntityGuid}" class="accept-vouch-link">Accept</a>
				</li>
				<li class="noborder">
					<a href="#${notification.guid}_${notification.fromEntityGuid}" class="deny-vouch-link">Ignore</a>
				</li>
			</ul>
		</c:when>
        <%-- GIFTS --%>
		<c:when test="${type eq 'gift'}">
            <ui:profileImage guid="${notification.fromEntityGuid}" size="small" color="white" linkToProfile="true" showMiniProfile="true" showUserType="true"/>
			<p><s:link beanclass="com.blackbox.presentation.action.persona.PersonaActionBean"> <s:param name="identifier" value="${notification.username}"/>${notification.displayName}</s:link> has gifted you</p>
			<ul>
				<li><a href="#${notification.guid}_${notification.fromEntityGuid}" class="accept-gift-link">Accept</a></li>
				<li class="noborder"><a href="#${notification.guid}_${notification.fromEntityGuid}" class="deny-gift-link">Ignore</a></li>
			</ul>
		</c:when>
        <%-- EVENTS --%>
        <c:when test="${type eq 'occasion'}">
            <div class="event-notification-image-container" style="float: left; margin-left: 3px; margin-right: 8px;"><img src='${notification.occasion.layout.transiantImage.location}' width="40" height="40"></div>
			<p><s:link beanclass="com.blackbox.presentation.action.psevent.PSShowEventActionBean"><s:param name="eventParam" value="${notification.occasion.guid}"/>${notification.occasion.name}</s:link> you are invited</p>
			<ul>
				<li class=""><a href="#${notification.occasion.guid}_${actionBean.currentUser.guid}" class="accept-invitation-link">Yes</a></li>
				<li class=""><a href="#${notification.occasion.guid}_${actionBean.currentUser.guid}" class="maybe-invitation-link">Maybe</a></li>
				<li class="noborder"><a href="#${notification.occasion.guid}_${actionBean.currentUser.guid}" class="decline-invitaion-link">No</a></li>
			</ul>
		</c:when>
	</c:choose>
</div>
<div class="clear"></div>
