/*
 * Copyright (c) 2009. Blackbox, LLC
 * All rights reserved.
 */

package com.blackbox.server.social;

import com.blackbox.server.user.IUserDao;
import com.blackbox.server.util.PersistenceUtil;
import com.blackbox.foundation.social.*;
import com.blackbox.foundation.user.IUserManager;
import com.blackbox.foundation.user.PaginationResults;
import com.blackbox.foundation.user.User;
import com.blackbox.foundation.util.DateUtil;
import com.google.common.base.Predicate;
import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import static com.google.common.collect.Collections2.filter;
import static com.google.common.collect.Lists.newArrayList;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.yestech.cache.ICacheManager;
import org.yestech.event.multicaster.IEventMulticaster;

import java.util.List;

import static com.blackbox.foundation.social.Connection.ConnectionType;
import static com.google.common.collect.Collections2.filter;
import static com.google.common.collect.Lists.newArrayList;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 *
 *
 */
@SuppressWarnings({"unchecked"})
@RunWith(MockitoJUnitRunner.class)
public class SocialManagerUnitTest {
    
    
    @Mock
    IVouchDao vouchDao;

    @Mock
    ICacheManager<String, List<Vouch>> vouchTargetCache;

    @Mock
    ICacheManager<String, List<Vouch>> vouchVoucherCache;

    @Mock
    ICacheManager<String, List<UserVouch>> userVouchCache;

    @Mock
    ICacheManager<VouchCountCacheKey, Integer> vouchCountCache;

    @Mock
    INetworkDao networkDao;

    @Mock
    ICacheManager<String, List<Connection>> connectionCache;

    @Mock
    IUserDao userDao;

    @Mock
    IEventMulticaster eventMulticaster;

    @Mock
    ICacheManager<String, RelationshipNetwork> relationshipCache;

    @Mock
    ICacheManager<String, List<Relationship>> followedByCache;


    @Mock
    IUserManager userManager;

    private static final String I_HEART_ANDY = "i heart andy";
    private static final String I_HEART_COLIN = "i heart colin";
    SocialManager  socialManager;

    @Before
    public void setup() {
        socialManager = new SocialManager();
        socialManager.connectionCache = connectionCache;
        socialManager.networkDao = networkDao;
        socialManager.vouchDao = vouchDao;
        socialManager.userDao = userDao;
        socialManager.vouchTargetCache = vouchTargetCache;
        socialManager.userVouchCache = userVouchCache;
        socialManager.vouchVoucherCache = vouchVoucherCache;
        socialManager.vouchCountCache = vouchCountCache;
        socialManager.relationshipCache = relationshipCache;
        socialManager.followedByCache = followedByCache;
        socialManager.userManager = userManager;
        socialManager.setEventMulticaster(eventMulticaster);
        PersistenceUtil.setTestOverride(true);
    }

    @Test
    public void testLoadConnections() {
        User owner = User.createUser();
        User u0 = User.createUser();
        User u1 = User.createUser();
        User u2 = User.createUser();
        User u3 = User.createUser();
        User u4 = User.createUser();

        // two way following test, should show up as mutual
        Relationship r0 = new Relationship(owner.toEntityReference(), u0.toEntityReference(), 0);
        Relationship r1 = new Relationship(u0.toEntityReference(), owner.toEntityReference(), 0);

        // friending test, should only be one connection
        Relationship r2 = new Relationship(owner.toEntityReference(), u1.toEntityReference(), 10);
        Relationship r3 = new Relationship(u1.toEntityReference(), owner.toEntityReference(), 10);

        // one way following
        Relationship r4 = new Relationship(owner.toEntityReference(), u2.toEntityReference(), 0);

        // one way followed
        Relationship r5 = new Relationship(u3.toEntityReference(), owner.toEntityReference(), 0);

        // a relationship, should just be treated as other friends
        Relationship r6 = new Relationship(owner.toEntityReference(), u4.toEntityReference(), 50);
        Relationship r7 = new Relationship(u4.toEntityReference(), owner.toEntityReference(), 50);

        when(networkDao.loadConnections(owner.getGuid())).thenReturn(newArrayList(r0, r1, r2, r3, r4, r5, r6, r7));

        PaginationResults<Connection> results =
                socialManager.loadConnections(owner.getGuid(), ConnectionType.ALL, 0, 1000);

        assertNotNull(results);
        assertEquals(5, results.getResults().size());
        assertEquals(5, results.getNumResults());
        assertEquals(5, results.getTotalResults());

        assertEquals(1, filter(results.getResults(), connectionTypePredicate(ConnectionType.FOLLOWED)).size());
        assertEquals(1, filter(results.getResults(), connectionTypePredicate(ConnectionType.FOLLOWING)).size());
        assertEquals(1, filter(results.getResults(), connectionTypePredicate(ConnectionType.MUTUAL_FOLLOWING)).size());
        assertEquals(2, filter(results.getResults(), connectionTypePredicate(ConnectionType.FRIEND)).size());


    }

