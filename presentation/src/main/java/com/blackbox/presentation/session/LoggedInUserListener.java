package com.blackbox.presentation.session;

import static com.blackbox.presentation.extension.DefaultBlackBoxContext.USER_KEY;
import static com.blackbox.presentation.action.util.PresentationUtil.getSpringContext;
import com.blackbox.user.User;
import org.springframework.web.context.WebApplicationContext;
import static org.springframework.web.context.WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE;

import javax.servlet.http.*;

/**
 * @author A.J. Wright
 */
public class LoggedInUserListener implements HttpSessionListener, HttpSessionAttributeListener {

    @Override
    public void attributeAdded(HttpSessionBindingEvent httpSessionBindingEvent) {
        if (httpSessionBindingEvent.getName().equals(USER_KEY)) {
            User user = (User) httpSessionBindingEvent.getValue();

            HttpSession session = httpSessionBindingEvent.getSession();

            DefaultUserSessionService svc = getUserSessionService(session);
            svc.addOnlineUser(user.getGuid());
        }
    }

    @Override
    public void attributeRemoved(HttpSessionBindingEvent httpSessionBindingEvent) {
        if (httpSessionBindingEvent.getName().equals(USER_KEY)) {
            User user = (User) httpSessionBindingEvent.getValue();

            HttpSession session = httpSessionBindingEvent.getSession();

            DefaultUserSessionService svc = getUserSessionService(session);
            svc.removeOnlineUser(user.getGuid());
        }
    }

    @Override
    public void attributeReplaced(HttpSessionBindingEvent httpSessionBindingEvent) {
        if (httpSessionBindingEvent.getName().equals(USER_KEY)) {
            User user = (User) httpSessionBindingEvent.getValue();

            HttpSession session = httpSessionBindingEvent.getSession();

            DefaultUserSessionService svc = getUserSessionService(session);
            svc.addOnlineUser(user.getGuid());
        }
    }

    @Override
    public void sessionCreated(HttpSessionEvent httpSessionEvent) {
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent httpSessionEvent) {
        HttpSession session = httpSessionEvent.getSession();
        User u = (User) session.getAttribute(USER_KEY);
        if (u != null) {
            DefaultUserSessionService svc = getUserSessionService(session);
            // Not sure why there is a null pointer here, make sure we handle anyways.
            if (u.getGuid() != null) {
                svc.removeOnlineUser(u.getGuid());
            }
        }
    }

    protected DefaultUserSessionService getUserSessionService(HttpSession session) {
        WebApplicationContext appContext = getSpringContext(session);

        return (DefaultUserSessionService)
                appContext.getBean("userSessionService", DefaultUserSessionService.class);
    }

}
