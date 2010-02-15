package com.blackbox.server.media.event;

import com.blackbox.EntityReference;
import org.yestech.event.event.BaseEvent;
import org.yestech.event.annotation.EventResultType;

import java.util.List;

/**
 *
 *
 */
@EventResultType(List.class)
public class LoadMediaLibrariesByOwnerEvent extends BaseEvent<EntityReference> {
    private static final long serialVersionUID = 1871990813493752266L;

    public LoadMediaLibrariesByOwnerEvent(EntityReference entityReference) {
        super(entityReference);
    }
}
