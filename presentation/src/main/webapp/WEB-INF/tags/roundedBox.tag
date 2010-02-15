<%@ tag language="java" %>
<%@ include file="/WEB-INF/jsp/include/taglibs.jspf" %>

<%@ attribute name="id" required="false" %>
<%@ attribute name="className" required="true" %>
<%@ attribute name="namespace" required="false" %>

<div <c:if test="not empty ${id}"> id="${id}" </c:if> class="rounded-box ${className}">
	<div class="rb-header<c:if test="${not empty namespace}"> ${namespace}_rb-header</c:if>">
		<div class="rb-header-r<c:if test="${not empty namespace}"> ${namespace}_rb-header-r</c:if>">&nbsp;</div>
	</div>
	<div class="rb-content<c:if test="${not empty namespace}"> ${namespace}_rb-content</c:if>">
		<div class="rb-content-r<c:if test="${not empty namespace}"> ${namespace}_rb-content-r</c:if>">
			<jsp:doBody/>
		</div>
	</div>
	<div class="rb-footer<c:if test="${not empty namespace}"> ${namespace}_rb-footer</c:if>">
		<div class="rb-footer-r<c:if test="${not empty namespace}"> ${namespace}_rb-footer-r</c:if>">&nbsp;</div>
	</div>
</div>



