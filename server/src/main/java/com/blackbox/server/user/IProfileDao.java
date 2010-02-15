package com.blackbox.server.user;

import com.blackbox.user.Profile;
import com.blackbox.social.Address;

import java.util.List;

/**
 *
 *
 */
public interface IProfileDao {

    Profile loadProfileByUserGuid(String guid);

}
