/*
 * Copyright (c) 2009. Blackbox, LLC
 * All rights reserved.
 */

package com.blackbox.service.point;

import com.blackbox.point.IPointManager;
import com.blackbox.point.Point;
import com.blackbox.point.Points;
import com.blackbox.user.User;
import static com.google.common.collect.Lists.newArrayList;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 *
 *
 */
@RunWith(JMock.class)
public class PointServiceUnitTest
{

    Mockery mockery = new JUnit4Mockery();

    @Test
    public void testLoadPointsByUserId()
    {

        final long points = 2392389L;
        final User user = User.createUser();
        final Points pointObject = new Points(newArrayList(new Point(user, points)));
        final IPointManager pointManager = mockery.mock(IPointManager.class, "pointManager");

        mockery.checking(new Expectations()
        {
            {
                oneOf(pointManager).loadPointsForUser(user.getGuid());
                will(returnValue(pointObject));
            }
        });

        PointService pointService = new PointService();
        pointService.setPointManager(pointManager);
        long result = pointService.loadPointsByUserGuid(user.getGuid());
        assertEquals(result, points);
    }
}
