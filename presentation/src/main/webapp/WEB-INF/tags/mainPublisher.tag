<%@ tag language="java" %>
<%@ attribute name="inputId" required="true" %>
<%@ attribute name="defaultText" required="true" %>

<%@ include file="/WEB-INF/jsp/include/taglibs.jspf" %>

<div class="mainpub-container">
	<div style="float:left;margin-right:6px;position:relative;">
		<ui:profileImage guid="${actionBean.currentUser.guid}" linkToProfile="false" showMiniProfile="false" size="small" color="grey" />
	</div>
	<div style="float:left;position:relative;">
		<span class="bubble-arrow"></span>
		<textarea id="${inputId}" name="messageBody" class="dv main-publisher-src publisher-msg-source input-default">${defaultText}</textarea>
	</div>
	<div class="clear"></div>
</div>