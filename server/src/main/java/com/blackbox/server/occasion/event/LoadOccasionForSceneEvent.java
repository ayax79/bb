package com.blackbox.server.occasion.event;

import org.yestech.event.event.BaseEvent;
import com.blackbox.foundation.search.ExploreRequest;
import com.blackbox.foundation.occasion.OccasionRequest;

/**
 *
 *
 */
public class LoadOccasionForSceneEvent extends BaseEvent<OccasionRequest> {
    private static final long serialVersionUID = -6716424535311243343L;

    public LoadOccasionForSceneEvent() {
    }

    public LoadOccasionForSceneEvent(OccasionRequest occasionRequest) {
        super(occasionRequest);
    }
}