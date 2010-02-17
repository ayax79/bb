package com.blackbox.server.message.listener;

import com.blackbox.server.BaseBlackboxListener;
import com.blackbox.server.message.event.DeleteMessageEvent;
import com.blackbox.server.activity.IActivityStreamDao;
import static com.blackbox.server.activity.ActivityUtil.isParent;
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
public class DeletePrimaryMessageFromActivityStreamListener extends BaseBlackboxListener<DeleteMessageEvent, Message> {

    @Resource(name = "activityStreamDao")
    private IActivityStreamDao activityStreamDao;

    @Override
    public void handle(DeleteMessageEvent event, ResultReference<Message> result) {
        final Message data = event.getType();
        activityStreamDao.deletePrimary(data, data.getRecipientDepth(), isParent(data));

    }

    public void setActivityStreamDao(IActivityStreamDao activityStreamDao) {
        this.activityStreamDao = activityStreamDao;
    }
}