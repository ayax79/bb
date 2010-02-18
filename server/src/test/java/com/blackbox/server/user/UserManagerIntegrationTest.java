package com.blackbox.server.user;

import com.blackbox.foundation.Utils;
import com.blackbox.foundation.exception.BlackBoxException;
import com.blackbox.foundation.exception.UserAlreadyExistsException;
import com.blackbox.foundation.search.ExploreRequest;
import com.blackbox.foundation.search.SearchResult;
import com.blackbox.foundation.user.*;
import com.blackbox.server.BaseIntegrationTest;
import com.blackbox.testingutils.UserFixture;
import com.blackbox.testingutils.UserHelper;
import org.junit.Test;

import javax.annotation.Resource;

import static junit.framework.Assert.*;

/**
 * @author colin@blackboxrepublic.com
 */
public class UserManagerIntegrationTest extends BaseIntegrationTest {

    @Resource
    private IUserManager userManager;

    @Test
    public void testLoadUserByUsernameIsCaseInsensitive() throws Exception {
        User user = userManager.loadUserByUsername("april");
        assertNotNull(user);
        assertFalse(userManager.isUsernameAvailable("april"));

        User userDifferentCase = userManager.loadUserByUsername("apRil");
        assertNotNull(userDifferentCase);
        assertFalse(userManager.isUsernameAvailable("apRil"));
        assertEquals(user, userDifferentCase);

        userDifferentCase = userManager.loadUserByUsername("APRIl");
        assertNotNull(userDifferentCase);
        assertFalse(userManager.isUsernameAvailable("APRIl"));
        assertEquals(user, userDifferentCase);

    }

    @Test(expected = UserAlreadyExistsException.class)
    public void testCreateIdenticalUserViaCreate() throws Exception {
        User user = userManager.createUser(UserHelper.createNamedUser("colin", userManager));
        assertNotNull(user);
        userManager.createUser(user);
    }

    @Test(expected = UserAlreadyExistsException.class)
    public void testCreateIdenticalUserViaSaveAndCreate() throws Exception {
        User user = userManager.save(UserHelper.createNamedUser("colin", userManager));
        assertNotNull(user);
        userManager.createUser(user);
    }

    @Test(expected = UserAlreadyExistsException.class)
    public void testCreateIdenticalUserNamedUserViaCreateAndCreate() throws Exception {
        User user = userManager.createUser(UserHelper.createNamedUser("colin", userManager));
        assertNotNull(user);
        user.setGuid(Utils.generateGuid());
        userManager.createUser(user);
    }

    @Test(expected = UserAlreadyExistsException.class)
    public void testCreateIdenticalUserNamedUserViaSaveAndCreate() throws Exception {
        User user = userManager.save(UserHelper.createNamedUser("colin", userManager));
        assertNotNull(user);
        user.setGuid(Utils.generateGuid());
        userManager.createUser(user);
    }


    @Test
    public void testDeferredDataLoadingWorksCorrectly() {
        ExploreRequest exploreRequest = new ExploreRequest();
        exploreRequest.setMaxResults(10);
        exploreRequest.setUserGuid(UserFixture.april.getGuid());
        exploreRequest.setZipCode(String.valueOf(97214));
        exploreRequest.setMaxDistance(Integer.MAX_VALUE);
        PaginationResults<SearchResult<User>> results = userManager.explore(exploreRequest);
        searchResultsHaveThe5DataPartsPushedOntoThem(results);
        exploreRequest.setStartIndex(10);
        results = userManager.explore(exploreRequest);
        searchResultsHaveThe5DataPartsPushedOntoThem(results);
    }

    private void searchResultsHaveThe5DataPartsPushedOntoThem(PaginationResults<SearchResult<User>> results) {
        for (SearchResult<User> userSearchResult : results.getResults()) {
            // not all users have latest activity or longitude/latitude so we can't assert that is not null always but the rest of these should not be...
            assertNotNull("we have incomplete user search results data [network]", userSearchResult.getNetwork());
            assertNotNull("we have incomplete user search results data [wish status]", userSearchResult.getWishStatus());
            assertNotNull("we have incomplete user search results data [vouch count]", userSearchResult.getVouchCount());
        }
    }

    @Test
    public void testRegistrationWithSingleUsePromoCode() {
        String promoCode = "xlnczjqo";
        BasePromoCode code = userManager.loadPromoCodeByCode(promoCode);
        assertNotNull(code);
        Registration registration = new Registration();
        registration.setUser(UserHelper.createNamedUser("colin", userManager));
        registration.setPromoCodeGuid(code.getGuid());
        userManager.register(registration);
        assertNull(userManager.loadPromoCodeByCode(promoCode));
    }

    @Test
    public void testRegistrationWithMultipleUsePromoCode() {
        String promoCode = "eeuw58wx";
        BasePromoCode code = userManager.loadPromoCodeByCode(promoCode);
        assertNotNull(code);
        Registration registration = new Registration();
        registration.setUser(UserHelper.createNamedUser("colin", userManager));
        registration.setPromoCodeGuid(code.getGuid());
        userManager.register(registration);
        assertNotNull(userManager.loadPromoCodeByCode(promoCode));
    }

}
