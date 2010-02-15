<%--@elvariable id="actionBean" type="com.blackbox.presentation.action.security.LoginActionBean"--%>
<%@ include file="/WEB-INF/jsp/include/page_header.jspf" %>

<html>
<head>
    <title><fmt:message key="pageTitlePrefix" /> : <fmt:message key="pageTitle.login" /></title>
    <link rel="stylesheet" href="${bb:libraryResource('/library/css/login.css')}" type="text/css" media="screen, projection"/>
</head>
<body>


<div class="login-container">
    <c:if test="${not empty actionBean.context.validationErrors}">
        <div class="login-errors">
            <ui:roundedBox className="rounded-comment xplr-results-container">
                <s:errors/>
            </ui:roundedBox>
        </div>
    </c:if>

    <c:if test="${not empty loginMessages}">
        <div class="login-errors">
            <ui:roundedBox className="rounded-comment xplr-results-container">
                <s:messages key="loginMessages"/>
            </ui:roundedBox>
        </div>
    </c:if>
    <!--[if lt IE 7]>
    <div style='border: 1px solid #F7941D; background: #FEEFDA; text-align: center; clear: both; height: 75px; position: relative;'>
      <div style='position: absolute; right: 3px; top: 3px; font-family: courier new; font-weight: bold;'><a href='#' onclick='javascript:this.parentNode.parentNode.style.display="none"; return false;'><img src='http://www.ie6nomore.com/files/theme/ie6nomore-cornerx.jpg' style='border: none;' alt='Close this notice'/></a></div>
      <div style='width: 640px; margin: 0 auto; text-align: left; padding: 0; overflow: hidden; color: black;'>
        <div style='width: 75px; float: left;'><img src='http://www.ie6nomore.com/files/theme/ie6nomore-warning.jpg' alt='Warning!'/></div>
        <div style='width: 275px; float: left; font-family: Arial, sans-serif;'>
          <div style='font-size: 14px; font-weight: bold; margin-top: 12px;'>You are using an outdated browser</div>
          <div style='font-size: 12px; margin-top: 6px; line-height: 12px;'>To use Blackbox Republic, please upgrade to a modern web browser.</div>
        </div>
        <div style='width: 75px; float: left;'><a href='http://www.firefox.com' target='_blank'><img src='http://www.ie6nomore.com/files/theme/ie6nomore-firefox.jpg' style='border: none;' alt='Get Firefox 3.5'/></a></div>
        <div style='width: 75px; float: left;'><a href='http://www.browserforthebetter.com/download.html' target='_blank'><img src='http://www.ie6nomore.com/files/theme/ie6nomore-ie8.jpg' style='border: none;' alt='Get Internet Explorer 8'/></a></div>
        <div style='width: 73px; float: left;'><a href='http://www.apple.com/safari/download/' target='_blank'><img src='http://www.ie6nomore.com/files/theme/ie6nomore-safari.jpg' style='border: none;' alt='Get Safari 4'/></a></div>
        <div style='float: left;'><a href='http://www.google.com/chrome' target='_blank'><img src='http://www.ie6nomore.com/files/theme/ie6nomore-chrome.jpg' style='border: none;' alt='Get Google Chrome'/></a></div>
      </div>
    </div>
    <![endif]-->


    <div class="login">
        <div class="login-contents">
            <h1 class="bbform-h1">Login<span>to the Republic, or <s:link href='/register'>sign up</s:link>!</span></h1>
            <%--<a class="rpxnow" onclick="return false;"--%>
               <%--href="https://blackbox-republic.rpxnow.com/openid/v2/signin?token_url=${actionBean.tokenUrl}">--%>
                <%--Use RPX--%>
            <%--</a>--%>
            <s:form beanclass="com.blackbox.presentation.action.security.LoginActionBean" title="Login" class="bbform">
                <fieldset>
                    <ul id="login">

                        <li>
                            <label for="login_username"><fmt:message key="login.field.username"/><em>*</em></label>
                            <span class="textinput37"><s:text id="login_username" name="user.username" /></span>
                        </li>
                        <li>
                            <label for="login_password"><fmt:message key="login.field.password"/><em>*</em></label>
                            <span class="textinput37"><s:password id="login_password" name="user.password" /></span>
                        </li>
                    </ul>

                </fieldset>
                <div><button name="login" type="submit" class="bbform-button bbbutton"><span>Go</span></button>
                <s:link beanclass="com.blackbox.presentation.action.security.ForgotActionBean" event="begin"
                        style="font-weight:normal;font-size:11px">Forgotten your password?</s:link>
				</div>
				<s:field-metadata  var="fieldMetadata">
				$(function(){
					$.fn.stripesValidation('${fieldMetadata.formId}', ${fieldMetadata});
				});
				</s:field-metadata>
            </s:form>
        </div>
    </div>
</div>
</body>
</html>
