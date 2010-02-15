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
public class LoadContentMetaDataEvent extends BaseEvent<String> {
    private static final long serialVersionUID = 6473003446572123864L;

    public LoadContentMetaDataEvent(String guid) {
        super(guid);
    }
}
