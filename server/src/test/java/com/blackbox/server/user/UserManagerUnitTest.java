/*
 * Copyright (c) 2009. Blackbox, LLC
 * All rights reserved.
 */

package com.blackbox.server.user;

import com.blackbox.foundation.media.MediaMetaData;
import com.blackbox.server.media.IMediaDao;
import com.blackbox.server.user.event.VerifyUserEvent;
import com.blackbox.foundation.user.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.yestech.event.multicaster.IEventMulticaster;

import java.util.Collection;

import static com.blackbox.foundation.user.User.UserType.AFFILIATE;
import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 *
 *
 */
@SuppressWarnings({"unchecked"})
@RunWith(MockitoJUnitRunner.class)
public class UserManagerUnitTest {

    @Mock
    IEventMulticaster eventMulticaster;

    @Mock
    IProfileDao profileDao;

    @Mock
    IMediaDao mediaDao;

    @Mock
    IUserDao userDao;

    @Mock
    IAffiliateMappingDao affiliateMappingDao;

    @Test
    public void verifyUserTest() {

        final String key = "sdvaskldjfalsdfj";
        final String userId = "238998238923L";

        when(eventMulticaster.process(new VerifyUserEvent(userId, key))).thenReturn(true);

        UserManager userService = new UserManager();
        userService.setEventMulticaster(eventMulticaster);
        VerificationResult result = userService.verifyUser(userId, key);
        assertTrue(result.isVerified());

    }

    @Test
    public void verifyUserWithNullTest() {

        final String key = "sdvaskldjfalsdfj";
        final String userId = "238998238923L";

        when(eventMulticaster.process(new VerifyUserEvent(userId, key))).thenReturn(null);

        UserManager userService = new UserManager();
        userService.setEventMulticaster(eventMulticaster);
        VerificationResult result = userService.verifyUser(userId, key);
        assertFalse(result.isVerified());

    }


    @Test
    public void testLoadProfileByUserGuid() {

        final User user = User.createUser();
        final Profile profile = Profile.createProfile();
        final MediaMetaData metaData = MediaMetaData.createMediaMetaData();
        metaData.setLocation("abcdddd");

        when(profileDao.loadProfileByUserGuid(user.getGuid())).thenReturn(profile);
        when(mediaDao.loadProfileMediaMetaDataByOwner(user.toEntityReference())).thenReturn(metaData);
        when(mediaDao.loadAvatarMediaMetaDataByOwner(user.toEntityReference())).thenReturn(metaData);

        UserManager mgr = new UserManager();
        mgr.mediaDao = mediaDao;
        mgr.profileDao = profileDao;

        Profile result = mgr.loadProfileByUserGuid(user.getGuid());
        assertEquals(metaData.getLocation(), result.getProfileImgUrl());
    }

    @Test
    public void testAffiliate() {
        User affiliate = User.createUser();
        affiliate.setType(AFFILIATE);
        User user = User.createUser();

        UserManager um = new UserManager();
        um.userDao = userDao;
        um.affiliateMappingDao = affiliateMappingDao;

        when(userDao.loadUserByGuid(affiliate.getGuid())).thenReturn(affiliate);
        when(userDao.loadUserByGuid(user.getGuid())).thenReturn(user);
        um.affiliate(affiliate.getGuid(), user.getGuid());

        ArgumentCaptor<AffiliateMapping> am = ArgumentCaptor.forClass(AffiliateMapping.class);
        verify(affiliateMappingDao).save(am.capture());


        AffiliateMapping affiliateMapping = am.getValue();
        Collection<User> users = affiliateMapping.getUsers();
        assertEquals(1, users.size());
        assertEquals(user, users.iterator().next());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNotAffiliate() {
        User affiliate = User.createUser();
        User user = User.createUser();

        UserManager um = new UserManager();
        um.userDao = userDao;
        um.affiliateMappingDao = affiliateMappingDao;

        when(userDao.loadUserByGuid(affiliate.getGuid())).thenReturn(affiliate);
        when(userDao.loadUserByGuid(user.getGuid())).thenReturn(user);
        um.affiliate(affiliate.getGuid(), user.getGuid());
    }


    @Test
    public void capture() {
        String email = "sldkjf@lskjdf.com";
        UserManager um = new UserManager();
        um.userDao = userDao;
        um.captureEmail(email);
        verify(userDao).save(new EmailCapture(email));
    }


}
