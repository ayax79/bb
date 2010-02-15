
var change_background_color=function(hex){
    layout_config.backgroundColor=hex;
    $("#event_layout").css('backgroundColor', '#' + hex)
}
var change_text_color=function(hex){
    layout_config.textColor=hex;
    $("#event_layout").css('color', '#' + hex)
}
//var change_layout_type=function(type){
//    crop_config.small.width=235;
//    crop_config.small.height=165;
//    for(var i=0;i<event_layout_type.length;i++){
//        var o=event_layout_type[i];
//        if(o["name"]==type){
//
//            $("#event_layout").addClass(o.clazz);
//            layout_config.layoutType=o.name;
//            if(type=="PORTRAIT"||type=="IMAGE_ONLY"||type=="VIDEO_AND_IMAGE"){
//                crop_config.big.width=598;
//                crop_config.big.height=390;
//                if(crop_box!=null){
//                    var image_src=$("#small_event_image").attr("src");
//                    $("#jcrop_box").text("");
//                    $("#jcrop_box").append("<img  id='small_event_image' src='"+image_src+"' onload='javascript:create_jcrop_for_samll_img();></img>");
//                    sma=true;
//                    $("#big_event_image").css({
//                        width:"598px",
//                        height:"390px",
//                        margin:"0 0 0 0"
//                    })
//                    crop_box.setOptions({
//                        aspectRatio: crop_config.big.width/crop_config.big.height
//                    });
//                }
//
//            }else if(type=="LANDSCAPE"){
//                crop_config.big.width=328;
//                crop_config.big.height=578;
//                if(crop_box!=null){
//                    crop_box.release();
//                    crop_box.setOptions({
//                        aspectRatio: crop_config.big.width/crop_config.big.height
//                    });
//                }
//            }
//        }else{
//            $("#event_layout").removeClass(o.clazz);
//        }
//    }
//}
var change_text_align=function(align){
    for(var i=0;i<event_text_align.length;i++){
        var o=event_text_align[i];
        if(o["name"]==align){
            $("#event_layout").addClass(o.clazz);
            layout_config.textAgiln=o.clazz;
        }else{
            $("#event_layout").removeClass(o.clazz);
        }
    }
}
var change_font=function(font_name){
    $("#font_select").val(font_name);
    for(var i=0;i<event_font.length;i++){
        var o=event_font[i];
        if(o["name"]==font_name){
            $("#event_layout").addClass(o.clazz);
            layout_config.font=o.clazz;
        }else{
            $("#event_layout").removeClass(o.clazz);
        }
    }
}
var heading_option=function(option){
    $("#heading_select").val(option);
    if(option=="show"){
        layout_config.showHeading='show';
        $(".field .title").show();
    }else{
        layout_config.showHeading="hide"
        $(".field .title").hide();
    }
}


var event_layout_type=[
{
    'name':'PORTRAIT',
    'clazz':'event_layout_main_protrait'
},
{
    'name':'LANDSCAPE',
    'clazz':'event_layout_main_landscape'
},
{
    'name':'IMAGE_ONLY',
    'clazz':'event_layout_main_imageonly'
},
{
    'name':'VIDEO_ONLY',
    'clazz':'event_layout_main_videoonly'
},
{
    'name':'VIDEO_AND_IMAGE',
    'clazz':'event_layout_main_videopic'
},
{
    'name':'VIDEO',
    'clazz':'event_layout_main_videoportrait'
}
]
var event_text_align=[
{
    "name":"left",
    "clazz":"event_text_align_left"
},
{
    "name":"center",
    "clazz":"event_text_align_center"
},
{
    "name":"right",
    "clazz":"event_text_align_right"
}
]
var event_font=[
{
    "name":"Arial",
    "clazz":"event_font_arial"
},
{
    "name":"Tahoma",
    "clazz":"event_font_tahoma"
},
{
    "name":"Courier",
    "clazz":"event_font_courier"
}
]
