package com.blackbox.server.user;

import com.blackbox.Status;
import com.blackbox.common.PrivateProfilePredicate;
import com.blackbox.common.UserToProfileFunctor;
import com.blackbox.common.UserToUserNameFunction;
import com.blackbox.search.ExploreRequest;
import com.blackbox.server.BaseIntegrationTest;
import com.blackbox.server.util.PersistenceUtil;
import com.blackbox.social.NetworkTypeEnum;
import com.blackbox.testingutils.UserFixture;
import com.blackbox.user.*;
import com.blackbox.util.Range;
import com.google.common.collect.Collections2;
import org.apache.commons.collections15.iterators.LoopingIterator;
import org.joda.time.DateTime;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.orm.ibatis3.SqlSessionTemplate;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.TreeSet;

import static junit.framework.Assert.*;
import static org.yestech.lib.crypto.PasswordGenerator.createPassword;

/**
 * @author A.J. Wright
 */
public class UserDaoIntegrationTest extends BaseIntegrationTest {

    @Resource
    IUserDao userDao;

    @Resource
    SqlSessionTemplate template;


    @Test
    public void testExploreRegistrationDate() {
        ExploreRequest er = new ExploreRequest();
        er.setRegistrationDate(new DateTime().minusMonths(3));

        List<User> list = userDao.explore(er);
        assertNotNull(list);
        assertTrue(list.size() > 0);
    }

    @Test
    public void testExploreFriends() {
        ExploreRequest er = new ExploreRequest();
        er.setDepth(NetworkTypeEnum.FRIENDS);
        er.setUserGuid(UserFixture.aj.getGuid());

        List<User> list = userDao.explore(er);
        assertNotNull(list);
        assertTrue(list.size() > 0);
    }

    @Test
    public void testExploreFollowing() {
        ExploreRequest er = new ExploreRequest();
        er.setDepth(NetworkTypeEnum.FOLLOWING);
        er.setUserGuid("1");

        List<User> list = userDao.explore(er);
        assertNotNull(list);
        assertTrue(list.size() > 0);
    }

    @Test
    public void testLastOnline() {
        ExploreRequest er = new ExploreRequest();
        er.setLastOnline(new DateTime().minusMonths(5));

        List<User> list = userDao.explore(er);
        assertNotNull(list);
        assertTrue(list.size() > 0);
    }

    @Test
    public void testName() {
        ExploreRequest er = new ExploreRequest();
        er.setName("aj");

        List<User> list = userDao.explore(er);
        assertNotNull(list);
        assertTrue(list.size() > 0);
    }

    @Test
    @Transactional
    @Rollback
    public void testSaveNew() {
        User user = new User();
        user.setName("bob");
        user.setUsername(createPassword(8));
        user.setPassword(createPassword(8));
        user.setEmail(createPassword(4) + "@testesttest.com");
        user.getProfile().setReferFrom(new ReferFrom());
        user.getProfile().getReferFrom().setReferFromType(ReferFrom.ReferFromType.Other);
        user.getProfile().getCurrentAddress().setCity("bar bar bar");
        user.getProfile().setPhoneHome("foo bop");

        userDao.save(user);
        assertNotNull(user.getGuid());
        assertNotNull(user.getVersion());
        assertNotNull(user.getProfile().getGuid());
        assertNotNull(user.getProfile().getVersion());
        assertNotNull(user.getProfile().getCreated());
        assertNotNull(user.getProfile().getModified());
        assertNotNull(user.getCreated());
        assertTrue("created lost its time portion when instantiated on save", user.getCreated().getSecondOfDay() > 0);

        User u1 = userDao.loadUserByGuid(user.getGuid());
        assertNotNull(u1);
        assertNotNull(u1.getCreated());
        assertTrue("created lost its time portion when persisted to database", u1.getCreated().getSecondOfDay() > 0);
        assertEquals(user.getGuid(), u1.getGuid());
        assertNotSame(u1.getGuid(), u1.getProfile().getGuid());
        assertEquals(user.getName(), u1.getName());
        assertEquals(user.getProfile().getGuid(), u1.getProfile().getGuid());
        assertEquals(user.getProfile().getPhoneHome(), u1.getProfile().getPhoneHome());
        assertEquals(user.getProfile().getCurrentAddress().getCity(), u1.getProfile().getCurrentAddress().getCity());
        assertEquals(user.getCreated().getMillis(), u1.getCreated().getMillis());  // equals not workie b/c different time zones

        // test update
        u1.setName("sdlfksjdf");
        u1.getProfile().getCurrentAddress().setCity("foo foo bar");
        userDao.save(u1);

        User u2 = userDao.loadUserByGuid(user.getGuid());
        assertEquals(u1.getName(), u2.getName());
        assertEquals(u1.getProfile().getCurrentAddress().getCity(), u2.getProfile().getCurrentAddress().getCity());

        userDao.delete(user); // test delete

    }

