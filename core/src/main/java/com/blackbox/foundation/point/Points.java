/*
 * Copyright (c) 2009. Blackbox, LLC
 * All rights reserved.
 */

package com.blackbox.foundation.point;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

/**
 *
 *
 */
@XmlRootElement
public class Points implements Serializable {

    private Collection<Point> points = new ArrayList<Point>();

    public Points() {
    }

    public Points(Collection<Point> points) {
        this.points = points;
    }

    public Collection<Point> getPoints() {
        return points;
    }

    public void setPoints(Collection<Point> points) {
        this.points = points;
    }

    public long getTotal() {
        int total = 0;
        for (Point point : points) {
            total += point.getValue();
        }
        return total;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Points)) return false;

        Points points = (Points) o;

        //noinspection RedundantIfStatement
        if (this.points != null ? !this.points.equals(points.points) : points.points != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        return points != null ? points.hashCode() : 0;
    }
}
