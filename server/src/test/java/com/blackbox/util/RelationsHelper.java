package com.blackbox.util;

import com.blackbox.social.ISocialManager;
import com.blackbox.social.Relationship;
import com.blackbox.user.User;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

/**
 * @author colin@blackboxrepublic.com
 */
public class RelationsHelper {

    private ISocialManager socialManager;

    public RelationsHelper(ISocialManager socialManager) {
        this.socialManager = Affirm.isNotNull(socialManager, "socialManager");
    }

    public void createFriendship(User from, User to) {
        socialManager.relate(Relationship.createRelationship(from.toEntityReference(), to.toEntityReference(), Relationship.RelationStatus.FRIEND_PENDING));
        socialManager.acceptRequest(to.getGuid(), from.getGuid());
        assertTrue(isFriends(from, to));
    }

    public void createBidirectionalFriendship(User one, User two) {
        createFriendship(one, two);
        createFriendship(two, one);
    }

    public void createFollowing(User from, User to) {
        socialManager.relate(Relationship.createRelationship(from.toEntityReference(), to.toEntityReference(), Relationship.RelationStatus.FOLLOW));
        assertTrue(isFollowing(from, to));
    }

    public boolean isFriends(User one, User two) {
        return socialManager.loadRelationshipNetwork(one.getGuid()).isFriend(two.getGuid());
    }

    public boolean isFollowing(User from, User to) {
        return !socialManager.loadRelationshipNetwork(to.getGuid()).isFollowing(from.getGuid());
    }

}