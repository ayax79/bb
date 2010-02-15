package com.blackbox.server.social;

import com.blackbox.social.Ignore;
import com.blackbox.EntityReference;

import java.util.List;

/**
 *
 *
 */
public interface IIgnoreDao {
    Ignore save(Ignore vouch);

    void delete(Ignore vouch);

    List<EntityReference> loadByIgnorer(String voucherGuid);

    List<EntityReference> loadByTarget(String targetGuid);

    Ignore loadByIgnorerAndTarget(String voucherGuid, String targetGuid);

    Ignore loadByGuid(String guid);

    List<EntityReference> loadByTargetandType(String targetGuid, Ignore.IgnoreType type);

    List<EntityReference> loadByIgnorerAndType(String ignorerGuid, Ignore.IgnoreType type);
}