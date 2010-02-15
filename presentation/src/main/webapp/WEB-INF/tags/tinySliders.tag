<%@ tag language="java" %>
<%@ include file="/WEB-INF/jsp/include/taglibs.jspf" %>
<fmt:setBundle basename="StripesResources"/>

<%@ attribute name="user" required="true" type="com.blackbox.user.User" %>
<%@ attribute name="className" required="false" %>

<c:choose>
	<c:when test="${not user.profile.mood.makePrivate}">
		<c:set var="orientation" value="${user.profile.mood.orientation}"/>
		<c:set var="relationshipStatus" value="${user.profile.mood.relationshipStatus}"/>
		<c:set var="polyStatus" value="${user.profile.mood.polyStatus}"/>
		<c:set var="interestLevel" value="${user.profile.mood.interestLevel}"/>
		<c:set var="energyLevel" value="${user.profile.mood.energyLevel}"/>
	</c:when>
</c:choose>

<div class="tiny-sliders ${className}">
	<ul>
		<li>
			<label><fmt:message key="settings.field.orientation"/></label><span class="tiny-slider-gfx tiny-slider-pos${orientation}">&nbsp;</span>
		</li>

		<li>
			<label><fmt:message key="settings.field.status"/></label><span class="tiny-slider-gfx tiny-slider-pos${relationshipStatus}">&nbsp;</span>
		</li>

		<li>
			<label><fmt:message key="settings.field.addPartnersShort"/></label><span class="tiny-slider-gfx tiny-slider-pos${polyStatus}">&nbsp;</span>
		</li>

		<li>
			<label><fmt:message key="settings.field.lovin"/></label><span class="tiny-slider-gfx tiny-slider-pos${interestLevel}">&nbsp;</span>
		</li>

		<li>
			<label><fmt:message key="settings.field.vibe"/></label><span class="tiny-slider-gfx tiny-slider-pos${energyLevel}">&nbsp;</span>
		</li>
	</ul>
	<c:if test="${user.profile.mood.makePrivate}">
		<div class="private-icon"></div>
	</c:if>
</div>