package com.blackbox.server.message.listener;

import com.blackbox.server.BaseBlackboxListener;
import com.blackbox.server.message.event.DeleteMessageEvent;
import com.blackbox.server.activity.IActivityStreamDao;
import com.blackbox.message.Message;
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
public class DeleteAssociatedMessageFromActivityStreamListener extends BaseBlackboxListener<DeleteMessageEvent, Message> {

    @Resource(name = "activityStreamDao")
    private IActivityStreamDao activityStreamDao;

    @Override
    public void handle(DeleteMessageEvent event, ResultReference<Message> result) {
        final Message data = event.getType();
        activityStreamDao.deleteAssociated(data, data.getRecipientDepth());

    }

    public void setActivityStreamDao(IActivityStreamDao activityStreamDao) {
        this.activityStreamDao = activityStreamDao;
    }
}