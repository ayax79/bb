package com.blackbox.presentation.action.admin;

import com.blackbox.presentation.action.BaseBlackBoxActionBean;
import com.blackbox.presentation.action.util.RequireUserType;
import com.blackbox.user.IUserManager;
import com.blackbox.user.User;
import com.blackbox.presentation.action.util.OccasionImportUtil;
import net.sourceforge.stripes.action.*;
import net.sourceforge.stripes.integration.spring.SpringBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static org.yestech.lib.crypto.MessageDigestUtils.sha1Hash;

import java.io.IOException;

/**
 * Administrative functions for user and BB platform management
 *
 * @author Andy Nelsen
 * @date Dec 9, 2009
 */
@UrlBinding("/admin/events/{$event}")
@RequireUserType(User.UserType.BLACKBOX_ADMIN)
public class EventsAdminActionBean extends BaseBlackBoxActionBean {
    private static final Logger _logger = LoggerFactory.getLogger(EventsAdminActionBean.class);

    @SpringBean("userManager")
    private IUserManager userManager;

    private User user;

    @DefaultHandler
    public Resolution begin() {
        _logger.info("Events admin begin");
        return new ForwardResolution("/WEB-INF/admin/events.jsp");
    }

    public Resolution importEvents() {
        _logger.info("Events import begin");

        return new ForwardResolution("/WEB-INF/admin/events_import.jsp");
    }

    public Resolution reviewFeed() throws IOException {
        _logger.info("Events review feed begin");
       String feed = OccasionImportUtil.parseFeed("http://carnalnation.com/datafeeds/2009-01-01T00:00/bbr-feed.xml");


        return new ForwardResolution("/WEB-INF/admin/view_feed.jsp");
    }




}