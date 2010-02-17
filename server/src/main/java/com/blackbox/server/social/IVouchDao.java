package com.blackbox.server.social;

import com.blackbox.foundation.social.Vouch;

import java.util.List;

import org.joda.time.DateTime;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 *
 */
public interface IVouchDao {
    @Transactional
    Vouch save(Vouch vouch);

    @Transactional
    void delete(Vouch vouch);

    List<Vouch> loadByVoucher(String voucherGuid);

    List<Vouch> loadOutgoingByVoucher(String voucherGuid);

    List<Vouch> loadByTarget(String targetGuid);

    Vouch loadByVoucherAndTarget(String voucherGuid, String targetGuid);

    Vouch loadByGuid(String guid);

    List<Vouch> loadOlderThanDateWithType(DateTime date);

    int countVouchesForEntity(String userGuid);

    List<Vouch> loadUserVouches(String guid);

    @SuppressWarnings({"unchecked"})
    List<Vouch> loadAllByTarget(String targetGuid);

    int countByUserGuidTypeAndDate(String userGuid, boolean permanent, DateTime created);

    @SuppressWarnings({"unchecked"})
    List<Vouch> loadAllByTarget(String targetGuid, boolean approved);

    Vouch loadVouchByVoucherAndVouchee(String fromGuid, String toGuid);
}
