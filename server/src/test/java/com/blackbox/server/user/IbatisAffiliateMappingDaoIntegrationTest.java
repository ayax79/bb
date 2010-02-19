package com.blackbox.server.user;

import org.junit.Test;

import javax.annotation.Resource;

import com.blackbox.foundation.user.User;
import com.blackbox.foundation.user.AffiliateMapping;
import com.blackbox.server.BaseIntegrationTest;

import static com.google.common.collect.Lists.newArrayList;

/**
 * @author A.J. Wright
 */
public class IbatisAffiliateMappingDaoIntegrationTest extends BaseIntegrationTest {

    @Resource
    protected IAffiliateMappingDao affiliateMappingDao;

    @Resource
    protected IUserDao userDao;

    @Test
    public void crudTest() {
        User issac = userDao.loadUserByUsername("aj");
        User jimmy = userDao.loadUserByUsername("blackboxangels");


        AffiliateMapping mapping = null;

        try {
            mapping = new AffiliateMapping();
            mapping.setAffiliate(issac);
            mapping.setUsers(newArrayList(jimmy));

            affiliateMappingDao.save(mapping);
        } finally {
            if (mapping != null) affiliateMappingDao.delete(mapping);
        }
    }

}
