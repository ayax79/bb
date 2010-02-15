package com.blackbox.presentation.extension;

import com.blackbox.presentation.action.util.PresentationResourceHolder;
import net.sourceforge.stripes.action.ActionBeanContext;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.controller.ExecutionContext;
import net.sourceforge.stripes.controller.Interceptor;
import net.sourceforge.stripes.controller.Intercepts;
import static net.sourceforge.stripes.controller.LifecycleStage.ActionBeanResolution;

/**
 * @author A.J. Wright
 */
@Intercepts(ActionBeanResolution)
public class ActionBeanContextHolderInterceptor implements Interceptor {

    @Override
    public Resolution intercept(ExecutionContext context) throws Exception {
        ActionBeanContext actionBeanContext = context.getActionBeanContext();
        PresentationResourceHolder.setContext(actionBeanContext);
        return context.proceed();
    }


}
