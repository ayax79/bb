/*
 * Copyright (c) 2009. Blackbox, LLC
 * All rights reserved.
 */

package com.blackbox.server.message.listener;

import com.blackbox.message.Message;
import com.blackbox.server.message.IMessageDao;
import com.blackbox.server.message.event.LoadMessageByGuidEvent;
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
public class LoadMessageByGuidListenerUnitTest
{

    Mockery mockery = new JUnit4Mockery();

    @Test
    public void testHandle()
    {
        final IMessageDao messageDao = mockery.mock(IMessageDao.class, "messageDao");

        final Message message = Message.createMessage();
        message.setBody("lksdjlksdjfdslj");
        message.setGuid("thisisakey");

        mockery.checking(new Expectations()
        {
            {
                oneOf(messageDao).loadByGuid(message.getGuid());
                will(returnValue(message));
            }
        });

        LoadMessageByGuidListener listener = new LoadMessageByGuidListener();
        listener.setMessageDao(messageDao);
        ResultReference ref = new ResultReference();
        listener.handle(new LoadMessageByGuidEvent(message.getGuid()), ref);
        Message result = (Message) ref.getResult();
        assertEquals(message, result);
    }
}