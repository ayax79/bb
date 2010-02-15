package com.blackbox.server.message.listener;

import com.blackbox.message.Message;
import com.blackbox.server.BaseBlackboxListener;
import com.blackbox.server.message.IMessageDao;
import com.blackbox.server.message.event.DeleteMessageEvent;
import org.yestech.event.ResultReference;
import org.yestech.event.annotation.AsyncListener;
import org.yestech.event.annotation.ListenedEvents;

import javax.annotation.Resource;

/**
 *
 *
 */
@ListenedEvents(DeleteMessageEvent.class)
@AsyncListener
public class DeleteMessageFromDatabaseListener extends BaseBlackboxListener<DeleteMessageEvent, Message> {

    @Resource(name = "messageDao")
    private IMessageDao messageDao;

    public IMessageDao getMessageDao() {
        return messageDao;
    }

    public void setMessageDao(IMessageDao messageDao) {
        this.messageDao = messageDao;
    }

    @Override
    public void handle(DeleteMessageEvent event, ResultReference<Message> result) {
        messageDao.delete(event.getType());
    }

}