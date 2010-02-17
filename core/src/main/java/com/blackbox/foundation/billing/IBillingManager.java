package com.blackbox.foundation.billing;

import com.blackbox.foundation.user.User;

/**
 * @author A.J. Wright
 */
public interface IBillingManager {

    Transaction createTransaction(Money amount, User user);

    Transaction save(Transaction tx);

    Transaction loadTransaction(long transactionId);
}
