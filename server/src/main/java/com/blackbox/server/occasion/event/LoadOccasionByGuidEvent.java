/*
 * Copyright (c) 2009. Blackbox, LLC
 * All rights reserved.
 */

package com.blackbox.server.occasion.event;

import com.blackbox.foundation.occasion.Occasion;
import org.yestech.event.event.BaseEvent;
import org.yestech.event.annotation.EventResultType;

/**
 *
 *
 */
@EventResultType(Occasion.class)
public class LoadOccasionByGuidEvent extends BaseEvent<String> {
    private static final long serialVersionUID = 2706874718693226488L;

    public LoadOccasionByGuidEvent(String guid) {
        super(guid);
    }
}