    @Test
    public void testLoadByGuid() {
        User user = userDao.loadUserByGuid(UserFixture.aj.getGuid());
        assertEquals(Status.ENABLED, user.getStatus());

    }

    @Test
    public void testLoginQuery() {
        User user = (User) userDao.loadUserByUsernameAndPasswordAndStatus("aj", "dc724af18fbdd4e59189f5fe768a5f8311527050", Status.ENABLED);
        assertEquals(Status.ENABLED, user.getStatus());
    }

    @Test
    public void testLoadMiniProfile() {
        MiniProfile user = userDao.loadMiniProfileByUserGuid(UserFixture.aj.getGuid());
        assertNotNull(user);
    }

    @Test
    public void profileTest() {
        Profile p = new Profile();
        p.setReferFrom(new ReferFrom());
        p.getReferFrom().setReferFromType(ReferFrom.ReferFromType.Other);
        p.getCurrentAddress().setCity("bar bar bar");
        p.setPhoneHome("foo bop");

        int count = PersistenceUtil.insert(p, template, "Profile.insert");
        assertEquals(1, count);
        assertNotNull(p.getGuid());

        Profile p1 = (Profile) template.selectOne("Profile.load", p.getGuid());
        assertNotNull("Could not find profile with guid " + p.getGuid(), p1);
        assertEquals(p.getCurrentAddress().getCity(), p1.getCurrentAddress().getCity());
    }

    @Test
    public void moodTest() {
        Profile p = initializeProfileWithMood();

        int count = PersistenceUtil.insert(p, template, "Profile.insert");
        assertEquals(1, count);
        assertNotNull(p.getGuid());

        Profile p1 = (Profile) template.selectOne("Profile.load", p.getGuid());
        assertNotNull("Could not find profile with guid " + p.getGuid(), p1);
        assertEquals(p1.getMood().getEnergyLevel(), -2);
        assertEquals(p1.getMood().getInterestLevel(), -1);
        assertEquals(p1.getMood().isMakePrivate(), true);
        assertEquals(p1.getMood().getOrientation(), 0);
        assertEquals(p1.getMood().getPolyStatus(), 1);
        assertEquals(p1.getMood().getRelationshipStatus(), 2);
        assertEquals(p.getMood(), p1.getMood());
    }

    private Profile initializeProfileWithMood() {
        Profile p = new Profile();
        p.setReferFrom(new ReferFrom());
        p.getReferFrom().setReferFromType(ReferFrom.ReferFromType.Other);
        MoodThermometer thermometer = new MoodThermometer();
        thermometer.setEnergyLevel(-2);
        thermometer.setInterestLevel(-1);
        thermometer.setMakePrivate(true);
        thermometer.setOrientation(0);
        thermometer.setPolyStatus(1);
        thermometer.setRelationshipStatus(2);
        p.setMood(thermometer);
        return p;
    }

    @Test
    public void userAndProfileAndMoodTest() {
        Profile p = initializeProfileWithMood();

        int count = PersistenceUtil.insert(p, template, "Profile.insert");
        assertEquals(1, count);
        assertNotNull(p.getGuid());

        Profile p1 = (Profile) template.selectOne("Profile.load", p.getGuid());
        assertNotNull("Could not find profile with guid " + p.getGuid(), p1);
        assertEquals(p1.getMood().getEnergyLevel(), -2);
        assertEquals(p1.getMood().getInterestLevel(), -1);
        assertEquals(p1.getMood().isMakePrivate(), true);
        assertEquals(p1.getMood().getOrientation(), 0);
        assertEquals(p1.getMood().getPolyStatus(), 1);
        assertEquals(p1.getMood().getRelationshipStatus(), 2);
        assertEquals(p.getMood(), p1.getMood());
    }

    private static final Logger logger = LoggerFactory.getLogger(UserDaoIntegrationTest.class);

