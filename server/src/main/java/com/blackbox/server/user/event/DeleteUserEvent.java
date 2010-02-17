package com.blackbox.server.user.event;

import org.yestech.event.event.IEvent;
import org.yestech.event.event.BaseEvent;
import com.blackbox.foundation.user.IUser;
import com.blackbox.foundation.user.User;

/**
 *
 *
 */
public class DeleteUserEvent extends AbstractUserEvent {

    public DeleteUserEvent(User user) {
        super(user);
    }

}
