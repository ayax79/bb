/*
 * Copyright (c) 2009. Blackbox, LLC
 * All rights reserved.
 */

/*
 *
 * Author:  Artie Copeland
 * Last Modified Date: $DateTime: $
 */
package com.blackbox.presentation.action;

import com.blackbox.presentation.extension.BlackBoxContext;
import com.blackbox.presentation.session.UserSessionService;
import com.blackbox.foundation.user.*;
import net.sourceforge.stripes.action.*;
import net.sourceforge.stripes.integration.spring.SpringBean;

/**
 * @author Artie Copeland
 * @version $Revision: $
 */
public abstract class BaseBlackBoxActionBean implements ActionBean {

    public static enum ViewType {
        html,
        json,
        xml
    }

    public static enum MenuLocation {
        dashboard,
        event,
        persona,
        messages,
        explore,
        faq,
		notifications,
		settings
    }

    private BlackBoxContext context;
    private ViewType view = ViewType.html;

    @SpringBean("userSessionService")
    private UserSessionService userSessionService;

    @Override
    public void setContext(ActionBeanContext context) {
        this.context = (BlackBoxContext) context;
    }

    @Override
    public BlackBoxContext getContext() {
        return context;
    }

    /**
     * Returns the user that is currently logged in if there is a user logged in.
     *
     * @return the current logged in user, else null.
     */
    public User getCurrentUser() {
        if (context != null) {
            return context.getUser();
        }
        return null;
    }

    /**
     * Returns true if there is a currently authenticated user.
     *
     * @return true if there is a currently authenticated user.
     */
    public boolean isAuthenticated() {
        return getCurrentUser() != null;
    }

    public ViewType getView() {
        return view;
    }

    public void setView(ViewType view) {
        this.view = view;
    }

    public Profile getCurrentProfile() {
        return getContext().getUser().getProfile();
    }


    public MenuLocation getMenuLocation() {
        return MenuLocation.dashboard;
    }

    public void setUserSessionService(UserSessionService userSessionService) {
        this.userSessionService = userSessionService;
    }

    public UserSessionService getUserSessionService() {
        return userSessionService;
    }

    protected Resolution logout() {        
        return new RedirectResolution("/logout");
    }

    public boolean isHasIntro() {
        return false;
    }
    
    public boolean isFirstTime() {
    	return false;
    }

}
