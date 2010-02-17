<%@ tag language="java" %>
<%@ include file="/WEB-INF/jsp/include/taglibs.jspf" %>

<%@ attribute name="msgObject" required="false" type="com.blackbox.foundation.BBPersistentObject" %>

<a href="${msgObject.location}">
	<c:choose>
		<c:when test="${empty msgObject.location}">
			<img src="${bb:libraryResource('/library/images/spacer.gif')}" alt="loading image..." class="lightbox"/>
		</c:when>
		<c:otherwise>
			<img src="${msgObject.thumbnailLocation}" alt="" class="lightbox"/>
		</c:otherwise>
	</c:choose>
</a>