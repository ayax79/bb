<%@ page import="com.blackbox.presentation.action.BaseBlackBoxActionBean" %>
<fmt:setBundle basename="StripesResources"/>
<%
    if (request.getParameter("show") == null) {
        response.sendRedirect("error-404.jsp?show=true");
        return;
    }

%>
<a href="/community/">
    <img src="/community/library/images/errors/error-404.jpg" alt="The page you were looking for was not found."/>
</a>
