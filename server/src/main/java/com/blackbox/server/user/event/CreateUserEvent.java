/*
 * Copyright (c) 2009. Blackbox, LLC
 * All rights reserved.
 */

package com.blackbox.server.user.event;

import com.blackbox.user.User;
import org.yestech.event.annotation.EventResultType;
import org.yestech.event.event.IEvent;
import org.yestech.event.event.BaseEvent;

/**
 *
 *
 */
@EventResultType(User.class)
public class CreateUserEvent extends AbstractUserEvent {

    public CreateUserEvent(User user) {
        super(user);
    }
}
