/*
 * Copyright (c) 2009. Blackbox, LLC
 * All rights reserved.
 */

package com.blackbox.server.point;

import com.blackbox.point.Point;

import java.util.Collection;

/**
 *
 *
 */
public interface IPointDao
{
    void save(Point point);

    @SuppressWarnings({"unchecked"})
    Collection<Point> loadPointsByUserGuid(String userGuid);
}
