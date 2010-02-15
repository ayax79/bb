package com.blackbox.server.user.event;

import com.blackbox.user.User;
import org.yestech.event.event.BaseEvent;

/**
 * @author A.J. Wright
 */
public abstract class AbstractUserEvent extends BaseEvent<User> {

    public AbstractUserEvent(User user) {
        super(user);
    }
}
