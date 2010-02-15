<%@include file="/WEB-INF/jsp/include/taglibs.jspf" %>
<%@ attribute name="user" required="true" type="com.blackbox.user.User" %>
<%@ attribute name="relationshipDescription" required="true" %>
<%@ attribute name="displayMode" required="false" %>
<%--@elvariable id="user" type="com.blackbox.user.User"--%>
<c:choose>
	<c:when test="${displayMode == 'horizontal'}">
		<div class="tog_list clearfix">
			<div class="tog_list_pic"><ui:profileImage guid="${user.guid}" size="small" color="black" showMiniProfile="true" linkToProfile="true"/></div>
			<div class="tog_list_tex">
				<span class="rel_description">${relationshipDescription}</span><br/>
				<span class="col_red">
				<s:link beanclass="com.blackbox.presentation.action.persona.PersonaActionBean"><s:param name="identifier" value="${user.username}"/>${user.username}</s:link>
				</span><br/>
				<%--<c:if test="${bb:isFriend(user)}"><span class="col_white1">${user.name}</span><br/></c:if>--%>
				<span class="col_white2">
					<c:set var="previousValue" value="false"/>
					<c:if test="${not empty user.profile.birthday}">
						${bb:age(user.profile.birthday)},
						<c:set var="previousValue" value="false"/>
					</c:if>
					<c:if test="${not empty user.profile.sex}">
					<c:if test="${previousValue}">/</c:if>
					<c:choose>
						<c:when test="${user.profile.sex == 'MALE'}">Male</c:when>
						<c:when test="${user.profile.sex == 'FEMALE'}">Female</c:when>
						<c:when test="${user.profile.sex == 'XXX'}">Not specified</c:when>
					</c:choose>
					<c:set var="previousValue" value="true"/>
				</c:if>
					<br/>
					<c:if test="${not empty user.profile.location.city}">
						${user.profile.location.city}
					</c:if>

					<c:if test="${!empty bb:stateAbv(user.profile.location.state)}">, ${bb:stateAbv(user.profile.location.state)}</c:if>
					
				</span>
			</div>
		</div>
	</c:when>
	<c:otherwise>
		<ui:roundedBox className="rounded-comment relationship-container">
			<a href="#${user.guid}" class="remove-relationship">Remove</a>

			<div style="float:left; margin-right:10px;">
				<ui:profileImage guid="${user.guid}" size="small" color="white" linkToProfile="true" showMiniProfile="true" showUserType="true"/>
			</div>
			<div style="float:left;">
				<span><strong>${user.username}</strong></span><br/>
				<span>${user.name}</span><br/>
				<span>${relationshipDescription} with</span>
			</div>
			<div class="clear"></div>
		</ui:roundedBox>
	</c:otherwise>
</c:choose>