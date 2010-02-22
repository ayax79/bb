/*
 * Copyright (c) 2009. Blackbox, LLC
 * All rights reserved.
 */

package com.blackbox.foundation.point;

/**
 *
 *
 */
public interface IPointManager {
    Points loadPointsForUser(String userGuid);

    void addPointsToUser(String userGuid, long points);
}
