package com.blackbox.server.social;

import com.blackbox.foundation.user.User;
import com.blackbox.server.BaseIntegrationTest;
import com.blackbox.server.user.IUserDao;
import com.blackbox.foundation.social.Ignore;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import org.junit.Test;

import javax.annotation.Resource;

/**
 *
 *
 */
public class IgnoreDaoIntegrationTest extends BaseIntegrationTest {

    @Resource(name = "ignoreDao")
    private IIgnoreDao ignoreDao;

    @Resource(name = "userDao")
    private IUserDao userDao;

    @Test
    public void testCrud() {
        User aj = userDao.loadUserByUsername("aj");
        User angels = userDao.loadUserByUsername("blackboxangels");

        Ignore ignore = Ignore.createIgnore();
        ignore.setTarget(angels.toEntityReference());
        ignore.setIgnorer(aj.toEntityReference());
        ignore.setType(Ignore.IgnoreType.HARD);

        Ignore result = ignoreDao.save(ignore);
        assertNotNull(result.getCreated());
        assertNotNull(result.getModified());
        assertNotNull(result.getVersion());

        assertNotNull(ignoreDao.loadByGuid(ignore.getGuid()));
        ignoreDao.delete(ignore);
    }

}