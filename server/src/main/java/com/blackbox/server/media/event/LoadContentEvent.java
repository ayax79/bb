/*
 * Copyright (c) 2009. Blackbox, LLC
 * All rights reserved.
 */

package com.blackbox.server.media.event;

import org.yestech.event.event.BaseEvent;

/**
 *
 *
 */
public class LoadContentEvent extends BaseEvent<Long> {
    private static final long serialVersionUID = -5632399678288215985L;

    public LoadContentEvent(long identifier) {
        super(identifier);
    }
}
