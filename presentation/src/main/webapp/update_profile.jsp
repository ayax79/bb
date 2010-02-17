<%@include file="/WEB-INF/jsp/include/page_header.jspf" %>
<%@ page import="com.blackbox.foundation.user.LookingForEnum" %>
<%@ page import="com.blackbox.foundation.user.SexEnum" %>

<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <title><fmt:message key="pageTitlePrefix" /></title>

    <link rel="stylesheet" href="${bb:libraryResource('/library/css/my_n.css')}" type="text/css" media="screen, projection"/>

    <!-- Librarys -->
    <script src="<s:url value="${bb:libraryResource('/library/js/tooltip.js')}"/>" type="text/javascript"></script>

    <script type="text/javascript">
		function saveProfile() {                    
                    $("#profileForm").submit();
		}
                /* if using ajax, need to do validation work,comment out now.
                function ajaxSaveProfile() {
                    var postData = $("#profileForm").serialize(true);
                    postData = postData + "&view=json"
                    var postUrl = '<s:url beanclass="com.blackbox.presentation.action.persona.PSProfileActionBean" />;jsessionid=<%=session.getId()%>'
                    $.post(postUrl, postData,
                        function(msg){
                            alert(msg);
                            if(msg == 'success') {
                                $.bbDialog.alert("Your profile information has been succesfully saved.", {
                                    onclose:function() {
                                    window.location = "/persona";
                                }
                             });
                            }else {
                                $.bbDialog.info("Update Failure! try later.");
                            }
                        }
                    );
                }
                */
                $(document).ready(function(){                    
                    if($("#success_msg_id").html() != "") {
                        $.bbDialog.alert("Your profile information has been succesfully saved.", {
                            onclose:function() {
                               window.location = "/persona";
                            }
                        });
                    }
                });
    </script>
</head>

<div class="container container-top"></div>
<div class="container darken">
<s:form id="profileForm" beanclass="com.blackbox.presentation.action.persona.PSProfileActionBean">
<div class="edit_pro_tit">Edit profile</div>
<div class="edit_pro1"></div>
<div class="edit_pro3">
<div class="edit_pro2_bg1"></div>
<div class="edit_pro2_con">

<div class="edit_con_men"><span
        style="font-size:18px; color:#000; font-weight:bold; font-style:normal;">Your info</span> This is what appears
    within the info tab on your persona page.
</div>
<div id="success_msg_id" style="display:none;"><s:messages/></div>
<s:errors/>

