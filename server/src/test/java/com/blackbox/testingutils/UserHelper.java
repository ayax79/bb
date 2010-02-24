package com.blackbox.testingutils;

import com.blackbox.foundation.Utils;
import com.blackbox.foundation.user.IUserManager;
import com.blackbox.foundation.user.User;

/**
 * @author colin@blackboxrepublic.com
 */
public class UserHelper {

    /**
     * @return a user, persisted to the database, with a user name that starts with nameRoot and ends with ":" + a 32-character
     *         guid with and email address of nameRoot + "@mailinator.com"
     */
    public static User createNamedUser(String nameRoot, IUserManager userManager) {
        User user = User.createUser();
        user.setName(nameRoot);
        user.setUsername(nameRoot + ":" + Utils.generateGuid());
        user.setFirstname(nameRoot);
        user.setLastname(nameRoot);
        user.setEmail(nameRoot + "@mailinator.com");
        user.setPassword(Utils.generateGuid());
        return userManager.createUser(user);
    }


}
