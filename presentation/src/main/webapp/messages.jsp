<%--@elvariable id="actionBean" type="com.blackbox.presentation.action.activity.InboxActionBean"--%>
<%@include file="/WEB-INF/jsp/include/page_header.jspf" %>
<c:set var="newLine" value="
"/>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
	<title><fmt:message key="pageTitlePrefix" /> : <fmt:message key="pageTitle.messages" /></title>

	<link rel="stylesheet" href="${bb:libraryResource('/library/css/messages.css')}" type="text/css" media="screen, projection"/>
	<link rel="stylesheet" href="${bb:libraryResource('/library/css/notifications.css')}" type="text/css" media="screen, projection"/>
	<script src="${bb:libraryResource('/library/js/mailbox.js')}" type="text/javascript"></script>

	<script type="text/javascript">
		//<![CDATA[
		$(function() {
			bb.pageInit.bindSliders($("#mailbox-sliders"));
			$("#mailbox-slider-search").click(function() {
				$(this).parent().find(".slider-search-sliders").toggle();
				return false;
			});
			$(".sub-tabs").bind('tabLoaded', function(event, tab) {
				var type = ($(tab).data("id"));
				var mailbox = new Mailbox(type, $(tab).data("container"));
			});
			$(".sub-tabs").bbTabs({skipLoadOnInit:true});
		});
		$(function() {
	
			<c:if test="${actionBean.firstTime}">
		    $.prettyPhoto.open('http://cdn.episodic.com/player/EpisodicPlayer.swf?width=480&height=360&config=http%3A%2F%2Fcdn.episodic.com%2Fshows%2Fob0pj9jq17up%2Fob1inuj0zy87%2Fconfig.xml');
		    </c:if>
		
		    $("#intro").click(function() {
		       $.prettyPhoto.open('http://cdn.episodic.com/player/EpisodicPlayer.swf?width=480&height=360&config=http%3A%2F%2Fcdn.episodic.com%2Fshows%2Fob0pj9jq17up%2Fob1inuj0zy87%2Fconfig.xml');
		    })

		});
		//]]>
	</script>
</head>
<body>

<div class="container container-top"></div>
<div class="container darken">
	<div class="main-content">
		<h1 class="white">Messages</h1>

		<div class="rsub-tab-container">
			<div class="rsub-tab-container-right" id="mailbox-tabs">
				<%--<div class="slider-search-menu">--%>
					<%--<a href="#" id="mailbox-slider-search"><span>Slider search</span></a>--%>

					<%--<div class="slider-search-sliders">--%>
						<%--<%@include file="/ajax/messaging/slider-search.jspf"%>--%>
					<%--</div>--%>
				<%--</div>--%>
				<%--<span class="mb-search"><span class="mb-search-r"><input type="text"/></span></span>--%>
				<ul class="sub-tabs">
					<li id="inboxTab">
						<s:link beanclass="com.blackbox.presentation.action.activity.InboxActionBean" event="load" title="Inbox">
							<s:param name="mailboxRequest.folder" value="INBOX_FOLDER"/>
							<s:param name="mailboxRequest.filter" value="ALL"/>
							<s:param name="mailboxRequest.readState" value="EITHER"/>
							Inbox
						</s:link>
						<%--<s:link beanclass="com.blackbox.presentation.action.activity.InboxActionBean" event="read" title="Inbox">--%>
							<%--<s:param name="parentGuid" value="ddeafe980f3843c816342c6a6016b7dfea543728"/>--%>
							<%--Inbox--%>
						<%--</s:link>--%>
					</li>
					<li id="sentTab">
						<s:link beanclass="com.blackbox.presentation.action.activity.InboxActionBean" event="load" title="Sent">
							<s:param name="mailboxRequest.folder" value="SENT_FOLDER"/>
							<s:param name="mailboxRequest.filter" value="ALL"/>
							<s:param name="mailboxRequest.readState" value="EITHER"/>
							Sent
						</s:link>
					</li>

					<li id="archiveTab">
						<s:link beanclass="com.blackbox.presentation.action.activity.InboxActionBean" event="load" title="Archive">
							<s:param name="mailboxRequest.folder" value="ARCHIVED_FOLDER"/>
							<s:param name="mailboxRequest.filter" value="ALL"/>
							<s:param name="mailboxRequest.readState" value="EITHER"/>
							Archive
						</s:link>
					</li>

					<%--<li id="draftsTab">--%>
						<%--<s:link beanclass="com.blackbox.presentation.action.activity.InboxActionBean" event="load" title="Drafts">--%>
							<%--<s:param name="mailboxRequest.folder" value="DRAFTS_FOLDER"/>--%>
							<%--<s:param name="mailboxRequest.filter" value="ALL"/>--%>
							<%--<s:param name="mailboxRequest.readState" value="EITHER"/>--%>
							<%--Drafts--%>
						<%--</s:link>--%>
					<%--</li>--%>

				</ul>

				<div class="clear"></div>
			</div>
		</div>
		<div class="clear"></div>
		<div id="inbox-2col" class="two-column-bg">
			<div class="bb-left main-col messages">
				<div class="messages-container" id="inbox_container">
					<jsp:include page="/ajax/messaging/mailbox.jspf" flush="true"/>
				</div>
				<div class="messages-container" id="sent_container"></div>
				<div class="messages-container" id="archive_container"></div>
				<div class="messages-container" id="drafts_container"></div>
				<div class="messages-container" id="read_container"></div>
				<br/>
			</div>
			<div class="bb-right right-col last messages">
				<%@include file="/WEB-INF/jsp/include/notifications-column.jspf" %>
			</div>
			<div class="clear"></div>

		</div>
		<div class="widget-bottom">
			<div class="widget-bottom-right"></div>
		</div>
		<br/><%-- for IE sux donkey --%>
	</div>
</div>
<div class="container container-bottom"></div>

</body>
</html>
