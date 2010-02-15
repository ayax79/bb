<%@ tag language="java" %>
<%@ attribute name="id" required="false" %>
<%@ attribute name="size" required="true" %>
<%@ attribute name="color" required="true" %>
<%@ attribute name="url" required="true" %>
<%@ attribute name="alt" required="false" %>
<%@include file="/WEB-INF/jsp/include/taglibs.jspf" %>
<span <c:if test="${not empty id}"> id="${id}" </c:if> class="roundedimage roundedimage-${color} roundedimage-${size}"><img class="roundedimage-image" alt="${alt}" src="${url}" /><span class="roundedimage-corners"><img class="roundedimage-cornersprite" alt="" src="${bb:libraryResource('/library/images/containers/rounded-image-corners.png')}" /></span></span>