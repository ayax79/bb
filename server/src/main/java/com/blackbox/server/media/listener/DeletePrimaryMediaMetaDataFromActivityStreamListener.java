package com.blackbox.server.media.listener;

import com.blackbox.foundation.media.MediaMetaData;
import com.blackbox.server.media.event.DeleteMediaMetaDataEvent;
import com.blackbox.server.BaseBlackboxListener;
import com.blackbox.server.activity.IActivityStreamDao;
import static com.blackbox.server.activity.ActivityUtil.isParent;
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
public class DeletePrimaryMediaMetaDataFromActivityStreamListener extends BaseBlackboxListener<DeleteMediaMetaDataEvent, MediaMetaData> {

    @Resource(name = "activityStreamDao")
    private IActivityStreamDao activityStreamDao;

    @Override
    public void handle(DeleteMediaMetaDataEvent event, ResultReference<MediaMetaData> result) {
        final MediaMetaData data = event.getType();
        activityStreamDao.deletePrimary(data, data.getRecipientDepth(), isParent(data));

    }

    public void setActivityStreamDao(IActivityStreamDao activityStreamDao) {
        this.activityStreamDao = activityStreamDao;
    }
}