package com.blackbox.presentation.action.util;

import org.apache.shiro.web.filter.authc.PassThruAuthenticationFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * A version of the {@link org.apache.shiro.web.filter.authc.PassThruAuthenticationFilter PassThruAuthenticationFilter }
 * that returns a 403 if a jquery ajax request header is found instead of redirecting to login if the user is not
 * authenticated.
 *
 * @author A.J. Wright
 */
public class AjaxFriendlyAuthenticationFilter extends PassThruAuthenticationFilter {

    @Override
    protected void redirectToLogin(ServletRequest request, ServletResponse response) throws IOException {

        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        if (httpServletRequest.getHeader("X-Requested-With") != null) {
            httpServletResponse.sendError(403);
        }
        else {
            super.redirectToLogin(request, response);
        }

    }
}
