/*
 * Copyright (c) 2009. Blackbox, LLC
 * All rights reserved.
 */
package com.blackbox.presentation.action.security;

import com.blackbox.presentation.action.BaseBlackBoxActionBean;
import com.blackbox.presentation.action.util.PresentationUtil;
import com.blackbox.presentation.session.UserSessionService;
import com.blackbox.user.IUserManager;
import com.blackbox.user.User;
import net.sourceforge.stripes.action.*;
import net.sourceforge.stripes.integration.spring.SpringBean;
import net.sourceforge.stripes.validation.*;
import org.jboss.resteasy.spi.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.io.IOException;


/*
*
* Author: Andy Nelsen
* Last Modified Date: $DateTime: $
*/
@UrlBinding("/forgot")
public class ForgotActionBean extends BaseBlackBoxActionBean {

    final private static Logger logger = LoggerFactory.getLogger(ForgotActionBean.class);

    @SpringBean("userManager")
    private IUserManager userManager;


    @SpringBean("userSessionService")
    private UserSessionService userSessionService;

    @ValidateNestedProperties({
            @Validate(field = "email", required = true, on = "resetPassword", converter = EmailTypeConverter.class)
    })
    private User user;


    @DontValidate
    @DefaultHandler
    public Resolution begin() throws IOException {
        return new ForwardResolution("/forgot.jsp");
    }

    @DontValidate
    public Resolution resetPassword() {
       Resolution result = null;
       try {
           userManager.forgotPassword(user.getEmail());
           //getContext().getMessages().add(new SimpleMessage("forgotPassword.email.sent"));
           result = new ForwardResolution("/forgot-success.jsp");
        }
        catch (Exception e) {
            if (PresentationUtil.isExceptionType(e, NotFoundException.class)) {
                ValidationErrors errors = new ValidationErrors();
                errors.addGlobalError(new LocalizableError("forgotPassword.email.notFound", user.getEmail()));
                getContext().setValidationErrors(errors);
                result = getContext().getSourcePageResolution();
                try {
                    // Add a two second delay to keep people from brute forcing
                    Thread.sleep(2000);
                } catch (InterruptedException e1) {
                    // ignored
                }
            }
        }
        
        return result;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setUserManager(IUserManager userManager) {
        this.userManager = userManager;
    }

    public void setUserSessionService(UserSessionService userSessionService) {
        this.userSessionService = userSessionService;
    }

    @Override
    public boolean isHasIntro() {
        return false;
    }
}