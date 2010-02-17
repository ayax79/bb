<%@include file="/WEB-INF/jsp/include/taglibs.jspf" %>

<%@ attribute name="id" required="false" %>
<%@ attribute name="guid" required="true" %>
<%@ attribute name="ownerType" required="true" %>
<%@ attribute name="defaultValue" required="true" %>
<%@ attribute name="beanclass" required="true" %>
<%@ attribute name="event" required="false" %>

<c:choose>
	<c:when test="${defaultValue == 'ALL_MEMBERS'}">
		<c:set var="pval" value="all members"/>
	</c:when>
	<c:when test="${defaultValue == 'WORLD'}">
		<c:set var="pval" value="public Life"/>
	</c:when>
	<c:when test="${defaultValue == 'FRIENDS'}">
		<c:set var="pval" value="friends"/>
	</c:when>
	<c:when test="${defaultValue == 'DIRECT'}">
		<c:set var="pval" value="Direct"/>          
	</c:when>
	<c:otherwise>
		<c:set var="pval" value="${defaultValue}"/>
	</c:otherwise>
</c:choose>
<ul class="privacy_control" <c:if test="${not empty id}"> id="${id}" </c:if>>
	<li class="menu_selection ${defaultValue}"><span href="#" class="top">To <span class="pval">${pval}</span></span>
		<%--<s:form beanclass="${beanclass}" method="post">--%>
		<%--<div>--%>
		<%--<input type="hidden" name="guid" value="${guid}"/>--%>
		<%--<input type="hidden" name="ownerType" value="${ownerType}"/>--%>
		<%--<input type="hidden" name="event" value="${event}"/>--%>
		<%--<ul class="privacy_options">--%>
			<%--<li><span>Change privacy</span></li>--%>
			<%--<li><span><input type="radio" name="privacy_settings" value="WORLD" <c:if test="${defaultValue == 'WORLD'}"> checked="checked"</c:if>/><label>World</label></span></li>--%>
			<%--<li><span><input type="radio" name="privacy_settings" value="ALL_MEMBERS" <c:if test="${defaultValue == 'ALL_MEMBERS'}"> checked="checked"</c:if>/><label>BB</label></span></li>--%>
			<%--<li><span><input type="radio" name="privacy_settings" value="FRIENDS" <c:if test="${defaultValue == 'FRIENDS'}"> checked="checked"</c:if>/><label>Friends</label></span></li>--%>
			<%--&lt;%&ndash;<li><span><a href="#">Delete video</a></span></li>&ndash;%&gt;--%>
			<%--<li class="last"><span>&nbsp;</span></li>--%>
		<%--</ul>--%>
		<%--</div>--%>
		<%--</s:form>--%>
	</li>
</ul>
