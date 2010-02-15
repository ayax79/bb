<head>
<title>Event</title>
<%@include file="/WEB-INF/jsp/include/ps_event/ps_event_header.jspf" %>
<%--@elvariable id="actionBean" type="com.blackbox.presentation.action.psevent.PSAjaxEventActionBean"--%>

<c:set var="userType" value="${s:enumName(actionBean.currentUser.type)}" />
<c:set var="event_image_location" value="${actionBean.occasion.layout.transiantImage.location}" />
<c:set var="layoutType" value="${actionBean.occasion.layout.layoutType}" />
<c:set var="textAlign" value="${actionBean.occasion.layout.textAlign}" />
<c:set var="textColor" value="${actionBean.occasion.layout.textColor}" />
<c:set var="backgroundColor" value="${actionBean.occasion.layout.backgroundColor}" />
<c:set var="boxcolor" value="${actionBean.occasion.layout.boxcolor}" />
<c:set var="newLine" value="
"/>

<script type="text/javascript" src="${bb:libraryResource('/library/uploadify/jquery.uploadify.js')}"></script>
<script src="${bb:libraryResource("/library/uploadify/swfobject.js")}" type="text/javascript"></script>
<script src="http://maps.google.com/maps?file=api&amp;v=2&amp;sensor=false&amp;key=ABQIAAAAp9yvzzCMVz8SKrPHBMtKBhRoR84c2uF6PsISXXf8g1VBgaojsRSd3xfJ2bYFyKbTkZ4c9b7tStQrFw" type="text/javascript"></script>

<script type="text/javascript">
$(document).ready(function() {

    
    if("true" == "${actionBean.occasion.withGoogleMap}") {
        create_gmap();
    } else {
        $("#event_layout_right").hide();
        $("#event_layout_left").css('width', '100%');
    }


    // setup the uploader for event images
    $('#userselectedimage').uploadify({
        'uploader': '${bb:libraryResource("/library/uploadify/uploadify.swf")}',
        'cancelImg': '${bb:libraryResource("/library/uploadify/cancel.png")}',
        'fileDesc' : 'User Image File',
        'fileExt': '*.jpg;*.jpeg;*.gif;*.png',
        'buttonImg' : '${bb:libraryResource("/library/images/events/upload_photo.png")}',
        'width' : '589',
        'height' : '40',
        'wmode'          : 'transparent',
        'script'    : '<s:url beanclass="com.blackbox.presentation.action.psevent.PSAjaxEventActionBean" />;jsessionid=<%=session.getId()%>',
        'scriptData' : {'_eventName' : 'addMediaToEvent'},
        'auto'      : true,
        'queueID'        : 'upload_image_dialog',
        'fileDataName' : 'fileData',
        'onError'   : function(event, queueID, fileObj, errorObj) {},
        'onProgress':function(event,queueID,fileObj,data){},
        'onComplete' : function(filePath,event,queueID,fileObj,resp,data) {
            var src='<s:url beanclass="com.blackbox.presentation.action.media.SessionImageActionBean"/>?'+new Date().getMilliseconds();
            sma=true;
            $("#big_event_image").attr("src", src);

        },
        'multi' : false
    });

    // setup the uploader for background images (reqires promoter)
    $('#userselectedbackground').uploadify({
        'uploader': '${bb:libraryResource("/library/uploadify/uploadify.swf")}',
        'cancelImg': '${bb:libraryResource("/library/uploadify/cancel.png")}',
        'fileDesc' : 'User Background File',
        'fileExt': '*.jpg;*.jpeg;*.gif;*.png',
        'buttonImg' : '${bb:libraryResource("/library/images/events/event_color_selector_bg.png")}',
        'width' : '29',
        'height' : '29',
        'wmode'          : 'transparent',
        'script'    : '<s:url beanclass="com.blackbox.presentation.action.psevent.PSAjaxEventActionBean" />;jsessionid=<%=session.getId()%>',
        'scriptData' : {'_eventName' : 'addMediaToEvent'},
        'auto'      : true,
        'queueID'        : 'upload_image_dialog',
        'fileDataName' : 'fileData',
        'onError'   : function(event, queueID, fileObj, errorObj) {},
        'onProgress':function(event,queueID,fileObj,data){},
        'onComplete' : function(filePath,event,queueID,fileObj,resp,data) {
            var src='<s:url beanclass="com.blackbox.presentation.action.media.SessionImageActionBean"/>?'+new Date().getMilliseconds();
            sma=true;
            $("#event_preview_container").css('backgroundImage','url(' + src +')');
            $("#background_image_selector").css('backgroundImage','url(' + src +')');


        },
        'multi' : false
    });

    // setup the uploader for event videos
    $('#userselectedvideo').uploadify({
        'uploader': '${bb:libraryResource("/library/uploadify/uploadify.swf")}',
        'cancelImg': '${bb:libraryResource("/library/uploadify/cancel.png")}',
        'fileDesc' : 'User Video File',
        'fileExt': '*.mov;*.mp4;*.m4v;*.swf',
        'buttonImg' : '/library/images/events/default_video.jpg',
        'width' : '230',
        'height' : '141',
        'wmode'          : 'transparent',
        'script'    : '<s:url beanclass="com.blackbox.presentation.action.psevent.PSAjaxEventActionBean" />;jsessionid=<%=session.getId()%>',
        'scriptData' : {'_eventName' : 'addMediaToEvent'},
        'auto'      : true,
        'queueID'        : 'upload_image_dialog',
        'fileDataName' : 'fileData',
        'onError'   : function(event, queueID, fileObj, errorObj) {},
         'onProgress':function(event,queueID,fileObj,data){
        },
        'onComplete' : function(filePath,event,queueID,fileObj,resp,data) {
            var src='<s:url beanclass="com.blackbox.presentation.action.media.SessionImageActionBean"/>?'+new Date().getMilliseconds();
            sma=true;
            //$("#jcrop_box").append("<img  id='small_event_image' src='"+src+"' onload='javascript:create_jcrop_for_samll_img();' onerror='Activity.Utils.imgPoll(event, this);' ></img>");


            $("#video_preview").hide();
            $("#video_player").show();
            $("#video_player").href(src);
            


        },
        'multi' : false
    });



    // set the defaults
    setDefaults();



});

