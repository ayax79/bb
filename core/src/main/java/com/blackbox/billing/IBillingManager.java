package com.blackbox.billing;

import com.blackbox.user.User;

/**
 * @author A.J. Wright
 */
public interface IBillingManager {

    Transaction createTransaction(Money amount, User user);

    Transaction save(Transaction tx);

    Transaction loadTransaction(long transactionId);
}
