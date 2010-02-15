package com.blackbox.server.social;

import com.blackbox.social.Vouch;
import org.joda.time.DateTime;

import java.io.Serializable;

/**
 * @author A.J. Wright
 */
public class VouchCountCacheKey implements Serializable {

    private String guid;
    private boolean permanent;
    private DateTime startTime;

    public VouchCountCacheKey() {
    }

    public VouchCountCacheKey(String guid, boolean permanent, DateTime startTime) {
        this.guid = guid;
        this.permanent = permanent;
        this.startTime = startTime;
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public boolean isPermanent() {
        return permanent;
    }

    public void setPermanent(boolean permanent) {
        this.permanent = permanent;
    }

    public DateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(DateTime startTime) {
        this.startTime = startTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        VouchCountCacheKey that = (VouchCountCacheKey) o;

        if (permanent != that.permanent) return false;
        if (guid != null ? !guid.equals(that.guid) : that.guid != null) return false;
        if (startTime != null ? !startTime.equals(that.startTime) : that.startTime != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = guid != null ? guid.hashCode() : 0;
        result = 31 * result + (permanent ? 1 : 0);
        result = 31 * result + (startTime != null ? startTime.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "VouchCountCacheKey{" +
                "guid='" + guid + '\'' +
                ", permanent=" + permanent +
                ", startTime=" + startTime +
                '}';
    }
}
