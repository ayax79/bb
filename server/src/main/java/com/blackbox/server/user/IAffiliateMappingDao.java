package com.blackbox.server.user;

import com.blackbox.foundation.user.AffiliateMapping;
import com.blackbox.foundation.user.User;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author A.J. Wright
 */
public interface IAffiliateMappingDao {

    AffiliateMapping loadByAffiliatesGuid(String userGuid);

    @Transactional
    void save(AffiliateMapping mapping);

    @Transactional
    void delete(AffiliateMapping mapping);

    /**
     * Associates the user to the affiliate mapping both in the database and on the object graph
     */
    @Transactional
    void affiliateUser(AffiliateMapping mapping, User user);
}
