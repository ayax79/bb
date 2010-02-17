<%@ page import="com.blackbox.foundation.EntityType" %>
<%@ include file="/WEB-INF/jsp/include/taglibs.jspf" %>
<html>
<head>
    <title>bookmarks</title>
</head>
<body>

<div id="bookmark-form">
<s:form beanclass="com.blackbox.presentation.action.bookmark.BookmarkActionBean">
    <ul>
        <li><label for="targetGuid" >Target Guid:</label><s:text name="target.guid" id="targetGuid"/></li>
        <li><label for="targetOwnerType">Target Owner Type:</label><s:text name="target.ownerType" id="targetOwnerType" /></li>
    </ul>
</s:form>
</div>


<%--@elvariable id="actionBean" type="com.blackbox.presentation.action.bookmark.BookmarkActionBean"--%>
<div id="bookmark-container">
<c:forEach items="${actionBean.bookmarks}" var="bookmark">
    <div>
        <c:choose>
            <c:when test="${s:enumName(bookmark.target.ownerType) eq 'USER'}" >
                <%--@elvariable id="user" type="com.blackbox.foundation.user.User"--%>
                <c:set var="user" value="${bookmark.targetObject}" />

                <s:link beanclass="com.blackbox.presentation.action.persona.PersonaActionBean">
                    <s:param name="identifier" value="${user.username}" />
                    ${bb:displayName(user)}
                </s:link>
            </c:when>
        </c:choose>
    </div>
</c:forEach>
</div>

</body>
</html>