// don't slide the image upload button on the first load
var subsequent = false;
function slideBtn() {
    if(subsequent) {
        $("#buttonslider").animate({
            top: ($("#big_event_image").height() - 40)
        }, 1000);
    } else {
        subsequent = true;
    }
}


function setTextColor(color) {
    $('#text_color_selector').css('backgroundColor', '#' + color);
    $('#event_layout_box').css('color', '#' + color);
    $('#special_preview_hr').css('backgroundColor', '#' + color);
    $('#special_preview_hr').css('color', '#' + color);
    $("#textColor").val(color);
}

function setBackgroundColor(color) {
    $('#background_color_selector').css('backgroundColor', '#' + color);
    $('#event_preview_container').css('backgroundColor', '#' + color);
    $("#backgroundColor").val(color);

}

function setBoxColor(color) {
    $('#box_color_selector').css('backgroundColor', '#' + color);
    $('#event_layout_box').css('backgroundColor', '#' + color);
    $("#boxcolor").val(color);
}


function setLayoutType(objid){

       // doesn't exist in UI

    // turn off any that are currently on
    $("#event_layout_photo_only").removeClass('event-layout-photo-only-on');
    $("#event_layout_video_only").removeClass('event-layout-video-only-on');
    $("#event_layout_photo_text").removeClass('event-layout-photo-text-on');
    $("#event_layout_video_text").removeClass('event-layout-video-text-on');
    $("#event_layout_photo_video_text").removeClass('event-layout-photo-video-text-on');

    $("#event_layout_photo_only").addClass('event-layout-photo-only-off');
    $("#event_layout_video_only").addClass('event-layout-video-only-off');
    $("#event_layout_photo_text").addClass('event-layout-photo-text-off');
    $("#event_layout_video_text").addClass('event-layout-video-text-off');
    $("#event_layout_photo_video_text").addClass('event-layout-photo-video-text-off');


    if( "event_layout_photo_only" == objid ) {
        $("#event_layout_photo_only").addClass('event-layout-photo-only-on');
        $("#photo_background").show();
        $("#event_layout_box").hide();
        $("#layoutType").val("IMAGE_ONLY");
    }
    if( "event_layout_video_only" == objid ) {
        $("#event_layout_video_only").addClass('event-layout-video-only-on');
        $("#photo_background").hide();
        $("#event_layout_box").show();
        $("#video_preview").show();
        $("#layoutType").val("VIDEO_ONLY");
    }
    if( "event_layout_photo_text" == objid ) {
        $("#event_layout_photo_text").addClass('event-layout-photo-text-on');
        $("#photo_background").show();
        $("#event_layout_box").show();
        $("#video_preview").hide();
        $("#layoutType").val("PORTRAIT");
    }
    if( "event_layout_video_text" == objid ) {
        $("#event_layout_video_text").addClass('event-layout-video-text-on');
        $("#photo_background").hide();
        $("#event_layout_box").show();
        $("#video_preview").show();
        $("#layoutType").val("VIDEO");
    }
    if( "event_layout_photo_video_text" == objid ) {
        $("#event_layout_photo_video_text").addClass('event-layout-photo-video-text-on');
        $("#photo_background").show();
        $("#event_layout_box").show();
        $("#video_preview").show();
        $("#layoutType").val("LANDSCAPE");
    }

}

