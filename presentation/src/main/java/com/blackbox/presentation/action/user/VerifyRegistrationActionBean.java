package com.blackbox.presentation.action.user;

import com.blackbox.presentation.action.BaseBlackBoxActionBean;
import com.blackbox.presentation.action.util.PresentationUtil;
import com.blackbox.presentation.action.security.LoginActionBean;
import com.blackbox.foundation.user.IUserManager;
import com.blackbox.foundation.user.VerificationResult;
import net.sourceforge.stripes.action.*;
import net.sourceforge.stripes.integration.spring.SpringBean;
import net.sourceforge.stripes.validation.Validate;

/**
 *
 *
 */
@UrlBinding("/verify")
public class VerifyRegistrationActionBean implements ActionBean {
    
    private ActionBeanContext actionBeanContext;


    @Override
    public ActionBeanContext getContext() {
        return actionBeanContext;
    }

    @Override
    public void setContext(ActionBeanContext actionBeanContext) {
        this.actionBeanContext = actionBeanContext;
    }

    public boolean isAuthenticated() {
        return false;
    }

    @SpringBean("userManager") private IUserManager userManager;

    @Validate(required = true) private String key;
    @Validate(required = true) private String userGuid;

    @DefaultHandler
    @DontValidate
    public Resolution view() {
        return new ForwardResolution("/verify.jsp");
    }

    public Resolution verify() {

        VerificationResult result = userManager.verifyUser(userGuid, key);
        if (result.isVerified()) {
            getContext().getMessages().add(new LocalizableMessage("register.verified"));
            return new RedirectResolution(LoginActionBean.class);
        } else {
            getContext().getMessages().add(new LocalizableMessage("register.notverfied"));
            return new ForwardResolution("/error.jsp");
        }
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getUserGuid() {
        return userGuid;
    }

    public void setUserGuid(String userGuid) {
        this.userGuid = userGuid;
    }

    public void setUserManager(IUserManager userManager) {
        this.userManager = userManager;
    }

    public String getRecaptchaPublicKey() {
        return PresentationUtil.getProperty("recaptcha.public.key");
    }
}
