/*
 * Copyright (c) 2009. Blackbox, LLC
 * All rights reserved.
 */

package com.blackbox.server.point;

import com.blackbox.foundation.point.Point;
import com.blackbox.foundation.point.Points;
import com.blackbox.server.point.event.AddPointEvent;
import com.blackbox.server.point.event.LoadPointsByUserGuidEvent;
import com.blackbox.foundation.user.User;
import static com.google.common.collect.Lists.newArrayList;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.yestech.event.multicaster.IEventMulticaster;

/**
 *
 *
 */
@RunWith(JMock.class)
@SuppressWarnings({"unchecked"})
public class PointManagerUnitTest
{

    @Test
    public void testLoadPointsByUserId()
    {
        final IEventMulticaster eventMulticaster = mockery.mock(IEventMulticaster.class, "eventMulticaster");

        final User user = User.createUser();
        final Points points = new Points(newArrayList(new Point(user, 5)));

        mockery.checking(new Expectations()
        {
            {
                oneOf(eventMulticaster).process(new LoadPointsByUserGuidEvent(user.getGuid()));
                will(returnValue(points));
            }
        });

        PointManager pointManager = new PointManager();
        pointManager.setEventMulticaster(eventMulticaster);
        Points result = pointManager.loadPointsForUser(user.getGuid());

        assertNotNull(result);
        assertEquals(5, result.getTotal());
        assertEquals(1, result.getPoints().size());
    }

    Mockery mockery = new JUnit4Mockery();

    @Test
    public void testAddPointsToUser()
    {
        final IEventMulticaster eventMulticaster = mockery.mock(IEventMulticaster.class, "eventMulticaster");

        final String userId = "ds;lzkxt;8D20oIAWYK";
        final long point = 3232L;

        mockery.checking(new Expectations()
        {
            {
                oneOf(eventMulticaster).process(new AddPointEvent(userId, point));
            }
        });

        PointManager pointManager = new PointManager();
        pointManager.setEventMulticaster(eventMulticaster);

        pointManager.addPointsToUser(userId, point);
    }
}
