/*
 *
 * Author:  Artie Copeland
 * Last Modified Date: $DateTime: $
 */
package com.blackbox.service.security;

import org.yestech.cache.ICacheManager;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author Artie Copeland
 * @version $Revision: $
 */
@Component
public class AuthenticationUtils {
    
    private ICacheManager cache;

    public ICacheManager getCache() {
        return cache;
    }

    @Resource(name = "serviceAuthenticatedCache")
    public void setCache(ICacheManager cache) {
        this.cache = cache;
    }


    public boolean isValidateMerchant(String merchantGuid) {
        return true;
    }

    public boolean isValidateMember(String memberGuid) {
        return true;
    }
}
