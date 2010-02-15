<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator" %>
<%--
  ~ Copyright (c) 2009. Blackbox, LLC
  ~ All rights reserved.
--%>

<%--@elvariable id="actionBean" type="able.stripes.AbleActionBean"--%>
<%@ include file="/WEB-INF/jsp/include/taglibs.jspf" %>
<%--<decorator:usePage id="pageContent" />--%>
<fmt:setBundle basename="StripesResources" />
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<link rel="stylesheet" href="/community/library/css/error.css" type="text/css" media="screen, projection" />
</head>
<body>

<div class="errorpage">
	<decorator:body />
	<div style="height:50px;"></div>
</div>
<ui:googleAnalytics scriptLocation="foot"/>
</body>
</html>