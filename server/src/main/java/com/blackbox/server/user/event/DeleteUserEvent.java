package com.blackbox.server.user.event;

import org.yestech.event.event.IEvent;
import org.yestech.event.event.BaseEvent;
import com.blackbox.user.IUser;
import com.blackbox.user.User;

/**
 *
 *
 */
public class DeleteUserEvent extends AbstractUserEvent {

    public DeleteUserEvent(User user) {
        super(user);
    }

}
