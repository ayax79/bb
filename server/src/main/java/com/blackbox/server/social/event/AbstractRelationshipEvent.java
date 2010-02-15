package com.blackbox.server.social.event;

import com.blackbox.social.Relationship;
import org.yestech.event.event.BaseEvent;

/**
 * @author A.J. Wright
 */
public abstract class AbstractRelationshipEvent extends BaseEvent<Relationship> {
    public AbstractRelationshipEvent(Relationship relationship) {
        super(relationship);
    }
}
