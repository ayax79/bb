package com.blackbox.foundation.common;

import com.blackbox.foundation.user.Profile;
import com.google.common.base.Predicate;

/**
 * @author colin@blackboxrepublic.com
 */
public final class PrivateProfilePredicate implements Predicate<Profile> {

    @Override
    public boolean apply(Profile input) {
        return input.getMood().isMakePrivate();
    }
}
