package com.blackbox.server.social;

import com.blackbox.server.BaseIntegrationTest;
import com.blackbox.social.Vouch;
import com.blackbox.user.User;

import javax.annotation.Resource;

import org.joda.time.DateTime;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

import java.util.List;

/**
 * @author A.J. Wright
 */
public class VouchDaoIntegrationTest extends BaseIntegrationTest {

    @Resource
    IVouchDao vouchDao;

    @Test
    public void testCrud() {

        User vouchTarget = User.createUser();
        User voucher = User.createUser();

        Vouch vouch = Vouch.createVouch();
        vouch.setCount(233);
        vouch.setDescription("sdfkjsdfsdflj");
        vouch.setExpirationDate(new DateTime().plusYears(33));
        vouch.setTarget(vouchTarget.toEntityReference());
        vouch.setVoucher(voucher.toEntityReference());
        vouchDao.save(vouch);

        Vouch result = vouchDao.loadByGuid(vouch.getGuid());
        assertNotNull(result);

        vouch.setDescription("sdfase4fa");
        vouch.setCount(233223);
        vouchDao.save(vouch);

        List<Vouch> vouches = vouchDao.loadUserVouches(vouch.getTarget().getGuid());
        assertNotNull(vouches);
        assertFalse(vouches.isEmpty());

        int count = vouchDao.countVouchesForEntity(vouch.getVoucher().getGuid());
        assertTrue(count > 0);

        vouches = vouchDao.loadAllByTarget(vouch.getTarget().getGuid());
        assertNotNull(vouches);
        assertFalse(vouches.isEmpty());

        result = vouchDao.loadByVoucherAndTarget(vouch.getVoucher().getGuid(), vouch.getTarget().getGuid());
        assertNotNull(result);

        vouches = vouchDao.loadOlderThanDateWithType(new DateTime().plusYears(111212));
        assertNotNull(vouches);
        assertFalse(vouches.isEmpty());

        vouches = vouchDao.loadOutgoingByVoucher(vouch.getVoucher().getGuid());
        assertNotNull(vouches);
        assertFalse(vouches.isEmpty());

        vouchDao.delete(vouch);
    }
}
