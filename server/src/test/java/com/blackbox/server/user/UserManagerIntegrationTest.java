package com.blackbox.server.user;

import com.blackbox.Utils;
import com.blackbox.exception.UserAlreadyExistsException;
import com.blackbox.search.ExploreRequest;
import com.blackbox.search.SearchResult;
import com.blackbox.server.BaseIntegrationTest;
import com.blackbox.testingutils.UserFixture;
import com.blackbox.testingutils.UserHelper;
import com.blackbox.user.IUserManager;
import com.blackbox.user.PaginationResults;
import com.blackbox.user.User;
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

//    @Test
//    public void testTimeToLoadAllUsers() {
//        long start = System.currentTimeMillis();
//        ExploreRequest exploreRequest = new ExploreRequest();
//        exploreRequest.setUserGuid("105433d2148545d371fb1c71ef4851efb8c04392");
//        userManager.explore(exploreRequest);
//        System.out.println("time to explore all users = " + (System.currentTimeMillis() - start));
//    }

}
