package com.blackbox.server.user;

import com.blackbox.foundation.user.AffiliateMapping;
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
}
