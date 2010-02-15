/*
 *
 * Author:  Artie Copeland
 * Last Modified Date: $DateTime: $
 */
package com.blackbox.security;

import static com.google.common.collect.Sets.newHashSet;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.terracotta.modules.annotations.InstrumentedClass;
import org.terracotta.modules.annotations.Root;
import org.yestech.cache.ICacheManager;

import java.util.Set;

/**
 * @author Artie Copeland
 * @version $Revision: $
 */
@InstrumentedClass
@SuppressWarnings("unchecked")
public class SecurityCache implements Cache {
    final private static Logger logger = LoggerFactory.getLogger(SecurityCache.class);

    private String cacheName;

    @Root
    private ICacheManager cache;

    public SecurityCache(String cacheName) {
        this.cacheName = cacheName;
    }

    public String getCacheName() {
        return cacheName;
    }

    public ICacheManager getCache() {
        return cache;
    }

    public void setCache(ICacheManager cache) {
        this.cache = cache;
    }

    @Override
    public Object get(Object key) throws CacheException {
        final Object result = cache.get(key);
        if (result == null) {
            if (logger.isInfoEnabled()) {
                logger.info("cache miss....");
            }
        } else {
            if (logger.isInfoEnabled()) {
                logger.info("cache hit....");
            }
        }
        return result;
    }

    @Override
    public void put(Object key, Object value) throws CacheException {
        cache.put(key, value);
    }

    @Override
    public void remove(Object key) throws CacheException {
        cache.flush(key);
    }

    @Override
    public void clear() throws CacheException {
        cache.flushAll();
    }

    @Override
    public int size() {
        return cache.keys().size();
    }

    @Override
    public Set keys() {
        return newHashSet(cache.keys());
    }

    @Override
    public Set values() {
        return newHashSet(cache.values());
    }
}
