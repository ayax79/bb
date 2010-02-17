/*
 * Copyright (c) 2009. Blackbox, LLC
 * All rights reserved.
 */

package com.blackbox.server.activity.event;

import com.blackbox.foundation.activity.Activity;
import org.yestech.event.event.BaseEvent;
import org.yestech.event.annotation.EventResultType;

import java.io.InputStream;

/**
 *
 *
 */
@EventResultType(Activity.class)
public class PublishActivityEvent extends BaseEvent<Activity> {
    private static final long serialVersionUID = 6637934039331764029L;
    private InputStream inputStream;

    public PublishActivityEvent(Activity activity) {
        super(activity);
    }

    public void setInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    public InputStream getInputStream() {
        return inputStream;
    }
}