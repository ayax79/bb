package com.blackbox.server.activity.event;

import com.blackbox.EntityReference;
import org.yestech.event.event.BaseEvent;

/**
 *
 *
 */
public class LoadLastActivityEvent extends BaseEvent<EntityReference> {

    private static final long serialVersionUID = 3756870162537974074L;

    public LoadLastActivityEvent() {
    }

    public LoadLastActivityEvent(EntityReference entityReference) {
        super(entityReference);
    }
}
