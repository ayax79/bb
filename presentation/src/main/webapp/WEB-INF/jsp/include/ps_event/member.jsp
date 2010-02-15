<head>
    <title>Event</title>
    <%@include file="/WEB-INF/jsp/include/ps_event/ps_event_header.jspf" %>
    <script type="text/javascript">
        var bbox_friends=[];
        $(function(){
            $("#wait").hide();
            $("#import_zone").hide();
            $("#import_navigation").tabs({selected:0,select:function(e,ui){}});
            $("input[name=bbUser_selector]").click(function(){
                var item=$(this)
                if(item.attr("checked")){
                    bbox_friends.push(item.attr("guid"))
                    $.unique(bbox_friends);
                }else{
                    $.unique(bbox_friends);
                    var p_index=$.inArray(item.attr("guid"), bbox_friends);
                    if(p_index>=0){
                        bbox_friends.splice(p_index,1);
                    }
                }
            })
            $("#add_email_btn").click(function(){
                saveReceiver(function(){
                    window.location.href="<s:url beanclass='com.blackbox.presentation.action.psevent.PSConfigEventActionBean'/>;jsessionid=<%=session.getId()%>?_eventName=guestList";
                });
            })
            $("#last_step_btn").click(function(){
                saveReceiver(function(){
                    window.location.href="<s:url beanclass='com.blackbox.presentation.action.psevent.PSConfigEventActionBean'/>;jsessionid=<%=session.getId()%>?_eventName=detail";
                });
            })
            var emailList="";
        <c:if test="${not empty tempEmailReceiver}">
                <c:forEach var="attendee" items="${tempEmailReceiver}">
                        emailList=emailList+"${attendee.email}"+"\n"
            </c:forEach>
        </c:if>
                    $("#email_importor").val(emailList)
                })
                var select_live_all=function(){
                    $("#select_all img").attr("src", "/library/images/psevent/sel_allbg1.jpg")
                    $("#select_none img").attr("src", "/library/images/psevent/sel_nonbg2.jpg")
                    $(".other_import").attr("checked", true)
                }
                var seleect_live_none=function(){
                    $("#select_all img").attr("src", "/library/images/psevent/sel_allbg2.jpg")
                    $("#select_none img").attr("src", "/library/images/psevent/sel_nonbg1.jpg")
                    $(".other_import").attr("checked", false)
                }
                var render_tab_header=function(header){
                    if(header=="bb"){
                        $("#bb_header").removeClass("bla_box_bg1")
                        $("#bb_header").addClass("bla_box_bg2")
                        $("#email_header").removeClass("ema_add_bg2")
                        $("#email_header").addClass("ema_add_bg1")
                        $("#import_header").removeClass("imp_bg2")
                        $("#import_header").addClass("imp_bg1")
                    }else if(header=="email"){
                        $("#bb_header").removeClass("bla_box_bg2")
                        $("#bb_header").addClass("bla_box_bg1")
                        $("#email_header").removeClass("ema_add_bg1")
                        $("#email_header").addClass("ema_add_bg2")
                        $("#import_header").removeClass("imp_bg2")
                        $("#import_header").addClass("imp_bg1")
                    }else if(header=="others"){
                        $("#bb_header").removeClass("bla_box_bg2")
                        $("#bb_header").addClass("bla_box_bg1")
                        $("#email_header").removeClass("ema_add_bg2")
                        $("#email_header").addClass("ema_add_bg1")
                        $("#import_header").removeClass("imp_bg1")
                        $("#import_header").addClass("imp_bg2")
                    }
                }
                var saveReceiver=function(cb){
                    var bb_str=""
                    $("input[name=bbUser_selector]").each(function(){
                        if($(this).attr("checked")){
                            var guid=$(this).attr("guid");
                            bb_str=bb_str+guid+",";
                        }
                    })
                    var live_str=""
                    $("input.other_import[name=live_import]").each(function(){
                        var temp=$(this);
                        if(temp.attr("checked")){
        <%--var name=temp.attr("value");--%>
                                    var email=temp.attr("email");
                                    live_str=live_str+email+",";
                                }
                            })
                            bb_str=bb_str.substr(0, bb_str.length-1);
                            live_str=live_str.substr(0, live_str.length-1);
                            var receiverList={
                                bboxReceiverGuidList:bb_str,
                                emailReceiverStr:$("#email_importor").val(),
                                live_import:live_str
                            }
                            $.post("<s:url event='saveReceiver' beanclass='com.blackbox.presentation.action.psevent.PSAjaxEventActionBean'/>"
                            , receiverList
                            ,function(data){
                                if(cb!=null){
                                    cb.call(this)
                                }
                            });
                        }
                        var import_type=null;
                        var fnished=[];
                        var mail_import=function(jid){
                            var import_list=$(".import_link")
                            for (var i=0;i<import_list.length;i++){
                                var target=$(jid)
                                if(import_list[i].id==target.attr("id")){
                                    $(import_list[i]).children("img").attr("src","/library/images/psevent/logo_0"+(i+1)+".jpg");
                                    var type=$(import_list[i]).attr("name")
                                    $("#import_name").text(type);
                                    import_type=type;
                                }else{
                                    $(import_list[i]).children("img").attr("src","/library/images/psevent/logobg_0"+(i+1)+".jpg");
                                }
                            }
                        }
                        var hook=false;
                        var import_receivers=function(){
                            if(import_type=="Live"){
                                window.open("https://blackbox-republic.rpxnow.com/liveid/start?token_url=${actionBean.encodedUrl}", "Import form MS Live")
                                $("#import_face").hide();
                                $("#wait").show();
                                timer = setInterval("time_out_callback();",600);
                            }
                        }
                        var time_out_callback=function(){
                            $.get("<s:url beanclass='com.blackbox.presentation.action.psevent.PSEventRpxActionBean' event='test'/>", {}, function(data){
                                if(data=="finish"){
                                    hook=true;
                                    $("#wait").hide();
                                    $("#import_zone").load("<s:url event='showRpxcontects' beanclass='com.blackbox.presentation.action.psevent.PSAjaxEventActionBean'/>", {}, function(){
                                        clearInterval(timer);
                                    })
                                    $("#import_zone").show();
                                }
                            })
                        }
    </script>
