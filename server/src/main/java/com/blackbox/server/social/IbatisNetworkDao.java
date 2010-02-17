/*
 * Copyright (c) 2009. Blackbox, LLC
 * All rights reserved.
 */

package com.blackbox.server.social;

import com.blackbox.foundation.EntityType;
import static com.blackbox.server.util.PersistenceUtil.insertOrUpdate;
import com.blackbox.foundation.social.Relationship;
import static com.blackbox.foundation.social.Relationship.RelationStatus.*;
import org.springframework.orm.ibatis3.SqlSessionOperations;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 *
 */
@Repository("networkDao")
public class IbatisNetworkDao implements INetworkDao {

    @Resource
    SqlSessionOperations template;


    @Override
    public List<Relationship> loadRelationshipsByFromEntityGuid(String guid) {
        return template.selectList("Relationship.loadRelationshipsByFromEntityGuid", guid);
    }

    @Override
    public List<Relationship> loadRelationshipPending(String guid) {
        Map<String,Object> params = new HashMap<String, Object>(3);
        params.put("guid", guid);
        params.put("startWeight", FRIEND_PENDING.getWeight());
        params.put("endWeight", IN_RELATIONSHIP_PENDING.getWeight());

        return template.selectList("Relationship.loadRelationshipsPending", params);
    }


    @Override
    public List<Relationship> loadRelationshipsByToEnityAndWeights(final String guid, final Integer... statuses) {
        Map<String, Object> params = new HashMap<String,Object>(2);
        params.put("guid", guid);
        params.put("weights", statuses);
        return template.selectList("Relationship.loadRelationshipsByToEntityAndWeights", params);
    }

    public int countFriendsOrGreater(final String guid) {
        Map<String, Object> params = new HashMap<String, Object>(2);
        params.put("guid", guid);
        params.put("weight", FRIEND.getWeight());
        return (Integer) template.selectOne("Relationship.countFriendsOrGreater", params);
    }


    @Override
    public List<Relationship> loadRelationshipsByFromEnityAndWeights(final String guid, final Integer... statuses) {
        Map<String, Object> params = new HashMap<String,Object>(2);
        params.put("guid", guid);
        params.put("weights", statuses);
        return template.selectList("Relationship.loadRelationshipsByFromEntityAndWeights", params);
    }

    @Override
    @SuppressWarnings({"unchecked"})
    public List<Relationship> loadRelationshipsByToEntityGuidAndWeightRange(String guid, Relationship.RelationStatus fromStatus, Relationship.RelationStatus toStatus) {
        Map<String, Object> params = new HashMap<String, Object>(3);
        params.put("guid", guid);
        params.put("startWeight", fromStatus.getWeight());
        params.put("endWeight", toStatus.getWeight());
        return template.selectList("Relationship.loadRelationshipsByToEntityGuidAndWeightRange", params);
    }

    @SuppressWarnings({"unchecked"})
    @Override
    public List<Relationship> loadRelationshipsByToEntityGuid(String guid) {
        return template.selectList("Relationship.loadRelationshipsByToEntityGuid", guid);
    }
    
    @Override
    @SuppressWarnings({"unchecked"})
    public List<Relationship> loadRelationshipsByToEntityGuidAndWeightRange(String guid, int start, int stop) {
        Map<String, Object> params = new HashMap<String, Object>(3);
        params.put("guid", guid);
        params.put("startWeight", start);
        params.put("endWeight", stop);
        return template.selectList("Relationship.loadRelationshipsByToEntityGuidAndWeightRange", params);
    }

    @Override
    public Relationship loadRelationshipByGuid(String relationshipGuid) {
        return (Relationship) template.selectOne("Relationship.loadRelationshipByGuid", relationshipGuid);
    }

    @Override
    @SuppressWarnings({"unchecked"})
    public List<Relationship> loadRelationshipsByFromEntityGuidAndWeightRange(String guid, int start, int stop) {
        Map<String, Object> params = new HashMap<String, Object>(3);
        params.put("guid", guid);
        params.put("startWeight", start);
        params.put("endWeight", stop);
        return template.selectList("Relationship.loadRelationshipsByFromEntityGuidAndWeightRange", params);
    }

    @Override
    public Relationship loadRelationshipByEntities(String fromGuid, String toGuid) {
        Map<String, Object> params = new HashMap<String, Object>(2);
        params.put("fromGuid", fromGuid);
        params.put("toGuid", toGuid);
        return (Relationship) template.selectOne("Relationship.loadRelationshipByEntities", params);
    }

    @Override
    public Relationship loadRelationshipByEntitiesAndStatus(String fromGuid, String toGuid, Relationship.RelationStatus follow) {
        Map<String, Object> params = new HashMap<String, Object>(3);
        params.put("fromGuid", fromGuid);
        params.put("toGuid", toGuid);
        params.put("weight", follow.getWeight());

        return (Relationship) template.selectOne("Relationship.loadRelationshipByEntitiesAndWeight", params);
    }

    @Transactional(readOnly = false)
    public void delete(Relationship r) {
        template.delete("Relationship.delete", r.getGuid());
    }

    @Transactional(readOnly = false)
    @Override
    public int bulkRelationshipDelete(String fromGuid, String toGuid) {
        Map<String, Object> params = new HashMap<String, Object>(2);
        params.put("fromGuid", fromGuid);
        params.put("toGuid", toGuid);
        int count = template.delete("Relationship.bulkRelationshipDelete", params);
        assert count < 2;
        return count;
    }

    @Transactional(readOnly = false)
    @Override
    public void save(Relationship r) {
        insertOrUpdate(r, template, "Relationship.insert", "Relationship.update");
    }

    @Override
    @SuppressWarnings({"unchecked"})
    public List<Relationship> loadConnections(final String guid) {
        Map<String, Object> params = new HashMap<String, Object>(4);
        params.put("fromGuid", guid);
        params.put("toGuid", guid);
        params.put("minWeight", Relationship.RelationStatus.FOLLOW.getWeight());
        params.put("maxWeight", FRIEND.getWeight());
        params.put("ownerType", EntityType.USER);
        return template.selectList("Relationship.loadConnections", params);
    }

    public int deleteRelationship(String fromGuid, String toGuid) {
        return bulkRelationshipDelete(fromGuid, toGuid);
    }


}
