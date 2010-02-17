/*
 * Copyright (c) 2009. Blackbox, LLC
 * All rights reserved.
 */

package com.blackbox.server.media.listener;

import com.blackbox.foundation.media.MediaMetaData;
import com.blackbox.server.media.IMediaDao;
import com.blackbox.server.media.event.LoadContentMetaDataEvent;
import com.blackbox.server.BaseBlackboxListener;
import org.yestech.event.ResultReference;
import org.yestech.event.annotation.ListenedEvents;

import javax.annotation.Resource;

/**
 *
 *
 */
@ListenedEvents(LoadContentMetaDataEvent.class)
public class LoadMetaDataListener extends BaseBlackboxListener<LoadContentMetaDataEvent, MediaMetaData>
{
    private IMediaDao mediaDao;

    @Override
    public void handle(LoadContentMetaDataEvent event, ResultReference<MediaMetaData> reference)
    {
        MediaMetaData metaData = mediaDao.loadMediaMetaDataByGuid(event.getType());
        reference.setResult(metaData);
    }

    @Resource(name = "mediaDao")
    public void setMediaDao(IMediaDao mediaDao)
    {
        this.mediaDao = mediaDao;
    }
}
