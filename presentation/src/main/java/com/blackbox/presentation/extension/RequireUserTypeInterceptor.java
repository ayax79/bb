package com.blackbox.presentation.extension;

import net.sourceforge.stripes.controller.Interceptor;
import net.sourceforge.stripes.controller.ExecutionContext;
import net.sourceforge.stripes.controller.Intercepts;
import net.sourceforge.stripes.controller.LifecycleStage;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.ActionBean;
import net.sourceforge.stripes.action.ActionBeanContext;
import org.apache.shiro.authz.AuthorizationException;
import com.blackbox.presentation.action.util.RequireUserType;
import com.blackbox.user.User;

/**
 * Check to see if the action contains a {@link com.blackbox.presentation.action.util.RequireUserType}
 * if it does the it will make sure that the current user is of one of the allowed types.
 */
@Intercepts(LifecycleStage.BindingAndValidation)
public class RequireUserTypeInterceptor implements Interceptor {

    @Override
    public Resolution intercept(ExecutionContext executionContext) throws Exception {
        ActionBean bean = executionContext.getActionBean();
        if (bean != null) { // it shouldn't be
            RequireUserType annotation = bean.getClass().getAnnotation(RequireUserType.class);
            if (annotation != null) {
                User.UserType[] types = annotation.value();
                if (types.length != 0) {
                    ActionBeanContext context = executionContext.getActionBeanContext();
                    User user = (User) context.getRequest().getSession().getAttribute(DefaultBlackBoxContext.USER_KEY);

                    // Note that this expects that authorization by existence of a user is handled elsewhere
                    // if there is no user in the session this will simply noop
                    if (user != null) {
                        boolean found = false;
                        for (User.UserType type : types) {
                            if (type == user.getType()) {
                                found = true;
                                break;
                            }
                        }

                        if (!found) {
                            throw new AuthorizationException("A User of the current type cannot access this action");
                        }
                    }
                }
            }
        }
        return executionContext.proceed();
    }
}
