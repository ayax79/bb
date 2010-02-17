/*
 * Copyright (c) 2009. Blackbox, LLC
 * All rights reserved.
 */

package com.blackbox.server.message.event;

import com.blackbox.foundation.message.Message;
import org.yestech.event.event.BaseEvent;
import org.yestech.event.annotation.EventResultType;

/**
 *
 *
 */
@EventResultType(Message.class)
public class CreateMessageEvent extends BaseEvent<Message> {
    
    private static final long serialVersionUID = 1391944375110276665L;

    public CreateMessageEvent(Message message) {
        super(message);
    }
}
