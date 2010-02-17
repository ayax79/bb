package com.blackbox.server.social;

import static com.blackbox.server.util.PersistenceUtil.insertOrUpdate;
import com.blackbox.foundation.social.Vouch;
import org.joda.time.DateTime;
import org.springframework.orm.ibatis3.SqlSessionOperations;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 *
 */
@Repository("vouchDao")
public class IbatisVouchDao implements IVouchDao {

    @Resource
    private SqlSessionOperations template;


    @Override
    @Transactional(readOnly = false)
    public Vouch save(Vouch vouch) {
        insertOrUpdate(vouch, template, "Vouch.insert", "Vouch.update");
        return vouch;
    }

    @Override
    public Vouch loadByGuid(String guid) {
        return (Vouch) template.selectOne("Vouch.load", guid);
    }

    @Override
    public void delete(Vouch vouch) {
        template.delete("Vouch.delete", vouch.getGuid());
    }

    public int countByUserGuidTypeAndDate(String userGuid, boolean permanent, DateTime created) {
        DateTime expirationDate = new DateTime().plusYears(1);

        Map<String, Object> params = new HashMap<String, Object>(3);
        params.put("created", created);
        params.put("guid", userGuid);
        params.put("expirationDate", expirationDate);

        if (permanent) {
            return (Integer) template.selectOne("Vouch.countbyUserGuidTypeAndDatePermanent", params);
        }
        else {
            return (Integer) template.selectOne("Vouch.countbyUserGuidTypeAndDateTemporary", params);
        }

    }

    @Override
    public int countVouchesForEntity(String userGuid) {
        Map<String, Object> params = new HashMap<String, Object>(2);
        params.put("expirationDate", new DateTime());
        params.put("guid", userGuid);

        return (Integer) template.selectOne("Vouch.countVouchesForEntity", params);
    }

    @Override
    @SuppressWarnings({"unchecked"})
    public List<Vouch> loadByVoucher(String voucherGuid) {
        return template.selectList("Vouch.loadByVoucher", voucherGuid);
    }

    @Override
    @SuppressWarnings({"unchecked"})
    public List<Vouch> loadOutgoingByVoucher(String voucherGuid) {
        return template.selectList("Vouch.loadOutgoingByVoucher", voucherGuid);
    }

    @Override
    @SuppressWarnings({"unchecked"})
    public List<Vouch> loadByTarget(String targetGuid) {
        Map<String, Object> params = new HashMap<String, Object>(2);
        params.put("targetGuid", targetGuid);
        return template.selectList("Vouch.loadByTargetAndApproval", params);
    }


    @Override
    @SuppressWarnings({"unchecked"})
    public List<Vouch> loadAllByTarget(String targetGuid) {
        Map<String, Object> params = new HashMap<String, Object>(2);
        params.put("targetGuid", targetGuid);
        params.put("expirationDate", new DateTime());

        return template.selectList("Vouch.loadByTarget", params);
    }

    @Override
    @SuppressWarnings({"unchecked"})
    public List<Vouch> loadAllByTarget(String targetGuid, boolean approved) {
        return loadByTarget(targetGuid);
    }
    
    @Override
    public Vouch loadByVoucherAndTarget(String voucherGuid, String targetGuid) {
        Map<String, Object> params = new HashMap<String, Object>(2);
        params.put("voucherGuid", voucherGuid);
        params.put("targetGuid", targetGuid);
        
        return (Vouch) template.selectOne("Vouch.loadByVoucherAndTarget", params);
    }

    public List<Vouch> loadOlderThanDateWithType(final DateTime date) {
        Map<String, Object> params = new HashMap<String, Object>(1);
        params.put("date", date);
        return template.selectList("Vouch.loadOlderThanDateWithType", params);
    }

    @Override
    @SuppressWarnings({"unchecked"})
    public List<Vouch> loadUserVouches(String guid) {
        return template.selectList("Vouch.loadUserVouches", guid);
    }

    @Override
    public Vouch loadVouchByVoucherAndVouchee(String fromGuid, String toGuid) {
        Map<String, Object> params = new HashMap<String, Object>(2);
        return (Vouch) template.selectOne("Vouch.loadByVoucherAndVouchee", params);
    }
}
