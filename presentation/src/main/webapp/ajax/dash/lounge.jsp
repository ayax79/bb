<%--@elvariable id="actionBean" type="com.blackbox.presentation.action.userplane.UserPlaneActionBean"--%>

<%@page import="com.blackbox.presentation.action.util.PresentationUtil" %>

<%@include file="/WEB-INF/jsp/include/page_header.jspf" %>

<c:set var="strFlashcomServer" value="flashcom.blackboxrepublic.userplane.com"/>
<c:set var="strInstanceID" value="lounge"/>
<c:set var="strSessionGUID" value="${actionBean.currentUser.guid}"/>
<c:set var="strSwfServer" value="swf.userplane.com" />
<c:set var="strApplicationName" value="CommunicationSuite" />
<c:set var="strLocale" value="english" />

<c:set var="domain"><%=PresentationUtil.getProperty("presentation.domain") %></c:set>
<c:choose>
	<c:when test="${domain == 'localhost'}">
		<c:set var="strDomainID" value="chattest.blackboxrepublic.com" />
	</c:when>
	<c:when test="${domain == 'dev.blackboxrepublic.com'}">
		<c:set var="strDomainID" value="dev.blackboxrepublic.com" />
	</c:when>
	<c:when test="${domain == 'www.blackboxrepublic.com'}">
		<c:set var="strDomainID" value="blackboxrepublic.com" />
	</c:when>
	<c:otherwise>
		<c:set var="strDomainID" value="dev.blackboxrepublic.com" />	
	</c:otherwise>
</c:choose>

<head>
	<meta http-equiv=Content-Type content="text/html;  charset=ISO-8859-1">
	<title>Userplane AV Webchat</title>

	<script type="text/javascript">
	<!--
		function csEvent( strEvent, strParameter1, strParameter2 )
		{
			if( strEvent == "InstantCommunicator.StartConversation" )
			{
				var strUserID = strParameter1;
				var bServer = strParameter2;
				// open up an InstantCommunicator window.  For example:
				launchWM( "${strSessionGUID}", strUserID );
			}
			else if( strEvent == "User.ViewProfile" )
			{
				var strUserID = strParameter1;
			}
			else if( strEvent == "User.Block" )
			{
				var strBlockedUserID = strParameter1;
				var bBlocked = strParameter2;
			}
			else if( strEvent == "User.AddFriend" )
			{
				var strFriendUserID = strParameter1;
				var bFriend = strParameter2;
			}
			else if( strEvent == "Chat.Help" )
			{
			}
			else if( strEvent == "User.NoTextEntry" )
			{
			}
			else if( strEvent == "Connection.Success" )
			{
			}
			else if( strEvent == "Connection.Failure" )
			{
				if( strParameter1 == "Session.Timeout" )
				{
					//handle timeout here, both inactivity and session timeouts
				}
				if( strParameter1 == "User.Banned" )
				{
					//handle ban event here
				}
			}
		}

		function launchWM( userID, destinationUserID )
		{
			var popupWindowTest = window.open( "wm.php?strDestinationUserID=" + destinationUserID, "WMWindow_" + replaceAlpha(userID) + "_" + replaceAlpha(destinationUserID), "width=468,height=595,toolbar=0,directories=0,menubar=0,status=0,location=0,scrollbars=0,resizable=1" );
			if( popupWindowTest == null )
			{
				alert( "Your popup blocker stopped an IM window from opening" );
			}
		}

		function replaceAlpha( strIn )
		{
			var strOut = "";
			for( var i = 0 ; i < strIn.length ; i++ )
			{
				var cChar = strIn.charAt(i);
				if( ( cChar >= 'A' && cChar <= 'Z' )
					|| ( cChar >= 'a' && cChar <= 'z' )
					|| ( cChar >= '0' && cChar <= '9' ) )
				{
					strOut += cChar;
				}
				else
				{
					strOut += "_";
				}
			}

			return strOut;
		}
	//-->
	</script>
</head>

<body>

<div id="flashcontent">
	<strong>You need to upgrade your Flash Player by clicking <a href="http://www.macromedia.com/go/getflash/" target="_blank">this link</a>.</strong><br><br><strong>If you see this and have already upgraded we suggest you follow <a href="http://www.adobe.com/cfusion/knowledgebase/index.cfm?id=tn_14157" target="_blank">this link</a> to uninstall Flash and reinstall again.</strong>
</div>

<script type="text/javascript">
	//<![CDATA[
	$(function() {
//		setTimeout(function() {
			var flashvars = {
				strServer: "${strFlashcomServer}",
				strSwfServer: "${strSwfServer}",
				strApplicationName: "${strApplicationName}",
				strDomainID: "${strDomainID}",
				strInstanceID: "${strInstanceID}",
				strSpawnID: "",
				strSessionGUID: "${strSessionGUID}",
				<%--strKey: "${strKey}",--%>
				strLocale: "${strLocale}",
			};
			var params = {
				scale: "noscale",
				menu: "false",
				salign: "LT",
				allowScriptAccess: "always"
			};
			swfobject.embedSWF("http://${strSwfServer}/${strApplicationName}/ch.swf", "flashcontent", "100%", "600", "9", null, flashvars, params, null, null);
//		}, 500);
	});
	//]]>
</script>

</body>