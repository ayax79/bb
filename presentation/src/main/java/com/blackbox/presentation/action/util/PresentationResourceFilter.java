package com.blackbox.presentation.action.util;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @author A.J. Wright
 */
public class PresentationResourceFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        PresentationResourceHolder.setRequest((HttpServletRequest) request);
        try {
            chain.doFilter(request, response);
        }
        finally {
            PresentationResourceHolder.clear();
        }
    }

    @Override
    public void destroy() {
    }
}
