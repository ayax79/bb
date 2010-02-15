package com.blackbox.server.user;

import com.blackbox.user.AffiliateMapping;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author A.J. Wright
 */
public interface IAffiliateMappingDao {

    @Transactional
    void insert(AffiliateMapping mapping);

    AffiliateMapping loadByAffiliatesGuid(String userGuid);

    @Transactional
    void delete(AffiliateMapping mapping);
}
