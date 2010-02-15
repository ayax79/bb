/*
 * Copyright (c) 2009. Blackbox, LLC
 * All rights reserved.
 */

package com.blackbox.server.social;

import com.blackbox.social.Relationship;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

/**
 *
 *
 */
public interface INetworkDao {

    int bulkRelationshipDelete(String fromGuid, String toGuid);

    void save(Relationship r);

    Relationship loadRelationshipByGuid(String relationshipGuid);

    Relationship loadRelationshipByEntities(String fromGuid, String toGuid);

    List<Relationship> loadRelationshipsByFromEntityGuid(String guid);

    List<Relationship> loadRelationshipsByToEntityGuid(String guid);

    List<Relationship> loadRelationshipsByToEntityGuidAndWeightRange(String guid, Relationship.RelationStatus fromStatus, Relationship.RelationStatus toStatus);

    void delete(Relationship r);

    List<Relationship> loadRelationshipPending(String guid);

    /**
     * Queries relationships for a specific guid with a weight range.
     *
     * @param guid The from guid, the object who network we are querying.
     * @param start First weight, inclusive.
     * @param stop End weight, exclusive.
     * @return The list of relationships.
     */
    List<Relationship> loadRelationshipsByFromEntityGuidAndWeightRange(String guid, int start, int stop);

    List<Relationship> loadRelationshipsByToEntityGuidAndWeightRange(String guid, int start, int stop);

    Relationship loadRelationshipByEntitiesAndStatus(String fromGuid, String toGuid, Relationship.RelationStatus follow);

    List<Relationship> loadRelationshipsByToEnityAndWeights(String guid, Integer... statuses);
   
    List<Relationship> loadRelationshipsByFromEnityAndWeights(String guid, Integer... statuses);

    List<Relationship> loadConnections(String guid);

    int deleteRelationship(String fromGuid, String toGuid);

    int countFriendsOrGreater(String guid);
}
