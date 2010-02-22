/*
 * Copyright (c) 2009. Blackbox, LLC
 * All rights reserved.
 */

package com.blackbox.foundation.point;

import java.util.Collection;

/**
 *
 *
 */
public interface IPointManager {
    Collection<Point> loadPointsForUser(String userGuid);

    void addPointsToUser(String userGuid, long points);
}
