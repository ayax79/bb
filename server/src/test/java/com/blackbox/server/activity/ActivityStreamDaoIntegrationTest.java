package com.blackbox.server.activity;

import com.blackbox.server.BaseIntegrationTest;
import com.blackbox.server.user.IUserDao;
import com.blackbox.user.User;
import com.blackbox.activity.AssociatedActivityFilterType;
import com.blackbox.activity.IActivityThread;
import com.blackbox.util.Bounds;
import com.blackbox.social.NetworkTypeEnum;
import org.junit.Test;

import javax.annotation.Resource;
import java.util.Collection;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertFalse;

/**
 * @author A.J. Wright
 */
public class ActivityStreamDaoIntegrationTest extends BaseIntegrationTest {

    @Resource
    IActivityStreamDao activityStreamDao;

    @Resource
    IUserDao userDao;


    @Test
    public void loadPublishedActivitiesTest() {

        User aj = userDao.loadUserByUsername("aj");
        Collection<IActivityThread> threads = activityStreamDao.loadPublishedActivities(aj.toEntityReference(),
                AssociatedActivityFilterType.ALL,
                new Bounds(0, 10), NetworkTypeEnum.ALL_MEMBERS, NetworkTypeEnum.FRIENDS, NetworkTypeEnum.FOLLOWERS);

        assertNotNull(threads);
        assertFalse(threads.isEmpty());
    }

}
