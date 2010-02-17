/*
 *
 * Author:  Artie Copeland
 * Last Modified Date: $DateTime: $
 */
package com.blackbox.foundation.security;

import static com.google.common.collect.Sets.newHashSet;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.terracotta.modules.annotations.InstrumentedClass;
import org.terracotta.modules.annotations.Root;
import org.yestech.cache.ICacheManager;
import org.yestech.cache.impl.TerracottaDistributedCacheManager;

import javax.annotation.PreDestroy;
import java.util.Set;

/**
 * @author Artie Copeland
 * @version $Revision: $
 */
@InstrumentedClass
public class SecurityCacheManager implements CacheManager {
    final private static Logger logger = LoggerFactory.getLogger(SecurityCacheManager.class);

    @Root
    private ICacheManager<String, SecurityCache> cache;

    /**
     * Builds a new {@link SecurityCache} with the given name.
     *
     * @param cacheName the name of the new cache to create.
     * @return a new cache.
     */
    public Cache getCache(String cacheName) {
        Cache resultCache = cache.get(cacheName);
        if (resultCache == null) {
            SecurityCache securityCache = new SecurityCache(cacheName);
            TerracottaDistributedCacheManager cacheManager = new TerracottaDistributedCacheManager();
            cacheManager.start();
            securityCache.setCache(cacheManager);
            cache.put(cacheName, securityCache);
            resultCache = securityCache;
            if (logger.isInfoEnabled()) {
                logger.info("cache miss....");
            }
        } else {
            if (logger.isInfoEnabled()) {
                logger.info("cache hit....");
            }
        }
        return resultCache;
    }

    public ICacheManager getCache() {
        return cache;
    }

    public void setCache(ICacheManager<String, SecurityCache> cache) {
        this.cache = cache;
    }

    @PreDestroy
    public void destroy() {
        Set<SecurityCache> internalCaches = newHashSet(cache.values());
        for (SecurityCache internalCache : internalCaches) {
            TerracottaDistributedCacheManager cacheManager = (TerracottaDistributedCacheManager) internalCache.getCache();
            cacheManager.stop();
        }
    }
}
