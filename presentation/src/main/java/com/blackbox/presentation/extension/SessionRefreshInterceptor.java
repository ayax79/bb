package com.blackbox.presentation.extension;

import net.sourceforge.stripes.controller.Interceptor;
import net.sourceforge.stripes.controller.ExecutionContext;
import net.sourceforge.stripes.controller.Intercepts;
import net.sourceforge.stripes.controller.LifecycleStage;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.ActionBeanContext;
import com.blackbox.presentation.action.util.PresentationUtil;
import com.blackbox.presentation.session.UserSessionService;
import org.springframework.web.context.WebApplicationContext;

/**
 * @author A.J. Wright
 */
@Intercepts(LifecycleStage.EventHandling)
public class SessionRefreshInterceptor implements Interceptor {

    @Override
    public Resolution intercept(ExecutionContext context) throws Exception {
        ActionBeanContext beanContext = context.getActionBeanContext();
        if (beanContext != null && beanContext instanceof BlackBoxContext) {
            WebApplicationContext applicationContext = PresentationUtil.getSpringContext(context.getActionBeanContext().getRequest().getSession());
            UserSessionService service = (UserSessionService) applicationContext.getBean("userSessionService");
            service.rebuildContextIfNeeded((BlackBoxContext) beanContext);
        }
        return context.proceed();
    }
}
