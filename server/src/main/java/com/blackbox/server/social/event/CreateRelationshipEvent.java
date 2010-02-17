package com.blackbox.server.social.event;

import com.blackbox.foundation.social.Relationship;

/**
 * @author A.J. Wright
 */
public class CreateRelationshipEvent extends AbstractRelationshipEvent{
    public CreateRelationshipEvent(Relationship relationship) {
        super(relationship);
    }
}
