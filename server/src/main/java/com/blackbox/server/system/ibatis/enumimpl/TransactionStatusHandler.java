package com.blackbox.server.system.ibatis.enumimpl;

import com.blackbox.foundation.billing.Transaction;
import com.blackbox.server.system.ibatis.OrdinalEnumTypeHandler;

/**
 * @author A.J. Wright
 */
public class TransactionStatusHandler extends OrdinalEnumTypeHandler {

    public TransactionStatusHandler() {
        super(Transaction.TransactionStatus.class);
    }
}
