/*
 *
 * Author:  Artie Copeland
 * Last Modified Date: $DateTime: $
 */
package com.blackbox.presentation.extension;

import com.blackbox.user.User;
import com.blackbox.social.RelationshipNetwork;
import com.blackbox.social.Relationship;
import com.blackbox.EntityReference;
import com.blackbox.bookmark.Bookmark;

import java.util.List;

/**
 * @author Artie Copeland
 * @version $Revision: $
 */
public class MockBlackBoxContext extends BlackBoxContext {
    private User user;
    private RelationshipNetwork network;
    private List<EntityReference> ignoring;
    private List<EntityReference> blockedBy;
    private List<EntityReference> vouches;
    private List<EntityReference> blocked;
    private List<EntityReference> vouchedBy;
    private List<Relationship> reverseRelationships;
    private List<Bookmark> wishedUsers;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public RelationshipNetwork getNetwork() {
        return network;
    }

    public void setNetwork(RelationshipNetwork network) {
        this.network = network;
    }

    public List<EntityReference> getBlocked() {
        return blocked;
    }

    public void setBlocked(List<EntityReference> blocked) {
        this.blocked = blocked;
    }

    public List<Relationship> getReverseRelationships() {
        return reverseRelationships;
    }

    public void setReverseRelationships(List<Relationship> reverseRelationships) {
        this.reverseRelationships = reverseRelationships;
    }

    public void setBookmarks(List<Bookmark> wishedUsers) {
        this.wishedUsers = wishedUsers;
    }
}