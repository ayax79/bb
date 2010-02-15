<%@ tag language="java" %>
<%@ attribute name="id"     required="true" %>
<%@ attribute name="xname" required="true" %>
<%@ attribute name="yname" required="true" %>
<%@ attribute name="wname" required="true" %>
<%@ attribute name="hname" required="true" %>
<%@ attribute name="prevTitle"  required="true" %>

<%@ include file="/WEB-INF/jsp/include/taglibs.jspf" %>

<div class="image-cropper" id="${id}">
	<input class="xf" id="${xname}" name="${xname}" value="0" type="hidden"/>
	<input class="yf" id="${yname}" name="${yname}" value="0" type="hidden"/>
	<input class="wf" id="${wname}" name="${wname}" value="0" type="hidden"/>
	<input class="hf" id="${hname}" name="${hname}" value="0" type="hidden"/>

	<div class="crop-control">
		<img class="crop-image" id="${id}_cropImage" src="" alt=""/>
	</div>
	<div class="crop-preview">
		<p class="preview-title">${prevTitle}</p>
		<div class="preview-wrap">
			<img class="preview-image" id="${id}_previewImage" src="" alt=""/>
		</div>
	</div>
</div>

<div class="clear"></div>