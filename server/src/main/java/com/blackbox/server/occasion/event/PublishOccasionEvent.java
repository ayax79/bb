/*
 * Copyright (c) 2009. Blackbox, LLC
 * All rights reserved.
 */

package com.blackbox.server.occasion.event;

import com.blackbox.foundation.occasion.Occasion;
import org.yestech.event.annotation.EventResultType;
import org.yestech.event.event.BaseEvent;

/**
 *
 *
 */
@EventResultType(Occasion.class)
public class PublishOccasionEvent extends BaseEvent implements OccasionEvent {
    private final Occasion occasion;
    private static final long serialVersionUID = -5603337927661224030L;

    public PublishOccasionEvent(Occasion occasion) {
        this.occasion = occasion;
    }

    public Occasion getOccasion() {
        return occasion;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PublishOccasionEvent that = (PublishOccasionEvent) o;

        if (occasion != null ? !occasion.equals(that.occasion) : that.occasion != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return occasion != null ? occasion.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "PublishOccasionEvent{" +
                "occasion=" + occasion +
                '}';
    }
}