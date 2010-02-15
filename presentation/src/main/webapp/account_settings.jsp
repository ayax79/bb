<%@include file="/WEB-INF/jsp/include/page_header.jspf" %>
<%--@elvariable id="actionBean" type="com.blackbox.presentation.action.user.AccountSettingsActionBean"--%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
	<title><fmt:message key="pageTitlePrefix"/> : <fmt:message key="pageTitle.accountsettings"/></title>
	<link rel="stylesheet" href="${bb:libraryResource('/library/css/settings.css')}" type="text/css" media="screen, projection"/>
	<%--<link rel="stylesheet" href="${bb:libraryResource('/library/css/register.css')}" type="text/css" media="screen, projection" />--%>

	<script src="${bb:libraryResource('/library/js/jquery.maskedinput-1.2.2.min.js')}" type="text/javascript"></script>
	<script type="text/javascript">

		<c:set var="activeTab" value="0"/>
		<c:if test="${'persona' == s:enumName(actionBean.settingsPage)}">
			<c:set var="activeTab" value="0"/>
		</c:if>
		<c:if test="${'privacy' == s:enumName(actionBean.settingsPage)}">
			<c:set var="activeTab" value="1"/>
		</c:if>
		<c:if test="${'notifications' == s:enumName(actionBean.settingsPage)}">
			<c:set var="activeTab" value="2"/>
		</c:if>
		<c:if test="${'connections' == s:enumName(actionBean.settingsPage)}">
			<c:set var="activeTab" value="3"/>
		</c:if>
		<c:if test="${'account' == s:enumName(actionBean.settingsPage)}">
			<c:set var="activeTab" value="4"/>
		</c:if>
		<c:if test="${'billing' == s:enumName(actionBean.settingsPage)}">
			<c:set var="activeTab" value="5"/>
		</c:if>

		//<![CDATA[
		$(function() {
			$("#settings-tabs-list").bbTabs({selected:${activeTab}, disabled:[2,3,5]});

			$("#settings-tabs-list").bind('tabClicked', function() {
				$("#validationErrors").remove();
			});

		<c:if test="${actionBean.showSaveMessage}">
			$("#settings-tabs-list").bind('tabLoaded', function(event, tab) {
				$(".saveNotification").show();
				$(".saveNotification").effect("pulsate", {}, 400, function() {
					$("#settings-tabs-list").unbind('tabLoaded');
					setTimeout(function() {
						$(".saveNotification").slideUp(400);
						$("#settings-tabs-list").unbind('tabLoaded');
					}, 1000);
				});
			});
		</c:if>
		});
		//]]>
	</script>
</head>

<body>
<div class="container container-top"></div>
<div class="container darken">
	<div class="main-content">
		<h1 class="white">Account Settings</h1>

		<div class="rsub-tab-container">
			<div id="settings-tabs" class="rsub-tab-container-right">
				<ul id="settings-tabs-list" class="sub-tabs">
					<%--Here we should detect which mode we are in - Member, Promoter or Charity and display the appropriate menus--%>
					<c:choose>
						<c:when test="${true}">
							<li id="personaTab"><a href='<s:url beanclass="com.blackbox.presentation.action.user.AccountSettingsActionBean" event="ajaxpersona"  />' title="Persona settings">Persona</a></li>
							<li id="privacyTab"><a href='<s:url beanclass="com.blackbox.presentation.action.user.AccountSettingsActionBean" event="ajaxprivacy" />' title="Privacy settings">Privacy</a></li>
							<li id="notifTab" class="ui-state-disabled"><a href='<s:url beanclass="com.blackbox.presentation.action.user.AccountSettingsActionBean" event="ajaxnotifications" />' title="Notification settings">Notifications</a></li>
							<li id="connectionsTab" class="ui-state-disabled"><a href='<s:url beanclass="com.blackbox.presentation.action.user.AccountSettingsActionBean" event="ajaxintegrations"  />' title="Connections settings">Connections
								<img src="${bb:libraryResource('/library/images/social/facebook-16x16.png')}" alt=""/>&nbsp;
								<img src="${bb:libraryResource('/library/images/social/twitter-16x16.png')}" alt=""/></a>
							</li>
							<li id="accountTab"><a href='<s:url beanclass="com.blackbox.presentation.action.user.AccountSettingsActionBean" event="ajaxaccount" />' title="Account settings">Account</a></li>
							<li id="billingTab" class="ui-state-disabled"><a href='<s:url beanclass="com.blackbox.presentation.action.user.AccountSettingsActionBean" event="ajaxbilling" />' title="Billing settings">Billing</a></li>
						</c:when>
					</c:choose>
				</ul>
			</div>
		</div>

		<div class="span-24 main-col settings">
			<div class="settings-content tabs-container">
				<div class="saveNotification">
					<p>Your changes have been saved</p>
				</div>

				<c:if test="${not empty actionBean.context.validationErrors}">
					<div id="validationErrors" style="margin-bottom:30px;">
						<s:errors/>
					</div>
				</c:if>

				<div id="persona_container"></div>
				<div id="privacy_container"></div>
				<div id="notif_container"></div>
				<div id="connections_container"></div>
				<div id="account_container"></div>
				<div id="billing_container"></div>
			</div>
			<div class="widget-bottom">
				<div class="widget-bottom-right"></div>
			</div>
		</div>
	</div>
</div>
</body>
</html>
