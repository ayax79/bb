/*
 * Copyright (c) 2009. Blackbox, LLC
 * All rights reserved.
 */

package com.blackbox.server.user.event;

import org.yestech.event.annotation.EventResultType;
import org.yestech.event.event.IEvent;

import java.util.List;

/**
 *
 *
 */
@EventResultType(List.class)
public class LoadUsersEvent implements IEvent
{

    private final int firstResult;
    private final int maxResults;
    private static final long serialVersionUID = -7692146506144385697L;

    public LoadUsersEvent(int firstResult, int maxResults)
    {
        this.firstResult = firstResult;
        this.maxResults = maxResults;
    }

    @Override
    public String getEventName() {
        return "LoadUsersEvent";
    }

    public int getFirstResult()
    {
        return firstResult;
    }

    public int getMaxResults()
    {
        return maxResults;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (!(o instanceof LoadUsersEvent)) return false;

        LoadUsersEvent that = (LoadUsersEvent) o;

        if (firstResult != that.firstResult) return false;
        //noinspection RedundantIfStatement
        if (maxResults != that.maxResults) return false;

        return true;
    }

    @Override
    public int hashCode()
    {
        int result = firstResult;
        result = 31 * result + maxResults;
        return result;
    }

    @Override
    public String toString()
    {
        return "LoadUsersEvent{" +
                "firstResult=" + firstResult +
                ", maxResults=" + maxResults +
                '}';
    }
}