    public static Predicate<Connection> connectionTypePredicate(final Connection.ConnectionType type) {
        return new Predicate<Connection>() {

            @Override
            public boolean apply(Connection input) {
                return input.getType() == type;
            }
        };
    }
    
        @Test
    public void testVouchTemporaryWithNoPreviousVouchesWithLimited() {

        User user = User.createUser();
        user.setType(User.UserType.LIMITED);
        Vouch vouch = Vouch.createVouch();
        vouch.setVoucher(user.toEntityReference());
        vouch.setTarget(User.createUser().toEntityReference());

        when(userDao.loadUserByGuid(vouch.getVoucher().getGuid())).thenReturn(user);
        when(vouchDao.save(vouch)).thenReturn(vouch);

        Vouch result = socialManager.vouch(new VouchRequest(vouch, false));
        assertNotNull(result);
        assertNotNull(result.getExpirationDate());
        assertTrue(result.getExpirationDate().isBefore(new DateTime().plusDays(Vouch.TEMPORARY_DAYS)));

        verify(vouchDao).save(result);
        verify(vouchCountCache, atLeastOnce()).flush(new VouchCountCacheKey(user.getGuid(), false, any(DateTime.class)));
    }

    @Test(expected = VouchException.class)
    public void testVouchTemporaryWithOldVouchWithLimited() {

        User user = User.createUser();
        user.setType(User.UserType.LIMITED);
        Vouch vouch = Vouch.createVouch();
        vouch.setVoucher(user.toEntityReference());
        vouch.setTarget(User.createUser().toEntityReference());

        Vouch old = Vouch.createVouch();
        old.setExpirationDate(new DateTime().plusDays(40));
        old.setCount(1);

        when(vouchDao.loadByVoucherAndTarget(vouch.getVoucher().getGuid(), vouch.getTarget().getGuid())).thenReturn(old);
        when(userDao.loadUserByGuid(vouch.getVoucher().getGuid())).thenReturn(user);

        Vouch result = socialManager.vouch(new VouchRequest(vouch, false));
        assertNotNull(result);
        assertNotNull(result.getExpirationDate());
        assertTrue(result.getExpirationDate().isBefore(new DateTime().plusDays(Vouch.TEMPORARY_DAYS)));

        verify(vouchDao, times(1)).save(result);
        verify(vouchCountCache).flush(new VouchCountCacheKey(user.getGuid(), false, any(DateTime.class)));
    }

    @Test(expected = VouchException.class)
    public void testVouchExceededMonthly() {


        User user = User.createUser();
        user.setType(User.UserType.LIMITED);
        Vouch vouch = Vouch.createVouch();
        vouch.setVoucher(user.toEntityReference());
        vouch.setTarget(User.createUser().toEntityReference());

        when(userDao.loadUserByGuid(vouch.getVoucher().getGuid())).thenReturn(user);
        when(vouchDao.save(vouch)).thenReturn(vouch);
        when(vouchCountCache.get(new VouchCountCacheKey(user.getGuid(), false, DateUtil.firstDayOfMonth()))).thenReturn(30);

        socialManager.vouch(new VouchRequest(vouch, false));
    }

