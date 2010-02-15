package com.blackbox.server.billing;

import com.blackbox.billing.BillingInfo;
import com.blackbox.billing.Transaction;
import com.blackbox.server.util.PersistenceUtil;
import org.joda.time.DateTime;
import org.springframework.orm.ibatis3.SqlSessionOperations;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * @author A.J. Wright
 */
@Repository("billingDao")
public class IbatisBillingDao implements IBillingDao {

    @Resource(name = "sqlSessionTemplate")
    SqlSessionOperations template;

    @Transactional
    public Transaction save(Transaction tx) {
        if (tx.getId() == null) {
            Long id = (Long) template.selectOne("Transaction.loadSequenceNextVal");
            tx.setVersion(0);
            tx.setId(id);
            tx.setCreated(new DateTime());
            template.insert("Transaction.insert", tx);
        }
        return tx;
    }

    public Transaction load(long id) {
        return (Transaction) template.selectOne("Transaction.insert", id);
    }

    @Transactional
    public BillingInfo save(BillingInfo billing) {
        PersistenceUtil.insertOrUpdate(billing, template, "BillingInfo.insert", "BillingInfo.update");
        return billing;
    }

    public BillingInfo loadByUser(String userGuid) {
        return (BillingInfo) template.selectOne("BillingInfo.loadByUser", userGuid);
    }

}
