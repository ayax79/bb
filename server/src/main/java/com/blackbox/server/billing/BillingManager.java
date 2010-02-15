package com.blackbox.server.billing;

import com.blackbox.billing.IBillingManager;
import com.blackbox.billing.Money;
import com.blackbox.billing.Transaction;
import com.blackbox.user.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * @author A.J. Wright
 */
@Service("billingManager")
public class BillingManager implements IBillingManager {

    @Resource
    protected IBillingDao billingDao;

    @Transactional(readOnly = false)
    public Transaction createTransaction(Money amount, User user) {
        return billingDao.save(new Transaction(amount, user));
    }

    @Transactional(readOnly = false)
    public Transaction save(Transaction tx) {
        return billingDao.save(tx);
    }

    public Transaction loadTransaction(long transactionId) {
        return billingDao.load(transactionId);
    }

    public void setBillingDao(IBillingDao billingDao) {
        this.billingDao = billingDao;
    }
}
