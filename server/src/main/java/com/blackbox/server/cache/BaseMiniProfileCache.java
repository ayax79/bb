package com.blackbox.server.cache;

import com.blackbox.user.MiniProfile;
import com.blackbox.activity.IActivity;
import org.yestech.cache.ICacheManager;

import javax.annotation.Resource;

/**
 *
 *
 */
public abstract class BaseMiniProfileCache {
    @Resource(name = "miniProfileCache")
    protected ICacheManager<String, MiniProfile> miniProfileCache;

    public void setMiniProfileCache(ICacheManager<String, MiniProfile> miniProfileCache) {
        this.miniProfileCache = miniProfileCache;
    }

    protected void updateMiniProfile(IActivity activity) {
        String senderGuid = activity.getSender().getGuid();
        MiniProfile miniProfile = miniProfileCache.get(senderGuid);
        if (miniProfile != null) {
            miniProfile.setActivity(activity);
            miniProfileCache.put(senderGuid, miniProfile);
        }
    }
}