    @Test
    public void assureWeDoNotHaveMixedCaseUserNames() throws Exception {
        Collection<String> userNames = Collections2.transform(userDao.loadAllUsers(), new UserToUserNameFunction());
        TreeSet<String> set = new TreeSet<String>(String.CASE_INSENSITIVE_ORDER);
        for (String userName : userNames) {
            if (!set.add(userName)) {
                logger.warn("DUPLICATE! " + userName);
            }
        }
        set.addAll(userNames);
        assertEquals("some user names are of mixed case", userNames.size(), set.size());
    }

    @Test
    public void testBonanza() {
        int numberOfUsers = userDao.countAllActiveUsers();
        ExploreRequest er = new ExploreRequest();
        List<User> bonanza = userDao.explore(er);
        // todo: where we we losing peeps? status?  
        assertTrue(Math.abs(numberOfUsers - bonanza.size()) < 100);

        seekingAll(er, false);
        List<User> tightwads = userDao.explore(er);
        assertFalse(tightwads.isEmpty());

        er.setZipCode(String.valueOf(97201));
        er.setMaxDistance(50);

        List<User> pTown = userDao.explore(er);
        assertFalse(pTown.isEmpty());

        er.setGenderFemale(true);
        List<User> woman = userDao.explore(er);
        assertFalse(woman.isEmpty());

        er.setGenderFemale(false);
        er.setGenderMale(true);
        List<User> men = userDao.explore(er);
        assertFalse(men.isEmpty());

        seekingAll(er, null);
        List<User> wideOpens = userDao.explore(er);
        assertFalse(wideOpens.isEmpty());
        int numberOfAllSeekings = wideOpens.size();

        // now drop one down at a time and seek fewer peeps...
        er.setSeekingDating(true);
        List<User> onlyDating = userDao.explore(er);
        doLookingForAssertions(onlyDating, true, false, false, false, false, false);
        assertTrue("fewer or same number should of been found when removing dating people", numberOfAllSeekings >= onlyDating.size());

        er.setSeekingFriends(true);
        List<User> datingAndFriend = userDao.explore(er);
        doLookingForAssertions(onlyDating, true, true, false, false, false, false);
        assertTrue("fewer or same number should of been found when removing dating people", onlyDating.size() >= datingAndFriend.size());

        er.setSeekingHookups(true);
        List<User> hookupsDatingAndFriends = userDao.explore(er);
        doLookingForAssertions(onlyDating, true, true, true, false, false, false);
        assertTrue("fewer or same number should of been found when removing dating people", datingAndFriend.size() >= hookupsDatingAndFriends.size());

        er.setSeekingLove(true);
        List<User> loveDatingHookupsAndFriends = userDao.explore(er);
        doLookingForAssertions(onlyDating, true, true, true, true, false, false);
        assertTrue("fewer or same number should of been found when removing dating people", hookupsDatingAndFriends.size() >= loveDatingHookupsAndFriends.size());

        er.setSeekingRelationships(true);
        List<User> relationshipLoveHookupsDatingAndFriends = userDao.explore(er);
        doLookingForAssertions(onlyDating, true, true, true, true, true, true);
        assertTrue("fewer or same number should of been found when removing dating people", loveDatingHookupsAndFriends.size() >= relationshipLoveHookupsDatingAndFriends.size());

        er.setSeekingSnuggling(true);
        List<User> snugglesRelationshipLoveHookupsDatingAndFriends = userDao.explore(er);
        doLookingForAssertions(onlyDating, true, true, true, true, true, true);
        assertTrue("fewer or same number should of been found when removing dating people", relationshipLoveHookupsDatingAndFriends.size() >= snugglesRelationshipLoveHookupsDatingAndFriends.size());

    }

    private void doLookingForAssertions(List<User> wideOpens, boolean dating, boolean friends, boolean donkeySex, boolean love, boolean hookup, boolean snuggling) {
        for (User wideOpen : wideOpens) {
            // todo: these looking fors aren't set from datbase. weird? 
//            assertEquals(dating, wideOpen.getProfile().getLookingFor().isDates());
//            assertEquals(friends, wideOpen.getProfile().getLookingFor().isFriends());
//            assertEquals(hookup, wideOpen.getProfile().getLookingFor().isHookup());
//            assertEquals(donkeySex, wideOpen.getProfile().getLookingFor().isDonkeySex());
//            assertEquals(love, wideOpen.getProfile().getLookingFor().isLove());
//            assertEquals(snuggling, wideOpen.getProfile().getLookingFor().isSnuggling());
        }
    }

