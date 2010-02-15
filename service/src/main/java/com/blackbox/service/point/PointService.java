/*
 * Copyright (c) 2009. Blackbox, LLC
 * All rights reserved.
 */

package com.blackbox.service.point;

import com.blackbox.point.IPointManager;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

/**
 *
 *
 */
@Service
public class PointService implements IPointService
{

    @Resource(name = "pointManager")
    private IPointManager pointManager;


    @Override
    public long loadPointsByUserGuid(String userGuid)
    {
        return pointManager.loadPointsForUser(userGuid).getTotal();
    }

    public void setPointManager(IPointManager pointManager)
    {
        this.pointManager = pointManager;
    }
}
