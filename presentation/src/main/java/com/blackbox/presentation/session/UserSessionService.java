package com.blackbox.presentation.session;

import com.blackbox.foundation.user.User;
import com.blackbox.presentation.extension.BlackBoxContext;

import java.util.Collection;

/**
 * @author A.J. Wright
 */
public interface UserSessionService {
    int getOnlineUserCount();

    boolean isUserOnline(String guid);

//    Collection<User> getOnlineUsers();

    void populateContext(User user, BlackBoxContext context);

    void rebuildContextIfNeeded(BlackBoxContext context);
}
