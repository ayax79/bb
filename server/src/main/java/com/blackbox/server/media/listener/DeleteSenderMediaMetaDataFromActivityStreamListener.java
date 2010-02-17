package com.blackbox.server.media.listener;

import com.blackbox.foundation.media.MediaMetaData;
import com.blackbox.server.media.event.DeleteMediaMetaDataEvent;
import com.blackbox.server.BaseBlackboxListener;
import com.blackbox.server.activity.IActivityStreamDao;
import org.yestech.event.ResultReference;
import org.yestech.event.annotation.ListenedEvents;
import org.yestech.event.annotation.AsyncListener;

import javax.annotation.Resource;

/**
 *
 *
 */
@ListenedEvents(DeleteMediaMetaDataEvent.class)
@AsyncListener
public class DeleteSenderMediaMetaDataFromActivityStreamListener extends BaseBlackboxListener<DeleteMediaMetaDataEvent, MediaMetaData> {

    @Resource(name = "activityStreamDao")
    private IActivityStreamDao activityStreamDao;

    @Override
    public void handle(DeleteMediaMetaDataEvent event, ResultReference<MediaMetaData> result) {
        final MediaMetaData data = event.getType();
        activityStreamDao.deleteSender(data);

    }

    public void setActivityStreamDao(IActivityStreamDao activityStreamDao) {
        this.activityStreamDao = activityStreamDao;
    }
}