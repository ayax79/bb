/*
 * Copyright (c) 2009. Blackbox, LLC
 * All rights reserved.
 */

package com.blackbox.server.occasion.event;

import com.blackbox.foundation.occasion.Occasion;
import com.blackbox.foundation.occasion.OccasionRequest;
import org.yestech.event.event.IEvent;
import org.yestech.event.event.BaseEvent;
import org.yestech.event.annotation.EventResultType;

/**
 *
 *
 */
public class OccasionRsvpEvent extends BaseEvent<OccasionRequest>
{
    private static final long serialVersionUID = 4116059016670341216L;

    public OccasionRsvpEvent(OccasionRequest occasionRequest) {
        super(occasionRequest);
    }
}