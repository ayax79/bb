<%@ include file="/WEB-INF/jsp/include/page_header.jspf" %>
<%--
  ~ Copyright (c) 2009. Blackbox, LLC
  ~ All rights reserved.
  --%>
<html>
<head>

</head>
<body>

<%--@elvariable id="friends" type="java.util.List"--%>
<c:forEach var="current" items="${friends}">
    ${current}<br/>
</c:forEach>

</body>
</html>

