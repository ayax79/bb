/*
 * Copyright (c) 2009. Blackbox, LLC
 * All rights reserved.
 */

package com.blackbox.server.occasion.event;

import com.blackbox.foundation.occasion.Attendee;
import com.blackbox.foundation.occasion.Occasion;
import org.yestech.event.event.IEvent;
import org.yestech.event.event.BaseEvent;
import org.yestech.event.annotation.EventResultType;

/**
 *
 *
 */
@EventResultType(Occasion.class)
public class OccasionRsvpNotificationEvent  extends BaseEvent implements IEvent, OccasionEvent
{
    private final Occasion occasion;
    private final Attendee attendee;
    private static final long serialVersionUID = -871255201117715311L;

    public OccasionRsvpNotificationEvent(Occasion occasion, Attendee attendee) {
        this.occasion = occasion;
        this.attendee = attendee;
    }

    public Occasion getOccasion() {
        return occasion;
    }

    public Attendee getAttendee() {
        return attendee;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        OccasionRsvpNotificationEvent that = (OccasionRsvpNotificationEvent) o;

        if (occasion != null ? !occasion.equals(that.occasion) : that.occasion != null) return false;
        if (attendee != null ? !attendee.equals(that.attendee) : that.attendee != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int h =  occasion != null ? occasion.hashCode() : 0;
        int h1 =  (attendee != null ? attendee.hashCode() : 0);
        return h + h1;
    }

    @Override
    public String toString() {
        return "CreateOccasionEvent{" +
                "occasion=" + occasion +
                "; attendee=" + attendee +
                '}';
    }
}
