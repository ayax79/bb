package com.blackbox.server.user;

import com.blackbox.server.BaseIntegrationTest;
import com.blackbox.foundation.user.ViewedBy;
import com.blackbox.foundation.user.User;
import org.junit.Test;
import org.joda.time.DateTime;

import javax.annotation.Resource;
import java.util.List;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

/**
 * @author A.J. Wright
 */
public class ViewedByDaoIntegrationTest extends BaseIntegrationTest {

    @Resource
    IViewedByDao viewedByDao;

    @Resource
    IUserDao userDao;

    @Test
    public void testCrud() {
        User aj = userDao.loadMiniProfileByUsername("aj");

        ViewedBy viewedBy = new ViewedBy(aj.getGuid(), ViewedBy.ViewedByType.PROFILE, new DateTime());
        viewedBy.setDestGuid("1");
        viewedByDao.insert(viewedBy);

        List<ViewedBy> list = viewedByDao.loadViewersByDestGuid("1");
        assertNotNull(list);
        assertFalse(list.isEmpty());

        int count = viewedByDao.loadViewNumByDestGuid("1");
        assertTrue(count > 0);
    }

}
