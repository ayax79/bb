/*
 * Copyright (c) 2009. Blackbox, LLC
 * All rights reserved.
 */

package com.blackbox.server.user;

import com.blackbox.foundation.Status;
import com.blackbox.foundation.search.ExploreRequest;
import com.blackbox.foundation.search.WordFrequency;
import com.blackbox.foundation.user.*;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Handles persistence of user objects.
 *
 * @author A.J. Wright
 */
public interface IUserDao {

    /**
     * Either creates the user or updates the user.
     * <p/>
     * This will save the user's version and id fields.
     *
     * @param user The user to create or save.
     */
    @Transactional
    User save(User user);

    /**
     * Deletes an existing user from the system.
     *
     * @param user The suer to delete.
     */
    @Transactional
    void delete(IUser user);

    /**
     * Loada a user by its email.
     *
     * @param email The email of user to load
     * @return Returns the user with the specified email.
     */
    User loadUserByEmail(String email);

    IUser loadUserByUsernameAndPasswordAndStatus(String username, String password, Status unverified);

    User loadUserByUsername(String username);

    User loadUserByGuid(String guid);

    List<WordFrequency> highFrequencyWords(String userGuid);

    List<User> explore(ExploreRequest er);

    User loadSessionCacheUserByGuid(String guid);

    MiniProfile loadMiniProfileByUserGuid(String guid);

    MiniProfile loadMiniProfileByUsername(String username);

    @Transactional
    void save(EmailCapture ec);

    @Transactional
    void deleteCapturedEmail(String email);

    void reindex();

    boolean isUsernameAvailable(String username);

    boolean isEmailAvailable(String email);

    Profile loadProfileByGuid(String guid);

    List<User> loadAllUsers();

    int countAllActiveUsers();
}
