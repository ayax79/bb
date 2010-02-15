package com.blackbox.server.user.event;

import org.yestech.event.event.IEvent;
import org.yestech.event.event.BaseEvent;

/**
 *
 *
 */
public class MarkProfileViewedEvent extends BaseEvent<String> {


    private String guid;
    private static final long serialVersionUID = 1297642553052060112L;

    public MarkProfileViewedEvent(String guid) {
        this.guid = guid;
    }

    public String getGuid() {
        return guid;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MarkProfileViewedEvent)) return false;

        MarkProfileViewedEvent that = (MarkProfileViewedEvent) o;

        //noinspection RedundantIfStatement
        if (guid != null ? !guid.equals(that.guid) : that.guid != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return guid != null ? guid.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "MarkProfileViewedEvent{" +
                "guid='" + guid + '\'' +
                '}';
    }
}
