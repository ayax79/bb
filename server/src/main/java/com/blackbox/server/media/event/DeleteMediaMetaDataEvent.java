/*
 * Copyright (c) 2009. Blackbox, LLC
 * All rights reserved.
 */

package com.blackbox.server.media.event;

import org.yestech.event.event.BaseEvent;
import org.yestech.event.annotation.EventResultType;
import com.blackbox.foundation.media.MediaLibrary;
import com.blackbox.foundation.media.MediaMetaData;

/**
 *
 *
 */
@EventResultType(MediaMetaData.class)
public class DeleteMediaMetaDataEvent extends BaseEvent<MediaMetaData> {
    private static final long serialVersionUID = -4617365294433017038L;

    public DeleteMediaMetaDataEvent(MediaMetaData mediaMetaData) {
        super(mediaMetaData);
    }

}