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
	<ui:chartbeat scriptLocation="head"/>
	<link rel="SHORTCUT ICON" href="/favicon.ico"  type="image/x-icon" />
	<link rel="icon" type="image/png" href="${bb:libraryResource('/library/images/branding/favicon_32.png')}" />

	<title><decorator:title default="Blackbox" /></title>

	<%-- Framework CSS --%>
	<link rel="stylesheet" href="${bb:libraryResource('/library/css/main.css')}" type="text/css" media="screen, projection" />
 	<link rel="stylesheet" href="${bb:libraryResource('/library/css/print.css')}" type="text/css" media="print" />
	<!--[if IE]><link rel="stylesheet" href="${bb:libraryResource('/library/css/ie.css')}" type="text/css" media="screen, projection"/><![endif]-->
	<link rel="stylesheet" href="${bb:libraryResource('/library/css/jquery.autocomplete.css')}" type="text/css" media="screen, projection" />
	<link rel="stylesheet" href="${bb:libraryResource('/library/css/publisher.css')}" type="text/css" media="screen, projection" />
	<%-- Libraries --%>
	<script src="${bb:libraryResource('/library/js/jquery-1.4.2.min.js')}" type="text/javascript"></script>
	<script src="${bb:libraryResource('/library/js/jquery-ui-1.7.2.custom.min.js')}" type="text/javascript"></script>
	<script src="${bb:libraryResource('/library/js/json_parse.js')}" type="text/javascript"></script>

	<script src="${bb:libraryResource('/library/js/autoresize.jquery.js')}" type="text/javascript"></script>
	<script src="${bb:libraryResource('/library/js/tooltip.js')}" type="text/javascript"></script>
	<script src="${bb:libraryResource('/library/js/jquery.autocomplete.js')}" type="text/javascript"></script>
	<script src="${bb:libraryResource('/library/js/jquery.bbdialog.js')}" type="text/javascript"></script>
	<script src="${bb:libraryResource('/library/js/jquery.validate.pack.js')}" type="text/javascript"></script>
	<script src="${bb:libraryResource('/library/js/jquery.stripesValidation.js')}" type="text/javascript"></script>
	<script src="${bb:libraryResource('/library/js/stripes.jquery.validation.js')}" type="text/javascript"></script>
	<%-- Our JS Libraries --%>
	<%@ include file="/WEB-INF/jsp/include/blackbox_js.jspf" %>
	<script src="${bb:libraryResource('/library/js/core.js')}" type="text/javascript"></script>
	<script src="${bb:libraryResource('/library/js/bb.plugins.js')}" type="text/javascript"></script>	
	<script src="${bb:libraryResource('/library/js/jquery.miniPersona.js')}" type="text/javascript"></script>
	<decorator:head />
</head>
<body>

<div class="nf">
	<decorator:body />
	<div style="height:50px;"></div>
</div>
<ui:chartbeat scriptLocation="foot"/>
<ui:googleAnalytics scriptLocation="foot"/>
</body>
</html>