/*
 * Copyright (c) 2009. Blackbox, LLC
 * All rights reserved.
 */

package com.blackbox.server.message.event;

import com.blackbox.foundation.EntityReference;
import org.yestech.event.event.BaseEvent;
import org.yestech.event.annotation.EventResultType;

import java.util.List;

/**
 *
 *
 */
@EventResultType(List.class)
public class RefreshMessagesByRecipientEvent extends BaseEvent<EntityReference> {
    private static final long serialVersionUID = -403282696917762733L;

    public RefreshMessagesByRecipientEvent(EntityReference owner) {
        super(owner);
    }
}