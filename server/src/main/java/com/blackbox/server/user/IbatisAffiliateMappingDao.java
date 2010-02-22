package com.blackbox.server.user;

import com.blackbox.foundation.user.User;
import com.blackbox.foundation.util.Affirm;
import com.blackbox.server.util.PersistenceUtil;
import com.blackbox.foundation.user.AffiliateMapping;
import org.springframework.orm.ibatis3.SqlSessionOperations;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.yestech.lib.util.Pair;

import javax.annotation.Resource;
import java.util.Collection;

import static com.google.common.collect.Lists.newArrayList;

/**
 * @author A.J. Wright
 */
@Repository("affiliateMappingDao")
public class IbatisAffiliateMappingDao implements IAffiliateMappingDao {

    @Resource
    private SqlSessionOperations template;

    @Override
    @Transactional
    public void save(AffiliateMapping mapping) {
        PersistenceUtil.insertOrUpdate(mapping, template, "AffiliateMapping.insert", "AffiliateMapping.update");
    }

    @Override
    public AffiliateMapping loadByAffiliatesGuid(String userGuid) {
        return (AffiliateMapping) template.selectOne("AffiliateMapping.loadByAffiliateGuid", userGuid);
    }

    @Override
    @Transactional
    public void delete(AffiliateMapping mapping) {
        template.delete("AffiliateMapping.delete", mapping.getGuid());
    }

    @Override
    @Transactional
    public void affiliateUser(AffiliateMapping mapping, User user) {
        template.insert("AffiliateMapping.insertNewlyAffiliatedUser", new Pair<String, String>(mapping.getGuid(), user.getGuid()));
        save(mapping);
    }

}