    @Test(expected = VouchException.class)
    public void testLimitedWithPermanent() {

        User user = User.createUser();
        user.setType(User.UserType.LIMITED);
        Vouch vouch = Vouch.createVouch();
        vouch.setVoucher(user.toEntityReference());
        vouch.setTarget(User.createUser().toEntityReference());

        when(userDao.loadUserByGuid(vouch.getVoucher().getGuid())).thenReturn(user);
        socialManager.vouch(new VouchRequest(vouch, true));
    }

    @Test(expected = VouchException.class)
    public void testVouchExceededMonthlyPermanent() {

        User user = User.createUser();
        Vouch vouch = Vouch.createVouch();
        vouch.setVoucher(user.toEntityReference());
        vouch.setTarget(User.createUser().toEntityReference());

        when(userDao.loadUserByGuid(vouch.getVoucher().getGuid())).thenReturn(user);
        when(vouchDao.save(vouch)).thenReturn(vouch);
        when(vouchCountCache.get(new VouchCountCacheKey(user.getGuid(), true, DateUtil.firstDayOfMonth()))).thenReturn(30);

        socialManager.vouch(new VouchRequest(vouch, true));
    }


    @Test(expected = VouchException.class)
    public void testVouchPermanentWithOldVouch() {

        User user = User.createUser();
        Vouch vouch = Vouch.createVouch();
        vouch.setVoucher(user.toEntityReference());
        vouch.setTarget(User.createUser().toEntityReference());

        Vouch old = Vouch.createVouch();
        old.setExpirationDate(new DateTime().plusYears(100));
        old.setCount(1);

        when(vouchDao.loadByVoucherAndTarget(vouch.getVoucher().getGuid(), vouch.getTarget().getGuid())).thenReturn(old);
        when(userDao.loadUserByGuid(vouch.getVoucher().getGuid())).thenReturn(user);

        socialManager.vouch(new VouchRequest(vouch, false));
    }

    @Test
    public void convertToPermanent() {

        User user = User.createUser();
        user.setType(User.UserType.LIMITED);

        Vouch vouch = Vouch.createVouch();
        vouch.setVoucher(user.toEntityReference());
        vouch.setTarget(User.createUser().toEntityReference());

        Vouch old = Vouch.createVouch();
        old.setExpirationDate(new DateTime().plusDays(20));
        old.setVoucher(vouch.getVoucher());
        old.setTarget(vouch.getTarget());
        old.setCount(2);

        when(vouchDao.loadByVoucherAndTarget(vouch.getVoucher().getGuid(), vouch.getTarget().getGuid())).thenReturn(old);
        when(userDao.loadUserByGuid(vouch.getVoucher().getGuid())).thenReturn(user);
        when(vouchDao.save(old)).thenReturn(old);

        Vouch result = socialManager.vouch(new VouchRequest(vouch, false));
        assertNotNull(result);
        assertNotNull(result.getExpirationDate());
        assertTrue(Vouch.isPermanent(result));

        verify(vouchDao, times(1)).save(result);
        verify(vouchCountCache, atLeastOnce()).flush(new VouchCountCacheKey(user.getGuid(), false, any(DateTime.class)));

    }

    @Test
    public void testRenewTempVouch() {

        User user = User.createUser();
        user.setType(User.UserType.LIMITED);

        Vouch vouch = Vouch.createVouch();
        vouch.setVoucher(user.toEntityReference());
        vouch.setTarget(User.createUser().toEntityReference());

        Vouch old = Vouch.createVouch();
        old.setExpirationDate(new DateTime().plusDays(20));
        old.setVoucher(vouch.getVoucher());
        old.setTarget(vouch.getTarget());
        old.setCount(1);

        when(vouchDao.loadByVoucherAndTarget(vouch.getVoucher().getGuid(), vouch.getTarget().getGuid())).thenReturn(old);
        when(userDao.loadUserByGuid(vouch.getVoucher().getGuid())).thenReturn(user);
        when(vouchDao.save(old)).thenReturn(old);

        Vouch result = socialManager.vouch(new VouchRequest(vouch, false));
        assertNotNull(result);
        assertNotNull(result.getExpirationDate());
        assertEquals(2, result.getCount());
        assertTrue(result.getExpirationDate().isBefore(new DateTime().plusDays(179)));
        assertTrue(result.getExpirationDate().isAfter(new DateTime().plusDays(30)));

        verify(vouchDao, times(1)).save(result);
        verify(vouchCountCache, atLeastOnce()).flush(new VouchCountCacheKey(user.getGuid(), false, any(DateTime.class)));

    }


