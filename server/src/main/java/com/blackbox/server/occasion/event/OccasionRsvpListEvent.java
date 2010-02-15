/*
 * Copyright (c) 2009. Blackbox, LLC
 * All rights reserved.
 */

package com.blackbox.server.occasion.event;

import com.blackbox.occasion.Occasion;
import org.yestech.event.event.IEvent;
import org.yestech.event.event.BaseEvent;
import org.yestech.event.annotation.EventResultType;

/**
 *
 *
 */
@EventResultType(Occasion.class)
public class OccasionRsvpListEvent  extends BaseEvent implements OccasionEvent
{
    private final Occasion occasion;
    private static final long serialVersionUID = 4116059016670341216L;

    public OccasionRsvpListEvent(Occasion occasion) {
        this.occasion = occasion;
    }

    public Occasion getOccasion() {
        return occasion;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        OccasionRsvpListEvent that = (OccasionRsvpListEvent) o;

        if (occasion != null ? !occasion.equals(that.occasion) : that.occasion != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return occasion != null ? occasion.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "Occasion1DayReminderEvent{" +
                "occasion=" + occasion +
                '}';
    }
}
