<%@ tag language="java" %>
<%@ attribute name="user" required="true" type="com.blackbox.user.User" %>
<%@ attribute name="relationshipType" required="true" %>
<%@ attribute name="beanclass" required="true" %>
<%@ attribute name="event" required="true" %>
<%@ attribute name="isVouched" required="false"%>
<%@ attribute name="isBlocked" required="false"%>

<%@ include file="/WEB-INF/jsp/include/taglibs.jspf" %>
<fmt:setBundle basename="StripesResources"/>

<c:set var="url"><s:url beanclass="${beanclass}" event="${event}" />&identifier=${user.username}</c:set>

<c:set var="weight" value="${bb:getWeight(user)}" />

<c:if test="${not empty isBlocked && isBlocked}">
	<c:set var="blockedClass" value="blocked" />
</c:if>

<c:choose>

	<%-- Friend Button --%>
	<c:when test="${relationshipType eq 'friend'}">
		<c:choose>
            <c:when test="${not empty weight and weight >= 10}"> <%-- friend --%>
				<c:set var="label"><fmt:message key="network.friend.active"/></c:set>
				<c:set var="state" value="state_active" />
			</c:when>
			<c:when test="${not empty weight and weight == 9}"> <%-- if pending --%>
				<c:set var="label"><fmt:message key="network.friend.pending"/></c:set>
				<c:set var="state" value="state_active" />
			</c:when>
			<c:otherwise>
				<c:set var="label"><fmt:message key="network.friend.label"/></c:set>
			</c:otherwise>
		</c:choose>
		<c:if test="${not empty isVouched && not isVouched}">
			<c:set var="unvouchedClass" value="unvouched" />
		</c:if>
	</c:when>

	<%-- Follow Button --%>
	<c:when test="${relationshipType eq 'follow'}">
		<c:choose>
            <c:when test="${not empty weight and weight >= 9}"> <%-- friend or pending --%>
				<c:set var="label"><fmt:message key="network.follow.active"/></c:set>
				<c:set var="state" value="state_implied" />
			</c:when>
			<c:when test="${not empty weight and weight >= 0}"> <%-- following --%> 
				<c:set var="label"><fmt:message key="network.follow.active"/></c:set>
				<c:set var="state" value="state_active" />
			</c:when>
			<c:otherwise>
				<c:set var="label"><fmt:message key="network.follow.label"/></c:set>
			</c:otherwise>
		</c:choose>
	</c:when>

	<%-- Donkey Button --%>
	<c:when test="${relationshipType eq 'donkey'}">
		<c:choose>
			<c:when test="${bb:isBlocked(user)}">
				<c:set var="label"><fmt:message key="network.donkey.active"/></c:set>
				<c:set var="state" value="state_active" />
			</c:when>
			<c:otherwise>
				<c:set var="label"><fmt:message key="network.donkey.label"/></c:set>
			</c:otherwise>
		</c:choose>
	</c:when>

</c:choose>

<ui:anchorButton href="${url}" typeClass="ntwrk-btn" styleClass="${state} ${relationshipType}_link ${relationshipType}_link_${user.username} ${unvouchedClass} ${blockedClass}">${label}</ui:anchorButton>