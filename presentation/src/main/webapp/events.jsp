<%--@elvariable id="actionBean" type="com.blackbox.presentation.action.occasion.MyEventsActionBean"--%>
<%@include file="/WEB-INF/jsp/include/page_header.jspf" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
        <title><fmt:message key="pageTitlePrefix" /> : <fmt:message key="pageTitle.events" /></title>
        <link rel="stylesheet" href="<s:url value="${bb:libraryResource('/library/css/events.css')}"/>" type="text/css" media="screen, projection" />
        <link rel="stylesheet" href="<s:url value="${bb:libraryResource('/library/css/my_n.css')}"/>" type="text/css" media="screen, projection" />
        <script type="text/javascript">
            var goto_edit=function(url){
                window.location.href='<s:url beanclass="com.blackbox.presentation.action.psevent.PSShowEventActionBean"/>/'+url+'?_eventName=edit';
            }
            var delete_event=function(guid){
                $.bbDialog.confirm("Do you want to remove this event?",function(){
                    $.get('<s:url beanclass="com.blackbox.presentation.action.psevent.PSEventsActionBean"/>', {"_eventName":"delete","eventGuid":guid}, function(){
                        $("#"+guid).slideUp(500);
                    })
                },function(){
                    this.close();
                })
            }
        </script>
    </head>
    <body>

        <div class="container container-top"></div>
        <div class="container darken">
            <div class="main-content">
                <h1 class="white event">Events</h1>
                <div class="widget-top">
                    <div class="widget-top-right"></div>
                </div>
                <div class="two-column-bg">
                    <div class="bb-left main-col">
                        <s:form beanclass="com.blackbox.presentation.action.psevent.PSEventsActionBean">
                            <div style="margin:14px;" class="mis_bg">
                                <div class="mis_bg_lef">Find events:</div>
                                <div class="mis_bg_mid">
                                    <div class="edit_inp_lef"></div>
                                    <div class="mis_inp_mid2">
                                        <s:text name="searchStr"></s:text>
                                    </div>
                                    <div class="edit_inp_rig"></div>
                                    <div class="clear"></div>
                                </div>
                                <div class="mis_bg_rig"><s:submit style="border:none;width:74px;height:36px;background:url(${bb:libraryResource('/library/images/psevent/mis_but.jpg')}) no-repeat;" value="   " name="search"></s:submit></div>
                                <div class="clear"></div>
                            </div>
                        </s:form>
                        <div id="events">
                            <h2>${actionBean.title}</h2>
                            <%--@elvariable id="event" type="com.blackbox.foundation.occasion.Occasion"--%>
                            <c:forEach var="event" items="${actionBean.occasions}">
                                <div id="${event.guid}">
                                    <div class="event-container">
                                        <div class="event-stats">
                                            <ui:roundedBox className="grey-inline">
                                                <ul>
                                                    <li>${f:length(event.attendees)} <a href="#">friends attending</a></li>
                                                    <li>${f:length(event.attendees)} <a href="#">followers attending</a></li>
                                                    <li><span class="vouch">7 vouches</span></li>
                                                    <li><span class="wish">4 wishes</span></li>
                                                </ul>
                                            </ui:roundedBox>
                                        </div>
                                        <c:choose>
                                            <c:when test="${empty event.avatarLocation}">
                                                <ui:roughBorderedImage url="${bb:libraryResource('/library/images/defaultplaceholder.jpg')}" alt="" onError="Activity.Utils.imgPoll(event, this);" className="event-image" />
                                            </c:when>
                                            <c:otherwise>
                                                <ui:roughBorderedImage url="${event.avatarLocation}" alt="" onError="Activity.Utils.imgPoll(event, this);" className="event-image" />
                                            </c:otherwise>
                                        </c:choose>
                                        <div class="event-body">
                                            <h2><a href="<s:url beanclass='com.blackbox.presentation.action.psevent.PSShowEventActionBean'/>/${event.eventUrl}">${event.name}</a></h2>
                                            <p class="event-description">${event.description}</p>
                                            <%--<p class="event-category">${event.name}</p>--%>
                                            <p class="event-type">[${event.occasionType}]</p>
                                            <p class="event-date">[${bb:formatDate(event.eventTime,"MM/d/YYYY")}]</p>
                                            <p class="event-time">[${bb:formatDate(event.eventTime,"HH:mm")}]</p>
                                        </div>
                                        <div class="clear"></div>
                                    </div>
                                    <c:if test="${event.owner.guid eq actionBean.currentUser.guid}">
                                        <div class="event-actions">
                                            <span style="float:right;"><a href="javascript:delete_event('${event.guid}');">Delete this event</a></span>
                                            <button onclick="goto_edit('${event.eventUrl}');" class="grey27 bbbutton" style="float:left;"><span>Edit event</span></button>
                                            <div class="clear"></div>
                                        </div>
                                    </c:if>
                                    <hr class="event-rule" />
                                </div>
                            </c:forEach>
                        </div>
                    </div>
                    <div class="bb-right right-col last">
                        <h2>Filter events by:</h2>
                        <div class="section">
                            <h3><s:link event="my" beanclass="com.blackbox.presentation.action.psevent.PSEventsActionBean">My Events</s:link></h3>
                            <h5>Events you have created</h5>
                        </div>
                        <!-- <div class="section">
                            <h3>Newest</h3>
                            <h5>Events most recently posted by</h5>
                            <select>
                                <option>BBR Members</option>
                            </select>
                        </div>
                        <div class="section">
                            <h3>By distance</h3>
                            <h5>Events within 5 miles</h5>
                            <input type="text" value="5" />
                        </div>
                        <div class="section">
                            <h3>Other options</h3>
                            <select>
                                <option>BBR Members</option>
                            </select>
                        </div>
                        -->
                        <%--<hr />--%>
                        <%--<p class="explore-more"><a href="#">Explore more events</a></p>--%>
                        <%--<hr />--%>
                    </div>
                    <div class="clear">&nbsp;</div>
                </div>
            </div>
        </div>
		<div class="container container-bottom"></div>
    </body>
</html>