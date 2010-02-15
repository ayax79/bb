/*
 * Copyright (c) 2009. Blackbox, LLC
 * All rights reserved.
 */

package com.blackbox.server.point.event;

import com.blackbox.point.Points;
import org.yestech.event.event.BaseEvent;
import org.yestech.event.annotation.EventResultType;

/**
 *
 *
 */
@EventResultType(Points.class)
public class LoadPointsByUserGuidEvent extends BaseEvent<String>
{
    private static final long serialVersionUID = -1273346451356954308L;

    public LoadPointsByUserGuidEvent(String identifier)
    {
        super(identifier);
    }

}
