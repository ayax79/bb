<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator" %>
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

	<link rel="stylesheet" href="${bb:libraryResource('/library/css/main.css')}" type="text/css" media="screen, projection" />
	<link rel="stylesheet" href="${bb:libraryResource('/library/css/print.css')}" type="text/css" media="print" />
	<!--[if IE]><link rel="stylesheet" href="${bb:libraryResource('/library/css/ie.css')}" type="text/css" media="screen, projection"/><![endif]-->
	<link rel="stylesheet" href="${bb:libraryResource('/library/css/jquery.autocomplete.css')}" type="text/css" media="screen, projection" />
	<link rel="stylesheet" href="${bb:libraryResource('/library/css/publisher.css')}" type="text/css" media="screen, projection" />
	<link rel="stylesheet" href="${bb:libraryResource('/library/css/prettyPhoto.css')}" type="text/css"/>

	<script src="${bb:libraryResource('/library/js/jquery-1.4.1.min.js')}" type="text/javascript"></script>
	<script src="${bb:libraryResource('/library/js/jquery-ui-1.7.2.custom.min.js')}" type="text/javascript"></script>
	<script src="${bb:libraryResource('/library/js/json_parse.js')}" type="text/javascript"></script>
	<script src="${bb:libraryResource('/library/js/autoresize.jquery.js')}" type="text/javascript"></script>
	<script src="${bb:libraryResource('/library/js/tooltip.js')}" type="text/javascript"></script>
	<script src="${bb:libraryResource('/library/js/jquery.autocomplete.js')}" type="text/javascript"></script>
	<script src="${bb:libraryResource('/library/js/jquery.tokeninput.js')}" type="text/javascript"></script>
	<script src="${bb:libraryResource('/library/js/jquery.bbdialog.js')}" type="text/javascript"></script>
	<script src="${bb:libraryResource('/library/js/jquery.validate.pack.js')}" type="text/javascript"></script>
	<script src="${bb:libraryResource('/library/js/jquery.stripesValidation.js')}" type="text/javascript"></script>
	<script src="${bb:libraryResource('/library/js/stripes.jquery.validation.js')}" type="text/javascript"></script>
	<script src="${bb:libraryResource('/library/js/flowplayer-3.1.1.min.js')}" type="text/javascript"></script>
	<script src="${bb:libraryResource('/library/js/jquery.oembed.min.js')}" type="text/javascript"></script>
	<script src="${bb:libraryResource('/library/js/jquery.qtip-1.0.0-rc3.min.js')}" type="text/javascript"></script>
    <script src="${bb:libraryResource('/library/js/jquery.timeago.js')}" type="text/javascript"></script>
    <script src="${bb:libraryResource('/library/js/jquery.prettyPhoto.js')}" type="text/javascript"></script>

    <script type="text/javascript">
    //<![CDATA[
	CurrentUser = {
		guid: '${actionBean.currentUser.guid}',
		name: '${actionBean.currentUser.name}',
		userName: '${actionBean.currentUser.username}'
	};
	DEFAULT_PROFILE_IMAGE = "${bb:libraryResource('/library/images/icons/profile_person.png')}";
	DEFAULT_VIDEO_IMAGE = "${bb:libraryResource('/library/images/icons/film-strip.png')}";

	$(function() {
		$("a[rel=prettyPhoto]").prettyPhoto({
			animationSpeed: 'normal', /* fast/slow/normal */
			padding: 20, /* padding for each side of the picture */
			showTitle: false, /* true/false */
			allowresize: false, /* true/false */
			counter_separator_label: '/', /* The separator for the gallery counter 1 "of" 2 */
			theme: 'dark_rounded', /* light_rounded / dark_rounded / light_square / dark_square */
			hideflash: true, /* Hides all the flash object on a page, set to TRUE if flash appears over prettyPhoto */
			modal: false, /* If set to true, only the close button will close the window */
			changepicturecallback: function(){}, /* Called everytime an item is shown/changed */
			callback: function(){} /* Called when prettyPhoto is closed */
		});
	});
	
    //]]>
    </script>
	<%@ include file="/WEB-INF/jsp/include/blackbox_js.jspf" %>
	<script src="${bb:libraryResource('/library/js/core.js')}" type="text/javascript"></script>
	<script src="${bb:libraryResource('/library/js/bb.plugins.js')}" type="text/javascript"></script>
	<script src="${bb:libraryResource('/library/js/video.js')}" type="text/javascript"></script>
	<script src="${bb:libraryResource('/library/js/jquery.miniPersona.js')}" type="text/javascript"></script>
	<decorator:head />

</head>
<body>

<noscript>
	<div id="no-javascript">
		<p>Hi there! This is a friendly alert from the developers at Blackbox. You either have JavaScript turned off in your web browser settings, or you are using a browser that doesn't support JavaScript. You need to enable JavaScript in order to use Blackbox Republic, because most of our fancy features require it.</p>
		<p>Need help enabling JavaScript in your browser? Here's some <a href="http://www.google.com/support/websearch/bin/answer.py?hl=en&answer=23852"> helpful instructions</a>.</p>
	</div>
</noscript>

<div class="nf">
	<%@ include file="/WEB-INF/jsp/include/header.jspf" %>
	<decorator:body />
	<div style="height:50px;"></div>
</div>

<%@ include file="/WEB-INF/jsp/include/footer.jspf" %>

<ui:chartbeat scriptLocation="foot"/>
<ui:googleAnalytics scriptLocation="foot"/>
</body>
</html>