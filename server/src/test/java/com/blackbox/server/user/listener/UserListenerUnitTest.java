/*
 * Copyright (c) 2009. Blackbox, LLC
 * All rights reserved.
 */

package com.blackbox.server.user.listener;

import com.blackbox.server.user.IUserDao;
import com.blackbox.server.user.event.DeleteUserEvent;
import com.blackbox.foundation.user.IUser;
import com.blackbox.foundation.user.User;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.yestech.event.ResultReference;

/**
 *
 *
 */
@RunWith(JMock.class)
public class UserListenerUnitTest
{

    Mockery mockery = new JUnit4Mockery();

    @Test
    public void testHandle()
    {

        final User user = User.createUser();

        final IUserDao userDao = mockery.mock(IUserDao.class, "userDao");
        mockery.checking(new Expectations()
        {{

                oneOf(userDao).delete(user);

            }});


        DeleteUserListener listener = new DeleteUserListener();
        listener.setUserDao(userDao);

        listener.handle(new DeleteUserEvent(user), new ResultReference<IUser>());
    }
}
