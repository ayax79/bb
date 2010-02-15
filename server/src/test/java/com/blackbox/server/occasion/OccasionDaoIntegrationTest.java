/*
 * Copyright (c) 2009. Blackbox, LLC
 * All rights reserved.
 */

package com.blackbox.server.occasion;

import com.blackbox.Utils;
import com.blackbox.activity.ActivityFactory;
import com.blackbox.business.UserToAttendingAttendeeFunction;
import com.blackbox.occasion.Attendee;
import com.blackbox.occasion.Occasion;
import com.blackbox.occasion.OccasionLevel;
import com.blackbox.occasion.OccasionRequest;
import com.blackbox.server.BaseIntegrationTest;
import com.blackbox.server.user.IUserDao;
import com.blackbox.social.Address;
import com.blackbox.testingutils.UserFixture;
import com.blackbox.user.User;
import com.blackbox.util.Bounds;
import org.joda.time.DateTime;
import org.junit.Ignore;
import org.junit.Test;
import org.yestech.lib.i18n.CountryEnum;
import org.yestech.lib.i18n.USStateEnum;

import javax.annotation.Resource;
import java.util.List;

import static org.junit.Assert.*;

/**
 *
 *
 */
public class OccasionDaoIntegrationTest extends BaseIntegrationTest {

    @Resource
    protected IOccasionDao occasionDao;

    @Resource
    protected IUserDao userDao;

    @Test
    public void testSave() {
        User user = createNamedUser(Utils.generateGuid());

        Occasion occasion = ActivityFactory.createOccasion();
        occasion.setDescription("llksdlkjdfljksd");
        occasion.setAddress(new Address("317 SW Alder St.", "suite 500", "Portland", USStateEnum.OREGON.name(), "97204", CountryEnum.UNITED_STATES));
        occasion.setOccasionLevel(OccasionLevel.DIAMOND);
        occasion.setEventTime(new DateTime().plusMonths(1));

        occasion.getAttendees().add(new UserToAttendingAttendeeFunction(new DateTime(), "no-password").apply(user));
        occasion.getAttendees().add(new UserToAttendingAttendeeFunction(new DateTime(), "no-password").apply(user));

        Attendee a = new Attendee();
        a.setBbxUserGuid("aj");
        a.setPassword("sdf923wast");
        occasion.getAttendees().add(a);

        assertNull(a.getGuid());
        occasionDao.save(occasion);
        assertNotNull(a.getGuid());
        assertNotNull(occasion.getVersion());

        occasion.setLocation("sw35ts3sd");
        occasionDao.save(occasion);

        assertNotNull(occasionDao.loadOccasionsByAttendee(a.getGuid(), Bounds.boundLess()));
        assertNotNull(occasionDao.loadAttendeeByGuidAndOccasionGuid(occasion.getGuid(), user.getGuid()));

        occasionDao.delete(occasion);
    }

    private User createNamedUser(String name) {
        User user = User.createUser();
        user.setName(name);
        user.setUsername(name + "-" + Utils.generateGuid());
        user.setFirstname(name);
        user.setLastname(name);
        user.setEmail(name + "@mailinator.com");
        user.setPassword("----XYX-----");
        return userDao.save(user);
    }


    @Test
    public void testLoadById() {

        Occasion occasion = occasionDao.loadByGuid("6af00dc86a289b0df18e893add7ccbab99e9bdcb");
        assertNotNull(occasion);
        //assertEquals("Miami", occasion.getLocation());
        //assertEquals("Some Event", occasion.getDescription());
    }

    @Ignore(value = "we the last event date was yesterday (2/11/2010) so now this test fails until the test can buile out its own data..csb")
    @Test
    public void testLoadForScene() {
        OccasionRequest request = new OccasionRequest();
        request.setOwnerGuid(UserFixture.aj.getGuid());

        List<Occasion> list = occasionDao.loadOccasionsForScene(request);
        assertNotNull(list);
        assertFalse(list.isEmpty());
    }

}
