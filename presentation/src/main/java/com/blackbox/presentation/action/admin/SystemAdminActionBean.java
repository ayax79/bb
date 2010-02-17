package com.blackbox.presentation.action.admin;

import com.blackbox.presentation.action.BaseBlackBoxActionBean;
import com.blackbox.presentation.action.util.DisplayNameCacheKey;
import com.blackbox.presentation.action.util.RequireUserType;
import com.blackbox.presentation.action.util.StringToLowerCaseConverter;
import com.blackbox.foundation.user.IUserManager;
import com.blackbox.foundation.user.User;
import net.sourceforge.stripes.action.*;
import net.sourceforge.stripes.integration.spring.SpringBean;
import net.sourceforge.stripes.controller.LifecycleStage;
import net.sourceforge.stripes.validation.Validate;
import net.sourceforge.stripes.validation.ValidateNestedProperties;
import net.sourceforge.stripes.validation.SimpleError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yestech.cache.ICacheManager;
import static org.yestech.lib.crypto.MessageDigestUtils.sha1Hash;
import static org.apache.commons.lang.StringUtils.isNotBlank;

/**
 * Administrative functions for user and BB platform management
 *
 * @author Andy Nelsen
 * @date Nov 30, 2009
 */
@UrlBinding("/admin/{$event}")
@RequireUserType(User.UserType.BLACKBOX_ADMIN)
public class SystemAdminActionBean extends BaseBlackBoxActionBean {
    private static final Logger _logger = LoggerFactory.getLogger(SystemAdminActionBean.class);

    @SpringBean("userManager")
    private IUserManager userManager;
    @SpringBean("displayNameCache")
    private ICacheManager<DisplayNameCacheKey, String> displayNameCache;

    /*@ValidateNestedProperties({
            @Validate(field = "username", required = true, on = "update_user"),
            @Validate(field = "name", required = true, on = "update_user"),
            @Validate(field = "lastname", required = true, on = "update_user"),
            @Validate(field = "type", required = true, on = "update_user"),
            @Validate(field = "email", required = true, on = "update_user")
    })*/

    @ValidateNestedProperties({
		@Validate(field = "password", converter = StringToLowerCaseConverter.class)
    })
    private User user;
    private String oldPasswordHash;

    @Before(stages = LifecycleStage.BindingAndValidation, on = "update_user")
    public void preProcess() {
        String guid = getContext().getRequest().getParameter("user.guid");
        if (isNotBlank(guid)) {
            user = userManager.loadUserByGuid(guid);
            oldPasswordHash = user.getPassword();
        }
    }

    @DefaultHandler
    public Resolution begin() {
        _logger.info("Administration begin");
        return new ForwardResolution("/WEB-INF/admin/dashboard.jsp");
    }

    public Resolution findUser() {
        _logger.info("Administration list_users");
        return new ForwardResolution("/WEB-INF/admin/finduser.jspf");
    }

    public Resolution list_users() {
        _logger.info("Administration list_users");
        return new ForwardResolution("/WEB-INF/admin/list_users.jsp");
    }

    public Resolution show_user() {
        Resolution resolution;
        if (isNotBlank(user.getUsername())) {
            user = userManager.loadUserByUsername(user.getUsername());
        } else if (isNotBlank(user.getEmail())) {
            user = userManager.loadUserByEmail(user.getEmail());
        }

        if(null == user) {
            getContext().getValidationErrors().add("username", new SimpleError(String.format("User %s does not exist", user)));
            resolution = new ForwardResolution("/WEB-INF/admin/finduser.jspf");
        } else {
            resolution = new ForwardResolution("/WEB-INF/admin/show_user.jsp");
        }


        _logger.info("Administration show_user");
        return resolution;
    }

    public Resolution deactivate_user() {
        _logger.info("Administration deactivate_user");
        
        return new ForwardResolution("/WEB-INF/admin/deactivate_user.jsp");
    }

    public Resolution delete_user() {
        _logger.info("Administration delete_user");
        return new ForwardResolution("/WEB-INF/admin/delete_user.jsp");
    }

    @DontValidate
    public Resolution update_user() {
        _logger.info("Administration update_user");
        if (isNotBlank(user.getPassword()) && !user.getPassword().equals(oldPasswordHash)) {
            user.setPassword(sha1Hash(user.getPassword()));
        } else {
            user.setPassword(oldPasswordHash);
        }

        user = userManager.save(user);

        String userGuid = user.getGuid();
        displayNameCache.flush(new DisplayNameCacheKey(userGuid, userGuid));
        getContext().getMessages().add(
                new SimpleMessage(user.getUsername() + " updated successfully")
        );
        return new RedirectResolution(com.blackbox.presentation.action.admin.SystemAdminActionBean.class);
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public IUserManager getUserManager() {
        return userManager;
    }

    public void setUserManager(IUserManager userManager) {
        this.userManager = userManager;
    }

    public ICacheManager<DisplayNameCacheKey, String> getDisplayNameCache() {
        return displayNameCache;
    }

    public void setDisplayNameCache(ICacheManager<DisplayNameCacheKey, String> displayNameCache) {
        this.displayNameCache = displayNameCache;
    }
}
