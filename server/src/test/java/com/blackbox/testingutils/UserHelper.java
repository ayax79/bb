package com.blackbox.testingutils;

import com.blackbox.Utils;
import com.blackbox.user.IUserManager;
import com.blackbox.user.User;

/**
 * @author colin@blackboxrepublic.com
 */
public class UserHelper {

    public static User createNamedUser(String nameRoot, IUserManager userManager) {
        User user = User.createUser();
        user.setName(nameRoot);
        user.setUsername(nameRoot + "-" + Utils.generateGuid());
        user.setFirstname(nameRoot);
        user.setLastname(nameRoot);
        user.setEmail(nameRoot + "@mailinator.com");
        user.setPassword("----XYX-----");
        return userManager.createUser(user);
    }


}