function setLayoutAlign(objid) {

    // turn off any that are currenlty on
    $("#event_text_align_left").removeClass('event-layout-text-left-on');
    $("#event_text_align_center").removeClass('event-layout-text-center-on');
    $("#event_text_align_right").removeClass('event-layout-text-right-on');

    $("#event_text_align_left").addClass('event-layout-text-left-off');
    $("#event_text_align_center").addClass('event-layout-text-center-off');
    $("#event_text_align_right").addClass('event-layout-text-right-off');

    if("event_text_align_left" == objid) {
        $("#event_text_align_left").addClass('event-layout-text-left-on');
        $("#event_layout_box").css('textAlign', "left");
    }

    if("event_text_align_center" == objid) {
        $("#event_text_align_center").addClass('event-layout-text-center-on');
        $("#event_layout_box").css('textAlign', "center");
    }

    if("event_text_align_right" == objid) {
        $("#event_text_align_right").addClass('event-layout-text-right-on');
        $("#event_layout_box").css('textAlign', "right");
    }

    $("#textAlign").val(objid);
}


function setDefaults() {
    // need to get the defaults and call the proper functions to setup the form

    if($("#textAlign").val() == "") {
        $("#textAlign").val("event_text_align_left");
    }
    if($("#boxcolor").val() == "") {
        $("#boxcolor").val("666666");
    }
    if($("#backgroundColor").val() == "") {
        $("#backgroundColor").val("cccccc");
    }
    if($("#textColor").val() == "") {
        $("#textColor").val("ffffff");
    }


    setLayoutType("event_layout_photo_text");
    setLayoutAlign($("#textAlign").val());
    setBoxColor($("#boxcolor").val());
    setBackgroundColor($("#backgroundColor").val());
    setTextColor($("#textColor").val());


$('#text_color_selector').ColorPicker({
	color: '#ffffff',
	onShow: function (colpkr) {
		$(colpkr).fadeIn(200);
		return false;
	},
	onHide: function (colpkr) {
		$(colpkr).fadeOut(200);
		return false;
	},
	onChange: function (hsb, hex, rgb) {
		$('#text_color_selector').css('backgroundColor', '#' + hex);
        $('#event_layout_box').css('color', '#' + hex);
        $('#special_preview_hr').css('backgroundColor', '#' + hex);
        $('#special_preview_hr').css('color', '#' + hex);
        $("#textColor").val(hex);
	}
});


$('#background_color_selector').ColorPicker({
	color: '#$cccccc',
	onShow: function (colpkr) {
		$(colpkr).fadeIn(200);
		return false;
	},
	onHide: function (colpkr) {
		$(colpkr).fadeOut(200);
		return false;
	},
	onChange: function (hsb, hex, rgb) {
		$('#background_color_selector').css('backgroundColor', '#' + hex);
        $('#event_preview_container').css('backgroundColor', '#' + hex);
        $("#backgroundColor").val(hex);

	}
});

    
$('#box_color_selector').ColorPicker({
    color: '#333333',
	onShow: function (colpkr) {
		$(colpkr).fadeIn(200);
		return false;
	},
	onHide: function (colpkr) {
		$(colpkr).fadeOut(200);
		return false;
	},
	onChange: function (hsb, hex, rgb) {
		$('#box_color_selector').css('backgroundColor', '#' + hex);
        $('#event_layout_box').css('backgroundColor', '#' + hex);
        $("#boxcolor").val(hex);
	}
});

}

