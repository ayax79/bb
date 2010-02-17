package com.blackbox.presentation.action.admin;

import com.blackbox.foundation.user.IUserManager;
import com.blackbox.foundation.user.User;
import com.blackbox.presentation.action.BaseBlackBoxActionBean;
import com.blackbox.presentation.action.util.RequireUserType;
import net.sourceforge.stripes.action.*;
import net.sourceforge.stripes.integration.spring.SpringBean;
import net.sourceforge.stripes.validation.SimpleError;

import static org.apache.commons.lang.StringUtils.isNotBlank;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Description
 *
 * @author Andy Nelsen
 * @date Dec 16, 2009
 */
@RequireUserType({User.UserType.BLACKBOX_ADMIN, User.UserType.BLACKBOX_MARKETING})
@UrlBinding("/admin/marketing/{$event}")
public class MarketingAdminActionBean extends BaseBlackBoxActionBean {
    private static final Logger _logger = LoggerFactory.getLogger(MarketingAdminActionBean.class);

    @SpringBean("userManager")
    private IUserManager userManager;

    private User user;
    private User currentUser;

    @Before
    public void preProcess() {
        currentUser = getCurrentUser();
    }

    @DefaultHandler
    public Resolution begin() {
        _logger.info("Marketing admin begin");
        return new ForwardResolution("/WEB-INF/admin/marketing/dashboard.jsp");
    }

    public Resolution findUser() {
         _logger.info("Administration findUser");
        return new ForwardResolution("/WEB-INF/admin/marketing/finduser.jspf");
    }

    public Resolution show_user() {
        _logger.info("Administration show_user");

        Resolution resolution;
        if (isNotBlank(user.getUsername())) {
            user = userManager.loadUserByUsername(user.getUsername());
        } else if (isNotBlank(user.getEmail())) {
            user = userManager.loadUserByEmail(user.getEmail());
        } else {
            getContext().getValidationErrors().add("username", new SimpleError(String.format("User not found")));
            resolution = new ForwardResolution("/WEB-INF/admin/marketing/dashboard.jspf");
        }
        resolution = new ForwardResolution("WEB-INF/admin/marketing/show_user.jsp");

        return resolution;
    }

    @DontValidate
    public Resolution update_user() {
        user = userManager.save(user);
        return new ForwardResolution("/WEB-INF/admin/marketing/dashboard.jsp");
    }
                                                                                      
    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }
}
