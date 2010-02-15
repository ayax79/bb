package com.blackbox.server.billing;

import com.blackbox.billing.Transaction;
import com.blackbox.billing.BillingInfo;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author A.J. Wright
 */
public interface IBillingDao {
    @Transactional
    Transaction save(Transaction tx);

    Transaction load(long id);

    @Transactional
    BillingInfo save(BillingInfo billing);

    BillingInfo loadByUser(String userGuid);
}
