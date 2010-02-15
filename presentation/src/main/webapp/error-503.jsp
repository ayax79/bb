<%@ page import="com.blackbox.presentation.action.BaseBlackBoxActionBean" %>
<fmt:setBundle basename="StripesResources"/>
<%
    if (request.getParameter("show") == null) {
        response.sendRedirect("error-503.jsp?show=true");
        return;
    }

%>
<a href="/community/">
    <img src="/community/library/images/errors/error-503.jpg" alt="The system is temporarily unavailable."/>
</a>
