<%--@elvariable id="actionBean" type="com.blackbox.presentation.action.search.ExploreActionBean"--%>
<%@include file="/WEB-INF/jsp/include/page_header.jspf" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
	<title>Blackbox</title>
	
	<link rel="stylesheet" href="${bb:libraryResource('/library/css/explorer.css')}" type="text/css" media="screen, projection" />	
    <script src="${bb:libraryResource('/library/js/explorer.js')}" type="text/javascript"></script>

<script type="text/javascript">

    $(document).ready( function() {
        $("#explore-tabs-top").tabs();
    });
    
    $(function() {
	
		<c:if test="${actionBean.firstTime}">
	    $.prettyPhoto.open('http://cdn.episodic.com/player/EpisodicPlayer.swf?width=480&height=360&config=http%3A%2F%2Fcdn.episodic.com%2Fshows%2Fob0pj9jq17up%2Fob1pgagvco3u%2Fconfig.xml');
	    </c:if>
	
	    $("#intro").click(function() {
	       $.prettyPhoto.open('http://cdn.episodic.com/player/EpisodicPlayer.swf?width=480&height=360&config=http%3A%2F%2Fcdn.episodic.com%2Fshows%2Fob0pj9jq17up%2Fob1pgagvco3u%2Fconfig.xml');
	    })

	});

</script>

</head>
<body>
<input type="hidden" name="targetSearch" id="targetSearch" value="<%= request.getParameter("_eventName") %>">
<div class="explore-page">
<div class="container container-top"></div>
<div class="container darken">
	<div class="main-content">
		<h1 class="white">Explore</h1>

			<div id="explore-tabs-top" class="rsub-tab-container">
				
				<ul class="rsub-tab-container-right sub-tabs">

                    <li><s:link beanclass="com.blackbox.presentation.action.search.AjaxExploreActionBean" event="people" title="tttt" >Members</s:link></li>
                    <li><s:link beanclass="com.blackbox.presentation.action.search.AjaxExploreActionBean" event="events" title="tttt">Events</s:link></li>
					<%--<li><a href="<s:url value='/ajax/explore/gifts.jspf' />" title="tttt">Gifts</a></li>--%>
					<%--<li><a href="<s:url value='/ajax/explore/camps.jspf' />" title="tttt">Camps</a></li>--%>
				</ul>
			
			</div>
			
			<div id="tttt" class="tabs-container explore-content clearfix">
	                <%-- dynamic content inserts here --%>
			</div>


			<div class="clear"></div>



		<br/><%-- for IE sux donkey --%>
	</div>
</div>
<div class="container container-bottom"></div>
</div>

</body>
</html>

</body>

