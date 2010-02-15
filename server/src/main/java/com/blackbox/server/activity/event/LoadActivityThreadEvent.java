package com.blackbox.server.activity.event;

import com.blackbox.activity.ActivityRequest;
import org.yestech.event.event.BaseEvent;

/**
 *
 *
 */
public class LoadActivityThreadEvent extends BaseEvent<ActivityRequest> {

    private static final long serialVersionUID = 3756870162537974074L;

    public LoadActivityThreadEvent() {
    }

    public LoadActivityThreadEvent(ActivityRequest activityRequest) {
        super(activityRequest);
    }
}