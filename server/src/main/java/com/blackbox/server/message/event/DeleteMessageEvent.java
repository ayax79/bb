/*
 * Copyright (c) 2009. Blackbox, LLC
 * All rights reserved.
 */

package com.blackbox.server.message.event;

import org.yestech.event.event.BaseEvent;
import org.yestech.event.annotation.EventResultType;
import com.blackbox.message.Message;

/**
 *
 *
 */
@EventResultType(Message.class)
public class DeleteMessageEvent extends BaseEvent<Message> {
    private static final long serialVersionUID = -4617365294433017038L;

    public DeleteMessageEvent(Message message) {
        super(message);
    }

}