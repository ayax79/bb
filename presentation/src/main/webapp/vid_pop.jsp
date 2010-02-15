<%@include file="/WEB-INF/jsp/include/taglibs.jspf"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml"><!-- Source is http://localhost:8080/main -->
<head>
	<title>Main</title>


	<link rel="stylesheet" href="${bb:libraryResource('/library/css/main.css')}" type="text/css" media="screen, projection" />
	<link rel="stylesheet" href="${bb:libraryResource('/library/css/my_n.css')}" type="text/css" media="screen, projection" />
	<link rel="stylesheet" href="${bb:libraryResource('/css/jquery.lightbox-0.5.css')}" type="text/css" />
	<link rel="stylesheet" href="${bb:libraryResource('/library/css/tagcloud.css')}" type="text/css" media="screen, projection" />
	<link rel="stylesheet" href="${bb:libraryResource('/library/css/vid_pop.css')}" type="text/css" media="screen, projection" />

</head>

<body style="background-image:url(${bb:libraryResource('/library/images/bg/rusty.jpg')}); font-family:Helvetica;">
<div class="vid_but_red" style="left:100px;top:300px;position: absolute;"> <img src="${bb:libraryResource('/library/images/persona/vid_but_red.png')}" /></div>
<div class="vid_tc_bg" style="left:100px;top:300px;position: absolute;">
	<div>Change privacy:</div>
	<div><input type="radio" name="radiobutton" value="radiobutton" />World</div>
	<div><input type="radio" name="radiobutton" value="radiobutton" />BB</div>
	<div><input type="radio" name="radiobutton" value="radiobutton" />Friends</div>
	<div class="vid_del">Delete video</div>
</div>
</body>
</html>