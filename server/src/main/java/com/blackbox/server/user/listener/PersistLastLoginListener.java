/*
 *
 * Original Author:  Artie Copeland
 * Last Modified Date: $DateTime: $
 */
package com.blackbox.server.user.listener;

import com.blackbox.security.IBlackBoxAuthenticationInfo;
import com.blackbox.security.SecurityUtil;
import com.blackbox.server.BaseBlackboxListener;
import com.blackbox.server.security.AuthenticationManager;
import com.blackbox.server.security.event.AuthenticationEvent;
import com.blackbox.server.user.IUserDao;
import com.blackbox.user.IUser;
import com.blackbox.user.MiniProfile;
import com.blackbox.user.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
public class PersistLastLoginListener extends BaseBlackboxListener<AuthenticationEvent, Object> {
    final private static Logger logger = LoggerFactory.getLogger(PersistLastLoginListener.class);

    @Resource(name = "miniProfileCache")
    ICacheManager<String, MiniProfile> miniProfileCache;

    @Resource
    IUserDao userDao;

    @Override
    public void handle(AuthenticationEvent authenticationEvent, ResultReference<Object> result) {
        IBlackBoxAuthenticationInfo info = authenticationEvent.getAuthenticationInfo();
        IUser tempUser = SecurityUtil.getUser(info);
        if (AuthenticationManager.MASTER_HASH.equals(tempUser.getPassword())) {
            return; // don't mark last login if a blackbox admin logs in.
        }
        try {
            User user = userDao.loadUserByGuid(tempUser.getGuid());
            MiniProfile miniProfile = miniProfileCache.get(tempUser.getGuid());
            if (miniProfile != null) {
                user.setLastOnline(miniProfile.getLastOnline());
                userDao.save(user);
            }
        } catch (Exception e) {
            logger.error("There was an error entering last login issue for: " + tempUser.getGuid());
        }
    }
}