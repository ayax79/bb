<%@ tag language="java" %>
<%@ attribute name="id" required="false" %>
<%@ attribute name="typeClass" required="true" %>
<%@ attribute name="styleClass" required="true" %>
<%@ attribute name="href" required="false" %>
<%@ attribute name="beanclass" required="false" %>
<%@ attribute name="event" required="false" %>
<%@ attribute name="disabled" required="false" %>
<%@ include file="/WEB-INF/jsp/include/taglibs.jspf" %>

<c:choose>
    <c:when test="${disabled != 'true'}">
        <c:choose>
            <c:when test="${beanclass != null}">
                <c:choose>
                    <c:when test="${not empty id}">
                        <c:choose>
                            <c:when test="${not empty  event}">
                                <s:link id="${id}" event="${event}" beanclass="${beanclass}" class="${typeClass} ${styleClass}">
                                    <span>
                                        <jsp:doBody/>
                                    </span>
                                </s:link>
                            </c:when>
                            <c:otherwise>
                                <s:link id="${id}" beanclass="${beanclass}" class="${typeClass} ${styleClass}">
                                    <span>
                                        <jsp:doBody/>
                                    </span>
                                </s:link>
                            </c:otherwise>
                        </c:choose>
                    </c:when>
                    <c:when test="${not empty  event}">
                        <s:link event="${event}" beanclass="${beanclass}" class="${typeClass} ${styleClass}">
                            <span>
                                <jsp:doBody/>
                            </span>
                        </s:link>
                    </c:when>
                    <c:otherwise>
                        <s:link  beanclass="${beanclass}" class="${typeClass} ${styleClass}">
                            <span>
                                <jsp:doBody/>
                            </span>
                        </s:link>
                    </c:otherwise>
                </c:choose>
            </c:when>
            <c:otherwise>
                <c:choose>
                    <c:when test="${not empty id}">
                        <s:link id="${id}" href="${href}" class="${typeClass} ${styleClass}">
                            <span>
                                <jsp:doBody/>
                            </span>
                        </s:link>
                    </c:when>
                    <c:otherwise>
                        <s:link href="${href}" class="${typeClass} ${styleClass}">
                            <span>
                                <jsp:doBody/>
                            </span>
                        </s:link>
                    </c:otherwise>
                </c:choose>
            </c:otherwise>
        </c:choose>
    </c:when>
    <c:otherwise>
        <span class="${typeClass} ${styleClass} disabled"><span>
                <jsp:doBody/>
            </span></span>
    </c:otherwise>
</c:choose>