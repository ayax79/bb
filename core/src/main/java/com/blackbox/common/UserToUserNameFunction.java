package com.blackbox.common;

import com.blackbox.user.User;
import com.google.common.base.Function;

/**
 * @author colin@blackboxrepublic.com
 */
public class UserToUserNameFunction implements Function<User, String> {

    @Override
    public String apply(User from) {
        return from.getUsername();
    }
}
