<%@ tag language="java" %>
<%@ include file="/WEB-INF/jsp/include/taglibs.jspf" %>

<%@ attribute name="sliderId" required="false" %>
<%@ attribute name="containerClass" required="false" %>
<%@ attribute name="labelLeft" required="false" %>
<%@ attribute name="labelCenter" required="false" %>
<%@ attribute name="labelRight" required="false" %>
<%@ attribute name="showQuarterTicks" required="false" %>

<div class="${containerClass}" >
	<div class="bbslider-slider-wrap clearfix">
		<div class="bbslider-slider-wrap-r">
			<div <c:if test="${not empty sliderId}"> id="${sliderId}"</c:if> class="bbslider"></div>
		</div>
		<div class="bbslider-tick bbslider-tick-left"></div>
		<c:if test="${showQuarterTicks eq 'true'}"><div class="bbslider-tick bbslider-tick-center-left"></div></c:if>
		<div class="bbslider-tick bbslider-tick-center"></div>
		<c:if test="${showQuarterTicks eq 'true'}"><div class="bbslider-tick bbslider-tick-center-right"></div></c:if>
		<div class="bbslider-tick bbslider-tick-right"></div>
		<div class="bbslider-tick-label bbslider-tick-label-left">${labelLeft}</div>
		<div class="bbslider-tick-label bbslider-tick-label-center">${labelCenter}</div>
		<div class="bbslider-tick-label bbslider-tick-label-right">${labelRight}</div>
	</div>
</div>