<%@ tag language="java" %>
<%@ include file="/WEB-INF/jsp/include/taglibs.jspf" %>

<%@ attribute name="color" required="true" %>

<li class="social-status-pill ${color}"><span><jsp:doBody/></span></li>