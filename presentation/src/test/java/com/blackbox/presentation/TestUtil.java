package com.blackbox.presentation;

import net.sourceforge.stripes.action.ActionBean;
import net.sourceforge.stripes.controller.DispatcherServlet;
import net.sourceforge.stripes.controller.StripesFilter;
import net.sourceforge.stripes.mock.MockRoundtrip;
import net.sourceforge.stripes.mock.MockServletContext;
import org.junit.Ignore;
import org.springframework.web.context.WebApplicationContext;

import java.util.HashMap;
import java.util.Map;

/**
 * @author A.J. Wright
 */
@Ignore
public final class TestUtil {

    private TestUtil() {
    }
    
    public static MockRoundtrip buildRoundTrip(WebApplicationContext context, Class<? extends ActionBean> clazz) {
        MockServletContext servletContext = buildMockServletContext(context);
        return new MockRoundtrip(servletContext, clazz);
    }

    public static MockRoundtrip buildRoundTrip(WebApplicationContext context, String url) {
        MockServletContext servletContext = buildMockServletContext(context);
        return new MockRoundtrip(servletContext, url);
    }

    private static MockServletContext buildMockServletContext(WebApplicationContext context) {
        Map<String, String> filterParams = new HashMap<String, String>();
        MockServletContext servletContext = new MockServletContext("test");
        filterParams.put("ActionResolver.Packages", "com.blackbox.presentation.action");
        filterParams.put("Interceptor.Classes", "net.sourceforge.stripes.integration.spring.SpringInterceptor,net.sourceforge.stripes.controller.BeforeAfterMethodInterceptor");
        filterParams.put("Extension.Packages", "com.blackbox.presentation.extension");
        filterParams.put("ActionBeanContext.Class", com.blackbox.presentation.extension.DefaultBlackBoxContext.class.getName());

        servletContext.addFilter(StripesFilter.class, "StripesFilter", filterParams);
        servletContext.setAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE, context);

        // Add the Stripes Dispatcher
        servletContext.setServlet(DispatcherServlet.class, "StripesDispatcher", null);
        return servletContext;
    }

}
