/*
 * Copyright (c) 2009. Blackbox, LLC
 * All rights reserved.
 */

package com.blackbox.foundation.social;

import com.blackbox.foundation.IEntity;
import com.blackbox.foundation.util.NameInfo;
import org.apache.commons.lang.StringUtils;
import org.terracotta.modules.annotations.InstrumentedClass;
import org.yestech.lib.util.Pair;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.*;

import static com.blackbox.foundation.Utils.createPair;
import static com.blackbox.foundation.social.Relationship.RelationStatus.*;
import static org.apache.commons.lang.StringUtils.isEmpty;

/**
 * Implementation of the network from the view of the entity it is created for.
 * The network object is initialized by calling the init method or the constructor that
 * takes the list.
 * <p/>
 * All nodes in the try will only show up in the relationship that they are first seen it. For an instance of the
 * network view is of a certain user you will not see that another two nodes down shares the same friend if you have already
 * marked them as a friend. I am not totally sure at this time if this is the ideal implementation but I will try it
 * this way for now, since it seems to be what {@link edu.uci.ics.jung.graph.DelegateTree} wants.
 * <p/>
 * It is expected that the list passed in is breadth first.
 *
 * @author A.J. Wright
 */
@InstrumentedClass
@XmlRootElement(name = "relationshipNetwork")
public class RelationshipNetwork implements Serializable {

    private static final long serialVersionUID = -1452130799131939176L;

    private Map<String, Relationship.RelationStatus> relationshipMap;
    private Map<String, NameInfo> nameInfoMap;

    public RelationshipNetwork() {
    }

    public RelationshipNetwork(Map<String, Relationship.RelationStatus> relationshipMap, Map<String, NameInfo> nameInfoMap) {
        this.relationshipMap = relationshipMap;
        this.nameInfoMap = nameInfoMap;
    }

    /**
     * Find children, based off name and username (users only). Only works for children at a depth of 1.
     *
     * @param search The name or fragement there off to search for in your network.
     * @return List of children matching the query.
     */
    public List<Pair<String, NameInfo>> find(String search) {
        if (isEmpty(search)) return Collections.emptyList();

        final LinkedList<Pair<String, NameInfo>> list = new LinkedList<Pair<String, NameInfo>>();
        for (Map.Entry<String, NameInfo> entry : nameInfoMap.entrySet()) {
            if (isFriend(entry.getKey())) {
                if (loadItemIfFound(search, entry.getValue())) list.add(createPair(entry));
            }
        }
        return list;
    }

    private boolean loadItemIfFound(String search, NameInfo nameInfo) {
        boolean found = StringUtils.containsIgnoreCase(nameInfo.getUsername(), search);
        return found || StringUtils.containsIgnoreCase(nameInfo.getDisplayName(), search);
    }


    public Collection<String> getWithRelationStatus(Relationship.RelationStatus... stati) {

        LinkedList<String> list = new LinkedList<String>();
        for (Map.Entry<String, Relationship.RelationStatus> e : relationshipMap.entrySet()) {
            Relationship.RelationStatus rs = e.getValue();

            for (Relationship.RelationStatus rs1 : stati) {
                if (rs == rs1) {
                    list.add(e.getKey());
                    break;
                }
            }
        }
        return list;
    }


    public Collection<String> getFollowing() {
        return getWithRelationStatus(FOLLOW);
    }

    public Collection<String> getFriends() {
        return getWithRelationStatus(FRIEND, IN_RELATIONSHIP);
    }

    public Collection<String> getInRelationship() {
        return getWithRelationStatus(IN_RELATIONSHIP);
    }

    public boolean isRelationStatus(String guid, Relationship.RelationStatus status) {
        Relationship.RelationStatus relationStatus = relationshipMap.get(guid);
        return relationStatus != null && relationStatus == status;
    }

    public Integer getWeightForRelationship(IEntity ref) {
        Relationship.RelationStatus rs = relationshipMap.get(ref.getGuid());
        return rs != null ? rs.getWeight() : null;
    }

    public boolean isFriend(String guid) {
        return guid != null && isRelationStatus(guid, FRIEND);
    }

    public boolean isPending(String guid) {
        return guid != null && isRelationStatus(guid, FRIEND_PENDING);
    }

    public boolean isFollowing(String guid) {
        return guid != null && isRelationStatus(guid, FOLLOW);
    }

    public boolean isInRelationship(String guid) {
        return guid != null && isRelationStatus(guid, IN_RELATIONSHIP);
    }

    public NameInfo getNameInfo(String guid) {
        return nameInfoMap.get(guid);
    }

    public Map<String, Relationship.RelationStatus> getRelationshipMap() {
        return relationshipMap;
    }

    public void setRelationshipMap(Map<String, Relationship.RelationStatus> relationshipMap) {
        this.relationshipMap = relationshipMap;
    }

    public Map<String, NameInfo> getNameInfoMap() {
        return nameInfoMap;
    }

    public void setNameInfoMap(Map<String, NameInfo> nameInfoMap) {
        this.nameInfoMap = nameInfoMap;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RelationshipNetwork that = (RelationshipNetwork) o;

        if (nameInfoMap != null ? !nameInfoMap.equals(that.nameInfoMap) : that.nameInfoMap != null) return false;
        //noinspection RedundantIfStatement
        if (relationshipMap != null ? !relationshipMap.equals(that.relationshipMap) : that.relationshipMap != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = relationshipMap != null ? relationshipMap.hashCode() : 0;
        result = 31 * result + (nameInfoMap != null ? nameInfoMap.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "RelationshipNetwork{" +
                "relationshipMap=" + relationshipMap +
                ", nameInfoMap=" + nameInfoMap +
                '}';
    }
}
