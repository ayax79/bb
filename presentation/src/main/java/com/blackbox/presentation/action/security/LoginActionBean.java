/*
 * Copyright (c) 2009. Blackbox, LLC
 * All rights reserved.
 */
package com.blackbox.presentation.action.security;

import com.blackbox.presentation.action.BaseBlackBoxActionBean;
import com.blackbox.presentation.action.user.DashBoardActionBean;
import com.blackbox.presentation.session.UserSessionService;
import com.blackbox.foundation.security.BlackBoxAuthenticationToken;
import com.blackbox.foundation.security.SecurityRealmEnum;
import com.blackbox.foundation.user.IUserManager;
import com.blackbox.foundation.user.User;
import net.sourceforge.stripes.action.*;
import net.sourceforge.stripes.integration.spring.SpringBean;
import net.sourceforge.stripes.validation.*;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;

import static org.yestech.lib.crypto.MessageDigestUtils.sha1Hash;


/*
*
* Original Author:  Artie Copeland
* Last Modified Date: $DateTime: $
*/
@UrlBinding("/login")
public class LoginActionBean extends BaseBlackBoxActionBean {

    final private static Logger logger = LoggerFactory.getLogger(LoginActionBean.class);

    @SpringBean("userManager")
    IUserManager userManager;

    @SpringBean("userSessionService")
    UserSessionService userSessionService;

    @ValidateNestedProperties({
            @Validate(field = "username", required = true, on = "login"),
            @Validate(field = "password", required = true, on = "login"),
            @Validate(field = "email", required = true, on = "resetPassword", converter = EmailTypeConverter.class)
    })
    private User user;
    private Boolean rememberMe = Boolean.FALSE;


    @DontValidate
    @DefaultHandler
    public Resolution begin() throws UnsupportedEncodingException {
        return new ForwardResolution("/login.jsp");
    }

    @HttpCache(allow = false)
    public Resolution login() {
        Resolution result = null;
        String plainText = user.getPassword();
        String newStyleHash = sha1Hash(plainText.toLowerCase());
        user.setPassword(newStyleHash);

        BlackBoxAuthenticationToken token = new BlackBoxAuthenticationToken(user.getUsername(), user.getPassword());
        token.setRememberMe(rememberMe);
        token.setRealm(SecurityRealmEnum.WEB);
        token.setOldHash(sha1Hash(plainText));

        try {
            Subject subject = SecurityUtils.getSubject();
            subject.login(token);

            User user = (User) SecurityUtils.getSubject().getPrincipal();

            userSessionService.populateContext(user, getContext());
            result =  new RedirectResolution(DashBoardActionBean.class);
        }
        catch (IncorrectCredentialsException e) {

            // This is messy, need to figure out why rest easy wraps shit so much
            ValidationErrors errors = new ValidationErrors();
            errors.addGlobalError(new LocalizableError("user.badLogin"));
            getContext().setValidationErrors(errors);
            result = getContext().getSourcePageResolution();
            try {
                // Add a two second delay to keep people from brute forcing
                Thread.sleep(2000);
            } catch (InterruptedException e1) {
                // ignored
            }

        } catch (Exception e) {
            logger.error("here is the error" + e.getMessage(), e);
            ValidationErrors errors = new ValidationErrors();
            errors.addGlobalError(new LocalizableError("general.error"));
            getContext().setValidationErrors(errors);
            result = getContext().getSourcePageResolution();

        } finally {
            token.clear();
        }
        return result;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Boolean getRememberMe() {
        return rememberMe;
    }

    public void setRememberMe(Boolean rememberMe) {
        if (rememberMe != null) {
            this.rememberMe = rememberMe;
        }
    }

    @Override
    public boolean isHasIntro() {
        return false;
    }
}
