package com.blackbox.server.media.listener;

import com.blackbox.foundation.EntityReference;
import org.yestech.event.ResultReference;
import org.yestech.event.annotation.ListenedEvents;
import com.blackbox.server.media.event.LoadProfileMediaMetaDataEvent;
import com.blackbox.server.media.IMediaDao;
import com.blackbox.server.BaseBlackboxListener;
import com.blackbox.foundation.media.MediaMetaData;

import javax.annotation.Resource;

/**
 *
 *
 */
@ListenedEvents(LoadProfileMediaMetaDataEvent.class)
public class LoadProfileMediaMetaDataListener extends BaseBlackboxListener<LoadProfileMediaMetaDataEvent, MediaMetaData> {

    @Resource(name = "mediaDao")
    private IMediaDao mediaDao;

    @Override
    public void handle(LoadProfileMediaMetaDataEvent event, ResultReference<MediaMetaData> result) {
        final EntityReference owner = event.getType();
        final MediaMetaData metaData = mediaDao.loadProfileMediaMetaDataByOwner(owner);
        result.setResult(metaData);
    }

    public void setMediaDao(IMediaDao mediaDao) {
        this.mediaDao = mediaDao;
    }
}
