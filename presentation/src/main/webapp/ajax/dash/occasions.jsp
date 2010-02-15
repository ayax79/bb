<%@include file="/WEB-INF/jsp/include/page_header.jspf" %>
<%--@elvariable id="actionBean" type="com.blackbox.presentation.action.occasion.OccasionsActionBean"--%>

<c:forEach var="occasion" items="${actionBean.occasions}">
    <c:set var="name" value="${occasion.name}" />
    <c:set var="description" value="${occasion.description}" />
    <c:set var="address1" value="${occasion.location.address1} " />
    <c:set var="address2" value="${occasion.location.address2} " />
    <c:set var="city" value="${occasion.location.city} " />
    <c:set var="state" value="${occasion.location.state} " />
    <c:set var="country" value="${occasion.location.country} " />
    <c:set var="zipCode" value="${occasion.location.zipCode} " />
    <c:set var="occasionLevel" value="${occasion.occasionLevel}" />
    <c:set var="suppressComments" value="true"/>
	<%@include file="/WEB-INF/jsp/include/templates/event.jspf" %>
</c:forEach>