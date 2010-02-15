<%@ tag language="java" %>
<%@ attribute name="user" required="true" type="com.blackbox.user.User"%>
<%@ attribute name="type" required="true"%>
<%@ attribute name="vouch" required="false" type="com.blackbox.social.UserVouch" %>
<%@ attribute name="wish" required="false" type="com.blackbox.bookmark.UserWish" %>
<%@ attribute name="hideActionMenu" required="false"%>
<%@include file="/WEB-INF/jsp/include/taglibs.jspf" %>

<tr>

	<td class="user-avatar">
		<ui:profileImage guid="${user.guid}" size="large" linkToProfile="true" showMiniProfile="true" showUserType="true"/>
	</td>

	<td class="user-info">
		<ui:entitylink entity="${user}">${bb:displayName(user)}</ui:entitylink><br/>
		<c:set var="prevToken" value="false"/>
		<c:if test="${not empty user.profile.location.city or not empty user.profile.location.state or not empty user.profile.location.country}">
		<span>
			<c:if test="${not empty user.profile.location.city}">
				${user.profile.location.city}
				<c:set var="prevToken" value="true"/>
			</c:if>
			<c:if test="${prevToken}">, </c:if>
			<c:if test="${not empty user.profile.location.state}">
				${bb:stateAbv(user.profile.location.state)}
			</c:if>
		</span><br/>
		</c:if>
		<span>
			<c:if test="${not user.profile.birthdayInVisible and not empty user.profile.birthday}">
				${bb:age(user.profile.birthday)} /
			</c:if>

			<c:if test="${not empty user.profile.sex}">
				<c:choose>
					<c:when test="${user.profile.sex == 'MALE'}">Male</c:when>
					<c:when test="${user.profile.sex == 'FEMALE'}">Female</c:when>
					<c:when test="${user.profile.sex == 'XXX'}">Not specified</c:when>
				</c:choose>
				<c:set var="previousValue" value="true"/>
			</c:if>
		</span><br/>
		<ui:tinySliders user="${user}"/>
	</td>

	<td class="item-content">
		<c:choose>
			<c:when test="${type == 'wishes'}">
				<c:if test="${not empty wish.toDescription}">
					<p class="frst"><strong>${bb:displayName(actionBean.currentUser)}</strong>: ${wish.toDescription}</p>
				</c:if>
				<c:if test="${not empty wish.fromDescription}">
					<p><strong>${bb:displayName(wish.user)}</strong>: ${wish.fromDescription}</p>
				</c:if>
			</c:when>
			<c:when test="${type == 'vouches'}">
				<c:choose>
					<c:when test="${s:enumName(vouch.direction) == 'INCOMING' or (s:enumName(vouch.direction) == 'MUTUAL' and actionBean.currentUser.guid == vouch.user.guid)}">
						<c:if test="${not empty vouch.toDescription}">
							<p class="frst"><strong>${bb:displayName(vouch.toUser)}</strong>: ${vouch.toDescription}</p>
						</c:if>
						<c:if test="${not empty vouch.fromDescription}">
							<p><strong>${bb:displayName(vouch.fromUser)}</strong>: ${vouch.fromDescription}</p>
						</c:if>
					</c:when>
					<c:when test="${s:enumName(vouch.direction) == 'OUTGOING' or (s:enumName(vouch.direction) == 'MUTUAL' and actionBean.currentUser.guid != vouch.user.guid)}">
						<c:if test="${not empty vouch.toDescription}">
							<p class="frst"><strong>${bb:displayName(vouch.fromUser)}</strong>: ${vouch.toDescription}</p>
						</c:if>
						<c:if test="${not empty vouch.fromDescription}">
							<p><strong>${bb:displayName(vouch.toUser)}</strong>: ${vouch.fromDescription}</p>
						</c:if>
					</c:when>
				</c:choose>
			</c:when>
			<c:otherwise>
				<span>${user.profile.lookingForExplain}</span>
			</c:otherwise>
		</c:choose>
	</td>

	<c:if test="${type == 'connection'}">
	<td class="connection-type">
		<ul>
		<c:if test="${bb:isFriend(user)}"><ui:socialStatusPill color="green">Friends</ui:socialStatusPill></c:if>
		<c:if test="${bb:isPending(user)}"><ui:socialStatusPill color="grey">Friend Pending</ui:socialStatusPill></c:if>
		<c:if test="${bb:isFollowing(user) && not bb:isFriend(user)}"><ui:socialStatusPill color="blue">Following</ui:socialStatusPill></c:if>
		<c:if test="${bb:isFollowedBy(user) && not bb:isFriend(user)}"><ui:socialStatusPill color="orange">Follower</ui:socialStatusPill></c:if>
		</ul>
	</td>
	</c:if>

	<c:if test="${type == 'wishes'}">
	<td class="connection-type">
		<ul>
		<c:if test="${s:enumName(wish.status) == 'MUTUAL'}"><ui:socialStatusPill color="green">Mutual wish</ui:socialStatusPill></c:if>
		<c:if test="${s:enumName(wish.status) == 'WISHED'}"><ui:socialStatusPill color="green">Wished</ui:socialStatusPill></c:if>
		<c:if test="${s:enumName(wish.status) == 'WISHED_BY'}"><ui:socialStatusPill color="orange">Wished you</ui:socialStatusPill></c:if>
		</ul>
	</td>
	</c:if>

	<c:if test="${type == 'vouches'}">
	<td class="connection-type">
		<ul>
		<c:if test="${s:enumName(vouch.direction) == 'OUTGOING'}"><ui:socialStatusPill color="blue">Vouched</ui:socialStatusPill></c:if>
		<c:if test="${s:enumName(vouch.direction) == 'MUTUAL'}"><ui:socialStatusPill color="green">Mutually vouched</ui:socialStatusPill></c:if>
		<c:if test="${s:enumName(vouch.direction) == 'INCOMING'}"><ui:socialStatusPill color="orange">Vouched</ui:socialStatusPill></c:if>
		</ul>
	</td>
	</c:if>
	<c:if test="${hideActionMenu ne true}">
	<td class="item-select">
		<ul class="gear-menu">
			<li class="gear-icon"><a class="icon-link" href="#"></a>
				<ul class="menu">
					<c:choose>
						<%-- CONNECTIONS MENU --%>
						<c:when test="${type == 'connection'}">
							<c:choose>
								<c:when test="${bb:isFriend(user)}">
									<li class="menu-item">
										<a class="unfriend-link" href="#${user.username}">Unfriend</a>
									</li>
								</c:when>
								<c:when test="${bb:isFollowing(user)}">
									<li class="menu-item">
										<a class="unfollow-link" href="#${user.username}">Unfollow</a>
									</li>
								</c:when>
								<c:when test="${bb:isFollowedBy(user)}">
									<li class="menu-item">
										<a class="block-link" href="#${user.username}">Donkey kick</a>
									</li>
								</c:when>
							</c:choose>
						</c:when>

						<%-- VOUCHES MENU --%>
						<c:when test="${type == 'vouches'}">

							    <c:if test="${s:enumName(vouch.direction) == 'OUTGOING'}">
									<li class="menu-item">
										<a class="unvouch-link" href="#${user.username}">Unvouch ${user.username}</a>
									</li>
								</c:if>

							    <c:if test="${s:enumName(vouch.direction) == 'INCOMING'}">
									<li class="menu-item">
										<a class="deletevouch-link" href="#${user.username}">Delete ${user.username}'s vouch</a>
									</li>

								</c:if>

							    <c:if test="${s:enumName(vouch.direction) == 'MUTUAL'}">
									<li class="menu-item">
										<a class="unvouch-link" href="#${user.username}">Unvouch ${user.username}</a>
									</li>
									<li class="menu-item">
										<a class="deletevouch-link" href="#${user.username}">Delete ${user.username}'s vouch</a>
									</li>
								</c:if>

								<%--<c:if test="${s:enumName(vouch.direction) == 'OUTGOING' or s:enumName(vouch.direction) == 'MUTUAL'}">--%>
									<%--<li class="menu-item">--%>
										<%--<a class="unvouch-link" href="#${user.username}">Unvouch</a>--%>
									<%--</li>--%>
								<%--</c:if>--%>
								<%--<c:if test="${s:enumName(vouch.direction) == 'INCOMING' or s:enumName(vouch.direction) == 'MUTUAL'}">--%>
									<%--<li class="menu-item">--%>
										<%--<a class="block-link" href="#${user.username}">Donkey kick</a>--%>
									<%--</li>--%>
								<%--</c:if>--%>
						</c:when>

						<%-- WISHES MENU --%>
						<c:when test="${type == 'wishes'}">
								<c:if test="${s:enumName(wish.status) == 'MUTUAL' || s:enumName(wish.status) == 'WISHED'}">
									<li class="menu-item">
										<a class="unwish-link" event="unwish" href="#${user.username}">Unwish</a>
									</li>
								</c:if>
								<c:if test="${s:enumName(wish.status) == 'MUTUAL' || s:enumName(wish.status) == 'WISHED_BY'}">
									<li class="menu-item">
										<a class="block-link" event="block" href="#${user.username}">Donkey kick</a>
									</li>
								</c:if>
						</c:when>
					</c:choose>
				</ul>
			</li>
		</ul>
	</td>
	</c:if>

</tr>
