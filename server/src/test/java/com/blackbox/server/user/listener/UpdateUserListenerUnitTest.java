/*
 * Copyright (c) 2009. Blackbox, LLC
 * All rights reserved.
 */

package com.blackbox.server.user.listener;

import com.blackbox.server.user.IUserDao;
import com.blackbox.server.user.event.UpdateUserEvent;
import com.blackbox.foundation.user.IUser;
import com.blackbox.foundation.user.User;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.yestech.event.ResultReference;

/**
 *
 *
 */
@RunWith(JMock.class)
public class UpdateUserListenerUnitTest
{

    Mockery mockery = new JUnit4Mockery();

    @Test
    public void testHandle()
    {
        final User user = User.createUser();
        user.setEmail("alksjdf@lskdfj.com");
        user.setName("lskdjf");
        user.setVersion(4L);

        final IUserDao userDao = mockery.mock(IUserDao.class, "userDao");
        mockery.checking(new Expectations()
        {{
                oneOf(userDao).save(user);
            }});

        UpdateUserListener listener = new UpdateUserListener();
        listener.setUserDao(userDao);
        ResultReference<IUser> ref = new ResultReference<IUser>();
        listener.handle(new UpdateUserEvent(user), ref);
        IUser result = ref.getResult();
        assertNotNull(result);
        assertEquals(user, result);
    }
}
