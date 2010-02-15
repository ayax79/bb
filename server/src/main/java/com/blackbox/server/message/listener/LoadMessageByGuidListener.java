/*
 * Copyright (c) 2009. Blackbox, LLC
 * All rights reserved.
 */

package com.blackbox.server.message.listener;

import com.blackbox.message.Message;
import com.blackbox.server.BaseBlackboxListener;
import com.blackbox.server.message.IMessageDao;
import com.blackbox.server.message.event.LoadMessageByGuidEvent;
import org.yestech.event.ResultReference;
import org.yestech.event.annotation.ListenedEvents;

import javax.annotation.Resource;

/**
 *
 *
 */
@ListenedEvents(LoadMessageByGuidEvent.class)
public class LoadMessageByGuidListener extends BaseBlackboxListener<LoadMessageByGuidEvent, Message>
{

    @Resource(name = "messageDao")
    private IMessageDao messageDao;

    @Override
    public void handle(LoadMessageByGuidEvent event, ResultReference<Message> result)
    {
        //TODO:  look for it in cache if not their then load
        String messageKey = event.getType();
        Message message = messageDao.loadByGuid(messageKey);
        result.setResult(message);
    }

    public void setMessageDao(IMessageDao messageDao)
    {
        this.messageDao = messageDao;
    }
}