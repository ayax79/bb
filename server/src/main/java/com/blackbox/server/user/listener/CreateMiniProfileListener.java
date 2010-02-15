/*
 *
 * Original Author:  Artie Copeland
 * Last Modified Date: $DateTime: $
 */
package com.blackbox.server.user.listener;

import com.blackbox.security.IBlackBoxAuthenticationInfo;
import com.blackbox.security.SecurityUtil;
import com.blackbox.server.BaseBlackboxListener;
import com.blackbox.server.security.event.AuthenticationEvent;
import com.blackbox.server.user.IUserDao;
import com.blackbox.user.IUser;
import com.blackbox.user.MiniProfile;
import org.joda.time.DateTime;
import org.yestech.cache.ICacheManager;
import org.yestech.event.ResultReference;
import org.yestech.event.annotation.AsyncListener;
import org.yestech.event.annotation.ListenedEvents;

import javax.annotation.Resource;

/**
 * @author Artie Copeland
 * @version $Revision: $
 */
@ListenedEvents(AuthenticationEvent.class)
@AsyncListener
public class CreateMiniProfileListener extends BaseBlackboxListener<AuthenticationEvent, Object> {

    @Resource(name = "miniProfileCache")
    ICacheManager<String, MiniProfile> miniProfileCache;

    @Override
    public void handle(AuthenticationEvent authenticationEvent, ResultReference<Object> result) {
        IBlackBoxAuthenticationInfo info = authenticationEvent.getAuthenticationInfo();
        IUser tempUser = SecurityUtil.getUser(info);
        MiniProfile profile = new MiniProfile();
        profile.setLastOnline(new DateTime());
        profile.setGuid(tempUser.getGuid());
        profile.setInitialized(false);
        miniProfileCache.put(tempUser.getGuid(), profile);
    }
}