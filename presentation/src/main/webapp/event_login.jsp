<%@include file="/WEB-INF/jsp/include/page_header.jspf" %>
<html>
    <head>
        <title>Event Login</title>
        <link media="screen, projection" type="text/css" href="${bb:libraryResource('/library/css/login.css')}" rel="stylesheet">

    </head>
    <body>
        <div class="login-container">
            <div class="login">
                <div class="login-contents">
                    <h1 class="bbform">Login<span>to the Event, or <s:link beanclass="com.blackbox.presentation.action.security.LoginActionBean">log into Blackbox</s:link>.</span></h1>
                    <s:messages/>
                    <s:form class="bbform" method="post" beanclass="com.blackbox.presentation.action.psevent.PSShowEventActionBean">
                        <fieldset>
                            <ul id="login">
                                <li>
                                    <label for="login_username">Email<em>*</em></label>
                                    <input type="text" class="text-input" name="email" id="login_username"/>

                                </li>
                                <li>
                                    <label for="login_password">Password<em>*</em></label>
                                    <input type="password" class="text-input" name="password" id="login_password"/>

                                </li>

                            </ul>

                        </fieldset>
                        <button class="bbform-button bbbutton" type="submit" name="login"><span>Go</span></button>
                    </s:form>

                </div>
            </div>
        </div>

    </body>
</html>
