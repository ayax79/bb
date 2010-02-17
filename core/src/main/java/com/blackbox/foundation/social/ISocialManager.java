/*
 * Copyright (c) 2009. Blackbox, LLC
 * All rights reserved.
 */

package com.blackbox.foundation.social;

import com.blackbox.foundation.EntityReference;
import com.blackbox.foundation.notification.Notification;
import com.blackbox.foundation.notification.NotificationGroup;
import com.blackbox.foundation.notification.Notifications;
import com.blackbox.foundation.user.PaginationResults;
import com.blackbox.foundation.util.Count;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 *
 *
 */
public interface ISocialManager {

    void deleteRelationship(String fromGuid, String toGuid);

    List<Relationship> loadRelationshipsByToEntityGuid(String toEntityGuid);

    RelationshipNetwork loadRelationshipNetwork(String userGuid);

    Relationship loadRelationshipByGuid(String relationshipGuid);

    Relationship loadRelationshipByEntities(String fromGuid, String toGuid);

    Vouch vouch(VouchRequest vouch);

    Vouch saveVouch(Vouch vouch);

    void deleteVouch(String voucherGuid, String targetGuid);

    List<Vouch> loadVouchByVoucher(String voucherGuid);

    List<Vouch> loadOutgoingByVoucher(String voucherGuid);

    List<Vouch> loadVouchByTarget(String targetGuid);

    List<EntityReference> loadIgnores(String ignorerGuid);

    List<EntityReference> loadIgnoreByTarget(String targetGuid);

    Ignore loadIgnoreByIgnorerAndTarget(String ignorerGuid, String targetGuid);

    Ignore ignore(Ignore ignore);

    /**
     * Creates a new relationship between to parties.
     * 
     * @param relationship The relationship to be created.
     */
    void relate(Relationship relationship);

    List<EntityReference> loadBlockedBy(String blockedGuid);

    void deleteIgnore(String fromGuid, String toGuid);

    List<EntityReference> loadBlocks(String ignorerGuid);

    List<Relationship> loadReverseRelationships(String targetGuid);

    List<Relationship> friendRequests(String userGuid);

    @Transactional
    void acceptRequest(String acceptorGuid, String requestorGuid);

    void rejectRequest(String rejectorGuid, String requestorGuid);

    Notifications loadLimitNotifications(String guid, int limit);

    Notifications loadNotifications(String guid);

    Notification loadNewestNotification(String guid, Notification.Type friend);

    NotificationGroup loadNewestNotificationWithLimit(String guid, Notification.Type type, int limit);

    List<Relationship> loadFollowedBy(String guid);

    PaginationResults<Connection> loadConnections(String guid, Connection.ConnectionType type, int startIndex, int maxResults);

    PaginationResults<UserVouch> loadUserVouches(String guid, int startIndex, int maxResults);

    Count monthlyVouchCount(String guid, boolean permanent);

    Vouch loadVouchByGuid(String guid);

    void deleteVouch(String vouchGuid);

    List<Relationship> loadRelationships(String guid, String callerGuid);

    Vouch loadVouchByVoucherAndVouchee(String fromGuid, String toGuid);
}
