<%@ page import="com.blackbox.presentation.action.BaseBlackBoxActionBean" %>
<fmt:setBundle basename="StripesResources"/>
<%
    if (request.getParameter("show") == null) {
        response.sendRedirect("error-500.jsp?show=true");
        return;
    }

%>
<a href="/community/">
    <img src="/community/library/images/errors/error-500.jpg"
         alt="There was a server error. Your request could not be processed."/>
</a>
