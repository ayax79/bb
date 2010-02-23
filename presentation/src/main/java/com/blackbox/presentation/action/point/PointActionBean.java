/*
 * Copyright (c) 2009. Blackbox, LLC
 * All rights reserved.
 */

package com.blackbox.presentation.action.point;

import com.blackbox.foundation.point.IPointManager;
import com.blackbox.foundation.point.Point;
import com.blackbox.presentation.action.BaseBlackBoxActionBean;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.HandlesEvent;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.StreamingResolution;
import net.sourceforge.stripes.integration.spring.SpringBean;
import net.sourceforge.stripes.validation.Validate;

import java.io.StringReader;
import java.util.Collection;

/**
 *
 *
 */
public class PointActionBean extends BaseBlackBoxActionBean {

    @SpringBean("pointManager")
    private IPointManager pointManager;

    @Validate(required = true)
    private String userGuid;
    private long points;

    @DefaultHandler
    @HandlesEvent("json")
    public Resolution pointsForUser() {

        Collection<Point> points = pointManager.loadPointsForUser(userGuid);
        return new StreamingResolution("text", new StringReader(String.valueOf(getTotal(points))));
    }

    @HandlesEvent("add")
    public Resolution addPoints() {
        pointManager.addPointsToUser(userGuid, points);

        return null;
    }

    public void setUserGuid(String userGuid) {
        this.userGuid = userGuid;
    }

    public void setPoints(long points) {
        this.points = points;
    }

    public void setPointManager(IPointManager pointManager) {
        this.pointManager = pointManager;
    }

    protected long getTotal(Collection<Point> points) {
        int total = 0;
        for (Point point : points) {
            total += point.getValue();
        }
        return total;
    }
}
