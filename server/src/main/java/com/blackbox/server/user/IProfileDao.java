package com.blackbox.server.user;

import com.blackbox.foundation.user.Profile;
import com.blackbox.foundation.social.Address;

import java.util.List;

/**
 *
 *
 */
public interface IProfileDao {

    Profile loadProfileByUserGuid(String guid);

}
