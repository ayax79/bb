<%@ tag language="java" %>
<%@ attribute name="id" required="true" %>
<%@ attribute name="className" required="false" %>
<%@ attribute name="items" required="true" %>
<%@ include file="/WEB-INF/jsp/include/taglibs.jspf" %>

<div class="paging-controls ${className}">
	<div class="paging-right">
		<a href="#" class="paging-button previous"><span class="label">Last <span class="numResults">-</span></span></a>
		<a href="#" class="paging-button next"><span class="label">Next <span class="numResults">-</span></span></a>
	</div>
	<div class="paging-left">
		<form action="#" method="post" class="paging-form">
		<p>Go to page <input type="text" name="pageNum" size="3"/> of <span class="total-pages">#</span></p>
		</form>
	</div>
</div>
<div class="clear"></div>