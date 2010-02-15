<%@ tag language="java" %>
<%@ include file="/WEB-INF/jsp/include/taglibs.jspf" %>
<%--@elvariable id="actionBean" type="com.blackbox.presentation.action.persona.PersonaActionBean"--%>

<pre>


total images       : ${actionBean.totalImages}
total albums       : ${f:length(actionBean.mediaLibList)}
album guid         : ${actionBean.albumGuid}
current album guid : ${actionBean.currentAlbum.guid}
current album total: ${f:length(actionBean.currentAlbum.media)}

</pre>
