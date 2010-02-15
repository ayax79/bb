package com.blackbox.server.bookmark;

import org.joda.time.DateTime;

import java.io.Serializable;

/**
 * @author A.J. Wright
 */
public class WishCountCacheKey implements Serializable {

    private String guid;
    private DateTime startTime;

    public WishCountCacheKey() {
    }

    public WishCountCacheKey(String guid, DateTime startTime) {
        this.guid = guid;
        this.startTime = startTime;
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
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

        WishCountCacheKey that = (WishCountCacheKey) o;

        if (guid != null ? !guid.equals(that.guid) : that.guid != null) return false;
        if (startTime != null ? !startTime.equals(that.startTime) : that.startTime != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = guid != null ? guid.hashCode() : 0;
        result = 31 * result + (startTime != null ? startTime.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "WishCountCacheKey{" +
                "guid='" + guid + '\'' +
                ", startTime=" + startTime +
                '}';
    }
}
