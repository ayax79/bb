<%@ page import="com.blackbox.presentation.action.BaseBlackBoxActionBean" %>
<%--@elvariable id="actionBean" type="com.blackbox.presentation.action.BaseBlackBoxActionBean"--%>
<%@include file="/WEB-INF/jsp/include/page_header.jspf" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title><fmt:message key="pageTitlePrefix" /> : <fmt:message key="pageTitle.home" /></title>

	<link rel="stylesheet" href="${bb:libraryResource('/library/css/dashboard.css')}" type="text/css" media="screen, projection" />
	<link rel="stylesheet" href="${bb:libraryResource('/library/css/jquery.lightbox-0.5.css')}" type="text/css" />

	<script src="${bb:libraryResource('/library/js/publisher.js')}" type="text/javascript"></script>
	<script src="${bb:libraryResource('/library/js/swfobject.js')}" type="text/javascript"></script>
	<script src="${bb:libraryResource('/library/js/jquery.lightbox-0.5.js')}" type="text/javascript"></script>
	<script src="${bb:libraryResource('/library/js/jquery.address-1.0.js')}" type="text/javascript"></script>
	<script src="${bb:libraryResource('/library/js/meBox.js')}" type="text/javascript"></script>

	<script type="text/javascript">
	//<![CDATA[

		$(function() {

			$("#stream-tabs").bbTabs({
				disableLoad:true,
				skipLoadOnInit:true
			});

			//Reset filters
			$("#stream-tabs").bind('tabClicked', function(event, tab) {
				$.address.value($(tab).data("url"));
				Activity.Stream.unregisterStreams();
				return false;
			});

			$.address.init(function(event) {
				//window.location.hash = $("#stream-tabs").bbTabs('getSelectedTabId');
			}).change(function(event) {
				$.bbDialog.closeAll();
				var selection = $('a[href=' + event.value + ']').parent();
				var hash = event.value;
				$("#stream-tabs").bbTabs('setTab', selection.data("id"));
				var tp = document.title.split(" : ");
				tp[2] = $("#stream-tabs").bbTabs('getSelectedTab').data("label");
				document.title = tp.join(" : ");
            });


			function updateChatCount() {
				$.getJSON("http://api.chartbeat.com/recent/?host=app.blackboxrepublic.com&path=/community/dashboard&apikey=1e3163125f7cea893e8e62c7c55d84e1&jsonp=?", function(data) {
					var count = 0;
					var uuids = [];
					for(var i = 0; i < data.length; i++) {
						if(data[i].i.indexOf("Lounge") != -1) {
							if($.inArray(data[i].u, uuids) == -1) {
								count++;
								uuids.push(data[i].u)
							}
						}
					}
					if(count > 0) {
						$("#lounge-count").show().find("span").text(count);
					} else {
						$("#lounge-count").hide();
					}
				});
				setTimeout(updateChatCount, 5000);
			}
//			updateChatCount();
		});


	//]]>
	</script>
</head>
<body>
<%--
TODO: Implement BB Admin messaging
<ui:roundedBox id="messages" className="rounded-notify container">
	<a href="#" class="msg-close"></a><strong>Oooo a BB update!</strong> The rockstar BB team just posted an awesome new blog post, it's got some juicy details about what you can expect in the next update.
</ui:roundedBox>
--%>

<script type="text/javascript" src="http://static.ak.connect.facebook.com/js/api_lib/v0.4/FeatureLoader.js.php"></script>

<div class="container container-top"></div>
<div class="container darken">
	<div class="main-content">
		<div id="activity">
			<ul id="stream-tabs" class="tabs clearfix">
				<li id="sceneTab">
					<s:link beanclass="com.blackbox.presentation.action.activity.SceneActivityActionBean" >
						<span class="tabLabel">The Scene</span>
						<span class="tab-description">What's going down</span>
					</s:link>
				</li>
				<li id="meTab">
					<s:link beanclass="com.blackbox.presentation.action.user.MePageActionBean">
						<span class="tabLabel">Me Box</span><%--<span class="notify-number"><span class="count">25</span></span>--%>
						<span class="tab-description">All things you</span>
					</s:link>
				</li>
				<li id="loungeTab">
					<s:link beanclass="com.blackbox.presentation.action.userplane.UserPlaneActionBean">
						<span id="lounge-count" class="nav-counter"><span class="count"></span></span>
						<span class="tabLabel">The Lounge</span><%--<span class="notify-number"><span class="count">25</span></span>--%>
						<span class="tab-description">Chat with folks</span>
					</s:link>
				</li>
				<%--<li id="giftsTab">--%>
					<%--<s:link beanclass="com.blackbox.presentation.action.activity.GiftActivityActionBean" >--%>
						<%--<span class="tabLabel">Gifts</span>--%>
						<%--<span class="tab-description">Gift Activity</span>--%>
					<%--</s:link>--%>
				<%--</li>--%>
				<%--<li id="legendsTab">--%>
					<%--<s:link beanclass="com.blackbox.presentation.action.activity.TrainRockActionBean				<span class="tabLabel">Trainwreck/Rockstar</span>--%>
						<%--<span class="tab-description">What's hot and what's not</span>--%>
					<%--</s:link>--%>
				<%--</li>--%>

			</ul>
			<div class="clear"></div>
			<div class="tab-containers" style="position:relative;">

				<div id="scene_container" class="stream-container two-column-bg"></div>

				<div id="me_container" class="stream-container two-column-bg"></div>

				<div id="lounge_container" class="stream-container"></div>

				<%--<div id="gifts_container" class="stream-container two-column-bg"></div>--%>

				<%--| ROCKSTAR/TRAINWRECK ACTIVITY STREAM |--%>
				<%--<div id="legends_container" class="stream-container two-column-bg"></div>--%>

				<div class="widget-bottom">
					<div class="widget-bottom-right"></div>
				</div>
			</div>


		</div>
	</div>
</div>
<%@include file="/WEB-INF/jsp/include/stream-templates.jspf" %>

<div class="container container-bottom"></div>

</body>
</html>
