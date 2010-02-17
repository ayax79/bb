package com.blackbox.server.media.listener;

import com.blackbox.foundation.media.MediaMetaData;
import com.blackbox.server.BaseBlackboxListener;
import com.blackbox.server.media.IMediaDao;
import com.blackbox.server.media.event.DeleteMediaMetaDataEvent;
import org.yestech.event.ResultReference;
import org.yestech.event.annotation.AsyncListener;
import org.yestech.event.annotation.ListenedEvents;

import javax.annotation.Resource;

/**
 *
 *
 */
@ListenedEvents(DeleteMediaMetaDataEvent.class)
@AsyncListener
public class DeleteMediaMetaDataFromDatabaseListener extends BaseBlackboxListener<DeleteMediaMetaDataEvent, MediaMetaData> {

    @Resource(name = "mediaDao")
    private IMediaDao mediaDao;

    @Override
    public void handle(DeleteMediaMetaDataEvent event, ResultReference<MediaMetaData> result) {
        mediaDao.delete(event.getType());
    }

    public void setMediaDao(IMediaDao mediaDao) {
        this.mediaDao = mediaDao;
    }
}