package com.blackbox.server.social;

import com.blackbox.server.BaseIntegrationTest;
import com.blackbox.server.user.IUserDao;
import com.blackbox.foundation.social.Relationship;
import com.blackbox.foundation.user.User;
import org.junit.Test;

import javax.annotation.Resource;
import java.util.List;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertFalse;

/**
 * @author A.J. Wright
 */
public class NetworkDaoIntegrationTest extends BaseIntegrationTest {

    @Resource
    INetworkDao networkDao;

    @Resource
    IUserDao userDao;

    @Test
    public void loadConnections() {
        List<Relationship> list = networkDao.loadConnections("1");
        assertNotNull(list);
        assertFalse(list.isEmpty());
    }

    @Test
    public void loadRelationshipsByFromEnityAndWeightsTest() {
        User aj = userDao.loadUserByUsername("aj");
        List<Relationship> list = networkDao.loadRelationshipsByFromEnityAndWeights(aj.getGuid(), Relationship.RelationStatus.FRIEND.getWeight());
        assertNotNull(list);
        assertFalse(list.isEmpty());
    }




}