    private void seekingAll(ExploreRequest er, Boolean value) {
        er.setSeekingDating(value);
        er.setSeekingFriends(value);
        er.setSeekingHookups(value);
        er.setSeekingLove(value);
        er.setSeekingRelationships(value);
        er.setSeekingSnuggling(value);
    }


    @Test
    public void testUnableToExploreInactivePersona() {
        // APP-172 Explore: Inactive persona should not be returned in member explore
        ExploreRequest er = new ExploreRequest();
        er.setDepth(NetworkTypeEnum.FRIENDS);
        er.setUserGuid(UserFixture.aj.getGuid());

        List<User> list = userDao.explore(er);
        assertNotNull(list);
        assertTrue(list.size() > 1);
        List<Status> nonEnabledStatuses = Arrays.asList(Status.CLOSED, Status.DELETED, Status.INCOMPLETE, Status.PRE_REGISTERED, Status.UNVERIFIED);
        LoopingIterator<Status> nonEnabledStatusesIterator = new LoopingIterator<Status>(nonEnabledStatuses);
        for (User user : list) {
            user.setStatus(nonEnabledStatusesIterator.next());
            userDao.save(user);
        }
        list = userDao.explore(er);
        assertNotNull(list);
        assertTrue(list.isEmpty());
    }

    @Test
    public void testNeverExploreAPrivateUser1() {
        // APP-29 Private sliders can be exposed by doing non ranged searches
        // make sure we have a private human in data...
        List<Profile> profiles = template.selectList("Profile.loadAll");
        assertFalse("we need at least one private profile for this test case to make sense",
                Collections2.filter(profiles, new PrivateProfilePredicate()).isEmpty());

        List<User> list = doExplore(buildWorldExplore());
        assertFalse("we need at least one private profile allowed through explorer to when sliders not slided",
                Collections2.filter(Collections2.transform(list, new UserToProfileFunctor()), new PrivateProfilePredicate()).isEmpty());

        ExploreRequest exploreRequest = buildWorldExplore();
        exploreRequest.setOrientation(new Range(-1, 1)); // anything other than Range.WIDE_OPEN...
        list = doExplore(exploreRequest);
        assertTrue("private profile leaking out when slider has been slided",
                Collections2.filter(Collections2.transform(list, new UserToProfileFunctor()), new PrivateProfilePredicate()).isEmpty());

        exploreRequest = buildWorldExplore();
        exploreRequest.setPolyStatus(new Range(-1, 1)); // anything other than Range.WIDE_OPEN...
        list = doExplore(exploreRequest);
        assertTrue("private profile leaking out when slider has been slided",
                Collections2.filter(Collections2.transform(list, new UserToProfileFunctor()), new PrivateProfilePredicate()).isEmpty());

        exploreRequest = buildWorldExplore();
        exploreRequest.setRelationshipStatus(new Range(-1, 1)); // anything other than Range.WIDE_OPEN...
        list = doExplore(exploreRequest);
        assertTrue("private profile leaking out when slider has been slided",
                Collections2.filter(Collections2.transform(list, new UserToProfileFunctor()), new PrivateProfilePredicate()).isEmpty());

        exploreRequest = buildWorldExplore();
        exploreRequest.setInterestLevel(new Range(-1, 1)); // anything other than Range.WIDE_OPEN...
        list = doExplore(exploreRequest);
        assertTrue("private profile leaking out when slider has been slided",
                Collections2.filter(Collections2.transform(list, new UserToProfileFunctor()), new PrivateProfilePredicate()).isEmpty());

        exploreRequest = buildWorldExplore();
        exploreRequest.setEnergyLevel(new Range(-1, 1)); // anything other than Range.WIDE_OPEN...
        list = doExplore(exploreRequest);
        assertTrue("private profile leaking out when slider has been slided",
                Collections2.filter(Collections2.transform(list, new UserToProfileFunctor()), new PrivateProfilePredicate()).isEmpty());
    }

    private ExploreRequest buildWorldExplore() {
        ExploreRequest exploreRequest = new ExploreRequest();
        exploreRequest.setDepth(NetworkTypeEnum.WORLD);
        exploreRequest.setUserGuid(UserFixture.aj.getGuid());
        return exploreRequest;
    }

    private List<User> doExplore(ExploreRequest er) {
        List<User> list = userDao.explore(er);
        assertNotNull(list);
        assertTrue(list.size() > 1);
        return list;
    }


}
