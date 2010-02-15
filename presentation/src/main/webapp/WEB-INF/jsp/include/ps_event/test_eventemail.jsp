<%@include file="/WEB-INF/jsp/include/page_header.jspf" %>
<head>
</head>
<body>
    <s:form beanclass="com.blackbox.presentation.action.psevent.PSEventSendEmailActionBean">
        <input name="_eventName" value="handleForm"/>
        <s:submit name="submit" value="submit"/>
    </s:form>
    <c:out value="contacts"/>
</body>

