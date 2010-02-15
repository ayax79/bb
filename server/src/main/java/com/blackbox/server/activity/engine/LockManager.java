package com.blackbox.server.activity.engine;

import static org.yestech.lib.concurrency.LockManager.getLock;
import org.yestech.lib.concurrency.LockType;

import java.util.concurrent.locks.Lock;

/**
 *
 *
 */
public class LockManager {

    final public static String GLOBAL_KEY = "GLOBAL:";
    private static final String FRIENDS_LOCK_QUALIFIER_KEY = ":friends";
    private static final String FOLLOWERS_LOCK_QUALIFIER_KEY = ":following";
    private static final String CHILD_LOCK_QUALIFIER_KEY = "CHILD:";
    private static final String MEBOX_PREFIX = "mebox:";

    public static Lock locateGlobalChildLock(String parentGuid) {
        return locateLock(CHILD_LOCK_QUALIFIER_KEY + GLOBAL_KEY + parentGuid);
    }

    public static Lock locateNonGlobalChildLock(String parentGuid) {
        return locateLock(CHILD_LOCK_QUALIFIER_KEY + parentGuid);
    }

    public static Lock locateMeBoxLock(String senderGuid) {
        return locateLock(MEBOX_PREFIX + senderGuid);
    }

    public static Lock locateFriendsPrimaryLock(String guid) {
        return locateLock(guid + FRIENDS_LOCK_QUALIFIER_KEY);
    }

    public static Lock locateFollowersPrimaryLock(String guid) {
        return locateLock(guid + FOLLOWERS_LOCK_QUALIFIER_KEY);
    }

    public static Lock locateGlobalLock() {
        return locateLock(GLOBAL_KEY);
    }

    public static Lock locateGlobalChildMetaDataLock(String parentGuid) {
        return locateLock(generateGlobalChildMetaDataKey(parentGuid));
    }

    public static String generateGlobalChildMetaDataKey(String parentGuid) {
        return GLOBAL_KEY + parentGuid;
    }

    private static Lock locateLock(final String lockKey) {
        return getLock(lockKey, LockType.OBJECT);
    }
}
