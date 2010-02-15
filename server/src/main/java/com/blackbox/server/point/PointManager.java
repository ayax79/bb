/*
 * Copyright (c) 2009. Blackbox, LLC
 * All rights reserved.
 */

package com.blackbox.server.point;

import com.blackbox.point.IPointManager;
import com.blackbox.point.Points;
import com.blackbox.server.point.event.AddPointEvent;
import com.blackbox.server.point.event.LoadPointsByUserGuidEvent;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yestech.event.multicaster.BaseServiceContainer;

/**
 *
 *
 */
@Service("pointManager")
public class PointManager extends BaseServiceContainer implements IPointManager {
    @Transactional(readOnly = true)
    public Points loadPointsForUser(String userGuid) {
        return (Points) getEventMulticaster().process(new LoadPointsByUserGuidEvent(userGuid));
    }

    @Transactional(readOnly = false)
    public void addPointsToUser(String userGuid, long points) {
        getEventMulticaster().process(new AddPointEvent(userGuid, points));
    }
}
