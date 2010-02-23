/*
 * Copyright (c) 2009. Blackbox, LLC
 * All rights reserved.
 */

package com.blackbox.foundation.point;

import com.blackbox.foundation.BBPersistentObject;
import com.blackbox.foundation.Utils;
import com.blackbox.foundation.user.User;


/**
 *
 *
 */
public class Point extends BBPersistentObject {

    private User user;
    private long value;

    public Point(User user, long value) {
        this.user = user;
        this.value = value;
    }

    public Point() {
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public long getValue() {
        return value;
    }

    public void setValue(long value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Point)) return false;
        if (!super.equals(o)) return false;

        Point point = (Point) o;

        if (value != point.value) return false;
        //noinspection RedundantIfStatement
        if (user != null ? !user.equals(point.user) : point.user != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (user != null ? user.hashCode() : 0);
        result = 31 * result + (int) (value ^ (value >>> 32));
        return result;
    }

    @Override
    public String toString() {
        return "Point{" +
                "user=" + user +
                ", value=" + value +
                "} " + super.toString();
    }

    public static Point createPoint() {
        Point p = new Point();
        Utils.applyGuid(p);
        return p;
    }

    public static Point createPoint(User user, long value) {
        Point p = new Point(user, value);
        Utils.applyGuid(p);
        return p;
    }
}
