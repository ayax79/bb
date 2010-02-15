<%@include file="/WEB-INF/jsp/include/taglibs.jspf" %>

<div class="feedback-content">
    <s:form class="bbform feedbackForm bbsettings" beanclass="com.blackbox.presentation.action.feedback.FeedbackActionBean">
		<p>
		Need help? Email the <a href="mailto:angels@blackboxrepublic.com">angels</a>.
        <br />
        Find a bug, or have a suggestion? Please <a href="mailto:feedback@blackboxrepublic.com">share it with us.</a>
        </p>
        <%--
        <input type="hidden" name="_eventName" value="feedback"/>
		<ul>
			<li>
				<label for="subject_id">Subject</label>
				<span class="textinput37"><s:text id="subject_id" name="subject" /></span>
			</li>
			<li>
				<label for="description_id">Description</label>
				<ui:roundedBox className="textarea1">
					<s:textarea name="description" id="description_id" />
				</ui:roundedBox>
				<span class="textinput37"><s:text id="description_id"  name="description" style="width:550px;"/></span>
			</li>
		</ul>
        --%>
    </s:form>
	<p class="success_message" style="display:none;">Your feedback has submitted successfully.</p>
	<p class="fail_message" style="display:none;">Feedback submit failed. Please try again later.</p>
</div>