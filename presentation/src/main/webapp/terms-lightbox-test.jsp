<%@ include file="/WEB-INF/jsp/include/taglibs.jspf" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<title><fmt:message key="pageTitlePrefix" /></title>

<link rel="stylesheet" href="${bb:libraryResource('/library/css/prettyPhoto.css')}" type="text/css"/>
<script src="${bb:libraryResource('/library/js/jquery-1.4.1.min.js')}" type="text/javascript"></script>
<script src="${bb:libraryResource('/library/js/jquery.prettyPhoto.js')}" type="text/javascript"></script>

<script type="text/javascript">

$(document).ready(function() {

	$("a[rel^='prettyPhoto']").prettyPhoto({
		animationSpeed: 'normal', /* fast/slow/normal */
		padding: 40, /* padding for each side of the picture */
		showTitle: false, /* true/false */
		allowresize: false, /* true/false */
		counter_separator_label: '/', /* The separator for the gallery counter 1 "of" 2 */
		theme: 'light_rounded', /* light_rounded / dark_rounded / light_square / dark_square */
		hideflash: true, /* Hides all the flash object on a page, set to TRUE if flash appears over prettyPhoto */
		modal: false, /* If set to true, only the close button will close the window */
		changepicturecallback: function(){}, /* Called everytime an item is shown/changed */
		callback: function(){} /* Called when prettyPhoto is closed */
	});

});
</script>

</head>

<body>
	<p><a href="terms.jsp?iframe=true&amp;width=700&amp;height=400" rel="prettyPhoto[iframes]">
	TERMS AND CONDITIONS</a></p>
</body>
</html>
