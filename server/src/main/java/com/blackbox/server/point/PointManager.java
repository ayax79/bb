/*
 * Copyright (c) 2009. Blackbox, LLC
 * All rights reserved.
 */

package com.blackbox.server.point;

import com.blackbox.foundation.point.IPointManager;
import com.blackbox.foundation.point.Point;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

/**
 *
 *
 */
// - disabled @Service("pointManager")
public class PointManager implements IPointManager {

    // - disabled @Resource
    IPointDao pointDao;


    @Transactional(readOnly = true)
    public Collection<Point> loadPointsForUser(String userGuid) {
        return pointDao.loadPointsByUserGuid(userGuid);
    }

    @Transactional(readOnly = false)
    public void addPointsToUser(String userGuid, long points) {
        throw new UnsupportedOperationException("not implemented");
    }
}
