<%@include file="/WEB-INF/jsp/include/taglibs.jspf" %>
<head>
    <title>Show More</title>
    <script type="text/javascript">
            $(document).ready(function(){
        bb.pageInit.bindMiniProfiles();});
        </script>
</head>
<body>
    <div class="container container-top"></div>
    <div class="container darken">
        <div class="widget-top">
            <div class="widget-top-right"></div>
        </div>
        <div class="ui-widget ui-widget-content" style="background:white;overflow:auto;position:relative;padding-bottom:40px;">
            <div style="display:block;margin-left:100px;">
                <div id="exp_results">
            <c:choose>
                <c:when test="${not empty actionBean.results.results}">

                    <h2 style="margin-top:0px;">Results (${f:length(actionBean.results.results)} people found)</h2>


                    <c:forEach var="result" items="${actionBean.results.results}">
                        <%--@elvariable id="result" type="com.blackbox.foundation.search.SearchResult"--%>

                        <c:set var="user" value="${result.entity}"/>
                        <%--@elvariable id="user" type="com.blackbox.foundation.user.User"--%>

                        <c:set var="activity" value="${result.latest}"/>
                        <%--@elvariable id="activity" type="com.blackbox.foundation.activity.IActivity"--%>

                        <c:set var="network" value="${result.network}"/>
                        <%--@elvariable id="network" type="com.blackbox.foundation.social.RelationshipNetwork"--%>

                        <ui:roundedBox className="rounded-comment xplr-results-container">
                            <div class="xplr-content-overlay">
                                <div class="profile-image-wrap">
                                    <div class="profile-image">
                                        <ui:profileImage guid="${user.guid}" size="medium" color="white" linkToProfile="true" showMiniProfile="true" showUserType="true"/>
                                    </div>
                                </div>
                                <div class="meta-container">
                                    <div class="name-stuff">
                                        <div class="meta-content">
                                            <div class="vouch-col">
                                                <div class="vouch-icon <c:if test="${!(result.vouchCount > 0)}"> no-icon</c:if>">&nbsp;</div>
                                            </div>
                                            <div class="name-col">
                                                <ui:entitylink entity="${user}">${bb:displayName(user)}</ui:entitylink> <%--[online] --%><br/>
                                                <c:choose>
                                                    <c:when test="${bb:isLimited(user)}">
                                                        <span class="type-limited">Limited member</span>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <span class="type-unlimited">Unlimited member</span>
                                                    </c:otherwise>
                                                </c:choose>
                                            </div>
                                            <div class="relationship-col">
                                                <c:choose>
                                                    <c:when test="${bb:isInRelationship(user)}">
                                                        ${bb:relationshipDescription(user)} with
                                                    </c:when>
                                                    <c:when test="${bb:isFriend(user)}">
											Friend
                                                    </c:when>
                                                    <c:when test="${bb:isFollowing(user)}">
											Following
                                                    </c:when>
                                                    <c:when test="${bb:isFollowedBy(user)}">
											Follower
                                                    </c:when>
                                                </c:choose>
                                            </div>
                                            <div class="clear"></div>
                                        </div>
                                    </div>
                                    <div class="location-stuff">
                                        <div class="meta-content">
                                            <dl>
                                                <dt class="grey">Current Location:</dt>
                                                <dd>N/A</dd>
                                            </dl>
                                            <dl>
                                                <dt class="grey">Location:</dt>
                                                <dd>
                                                    <c:choose>
                                                        <c:when test="${not empty user.profile.currentAddress.city}">
                                                            ${user.profile.currentAddress.city}
                                                        </c:when>
                                                        <c:otherwise>
												N/A
                                                        </c:otherwise>
                                                    </c:choose>
                                                </dd>
                                            </dl>
                                            <dl>
                                                <dt class="grey">Age:</dt>
                                                <dd>
                                                    <c:choose>
                                                        <c:when test="${not empty user.profile.birthday}">
                                                            ${bb:age(user.profile.birthday)}
                                                        </c:when>
                                                        <c:otherwise>
												N/A
                                                        </c:otherwise>
                                                    </c:choose>
                                                </dd>
                                            </dl>
                                        </div>
                                    </div>
                                </div>
                                <div class="clear"></div>
                            </div>
                            <div class="clear"></div>
                        </ui:roundedBox>
                    </c:forEach>
                </c:when>
                <c:otherwise>
                    <p>No results found</p>
                </c:otherwise>
            </c:choose>
                </div>
            </div>
        </div>
        <div class="widget-bottom">
            <div class="widget-bottom-right"></div>
        </div>
    </div>
    <div class="container container-bottom"></div>
</body>
</html>
