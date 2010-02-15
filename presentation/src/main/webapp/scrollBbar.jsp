<%-- 
    Document   : scrollBbar
    Created on : 2009-9-15, 15:34:51
    Author     : Iad
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" trimDirectiveWhitespaces="true" %>
<%@include file="/WEB-INF/jsp/include/taglibs.jspf"%>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
	<title>jQuery UI Slider</title>
	<style>
		#slider {
			background-color: white;
			height: 5px;
			width: 400px;
			position: relative;
		}

		#slider a {
			cursor: default;
			background-image: url(${bb:libraryResource('/library/images/controls/login_ron.png')});
			height: 12px;
			width: 12px;
			margin-left: -5px;
			top: -5px;
			position: absolute;
			z-index: 2;
			border: 1px solid #D3D3D3;
		}
	</style>
</head>
<body>
<div id="sliderContent" style="margin:200px;">
	<div id="slider"></div>
</div>
<script type="text/javascript">
	$(function() {
		$("#slider").slider({slide:function(e, ui) {
		<%--console.log(ui.value)--%>
		}});
	});
</script>
</body>
</html>
