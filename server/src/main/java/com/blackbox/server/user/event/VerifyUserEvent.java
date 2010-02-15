/*
 * Copyright (c) 2009. Blackbox, LLC
 * All rights reserved.
 */

package com.blackbox.server.user.event;

import org.yestech.event.event.IEvent;

/**
 *
 *
 */
public class VerifyUserEvent implements IEvent
{

    private String userGuid;
    private String key;
    private static final long serialVersionUID = -1043837002881313477L;

    public VerifyUserEvent(String userGuid, String key)
    {
        this.userGuid = userGuid;
        this.key = key;
    }

    @Override
    public String getEventName() {
        return "VerifyUserEvent";
    }

    public String getKey()
    {
        return key;
    }

    public String getUserGuid()
    {
        return userGuid;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof VerifyUserEvent)) return false;

        VerifyUserEvent that = (VerifyUserEvent) o;

        if (key != null ? !key.equals(that.key) : that.key != null) return false;
        if (userGuid != null ? !userGuid.equals(that.userGuid) : that.userGuid != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = userGuid != null ? userGuid.hashCode() : 0;
        result = 31 * result + (key != null ? key.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "VerifyUserEvent{" +
                "key='" + key + '\'' +
                ", userGuid='" + userGuid + '\'' +
                '}';
    }
}
