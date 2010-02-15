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
public class Occasion7DayReminderEvent  extends BaseEvent implements IEvent, OccasionEvent
{
    private final Occasion occasion;
    private static final long serialVersionUID = -3825363158203819712L;

    public Occasion7DayReminderEvent(Occasion occasion) {
        this.occasion = occasion;
    }

    public Occasion getOccasion() {
        return occasion;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Occasion7DayReminderEvent that = (Occasion7DayReminderEvent) o;

        if (occasion != null ? !occasion.equals(that.occasion) : that.occasion != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return occasion != null ? occasion.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "Occasion7DayReminderEvent{" +
                "occasion=" + occasion +
                '}';
    }
}
