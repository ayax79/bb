package com.blackbox.presentation.action.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.commons.io.IOUtils;
import static org.apache.commons.io.IOUtils.copy;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import static java.lang.Boolean.getBoolean;

/**
 * @author A.J. Wright
 */
public class LibraryResourceFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest servletRequest,
                         ServletResponse servletResponse,
                         FilterChain filterChain) throws IOException, ServletException {

        String libraryDir = System.getProperty("library.parent");

        if (getBoolean("local") && libraryDir != null) {

            HttpServletRequest request = (HttpServletRequest) servletRequest;
            HttpServletResponse response = (HttpServletResponse) servletResponse;

            String requestURI = request.getRequestURI().replace(request.getContextPath(), "");
            File file = new File(libraryDir, requestURI);
            StringWriter writer = new StringWriter();
            copy(new FileReader(file), writer);

            String content = writer.toString().replace("${library.prefix}", request.getContextPath() + "/library");

            byte[] bytes = content.getBytes("utf-8");
            response.setContentType("text/css");
            response.setContentLength(bytes.length);
            copy(new ByteArrayInputStream(bytes), response.getOutputStream());
        }
        else {
            filterChain.doFilter(servletRequest, servletResponse);
        }
    }

    @Override
    public void destroy() {
    }

}
