package com.blackbox.server.social.event;

import com.blackbox.foundation.social.Relationship;

/**
 * @author A.J. Wright
 */
public class UpdateRelationshipEvent extends AbstractRelationshipEvent {
    public UpdateRelationshipEvent(Relationship relationship) {
        super(relationship);
    }
}