var create_gmap=function(){

    try{
        var map = new GMap2(document.getElementById("google_map_canvas"));
        var geocoder = new GClientGeocoder();
        var address="${actionBean.occasion.address.address1},${actionBean.occasion.address.city},${actionBean.occasion.address.state},USA";
        geocoder.getLatLng(
        address,
        function(point) {
            if (point) {
                map.setCenter(point, 13);
                var marker = new GMarker(point);
                map.addOverlay(marker);
            }
        }
    );
    }catch(e){alert(e);};

}

var saveConfig=function(){
$.post("<s:url beanclass='com.blackbox.presentation.action.psevent.PSAjaxEventActionBean'/>;jsessionid=<%=session.getId()%>",
    {
        // rhayes confirm fields with AJ.  Why are these not ALL in the occasion.layout obj?
        "occasion.layout.layoutType": $("#layoutType").val(),
        "occasion.layout.textAlign": $("#textAlign").val(),
        "occasion.layout.textColor": $("#textColor").val(),
        "occasion.layout.backgroundColor": $("#backgroundColor").val(),
        "occasion.layout.boxcolor": $("#boxcolor").val(),
        //"headerFont": $("#headerFont").val(),
        //"font": $("#font").val(),
        //"headerSize": $("#headerSize").val(),
        //"bodySize": $("#bodySize").val(),
        "occasion.layout.dataFormat": "MMDDYYYY",  // typo on name should be "dateFormat"
        "_eventName": "saveEventLayout"
    },

    function(data){
          if("success" == data) {
            window.location.href="<s:url beanclass='com.blackbox.presentation.action.psevent.PSConfigEventActionBean'/>;jsessionid=<%=session.getId()%>?_eventName=guestList";
          } else {
              // it's bad, but we don't want to show the users...
              //alert(data);
          }
    });
};



</script>