</head>

<body>
    <div class="cho_pep_men">Choose people to invite</div>
    <div id="import_navigation" class="cho_men_bg">
        <ul style="margin: 0pt; padding: 0pt; position: relative; list-style-type: none;">

            <li id="bb_header" class="bla_box_bg2">
                <a href="#bbox" onclick="javascript:render_tab_header('bb')">
                    Blackbox<br /><span class="pre_men_tex1">Your network</span>
                </a>
            </li>
            <li id="email_header" class="ema_add_bg1">
                <a href="#email" onclick="javascript:render_tab_header('email')">
                    Email addresses<br /><span class="pre_men_tex1">Cut &amp; Paste email addresses</span>
                </a>
            </li>
            <li id="import_header" class="imp_bg1">
                <a href="#others" onclick="javascript:render_tab_header('others')">
                    Import<br /><span class="pre_men_tex1">Facebook, GMail, Yahoo! etc</span>
                </a>
            </li>
            <div class="clear"></div>
        </ul>
    </div>
    <div id="bbox" class="bla_box">
        <%@include file="/WEB-INF/jsp/include/ps_event/blackbox_connections.jsp"%>
    </div>
    <div id="email" class="bla_box">
        <div class="cho_pep_sel1">Please make sure email addresses are seperated by commas or are on seperate lines.</div>
        <div class="emd_add_con">
            <div  class="add_con_bg">
                <textarea name="email_importor" id="email_importor"></textarea>
            </div>
        </div>
    </div>
    <div id="others" class="bla_box">
        <div class="imp_con">
            <div class="imp_con_lef">
                <ul>
                    <li><a class="import_link" name="Gmail" id="gmail_import" href="javascript:mail_import('#gmail_import');"><img src="${bb:libraryResource('/library/images/psevent/logo_01.jpg')}" border="0" /></a></li>
                    <li><a class="import_link" name="Live" id="live_import" href="javascript:mail_import('#live_import');"><img src="${bb:libraryResource('/library/images/psevent/logobg_02.jpg')}" border="0" /></a></li>
                    <li style="visibility:hidden;"><a class="import_link" name="Yahoo" id="yahoo_import" href="javascript:mail_import('#yahoo_import');"><img src="${bb:libraryResource('/library/images/psevent/logobg_03.jpg')}" border="0" /></a></li>
                    <li style="visibility:hidden;"><a class="import_link" name="Evite" id="evite_import" href="javascript:mail_import('#evite_import');"><img src="${bb:libraryResource('/library/images/psevent/logobg_04.jpg')}" border="0" /></a></li>
                    <li style="visibility:hidden;"><a class="import_link" name="Plaxo" id="plaxo_import" href="javascript:mail_import('#plaxo_import');"><img src="${bb:libraryResource('/library/images/psevent/logobg_05.jpg')}" border="0" /></a></li>
                    <li style="visibility:hidden;"><a class="import_link" name="AOL" id="aol_import" href="javascript:mail_import('#aol_import');"><img src="${bb:libraryResource('/library/images/psevent/logobg_06.jpg')}" border="0" /></a></li>
                    <li style="visibility:hidden;"><a class="import_link" name="File" id="file_import" href="javascript:mail_import('#file_import');"><img src="${bb:libraryResource('/library/images/psevent/logobg_07.jpg')}" border="0" /></a></li>
                </ul>
            </div>
            <div id="import_face" class="imp_con_rig">
                <div class="det_lef_tit2">Securely login to <span id="import_name">Gmail</span>.</div>
                <div class="imp_but">
                    <a style="color:white;" href="javascript:import_receivers()"><img src="${bb:libraryResource('/library/images/psevent/imp_but.jpg')}" border="0" /></a>
                </div>
            </div>
            <div id="wait" class="loa_con_rig">
                <div class="imp_loa"><img src="${bb:libraryResource('/library/images/psevent/loading.gif')}" border="0" /><br />Importing contacts</div>
            </div>
            <%@include file="/WEB-INF/jsp/include/ps_event/rpx_import.jspf"%>
            <div class="clear"></div>
        </div>
    </div>
    <div class="bla_box_but" style="margin:0 auto;">
        <div class="box_but_rig" style="float:left;">
            <div class="but_gre">
                <a id="last_step_btn" href="javascript:void(0);" ><div class="but_gre_rig"><span style="color:white;">Back to event detail</span></div></a>
            </div>
        </div>
        <div class="box_but_rig">
            <div class="but_gre">
                <a id="add_email_btn" href="javascript:void(0);"> <div class="but_gre_rig"><span style="color:white;">Add emails to guest list</span></div></a>
            </div>
        </div>
        <div class="clear"></div>
    </div>
    <div class="cho_pep_bot"></div>
    <br />
    <br />
</body>