    @Test
    public void testVouchPermanentWithNoPreviousVouches() {

        User user = User.createUser();
        Vouch vouch = Vouch.createVouch();
        vouch.setVoucher(user.toEntityReference());
        vouch.setTarget(User.createUser().toEntityReference());

        when(userDao.loadUserByGuid(vouch.getVoucher().getGuid())).thenReturn(user);
        when(vouchDao.save(vouch)).thenReturn(vouch);

        Vouch result = socialManager.vouch(new VouchRequest(vouch, true));
        assertNotNull(result);
        assertNotNull(result.getExpirationDate());
        assertTrue(Vouch.isPermanent(result));

        verify(vouchDao).save(result);
        verify(vouchCountCache, atLeastOnce()).flush(new VouchCountCacheKey(user.getGuid(), true, any(DateTime.class)));
    }


    @Test
    public void testDowngradeFromInrelationshipToFriend() {
        User user1 = User.createUser();
        User user2 = User.createUser();

        Relationship oldValue = Relationship.createRelationship(user1, user2, Relationship.RelationStatus.IN_RELATIONSHIP);
        oldValue.setDescription("sadlkjasdlfasdf");

        Relationship newValue = Relationship.createRelationship(user1, user2, Relationship.RelationStatus.FRIEND);

        when(networkDao.loadRelationshipByEntities(user1.getGuid(), user2.getGuid())).thenReturn(oldValue);

        socialManager.relate(newValue);

        verify(networkDao).loadRelationshipByEntities(user1.getGuid(), user2.getGuid());
        verify(networkDao).save(oldValue);

        assertEquals(newValue.getWeight(), oldValue.getWeight());
    }

    // now let's emulate the ui...it is incorrect when the current user (logged in) is not the user

    private void doUiEmulation(User targetUser) {
        List<UserVouch> currentUsersVouches = socialManager.loadUserVouches(targetUser.getGuid(), 0, 666).getResults();
        for (UserVouch userVouch : currentUsersVouches) {
            assertTrue("this is a precondition to enter into this part of ui emulation",
                    UserVouch.VouchDirection.OUTGOING.equals(userVouch.getDirection()) || UserVouch.VouchDirection.MUTUAL.equals(userVouch.getDirection()));
            assertNotNull("mutual vouches must have from description", userVouch.getFromDescription());
            assertNotNull("mutual vouches must have to description", userVouch.getToDescription());

            String toDescription = userVouch.getToDescription();
            if (StringUtils.isNotBlank(toDescription)) {
                assertDescriptionValuesAreCorrect(userVouch.getToUser(), toDescription);
            }
            String fromDescription = userVouch.getFromDescription();
            if (StringUtils.isNotBlank(fromDescription)) {
                assertDescriptionValuesAreCorrect(userVouch.getFromUser(), fromDescription);
            }
        }
    }

// ------------------- ui loop -----------------------------------------------------------------------------------------
//        <c:when test="${s:enumName(vouch.direction) == 'INCOMING' or (s:enumName(vouch.direction) == 'MUTUAL'}">
//						<c:if test="${not empty vouch.toDescription}">
//							<p class="frst"><strong>${bb:displayName(vouch.toUser)}</strong>: ${vouch.toDescription}</p>
//						</c:if>
//						<c:if test="${not empty vouch.fromDescription}">
//							<p><strong>${bb:displayName(vouch.fromUser)}</strong>: ${vouch.fromDescription}</p>
//						</c:if>
//					</c:when>

    private void assertDescriptionValuesAreCorrect(User user, String description) {
        if ("colin".equals(user.getUsername())) {
            assertEquals(I_HEART_ANDY, description);
        }
        if ("andy".equals(user.getUsername())) {
            assertEquals(I_HEART_COLIN, description);
        }
    }

    private User buildUser(String username) {
        return new User(username, username, username, "last");
    }

}
