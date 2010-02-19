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

	<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
	<title><fmt:message key="pageTitlePrefix" /> : <fmt:message key="pageTitle.register" /></title>

	<link rel="stylesheet" href="${bb:libraryResource('/library/css/main.css')}" type="text/css" media="screen, projection" />
	<link rel="stylesheet" href="${bb:libraryResource('/library/css/register.css')}" type="text/css" media="screen, projection" />
	<link rel="stylesheet" href="${bb:libraryResource('/library/css/explorer.css')}" type="text/css" media="screen, projection" />
	<link rel="stylesheet" href="${bb:libraryResource('/library/uploadify/uploadify.css')}" type="text/css"/>
	<!--[if IE]><link rel="stylesheet" href="${bb:libraryResource('/library/css/ie.css')}" type="text/css" media="screen, projection"/><![endif]-->
	<script src="${bb:libraryResource('/library/js/jquery-1.4.2.min.js')}" type="text/javascript"></script>
	<script src="${bb:libraryResource('/library/js/jquery-ui-1.7.2.custom.min.js')}" type="text/javascript"></script>
	<script src="${bb:libraryResource('/library/js/jquery.validate.pack.js')}" type="text/javascript"></script>
	<script src="${bb:libraryResource('/library/js/jquery.stripesValidation.js')}" type="text/javascript"></script>
	<script src="${bb:libraryResource('/library/js/stripes.jquery.validation.js')}" type="text/javascript"></script>
	<script src="${bb:libraryResource('/library/js/jquery.maskedinput-1.2.2.min.js')}" type="text/javascript" ></script>
	<script src="${bb:libraryResource('/library/js/core.js')}" type="text/javascript" ></script>
	<script src="${bb:libraryResource('/library/js/registration.js')}" type="text/javascript" ></script>
	<script src="${bb:libraryResource('/library/js/jquery.bbdialog.js')}" type="text/javascript" ></script>
	<script src="${bb:libraryResource("/library/uploadify/jquery.uploadify.js")}" type="text/javascript"></script>
	<script src="${bb:libraryResource("/library/uploadify/swfobject.js")}" type="text/javascript"></script>
    <ui:chartbeat scriptLocation="head"/>
	<decorator:head />
	
</head>

<body class="register">
<!--[if lt IE 7]>
	<div id="ie6">
		<p><img src='http://www.ie6nomore.com/files/theme/ie6nomore-warning.jpg' alt='Warning!' style="float: left; margin-bottom: 250px;margin-right: 15px;"/>Hi there! This is a friendly alert from the developers at Blackbox. You appear to be using an outdated version of Internet Explorer, probably version 6 or below. We strongly suggest upgrading your web browser to one of the more modern browsers listed below. This will make your browsing experience much more enjoyable, both at Blackbox and elsewhere on the web.</p>
		<p style="padding-left: 110px;">
			<a href='http://www.firefox.com' target='_blank'><img src='http://www.ie6nomore.com/files/theme/ie6nomore-firefox.jpg' style='border: none;' alt='Get Firefox 3.5'/></a>
        	<a href='http://www.browserforthebetter.com/download.html' target='_blank'><img src='http://www.ie6nomore.com/files/theme/ie6nomore-ie8.jpg' style='border: none;' alt='Get Internet Explorer 8'/></a>
       		<a href='http://www.apple.com/safari/download/' target='_blank'><img src='http://www.ie6nomore.com/files/theme/ie6nomore-safari.jpg' style='border: none;' alt='Get Safari 4'/></a>
        	<a href='http://www.google.com/chrome' target='_blank'><img src='http://www.ie6nomore.com/files/theme/ie6nomore-chrome.jpg' style='border: none;' alt='Get Google Chrome'/></a>
        </p>
	</div>
<![endif]-->
<div class="bbform-container">
	<div class="bbform-header">
		<h1 class="bbform-h1">Join the Republic <span>(or <s:link beanclass="com.blackbox.presentation.action.security.LoginActionBean">login</s:link> if you&#8217;re already a member)</span></h1>
	</div>
	<div class="bbform-body register clearfix">
		<decorator:body />
	</div>
	<div class="bbform-footer"></div>
</div>
<ui:chartbeat scriptLocation="foot"/>
<ui:googleAnalytics scriptLocation="foot"/>
</body>
</html>