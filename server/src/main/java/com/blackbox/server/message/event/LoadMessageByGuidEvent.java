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
public class LoadMessageByGuidEvent extends BaseEvent<String> {
    private static final long serialVersionUID = 6637934039331764029L;

    public LoadMessageByGuidEvent(String key) {
        super(key);
    }
}