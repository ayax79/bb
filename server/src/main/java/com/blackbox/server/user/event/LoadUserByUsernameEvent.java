/*
 * Copyright (c) 2009. Blackbox, LLC
 * All rights reserved.
 */

package com.blackbox.server.user.event;

import com.blackbox.user.User;
import org.yestech.event.annotation.EventResultType;
import org.yestech.event.event.BaseEvent;

/**
 *
 *
 */
@EventResultType(User.class)
public class LoadUserByUsernameEvent extends BaseEvent<String>
{
    private static final long serialVersionUID = 2588108668548039215L;

    public LoadUserByUsernameEvent(String username) {
        super(username);
    }

}
