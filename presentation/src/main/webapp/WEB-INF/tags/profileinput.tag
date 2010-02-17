<%@ tag language="java" %>
<%@ attribute name="field" required="true" type="com.blackbox.foundation.user.profile.ProfileField"  description="The profile field we should render a tag for" %>
<%@ attribute name="prefix" required="true" type="java.lang.String" description="Prefix should be something like actionBean.mymap" %>
<%@ include file="/WEB-INF/jsp/include/taglibs.jspf"%>
<%@ tag import="com.blackbox.foundation.user.profile.ProfileField.FieldType" %>


<c:choose>
    <c:when test="${field.fieldType eq FieldType.BOOLEAN}">
        render a boolean field
    </c:when>
    <c:otherwise>
        <s:text name="${prefix}.${field.key}" />
    </c:otherwise>
</c:choose>
