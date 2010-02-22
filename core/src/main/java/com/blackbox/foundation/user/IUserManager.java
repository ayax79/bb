/*
 * Copyright (c) 2009. Blackbox, LLC
 * All rights reserved.
 */

package com.blackbox.foundation.user;

import com.blackbox.foundation.search.ExploreRequest;
import com.blackbox.foundation.search.SearchResult;
import com.blackbox.foundation.search.WordFrequency;
import com.blackbox.foundation.security.Privacy;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Provides CRUD methods for manipulating {@link User} objects.
 * <p/>
 * Actually, note that there is not really a method to delete a user, since we generally do not want to do that.
 * There several {@link com.blackbox.foundation.Status} method that pertain specifically to users. If a user should not be active in the system
 * the user should be marked as {@link com.blackbox.foundation.Status#DELETED}.
 * <p/>
 * <h5>Status values as they pertain to users</h5>
 * <ul>
 * <li>{@link com.blackbox.foundation.Status#ENABLED} - User is enabled and able to access the site as normal.<li>
 * <li>{@link com.blackbox.foundation.Status#DISABLED} - User is disabled and denied login. Content remains visible.</li>
 * <li>{@link com.blackbox.foundation.Status#DELETED} - User is denied login and their content is nolonger visible.</li>
 * <li>{@link com.blackbox.foundation.Status#UNVERIFIED} - User has not yet verified their registration. Login is denied until they click on the verification link in their email.</li>
 * </ul>
 *
 * @see com.blackbox.foundation.user.User
 */
public interface IUserManager {


    /**
     * Creates a new user in the blackbox application.
     * <p/>
     * Consider using {@link #register(Registration)} instead.
     * <p/>
     * If the guid of the user has not been populated it will be assigned a new guid.
     * If you wish to have the user guid before the user is created create a new instance of the user using the
     * {@link com.blackbox.foundation.user.User#createUser()} method.
     * <p/>
     * <h5>After this method is called the following fields will be populated.</h5>
     * <ul>
     * <li>created - the time this object was created</li>
     * <li>version - a valid version number for this object, should be 0</li>
     * <li>modified - the time this object was created</li>
     * </ul>
     *
     * @param user the user to create.
     * @return The newly created User.
     */
    User createUser(User user);

    /**
     * Persists settings and changes of a user.
     * <p/>
     * <h5>The following fields should not be modified.</h5>
     * <ul>
     * <li>created</li>
     * <li>modified</li>
     * <li>guid</li>
     * </ul>
     * <p/>
     * <h5>After the following can be expected</h5>
     * <ul>
     * <li>The version field should be incremented.</li>
     * <li>The modified field should contain the time updated.</li>
     * </ul>
     *
     * @param user The user to persist options for.
     * @return The updated user
     */
    User save(User user);

    /**
     * Loads the first user with the specified email.
     *
     * @param email The email to find a user by.
     * @return The first user with the matched email
     */
    User loadUserByEmail(String email);

    /**
     * Used to verify a user that has not yet been verified.
     * <p/>
     * If the user is any other state but UNVERFIED false will be returned.
     *
     * @param userGuid The guid of the user.
     * @param key      The has that was sent in an email used by the user to verify.
     * @return True if the user was succesfully verified.
     */
    VerificationResult verifyUser(String userGuid, String key);

    /**
     * Retrieves a user with the specified username.
     *
     * @param username The name of the user to retrive.
     * @return The user with the specified username.
     */
    User loadUserByUsername(String username);

    /**
     * Used for registering a new user in the system.
     *
     * @param registration All the registration data
     */
    @Transactional
    void register(Registration registration);

    /**
     * Retrieve the user with the specified guid.
     *
     * @param guid The guid of the user to load.
     * @return The user with the matching guid.
     */
    User loadUserByGuid(String guid);

    /**
     * Retrieve the user with the specified guid.
     *
     * @param guid The guid of the user to load.
     * @return The user with the matching guid.
     */
    User loadUserByGuidForDisplayName(String guid);

    void markProfileViewed(String userGuid);

    void saveViewedBy(String viewingGuid, String destGuid, ViewedBy.ViewedByType viewedByType);

    List<ViewedBy> loadViewersByDestGuid(String destGuid);

    Integer loadViewNumByDestGuid(String destGuid);

    UserStats loadUserStats(String userGuid);

    void forgotPassword(String email);

    @Transactional(readOnly = true)
    Profile loadProfileByUserGuid(String userGuid);


    List<WordFrequency> highFrequencyWords(String userGuid);

    void addOnlineUser(String userGuid);

    void removeOnlineUser(String userGuid);

    int getOnlineUserCount();

    BooleanResponse isUserOnline(String userGuid);

//    @GET
//    @Path("/online")
//    @Wrapped(element = "users")
//    Collection<User> getOnlineUsers();

    void setSessionRefreshNeeded(String guid);

    BooleanResponse isSessionRefreshNeeded(String guid);

    PaginationResults<SearchResult<User>> explore(ExploreRequest er);

    /**
     * Affiliates the userGuid user with the affiliateIdentifier affiliate.
     *
     * @param affiliateIdentifier may be either the affiliate guid or the affiliate user name
     * @throws "NotFoundException" should the affiliate or the user not be found
     */
    void affiliate(String affiliateIdentifier, String userGuid);

    User loadSessionCacheUserByGuid(String guid);

    UserPersona loadUserPersona(String guid, String callerGuid);

    MiniProfile loadMiniProfile(String identifier);

    ExploreRequest lastSearch(String guid);

    BasePromoCode loadPromoCodeByCode(String promoCode);

    List<ExternalCredentials> loadExternalCredentialsForUser(String userGuid);

    ExternalCredentials saveExternalCredential(ExternalCredentials ec);

    ExternalCredentials loadExternalCredentialsForUserAndCredType(String userGuid, ExternalCredentials.CredentialType type);

    Privacy loadPrivacy(String guid);

    void captureEmail(String email);

    @Transactional
    SingleUsePromoCode createSingleUsePromoCode(SingleUsePromoCode code);

    @Transactional
    MultiUserPromoCode createMultiUserPromoCode(MultiUserPromoCode code);

    /**
     * Used to check to see if a given username is available in the system.
     * This check will ignore username's for incomplete registrations.
     *
     * @param username The username to check availability on.
     * @return True if the username is available.
     */
    boolean isUsernameAvailable(String username);

    /**
     * Used to check to see if a given email address is available in the system.
     * This check will ignore email addresses' for incomplete registrations.
     *
     * @param email The email address to check availability on.
     * @return True if the email address is available.
     */
    boolean isEmailAvailable(String email);

}
