<%@include file="/WEB-INF/jsp/include/page_header.jspf" %>
<div class="cho_pep_sel">
    <!-- <div class="pep_sel_lef"><select>
            <option>All People&nbsp;&nbsp;&nbsp;&nbsp;</option>
        </select></div>
    <div class="pep_sel_mid">Search by Name</div>
    <div class="pep_sel_rig"><input type="text" name="textfield" /></div>
    -->
    <div class="clear"></div>
</div>
<div class="bla_box_con">
    <ul>
        <c:set var="friendMaps" value="${actionBean.uvMapList}" />
        <c:forEach var="friend" items="${friendMaps}">
            <c:forEach varStatus="status" var="value" items="${friend}">
                <c:choose>
                    <c:when test="${status.count==1}">
                        <c:set var="bbUser" value="${value.toEntityObject}"/>
                    </c:when>
                    <c:when test="${status.count==2}">
                        <c:set var="voucheCount" value="${value}"/>
                    </c:when>
                    <c:when test="${status.count==3}">
                        <c:set var="selected" value="${value}"/>
                    </c:when>
                </c:choose>
            </c:forEach>
            <li class="bla_box_li2">
                <div class="bla_lis_lef"><a href="#">
                        <c:choose>
                            <c:when test="${empty bbUser.profile.avatarUrl or bbUser.profile.avatarUrl == ''}">
                                <img src="${bb:libraryResource('/library/images/defaultplaceholder.jpg')}"/>
                            </c:when>
                            <c:otherwise>
                                <img src="${bbUser.profile.avatarUrl}"/>
                            </c:otherwise>
                        </c:choose>    
                    </a></div>
                <div class="bla_lis_rig">
                    <%--<div class="bla_tex1">absfuckinglutly</div>--%>
                    <div class="bla_tex1"></div>
                    <div class="bla_tex2">${bbUser.name}</div>
                    <div class="bla_tex3">${voucheCount} vouches</div>
                    <div class="bla_che">
                        <c:choose>
                            <c:when test="${selected}">
                                <input type="checkbox" checked="${selected}" name="bbUser_selector" guid="${bbUser.guid}" />
                            </c:when>
                            <c:otherwise>
                                <input type="checkbox" name="bbUser_selector" guid="${bbUser.guid}" />
                            </c:otherwise>
                        </c:choose>
                    </div>
                    <div class="clear"></div>
                </div>
                <div class="clear"></div>
            </li>
        </c:forEach>




        <%--<li class="bla_box_li1">
            <div class="bla_lis_lef"><a href="#"><img src="${bb:libraryResource('/library/images/persona/pic2.png')}" border="0" /></a></div>
            <div class="bla_lis_rig">
                <div class="bla_tex1">Daddy3rdLeg</div>
                <div class="bla_tex2">James Deer</div>
                <div class="bla_tex3">15 vouches</div><div class="bla_che"><input type="checkbox" name="checkbox" value="checkbox" /></div>
                <div class="clear"></div>
            </div>
            <div class="clear"></div>
        </li>--%>

        <div class="clear"></div>
    </ul>
</div>
<%--<div class="bla_box_but">
    <div class="box_but_lef">A total 15 people are currently selected.</div>
    <div class="box_but_rig">
        <div class="but_gre">
            <div class="but_gre_rig"><a href="javascript:void(0);">Add emails to guest list</a></div>
        </div>
    </div>
    <div class="clear"></div>
</div>
<div class="cho_pep_bot"></div>--%>