</head>
<c:set value='yes' var='noStream' scope='request'/>
<body>
    <div class="container container-top"></div>
    <div class="container darken">
        <form name="ccustomise_event_form" autocomplete="off" onsubmit="return false;">

			<h1 class="white event">Customize your event</h1>

        <div class="widget-top" style='margin-left: 11px; margin-right: 11px;'><div class="widget-top-right"></div></div>
        <div id='event-body' class="event-body clearfix">
            <div id="event-workflow2">
                <div id="event-workflow-box1"></div>
            </div>


            <div style="margin: 10px 3px 10px 4px;">

                <%-- left side --%>
                <div style="float: left; width: 280px;">
                    <div style="background:url(/library/images/events/event_customize_head_bg.png); width: 265px; height: 54px; font-weight: bold; font-size: 18px; line-height: 54px; padding-left: 15px;">Customize your invitation</div>
                    <div style="width: 280px; height: 720px; background-color: #ecf7fd;">
                        <div style="padding: 20px 10px 10px 10px;">


                        <c:choose>
                            <c:when test="${userType eq 'PROMOTER'}">

                                <%-- group 1  promoter --%>
                                <table cellpadding="0" cellspacing="0" border="0" width="100%">
                                    <tr>
                                        <td valign="top" align="center" width="33%">
                                            <div id="event_layout_photo_only" class='event-layout-photo-only-off' onclick="setLayoutType(this.id);"></div>
                                            <div class="event-layout-label">PHOTO</div>
                                        </td>
                                        <td valign="top" align="center" width="33%">
                                            <div id="event_layout_video_only" class='event-layout-video-only-off' onclick="setLayoutType(this.id);"></div>
                                            <div class="event-layout-label">VIDEO</div>
                                        </td>
                                        <td valign="top" align="center" rowspan="2" width="33%">
                                            <div id="event_layout_photo_video_text" class='event-layout-photo-video-text-off' onclick="setLayoutType(this.id);"></div>
                                            <div class="event-layout-label">PHOTO/VIDEO/TEXT</div>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td valign="top" align="center">
                                            <div id="event_layout_photo_text" class="event-layout-photo-text-off" onclick="setLayoutType(this.id);"></div>
                                            <div class="event-layout-label">PHOTO/TEXT</div>
                                        </td>
                                        <td valign="top" align="center">
                                            <div id="event_layout_video_text" class="event-layout-video-text-off" onclick="setLayoutType(this.id);"></div>
                                            <div class="event-layout-label">VIDEO/TEXT</div>
                                        </td>
                                    </tr>
                                </table>

                            </c:when>

                            <c:otherwise>

                                <%-- group 1 user --%>
                                <table cellpadding="0" cellspacing="0" border="0" width="100%">
                                    <tr>
                                        <td valign="top" align="center" width="33%">
                                            <div id="event_layout_photo_only" class='event-layout-photo-only-off' onclick="setLayoutType(this.id);"></div>
                                            <div class="event-layout-label">PHOTO</div>
                                        </td>
                                        <td valign="top" align="center" width="33%">
                                            <div id="event_layout_photo_text" class='event-layout-photo-text-off' onclick="setLayoutType(this.id);"></div>
                                            <div class="event-layout-label">PHOTO/TEXT</div>
                                        </td>
                                        <td valign="top" align="center" rowspan="2" width="33%">
                                            <%--
                                            <div id="event_layout_photo_video_text" class='event-layout-photo-video-text-off' onclick="setLayoutType(this.id);"></div>
                                            <div class="event-layout-label">PHOTO/VIDEO/TEXT</div>
                                            --%>
                                        </td>
                                    </tr>
                                </table>

                            </c:otherwise>
                        </c:choose>







                            <hr style="margin-bottom: 10px;">

                            <%-- group 2 --%>
                            <table cellpadding="0" cellspacing="0" border="0" width="100%">
                                <tr>
                                    <td valign="top" align="center" width="33%">
                                        <div id="event_text_align_left" class="event-layout-text-left-off" onclick="setLayoutAlign(this.id);"></div>
                                        <div class="event-layout-label">LEFT ALIGNED</div>
                                    </td>
                                    <td valign="top" align="center" width="33%">
                                        <div id="event_text_align_center" class="event-layout-text-center-off" onclick="setLayoutAlign(this.id);"></div>
                                        <div class="event-layout-label">CENTER ALIGNED</div>
                                    </td>
                                    <td valign="top" align="center" width="33%">
                                        <div id="event_text_align_right" class="event-layout-text-right-off" onclick="setLayoutAlign(this.id);"></div>
                                        <div class="event-layout-label">RIGHT ALIGNED</div>
                                    </td>
                                </tr>
                            </table>

                            <hr style="margin-bottom: 10px;">

                            <table cellpadding="0" cellspacing="0" border="0" width="100%">
                                <tr>
                                    <td width="25%" align="center">
                                        <div id='text_color_selector'></div>
                                        <div class="event-layout-label">TEXT<br />COLOR</div>
                                    </td>
                                    <td width="25%" align="center">
                                        <div id='background_color_selector'></div>
                                        <div class="event-layout-label">BACKGROUND<br />COLOR</div>
                                    </td>
                                    <td width="25%" align="center">
                                        <div id='box_color_selector'></div>
                                        <div class="event-layout-label">BOX<br />COLOR</div>
                                    </td>
                                    <td width="25%" align="center">

                                        <c:choose>
                                            <c:when test="${userType eq 'PROMOTER'}">
                                                 <div id='background_image_selector'><input type="file" id="userselectedbackground" name="userselectedbackground" /></div>
                                                 <div class="event-layout-label">BACKGROUND<br />IMAGE</div>
                                            </c:when>
                                        </c:choose>


                                    </td>
                                </tr>
                            </table>

                        </div>

                    </div>
                </div>

                <%-- right side --%>
                <div style="float: left; width: 631px; margin-left: 10px;">
                    <div style="background:url(/library/images/events/event_preview_head_bg.png); width: 616px; height: 54px; font-weight: bold; font-size: 18px; line-height: 54px; padding-left: 15px;">Preview your design</div>
                    <div id="event_preview_container" style="border: 1px solid #f2f2f2; padding: 9px; background-color: #fff;">


                        <div id='photo_background' style="width: 591px; padding: 10px; background-color: #fff;">

                            <div id="image_zone" class="event_media_pic" style="position:relative;">

                                <c:choose>
                                    <c:when test="${not empty com.blackbox.presentation.SESSION_IMAGE}">
                                        <img id="big_event_image" src="<s:url beanclass="com.blackbox.presentation.action.media.SessionImageActionBean"/>" onload="slideBtn();" />
                                    </c:when>
                                    <c:when test="${not empty event_image_location}">
                                        <img id="big_event_image" src="${event_image_location}" width="589" onload="slideBtn();" />
                                    </c:when>
                                    <c:otherwise>
                                        <img id="big_event_image" src="${bb:libraryResource('/library/images/events/event_default_photo.png')}" width="589" onload="slideBtn();" />
                                    </c:otherwise>
                                </c:choose>

                                <div>
                                    <div id="buttonslider" style="position: absolute; top: 174px; left: 0px;"><input id='userselectedimage'name='userselectedimage' type='file'/></div>
                                </div>
                            </div>

                        </div>
                        <div id="photo_shadow"></div>

                         <%-- ${actionBean.occasion} --%>  
                        <div id="event_layout_box" style="width: 591px; overflow: auto; padding: 10px; margin-top: 10px; color: #000000; background-color: #cccccc; text-align: left;">

                            <div id="event_layout_left" style="width: 335px; float: left;">
                                <div id='preview_event_title' style="font-weight: bold; font-size: 16px;">${actionBean.occasion.name}</div>

                                <div id="preview_event_body" style="margin-top: 10px; font-size: 12px;">

                                    ${f:replace(actionBean.occasion.description, newLine, "<br/>")}

                                    <hr id="special_preview_hr" style='color: #f00; background-color: #f00; height: 1px; margin-top: 10px;'>

                                    <table cellpadding="0" cellspacing="10" border="0" width="100%">
                                        <tr>
                                            <td align="right" valign='top' style="font-weight: bold;">
                                                When
                                            </td>
                                            <td valign='top' width="100%">
                                                ${bb:formatDate(actionBean.occasion.eventTime, "M/dd/yyyy")}
                                                <br />
                                                ${bb:formatDate(actionBean.occasion.eventTime, "H:mm a")}  to ${bb:formatDate(actionBean.occasion.eventEndTime, "H:mm a")}
                                            </td>
                                        </tr>
                                        <tr>
                                            <td align="right" valign="top" style="font-weight: bold;">
                                                Where
                                            </td>
                                            <td>
                                                ${actionBean.occasion.address.address1}
                                                <br />
                                                ${actionBean.occasion.address.address2}
                                                <br />
                                                ${actionBean.occasion.address.city}, ${actionBean.occasion.address.state} &nbsp; ${actionBean.occasion.address.zipCode}
                                            </td>
                                        </tr>
                                        <tr>
                                            <td align="right" valign="top" style="font-weight: bold;">
                                                Host
                                            </td>
                                            <td valign="top">
                                                ${actionBean.occasion.hostBy}
                                                <br />
                                                ${actionBean.occasion.phoneNumber}
                                                <br />
                                                ${actionBean.occasion.email}
                                                <br />
                                                ${actionBean.occasion.eventUrl}
                                            </td>
                                        </tr>
                                    </table>


                                </div>
                            </div>
                            <div id="event_layout_right" style="width: 245px; float: right;">
                                <div id="video_preview">
                                    <input type="file" id="userselectedvideo" name="userselectedvideo">
                                </div>


                                <div id="google_map_canvas" style="height: 145px; background-color: #fff;">
                                    
                                </div>

                            </div>

                        </div>

                        <div style="clear: both;"></div>

                    </div>

                </div>

                <div  style="clear: both;"></div>


                <input type="hidden" name="layoutType" id="layoutType" value="${layoutType}">
                <input type="hidden" name="textAlign" id="textAlign" value="${textAlign}">
                <input type="hidden" name="textColor" id="textColor" value="${textColor}">
                <input type="hidden" name="backgroundColor" id="backgroundColor" value="${backgroundColor}">
                <input type="hidden" name="boxcolor" id="boxcolor" value="${boxcolor}">
                
                <input type="hidden" name="images" id="images" value="${event_image_location}">

            </div>






            <div style="clear:both;">
                <div style="float: right; cursor: pointer;"><img src='${bb:libraryResource('/library/images/events/design_btn3.png')}' onclick="saveConfig();"></div>
            </div>

         </div>
    </div>






































</body>

