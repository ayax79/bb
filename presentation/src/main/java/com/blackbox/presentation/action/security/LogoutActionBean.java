/*
 * Copyright (c) 2009. Blackbox, LLC
 * All rights reserved.
 */
package com.blackbox.presentation.action.security;

import com.blackbox.presentation.action.BaseBlackBoxActionBean;
import net.sourceforge.stripes.action.*;

import javax.servlet.http.HttpSession;

import org.apache.shiro.subject.Subject;
import org.apache.shiro.SecurityUtils;

/*
 * Logout the current user.
 * 
 * Original Author:  Artie Copeland
 * Last Modified Date: $DateTime: $
 */
@UrlBinding("/logout")
public class LogoutActionBean extends BaseBlackBoxActionBean {

    @DontValidate
    @HttpCache(allow = false)
    public Resolution logout() {
        Subject subject = SecurityUtils.getSubject();
        if (subject != null) {
            subject.logout();
        }

        HttpSession session = getContext().getRequest().getSession(false);
        if (session != null) {
            session.invalidate();
        }
        return new RedirectResolution(LoginActionBean.class);
    }

    @Override
    public boolean isHasIntro() {
        return false;
    }
}
