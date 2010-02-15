<%@ tag import="org.springframework.web.context.WebApplicationContext" %>
<%@ tag import="org.springframework.web.context.support.WebApplicationContextUtils" %>
<%@ tag import="com.blackbox.presentation.action.util.AvatarCacheKey" %>
<%@ tag import="com.blackbox.media.IMediaManager" %>
<%@ tag import="com.blackbox.user.IUserManager" %>
<%@ tag import="com.blackbox.user.User" %>
<%@ tag import="com.blackbox.presentation.extension.DefaultBlackBoxContext" %>
<%@ tag import="com.blackbox.media.AvatarImage" %>
<%@ tag import="org.yestech.cache.ICacheManager" %>
<%@ tag import="com.blackbox.EntityType" %>
<%@ tag import="com.blackbox.presentation.action.util.JspFunctions" %>
<%@ tag import="static org.apache.commons.lang.StringUtils.isNotBlank" %>
<%@ tag import="com.blackbox.presentation.action.util.PresentationUtil" %>
<%@ tag language="java" %>
<%@ attribute name="guid" required="true" %>
<%@ attribute name="showMiniProfile" required="false" %>
<%@ attribute name="size" required="true" %>
<%@ attribute name="color" required="false" %>
<%@ attribute name="showUserType" required="false" %>
<%@ attribute name="linkToProfile" required="false" %>
<%@ attribute name="entityType" type="com.blackbox.EntityType" %>

<%@include file="/WEB-INF/jsp/include/taglibs.jspf" %>

<cache:cache key="profileImage-${guid}-${size}-${showUserType}-${linkToProfile}" time="10" scope="session" refresh="${not empty param.refresh}">

<%
    boolean showDefault = false;
    boolean owner = false;
    AvatarImage avatarImage = null;

    User currentUser = (User) session.getAttribute(DefaultBlackBoxContext.USER_KEY);
    if (currentUser != null && isNotBlank(guid)) {
        avatarImage = PresentationUtil.getAvatarImage(entityType, guid);
        showDefault = avatarImage == null || avatarImage.getImageLink() == null;
        owner = guid.equals(currentUser.getGuid());
    }

    jspContext.setAttribute("showDefault", showDefault);
    jspContext.setAttribute("owner", owner);
    jspContext.setAttribute("avatarImage", avatarImage);

%>

<c:if test="${showMiniProfile == 'true'}">
    <c:set var="linkClass" value="userProfileLink allow-mini-profile"/>
</c:if>

<%-- Set profile link --%>
<c:choose>
	<c:when test="${linkToProfile == 'false'}">
		<s:url var="linkString" value="" anchor="#/${guid}"/>
	</c:when>
	<c:otherwise>
		<c:choose>
			<c:when test="${not empty avatarImage}">
				<s:url var="linkString" beanclass="com.blackbox.presentation.action.persona.PersonaActionBean"><s:param name="identifier" value="${avatarImage.name}"/></s:url>
		</c:when>
			<c:otherwise>
				<s:url var="linkString" beanclass="com.blackbox.presentation.action.persona.PersonaActionBean"><c:if test="${not empty guid}"><s:param name="identifier" value="${guid}"/></c:if></s:url>
			</c:otherwise>
		</c:choose>
	</c:otherwise>
</c:choose>

<%-- Set user type --%>
<c:choose>
	<c:when test="${not empty avatarImage}">
		<c:set var="userType" value="${avatarImage.userType}"/>
	</c:when>
	<c:otherwise>
		<c:set var="userType" value="NORMAL"/>
	</c:otherwise>
</c:choose>

<%-- Set Image attributes --%>
<c:choose>
	<c:when test="${showDefault}">
		<c:set var="imageUrl" value="${bb:libraryResource('/library/images/icons/profile_person.png')}"/>
		<c:set var="alt" value="${avatarImage.name}"/>
	</c:when>
	<c:when test="${not empty avatarImage}">
		<c:set var="imageUrl" value="${avatarImage.imageLink}"/>
		<c:set var="alt" value="${avatarImage.name}"/>
	</c:when>
	<c:otherwise>
		<c:set var="imageUrl" value="${bb:libraryResource('/library/images/spacer.gif')}"/>
		<c:set var="alt" value="avatar"/>
	</c:otherwise>
</c:choose>

<div class="profile-image-container s_${size} UT_${userType}">
	<c:if test="${linkToProfile == 'true' or showMiniProfile == 'true'}"><a href="${linkString}" class="${linkClass}"></c:if>
	<img class="profile-image" alt="${alt}" src="${imageUrl}" />
    <c:if test="${linkToProfile == 'true' or showMiniProfile == 'true'}"></a> </c:if>
</div>
</cache:cache>