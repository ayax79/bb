package com.blackbox.server.user.event;

import org.yestech.event.event.IEvent;
import org.yestech.event.event.BaseEvent;
import com.blackbox.foundation.user.User;

/**
 *
 *
 */
public class UpdateUserEvent extends AbstractUserEvent
{
    private static final long serialVersionUID = -1835906732252664027L;

    public UpdateUserEvent(User user) {
        super(user);
    }

}
