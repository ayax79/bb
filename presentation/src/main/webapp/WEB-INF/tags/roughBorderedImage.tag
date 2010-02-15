<%@ tag language="java" %>
<%@ attribute name="id" required="false" %>
<%@ attribute name="url" required="true" %>
<%@ attribute name="alt" required="true" %>
<%@ attribute name="onError" required="false" %>
<%@ attribute name="className" required="false" %>
<%@include file="/WEB-INF/jsp/include/taglibs.jspf" %>

<div class="bordered-image">
	<img src="${url}" alt="${alt}" <c:if test="${not empty className}">class="${className}"</c:if> <c:if test="${not empty onError}">onerror="${onError}"</c:if> />
	<div class="image-tl"></div>
	<div class="image-br"></div>
</div>