<%--@elvariable id="actionBean" type="com.blackbox.presentation.action.notification.BaseNotificationActionBean"--%>
<%@include file="/WEB-INF/jsp/include/page_header.jspf" %>
<c:choose>
	<c:when test="${actionBean.viewAllType eq 'friend'}">
		  <c:set var="selectedTab" value="0"/>
	</c:when>
	<c:when test="${actionBean.viewAllType eq 'follow'}">
		  <c:set var="selectedTab" value="1"/>
	</c:when>
	<c:when test="${actionBean.viewAllType eq 'wish'}">
		  <c:set var="selectedTab" value="2"/>
	</c:when>
	<c:when test="${actionBean.viewAllType eq 'gift'}">
		  <c:set var="selectedTab" value="3"/>
	</c:when>
	<c:when test="${actionBean.viewAllType eq 'vouch'}">
		  <c:set var="selectedTab" value="4"/>
	</c:when>
	<c:when test="${actionBean.viewAllType eq 'occasion'}">
		  <c:set var="selectedTab" value="5"/>
	</c:when>
</c:choose>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
	<title><fmt:message key="pageTitlePrefix" /> : <fmt:message key="pageTitle.notifications" /></title>
	<link rel="stylesheet" href="${bb:libraryResource('/library/css/messages.css')}" type="text/css" media="screen, projection"/>
	<link rel="stylesheet" href="${bb:libraryResource('/library/css/notifications.css')}" type="text/css" media="screen, projection"/>

	<script type="text/javascript">
		$(function() {
			$("#notifications-tabs").bbTabs({
				selected:${selectedTab}
			});
			var getSelectedNotifications = function($scope) {
				var ids = [];
				var entityIds = [];
				$.each($scope.find(":checked"), function() {
					var vals = $(this).val().split("_");
					ids.push(vals[0]);
					entityIds.push(vals[1]);
				});
				return {ids:ids, entityIds:entityIds};
			};
			var submitSelections = function($container, url) {
				var data = getSelectedNotifications($container);
				if(!data.ids || data.ids.length < 1) return;
				$.post(url, {ids:data.ids.join(","), entityIds:data.entityIds.join(",")}, function() {
					$.each(data.ids, function() {
						$(".notification_" + this).hide().remove();
					});
					if($("#notifications .user").length < 1) {
						$("#notifications").find(".noMoreNotifications").show();
					}
				});
			};
			$("#notifications-tabs").bind('tabLoaded', function(event, tab) {
				bb.notifications.bindEvents($("#notifications"), true);
				bb.notifications.bindEvents($("#notifications-col"));
				$("#selectAll").click(function() {
					$("#notifications").find(":checkbox").attr("checked","checked");
					return false;
				});
				$("#selectNone").click(function() {
					$("#notifications").find(":checkbox").removeAttr("checked");
					return false;
				});
				$(".accept-all-friends-link").click(function() {
					submitSelections($(tab).data("container"), bb.urls.notifications.friendAccept);
				});
				$(".ignore-all-friends-link").click(function() {
					submitSelections($(tab).data("container"), bb.urls.notifications.friendIgnore);
				});
				$(".acknowledge-all-follow-link").click(function() {
					submitSelections($(tab).data("container"), bb.urls.notifications.followAck);
				});
				$(".follow-back-all-link").click(function() {
					submitSelections($(tab).data("container"), bb.urls.notifications.follow);
				});
				$(".accept-all-wish-link").click(function() {
					submitSelections($(tab).data("container"), bb.urls.notifications.wishAccept);
				});
				$(".deny-all-wish-link").click(function() {
					submitSelections($(tab).data("container"), bb.urls.notifications.wishReject);
				});
				$(".accept-all-vouch-link").click(function() {
					submitSelections($(tab).data("container"), bb.urls.notifications.vouchAccept);
				});
				$(".deny-all-vouch-link").click(function() {
					submitSelections($(tab).data("container"), bb.urls.notifications.vouchReject);
				});
				$(".accept-all-gift-link").click(function() {
					submitSelections($(tab).data("container"), bb.urls.notifications.giftAccept);
				});
				$(".deny-all-gift-link").click(function() {
					submitSelections($(tab).data("container"), bb.urls.notifications.giftDeny);
				});
                $(".accept-all-occasion-link").click(function() {
                    submitSelections($(tab).data("container"), bb.urls.notifications.occasionAccept);
                });
                $(".maybe-all-occasion-link").click(function() {
                    submitSelections($(tab).data("container"), bb.urls.notifications.occasionMaybe);    
                });
                $(".decline-all-occasion-link").click(function() {
                    submitSelections($(tab).data("container"), bb.urls.notifications.occasionDeny);
                });

				$("#back-button").click(function() {
					window.location = "<%=request.getHeader("referer")%>";
					return false;
				});
			});
		});
	</script>
</head>
<body>

<div class="container container-top"></div>
<div class="container darken">
	<div class="main-content">
		<h1 class="white">Notifications</h1>

		<div class="rsub-tab-container">
			<div class="rsub-tab-container-right" id="mailbox-tabs">
				<ul id="notifications-tabs" class="sub-tabs">
					<li id="friendsTab" class="ui-tabs-selected">
						<s:link beanclass="com.blackbox.presentation.action.notification.AjaxNotificationActionBean" event="viewAllList"><s:param name="viewAllType" value="friend"/>Friends</s:link>
					</li>
					<li id="followingTab">
						<s:link beanclass="com.blackbox.presentation.action.notification.AjaxNotificationActionBean" event="viewAllList"><s:param name="viewAllType" value="follow"/>Following</s:link>
					</li>
					<li id="wishesTab">
						<s:link beanclass="com.blackbox.presentation.action.notification.AjaxNotificationActionBean" event="viewAllList"><s:param name="viewAllType" value="wish"/>Wishes</s:link>
					</li>
					<li id="giftsTab">
						<s:link beanclass="com.blackbox.presentation.action.notification.AjaxNotificationActionBean" event="viewAllList"><s:param name="viewAllType" value="gift"/>Gifts</s:link>
					</li>
					<li id="vouchesTab">
						<s:link beanclass="com.blackbox.presentation.action.notification.AjaxNotificationActionBean" event="viewAllList"><s:param name="viewAllType" value="vouch"/>Vouches</s:link>
					</li>
                    <li id="occasionsTab">
                        <s:link beanclass="com.blackbox.presentation.action.notification.AjaxNotificationActionBean" event="viewAllList"><s:param name="viewAllType" value="occasion"/>Events</s:link>    
                    </li>
				</ul>

				<ul class="settings-menu" style="margin-right:10px;">
					<li><a href="#" class="smlbtn menu-item" id="back-button"><span>Back</span></a></li>
				</ul>

			</div>
		</div>
		<div class="tab-containers" style="position:relative;">
			<div id="friends_container"></div>
			<div id="following_container"></div>
			<div id="wishes_container"></div>
			<div id="gifts_container"></div>
			<div id="vouches_container"></div>
			<div id="occasions_container"></div>
        </div>
		<div class="widget-bottom">
			<div class="widget-bottom-right"></div>
		</div>
		<br/><%-- for IE sux donkey --%>
	</div>
</div>

</body>
</html>