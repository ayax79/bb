/*
 * Copyright (c) 2009. Blackbox, LLC
 * All rights reserved.
 */

package com.blackbox.server.media.event;

import org.yestech.event.event.BaseEvent;
import org.yestech.event.annotation.EventResultType;
import com.blackbox.media.MediaLibrary;

/**
 *
 *
 */
@EventResultType(MediaLibrary.class)
public class SaveMediaLibraryEvent extends BaseEvent<MediaLibrary> {
    private static final long serialVersionUID = 3486690138536636408L;

    public SaveMediaLibraryEvent(MediaLibrary mediaLibrary) {
        super(mediaLibrary);
    }
    
}