<c:set value='yes' var='noStream' scope='request'/>
	<input type="hidden" name="_eventName" value="store"/>
    <div class="edit_con_list">
        <div class="edit_con_tit"><s:label for="profile.birthday"/></div>
        <div class="edit_con_inp">
            <div class="edit_inp_lef"></div>
            <div class="edit_inp_mid"><s:text name="profile.birthday" formatPattern="MM/dd/yyyy" formatType="datetime" /></div>
            <div class="edit_inp_rig"></div>
            <div class="clear"></div>
        </div>
    </div>
    <div class="edit_con_list">
        <div class="edit_con_tit"><s:label for="profile.location.city" /> </div>
        <div class="edit_con_inp">
            <div class="edit_inp_lef"></div>
            <div class="edit_inp_mid"><s:text name="profile.location.city"/></div>
            <div class="edit_inp_rig"></div>
            <div class="clear"></div>
        </div>
    </div>
    <div class="edit_con_list">
        <div class="edit_con_tit"><s:label for="profile.location.zipCode" /> </div>
        <div class="edit_con_inp">
            <div class="edit_inp_lef"></div>
            <div class="edit_inp_mid"><s:text name="profile.location.zipCode"/></div>
            <div class="edit_inp_rig"></div>
            <div class="clear"></div>
        </div>
    </div>
    <div class="edit_con_list">
        <div class="edit_con_tit"><s:label for="profile.sex"/></div>
        <div class="edit_con_inp">
            <c:forEach var="val" items="${bb:enumValues('com.blackbox.foundation.user.SexEnum')}" varStatus="status">
                <div class="edit_inp_con1">
                    <s:checkbox name="profile.sex" value="${s:enumName(val)}"/><fmt:message key="SexEnum.${s:enumName(val)}"/>
                </div>
                <c:if test="${(status.index + 1) % 3 == 0}">
                    <div class='clear'></div>
                </c:if>
            </c:forEach>
            <div class="clear"></div>
        </div>
    </div>
    <div class="edit_con_list">
        <div class="edit_con_tit"><s:label for="profile.acceptsGifts"/></div>
        <div class="edit_con_inp">
            <div class="edit_inp_con1"><s:radio name="profile.acceptsGifts" value="true"/>Yes</div>
            <div class="edit_inp_con1"><s:radio name="profile.acceptsGifts" value="false"/>No</div>
            <div class="clear"></div>
        </div>
    </div>
    <div class="edit_con_list">
        <div class="edit_con_tit"><s:label for="profile.lookingFor"/></div>
        <div class="edit_con_inp">
            <c:forEach var="val" items="${bb:enumValues('com.blackbox.foundation.user.LookingForEnum')}"
                       varStatus="status">
                <div class="edit_inp_con1"><s:checkbox name="profile.lookingFor.${s:enumName(val)}"/><fmt:message
                        key="LookingForEnum.${s:enumName(val)}"/></div>
                <c:if test="${(status.index + 1) % 3 == 0}">
                    <div class='clear'></div>
                </c:if>
            </c:forEach>
            <div class="clear"></div>
        </div>
    </div>
    <div class="edit_con_list">
        <div class="edit_con_tit"><s:label for="profile.politicalViews"/></div>
        <div class="edit_con_inp">
            <div class="edit_inp_lef"></div>
            <div class="edit_inp_mid"><s:text name="profile.politicalViews"/></div>
            <div class="edit_inp_rig"></div>
            <div class="clear"></div>
        </div>
    </div>
    <div class="edit_con_list">
        <div class="edit_con_tit"><s:label for="profile.religiousViews"/></div>
        <div class="edit_con_inp">
            <div class="edit_inp_lef"></div>
            <div class="edit_inp_mid"><s:text name="profile.religiousViews"/></div>
            <div class="edit_inp_rig"></div>
            <div class="clear"></div>
        </div>
    </div>
    <div class="edit_con_list">
        <div class="edit_con_tit"><s:label for="profile.website"/></div>
        <div class="edit_con_inp">
            <div class="edit_inp_lef"></div>
            <div class="edit_inp_mid"><s:text name="profile.website"/></div>
            <div class="edit_inp_rig"></div>
            <div class="clear"></div>
        </div>
    </div>
    <div class="edit_con_list">
        <div class="edit_con_tit"><s:label for="profile.bodyMods"/></div>
        <div class="edit_con_inp">
            <div class="edit_inp_lef"></div>
            <div class="edit_inp_mid"><s:text name="profile.bodyMods"/></div>
            <div class="edit_inp_rig"></div>
            <div class="clear"></div>
        </div>
    </div>
    <div class="edit_con_list">
        <div class="edit_con_tit"><s:label for="profile.mostly"/></div>
        <div class="edit_con_inp">
            <div class="edit_inp_lef"></div>
            <div class="edit_inp_mid"><s:text name="profile.mostly"/></div>
            <div class="edit_inp_rig"></div>
            <div class="clear"></div>
        </div>
    </div>

	</div>

    <div class="edit_pro2_bg2"></div>
    </div>
    <div class="edit_con_but">		
	<div class="but_edi_pro">
			<div class="but_edi_profile_button" id="cancelBut"><a href='<s:url value="/persona"/>'>Cancel</a></div>
	</div>
	<div class="but_edi_pro">
		<div class="but_edi_profile_button" id="saveBut"><a href="#" onclick="saveProfile(); return false;">Save changes</a></div>
	</div>
   </div>
    <div class="edit_pro2"></div>
</s:form>
</div>