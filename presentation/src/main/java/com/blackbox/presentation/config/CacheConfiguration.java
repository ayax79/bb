package com.blackbox.presentation.config;

import com.blackbox.foundation.media.AvatarImage;
import com.blackbox.foundation.message.Message;
import com.blackbox.presentation.action.util.AvatarCacheKey;
import com.blackbox.presentation.action.util.DisplayNameCacheKey;
import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.yestech.cache.ICacheManager;
import org.yestech.cache.impl.EhCacheCacheManager;

import java.util.Collection;

@Configuration
public class CacheConfiguration {

    @Bean
    public CacheManager cacheManager() {
        return CacheManager.create(getClass().getResourceAsStream("/cache/ehcache.xml"));
    }

    @Bean
    public ICacheManager<AvatarCacheKey, AvatarImage> avatarCache() {
        return createCache("avatarCache");
    }

    @Bean
    public ICacheManager<DisplayNameCacheKey, String> displayNameCache() {
        return createCache("displayNameCache");
    }

    @Bean
    public ICacheManager<String, String> corkboardCache() {
        return createCache("corkboardCache");
    }

    @Bean
    public ICacheManager<Object, Object> publicCache() {
        return createCache("publicCache");
    }

    @Bean
    public ICacheManager<String, Collection<Message>> prePublishedMessageCache() {
        return createCache("prePublishedMessageCache");
    }

    private <K, V> ICacheManager<K, V> createCache(String name) {
        Cache cache = cacheManager().getCache(name);
        EhCacheCacheManager<K, V> cm = new EhCacheCacheManager<K, V>();
        cm.setCache(cache);
        return cm;
    }


}
