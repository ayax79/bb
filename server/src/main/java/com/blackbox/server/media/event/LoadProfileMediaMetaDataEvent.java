/*
 * Copyright (c) 2009. Blackbox, LLC
 * All rights reserved.
 */

package com.blackbox.server.media.event;

import com.blackbox.foundation.EntityReference;
import com.blackbox.foundation.media.MediaMetaData;
import org.yestech.event.event.BaseEvent;
import org.yestech.event.annotation.EventResultType;

/**
 *
 *
 */
@EventResultType(MediaMetaData.class)
public class LoadProfileMediaMetaDataEvent extends BaseEvent<EntityReference> {
    private static final long serialVersionUID = -6778901612538369579L;

    public LoadProfileMediaMetaDataEvent(EntityReference reference) {
        super(reference);
    }
}