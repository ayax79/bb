<%@ tag language="java" %>
<%@ attribute name="id" required="false" %>
<%@ attribute name="typeClass" required="true" %>
<%@ attribute name="styleClass" required="true" %>
<%@ attribute name="href" required="true" %>
<%@ attribute name="title" required="false" %>
<li id="${id}" class="${typeClass} ${styleClass}">
	<a href="${href}" title="${title}">
	   <jsp:doBody/>
	</a>
</li>