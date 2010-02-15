package com.blackbox.presentation.action.util;

import net.sourceforge.stripes.action.ActionBeanContext;

import javax.servlet.http.HttpServletRequest;


/**
 * @author A.J. Wright
 */
public class PresentationResourceHolder {

    private static final ThreadLocal<ActionBeanContext> contextThreadLocal = new ThreadLocal<ActionBeanContext>();
    private static final ThreadLocal<HttpServletRequest> requestThreadLocal = new ThreadLocal<HttpServletRequest>();


    public static ActionBeanContext getContext() {
        return contextThreadLocal.get();
    }

    public static void setContext(ActionBeanContext actionBeanContext) {
        contextThreadLocal.set(actionBeanContext);
    }

    public static void setRequest(HttpServletRequest request) {
        requestThreadLocal.set(request);
    }

    public static HttpServletRequest getRequest() {
        return requestThreadLocal.get();
    }

    public static void clear() {
        contextThreadLocal.remove();
        requestThreadLocal.remove();
    }

}
