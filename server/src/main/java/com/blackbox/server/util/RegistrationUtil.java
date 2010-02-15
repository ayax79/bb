/*
 * Copyright (c) 2009. Blackbox, LLC
 * All rights reserved.
 */

/*
 *
 * Original Author:  Artie Copeland
 * Last Modified Date: $DateTime: $
 */
package com.blackbox.server.util;

import com.blackbox.user.IUser;
import com.blackbox.user.User;
import org.apache.commons.lang.StringUtils;
import static org.yestech.lib.crypto.MessageDigestUtils.sha1Hash;
import org.yestech.cache.ICacheManager;
import org.joda.time.DateTime;

/**
 * @author $Author: $
 * @version $Revision: $
 */
public class RegistrationUtil
{

    public static String generateValidationHash(IUser user) {
        final long time = System.currentTimeMillis();
        return sha1Hash(user.getGuid() + ":" + time + ":" + user.getUsername());
    }

    public static boolean checkValidationHash(String hash, ICacheManager<String, DateTime> cache)
    {
        boolean valid = false;
        if (!StringUtils.isBlank(hash)) {
            DateTime dateTime = cache.get(hash);
            if (dateTime != null) {
                valid = true;
            }
        }
        return valid;
    }
}
