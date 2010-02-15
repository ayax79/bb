package com.blackbox.common;

import com.blackbox.user.Profile;
import com.blackbox.user.User;
import com.google.common.base.Function;

/**
 * @author colin@blackboxrepublic.com
 */
public class UserToProfileFunctor implements Function<User, Profile> {
    @Override
    public Profile apply(User from) {
        return from == null ? null : from.getProfile();
    }
}
