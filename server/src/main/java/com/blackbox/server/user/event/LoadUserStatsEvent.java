package com.blackbox.server.user.event;

import org.yestech.event.event.IEvent;

/**
 *
 *
 */
public class LoadUserStatsEvent implements IEvent {


    private String guid;
    private static final long serialVersionUID = -4686653151504936430L;

    public LoadUserStatsEvent(String guid) {
        this.guid = guid;
    }

    @Override
    public String getEventName() {
        return "LoadUserStatsEvent";
    }

    public String getGuid() {
        return guid;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LoadUserStatsEvent)) return false;

        LoadUserStatsEvent that = (LoadUserStatsEvent) o;

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