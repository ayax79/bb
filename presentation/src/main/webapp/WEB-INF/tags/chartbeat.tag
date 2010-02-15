<%@ tag language="java" %>
<%@ attribute name="scriptLocation" required="true" %>
<%@ include file="/WEB-INF/jsp/include/taglibs.jspf" %>
<c:choose>
	<c:when test="${scriptLocation == 'head'}">
<script type="text/javascript">var _sf_startpt=(new Date()).getTime();</script>
	</c:when>
	<c:when test="${scriptLocation == 'foot'}">
<script type="text/javascript">

var _sf_async_config={uid:2828,domain:"app.blackboxrepublic.com"};
(function(){
	function loadChartbeat() {
		window._sf_endpt=(new Date()).getTime();
		var e = document.createElement('script');
		e.setAttribute('language', 'javascript');
		e.setAttribute('type', 'text/javascript');
		e.setAttribute('src',
		(("https:" == document.location.protocol) ? "https://s3.amazonaws.com/" : "http://") +
		"static.chartbeat.com/js/chartbeat.js");
		document.body.appendChild(e);
	}
	var oldonload = window.onload;
	window.onload = (typeof window.onload != 'function') ?
	loadChartbeat : function() { oldonload(); loadChartbeat(); };
})();
</script>
	</c:when>
</c:choose>