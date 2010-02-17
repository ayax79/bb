<%@ tag language="java" %>
<%@ attribute name="entity" required="true" type="com.blackbox.foundation.IEntity" %>
<%@ attribute name="classClass" required="false" %>
<%@ attribute name="id" required="false" %>
<%@ include file="/WEB-INF/jsp/include/taglibs.jspf"%>

<c:choose>
    <c:when test="${not empty entity.ownerType and 'USER' == s:enumName(entity.ownerType)}">
        <s:link beanclass="com.blackbox.presentation.action.persona.PersonaActionBean" id="${id}" class="${classClass}" >
            <s:param name="identifier" value="${entity.username}" />
            <jsp:doBody/>
        </s:link>
    </c:when>
</c:choose>


