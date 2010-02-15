package com.blackbox.presentation.action.user;

import net.sourceforge.stripes.action.*;
import net.sourceforge.stripes.integration.spring.SpringBean;
import com.blackbox.user.IUserManager;
import com.blackbox.media.IMediaManager;
import static com.blackbox.presentation.action.util.PresentationUtil.getProperty;
import static org.apache.commons.lang.StringUtils.isNotBlank;

import javax.servlet.http.Cookie;

/**
 * Description
 *
 * @author Andy Nelsen
 * @date Dec 21, 2009
 */

@UrlBinding("/campaign/{identifier}")
public class CampaignActionBean implements ActionBean {

    public static final String AFFILIATE_COOKIE_KEY = "blackbox.affiliate";

    @SpringBean("userManager")
    IUserManager userManager;

    protected ActionBeanContext context;
    protected String identifier;

    @DefaultHandler
    public Resolution begin() {

        if (isNotBlank(identifier)) {
            Cookie cookie = new Cookie(AFFILIATE_COOKIE_KEY, identifier);
            cookie.setMaxAge(60 * 60 * 24 * 365);
            cookie.setPath("/");
            getContext().getResponse().addCookie(cookie);
        }
        //String destination = getProperty("campaign.landing.page") + identifier;
        return new RedirectResolution(getProperty("campaign.landing.page") + identifier + "?", false);
    }

    @Override
    public void setContext(ActionBeanContext context) {
        this.context = context;
    }

    @Override
    public ActionBeanContext getContext() {
        return context;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }
}
