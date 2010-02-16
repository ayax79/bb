<%--@elvariable id="actionBean" type="com.blackbox.presentation.action.persona.MiniPersonaActionBean"--%>
<%@include file="/WEB-INF/jsp/include/page_header.jspf" %>
<c:set var="activity" value="${actionBean.profile.activity}"/>

<div class="mini-member-persona">
	<div class="mini-member-persona-interior clearfix">
	    <div class="mp-cl">
	        <div class="mp-cr">
	            <div class="mp-content clearfix">

	                <div class="col-l">
	                <ui:profileImage guid="${actionBean.profile.guid}" showMiniProfile="true" size="large" color="white"/>
		               <%-- <div class="mp-vouches">
			                <strong>Vouch status</strong>
							<span class="box clearfix">
								<img src="${bb:libraryResource('/library/images/persona/mini-persona-vouch-icon.png')}"/>
								<p><span>${actionBean.profile.totalVouches}</span> Vouches</p>
							</span>
		           		</div>
		           		--%>
	                </div>
	                <div class="col-r">
	                	<div class="col-r-top">
	                    <%--<ui:roundedBox className="black65 member-data">--%>
				            <%--<div class="mp-names">--%>
								<span class="iefpos">
									<span class="userName"><strong>${actionBean.profile.username}</strong></span>
								<c:if test="${bb:isFriend(actionBean.profile) || bb:isInRelationship(actionBean.profile)}"><span class="realName">(${actionBean.profile.name})</span></c:if>
									</span>
				            <%--</div>--%>
	                        <div class="member-location">
	                            <p class="location-home">
	                            	<%--<c:if test="${not empty user.profile.sex}">
			                            <c:choose>
			                                <c:when test="${user.profile.sex == 'MALE'}">Male</c:when>
			                                <c:when test="${user.profile.sex == 'FEMALE'}">Female</c:when>
			                                <c:when test="${user.profile.sex == 'XXX'}">Not specified</c:when>
			                            </c:choose>
			                            <c:set var="previousValue" value="true"/>
			                        </c:if>
			                        <c:if test="${not user.profile.birthdayInVisible and not empty user.profile.birthday}">
			                            , ${bb:age(user.profile.birthday)}
			                        </c:if>--%>
									<c:if test="${not empty actionBean.profile.locationCity}">
										${actionBean.profile.locationCity}
									</c:if>
									<c:if test="${not empty actionBean.profile.locationCity and not empty actionBean.profile.locationState}">, </c:if>
                                    <c:if test="${!empty bb:stateAbv(actionBean.profile.locationState)}">${bb:stateAbv(actionBean.profile.locationState)}</c:if>
									<br/>
									<c:if test="${not empty actionBean.profile.currentCity}">
										Currently in ${actionBean.profile.currentCity}
									</c:if>
								</p>
								
								<c:if test="${not empty actionBean.profile.lastOnline}">
								<p>Last online: <abbr class="timeago" title="${bb:timeagoFormatDate(actionBean.profile.lastOnline)}">${bb:timeagoFormatDate(actionBean.profile.lastOnline)}</abbr></p>
							</c:if>
	                                <%--<span class="location-current">&mdash; currently in ???</span>--%>
	                        </div>

							<p class="looking-for-explain">${actionBean.profile.lookingForExplain}</p>

	                        <c:if test="${actionBean.identifier != actionBean.currentUser.username}">
	                            <div class="network-menu">
	
									<ui:relationshipsButton user="${actionBean.profile}" relationshipType="friend" beanclass="com.blackbox.presentation.action.persona.PersonaActionBean" event="friend" isVouched="${bb:isVouched(actionBean.currentUser.guid)}" isBlocked="${actionBean.blocksCurrentUser}"/>
									<c:if test="${bb:isAllowedToFollow(actionBean.currentUser, actionBean.profile.guid)}"><ui:relationshipsButton user="${actionBean.profile}" relationshipType="follow" beanclass="com.blackbox.presentation.action.persona.PersonaActionBean" event="follow" isBlocked="${actionBean.blocksCurrentUser}"/></c:if>
									<%--<ui:relationshipsButton user="${actionBean.profile}" relationshipType="donkey" beanclass="com.blackbox.presentation.action.persona.MiniPersonaActionBean" event="block"/>--%>
									<c:if test="${bb:isAllowedToPrivateMessage(actionBean.currentUser, actionBean.profile.guid)}"><a href="#${actionBean.profile.guid}" class="ntwrk-btn compose-link <c:if test="${not bb:isVouched(actionBean.currentUser.guid) || actionBean.blocksCurrentUser}"> disabled </c:if><c:if test="${not bb:isVouched(actionBean.currentUser.guid)}"> unvouched </c:if><c:if test="${actionBean.blocksCurrentUser}"> blocked </c:if>"><span>Send Message</span></a></c:if>
	                            </div>
	                        </c:if>
	                        <%--<p>Has been wished ${actionBean.profile} times</p>--%>
	                    <%--</ui:roundedBox>--%>
	                    </div>
	                    

	                </div>
	              
	            </div>
	        </div>
	    </div>
	</div>
</div>