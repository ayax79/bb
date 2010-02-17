package com.blackbox.server.billing;

import com.blackbox.server.user.IUserDao;
import com.blackbox.foundation.user.User;
import org.junit.Test;
import com.blackbox.server.BaseIntegrationTest;
import com.blackbox.foundation.billing.IBillingManager;
import com.blackbox.foundation.billing.Transaction;
import com.blackbox.foundation.billing.Money;

import javax.annotation.Resource;

import static junit.framework.Assert.*;

/**
 * @author A.J. Wright
 */
public class BillingManagerIntegrationTest extends BaseIntegrationTest {

    @Resource
    IBillingManager billingManager;

    @Resource
    IUserDao userDao;


    @Test
    public void testCreateTransaction() {
        Money amount = new Money(122);
        User aj = userDao.loadUserByUsername("aj");
        Transaction transaction = billingManager.createTransaction(amount, aj);
        assertNotNull(transaction);
        assertEquals(amount, transaction.getAmount());
        assertNotNull(transaction.getId());
        assertTrue(transaction.getId() > -1);
        assertNotNull(transaction.getVersion());
        assertTrue(transaction.getVersion() > -1);
        assertNotNull(transaction.getCreated());
    }
}
