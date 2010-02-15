<%@ tag import="java.util.ArrayList" %>
<%@ tag import="java.util.List" %>
<%@ tag language="java" %>
<%@include file="/WEB-INF/jsp/include/taglibs.jspf" %>

<div id="mini-bio" class="clearfix">
	<ui:profileImage guid="${actionBean.user.guid}" showUserType="true" linkToProfile="true" showMiniProfile="true" size="medium_large"/>
	<ui:tinySliders user="${msgUser}"/>
	<h3>Hey, I&#8217;m ${bb:displayName(actionBean.user)}</h3>
	<c:if test="${not empty param.lookingFor}">
		<p><strong>Right now I&#8217;m looking for
			<c:forEach var="thang" items="${param.lookingFor}" varStatus="index">
				${thang}
				<c:choose>
					<c:when test="${index.last}">.</c:when>
					<c:when test="${index.count == (f:length(param.lookingFor) - 1)}">, and </c:when>
					<c:when test="${index.count != (f:length(param.lookingFor) - 1) && !index.last}">, </c:when>
				</c:choose>
			</c:forEach>
		</strong></p>
	</c:if>
</div>