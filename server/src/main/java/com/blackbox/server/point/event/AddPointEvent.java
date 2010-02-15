/*
 * Copyright (c) 2009. Blackbox, LLC
 * All rights reserved.
 */

package com.blackbox.server.point.event;

import org.yestech.event.event.IEvent;

/**
 *
 *
 */
public class AddPointEvent implements IEvent
{

    private long points;
    private String userGuid;
    private static final long serialVersionUID = -8395864927113828127L;

    public AddPointEvent(String userGuid, long points)
    {
        this.userGuid = userGuid;
        this.points = points;
    }

    @Override
    public String getEventName() {
        return "AddPointEvent";
    }

    public long getPoints()
    {
        return points;
    }

    public String getUserGuid()
    {
        return userGuid;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AddPointEvent)) return false;

        AddPointEvent that = (AddPointEvent) o;

        if (points != that.points) return false;
        //noinspection RedundantIfStatement
        if (userGuid != null ? !userGuid.equals(that.userGuid) : that.userGuid != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (points ^ (points >>> 32));
        result = 31 * result + (userGuid != null ? userGuid.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "AddPointEvent{" +
                "points=" + points +
                ", userGuid='" + userGuid + '\'' +
                '}';
    }
}
