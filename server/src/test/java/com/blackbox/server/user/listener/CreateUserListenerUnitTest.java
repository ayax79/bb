/*
 * Copyright (c) 2009. Blackbox, LLC
 * All rights reserved.
 */

package com.blackbox.server.user.listener;

import com.blackbox.server.user.IUserDao;
import com.blackbox.server.user.event.CreateUserEvent;
import com.blackbox.user.IUser;
import com.blackbox.user.User;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.yestech.event.ResultReference;

/**
 *
 *
 */
@RunWith(JMock.class)
public class CreateUserListenerUnitTest
{

    Mockery mockery = new JUnit4Mockery();

    @Test
    public void testHandle()
    {

        final User user = new User();

        final IUserDao userDao = mockery.mock(IUserDao.class, "userDao");
        mockery.checking(new Expectations()
        {{
                oneOf(userDao).save(user);

            }});
        CreateUserListener listener = new CreateUserListener();
        listener.setUserDao(userDao);
        ResultReference<IUser> ref = new ResultReference<IUser>();
        listener.handle(new CreateUserEvent(user), ref);
        IUser result = ref.getResult();
        assertEquals(user, result);
    }
}
