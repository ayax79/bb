<%@ tag language="java" %>
<%@ attribute name="id" required="true" %>
<%@ attribute name="uri" required="true" %>
<%@ attribute name="cssClass" required="false" %>
<%@ attribute name="excludeJS" required="false" %>
<%@ include file="/WEB-INF/jsp/include/taglibs.jspf" %>
<a href="${uri}" <c:if test="${cssClass != null}">class="${cssClass}"</c:if> id="${id}"></a>
<c:if test="${excludeJS != 'true'}">
	<script type="text/javascript">
	//<![CDATA[
	$(function() {
		bb.embedFlowPlayer('${id}');
	});
	//]]>
	</script>
</c:if>	