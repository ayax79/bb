package com.blackbox.server.message.listener;

import com.blackbox.server.BaseBlackboxListener;
import com.blackbox.server.message.event.DeleteMessageEvent;
import com.blackbox.server.activity.IActivityStreamDao;
import com.blackbox.foundation.message.Message;
import org.yestech.event.ResultReference;
import org.yestech.event.annotation.ListenedEvents;
import org.yestech.event.annotation.AsyncListener;

import javax.annotation.Resource;

/**
 *
 *
 */
@ListenedEvents(DeleteMessageEvent.class)
@AsyncListener
public class DeleteSenderMessageFromActivityStreamListener extends BaseBlackboxListener<DeleteMessageEvent, Message> {

    @Resource(name = "activityStreamDao")
    private IActivityStreamDao activityStreamDao;

    @Override
    public void handle(DeleteMessageEvent event, ResultReference<Message> result) {
        final Message data = event.getType();
        activityStreamDao.deleteSender(data);

    }

    public void setActivityStreamDao(IActivityStreamDao activityStreamDao) {
        this.activityStreamDao = activityStreamDao;
    }
}