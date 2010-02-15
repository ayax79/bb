/*
 *
 * Author:  Artie Copeland
 * Last Modified Date: $DateTime: $
 */
package com.blackbox.presentation.action;

import net.sourceforge.stripes.mock.MockServletContext;
import net.sourceforge.stripes.controller.StripesFilter;
import net.sourceforge.stripes.controller.DispatcherServlet;

import java.util.Map;
import java.util.HashMap;

import com.blackbox.presentation.extension.MockBlackBoxContext;

/**
 * @author Artie Copeland
 * @version $Revision: $
 */
public class StripesInitializer {
    public static void init() {
        MockServletContext context = new MockServletContext("/*");

        // Add the Stripes Filter
        Map<String, String> filterParams = new HashMap<String, String>();
        filterParams.put("ActionResolver.Packages", "com.blackbox.presentation.action,com.blackbox.presentation.action.dev");
//        filterParams.put("Extension.Packages", "com.blackbox.presentation.extension");
        filterParams.put("MultipartWrapper.Class", "net.sourceforge.stripes.controller.multipart.CommonsMultipartWrapper");
        context.addFilter(StripesFilter.class, "StripesFilter", filterParams);
        // Add the Stripes Dispatcher
        context.setServlet(DispatcherServlet.class, "StripesDispatcher", null);

    }

    public static void setContext(BaseBlackBoxActionBean bean) {
        MockBlackBoxContext abcontext = new MockBlackBoxContext();
        bean.setContext(abcontext);        
    }
}
