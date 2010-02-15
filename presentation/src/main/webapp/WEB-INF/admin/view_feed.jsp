<%@include file="/WEB-INF/jsp/include/page_header.jspf" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <title><fmt:message key="pageTitlePrefix"/> : Administration</title>
    <link rel="stylesheet" href="${bb:libraryResource('/library/css/admin.css')}" type="text/css"
          media="screen, projection"/>
</head>

<body>
<div class="container container-top"></div>
<div class="container darken">
    <div class="main-content">
        <h1 class="white">Blackbox Events Administration</h1>

        <div class="span-24 main-col">
            <div class="settings-content">
                ${actionBean.feed}
            </div>
            <div class="widget-bottom">
                <div class="widget-bottom-right"></div>
            </div>
        </div>
    </div>
</div>
</body>
</html>