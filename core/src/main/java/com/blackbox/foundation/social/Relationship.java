/*
 * Copyright (c) 2009. Blackbox, LLC
 * All rights reserved.
 */

package com.blackbox.foundation.social;

import com.blackbox.foundation.BBPersistentObject;
import com.blackbox.foundation.BaseEntity;
import com.blackbox.foundation.EntityReference;

import static com.blackbox.foundation.Utils.applyGuid;

import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 *
 */
@XmlRootElement(name = "relationship")
public class Relationship extends BBPersistentObject {
    private boolean acknowledged;
    private static final long serialVersionUID = -7191406241022658727L;

    public static enum RelationStatus implements Comparable<RelationStatus> {
        IN_RELATIONSHIP(50),
        FRIEND(10),
        FOLLOW(0),
        NEGATIVE(-10),
        FRIEND_PENDING(9),
        IN_RELATIONSHIP_PENDING(49),
        MAX_STATUS(500);

        private Integer weight;

        RelationStatus(Integer weight) {
            this.weight = weight;
        }

        public int getWeight() {
            return weight;
        }

        public int compareTo(Relationship r) {
            return weight.compareTo(r.weight);
        }

        public static RelationStatus getClosestStatusForWeight(int w) {

            if (w > 49) {
                return IN_RELATIONSHIP;
            } else if (w == 49) {
                return IN_RELATIONSHIP_PENDING;
            } else if (w > 9) {
                return FRIEND;
            } else if (w < 0) {
                return NEGATIVE;
            } else if (w == 9) {
                return FRIEND_PENDING;
            } else {
                return FOLLOW;
            }
        }

    }

    private EntityReference fromEntity;
    private EntityReference toEntity;
    private BaseEntity fromEntityObject;
    private BaseEntity toEntityObject;
    private String description;
    private int weight = 1;
    private Integer previousWeight;

    public Relationship() {
    }

    public Relationship(EntityReference fromEntity, EntityReference toEntity) {
        this.fromEntity = fromEntity;
        this.toEntity = toEntity;
    }

    public Relationship(EntityReference fromEntity, EntityReference toEntity, int weight) {
        this.fromEntity = fromEntity;
        this.toEntity = toEntity;
        this.weight = weight;
    }

    public boolean isAcknowledged() {
        return acknowledged;
    }

    public void setAcknowledged(boolean acknowledged) {
        this.acknowledged = acknowledged;
    }

    public EntityReference getFromEntity() {
        return fromEntity;
    }

    public void setFromEntity(EntityReference fromEntity) {
        this.fromEntity = fromEntity;
    }

    public EntityReference getToEntity() {
        return toEntity;
    }

    public void setToEntity(EntityReference toEntity) {
        this.toEntity = toEntity;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public void setWeightWithRelationshipStatus(RelationStatus rs) {
        this.weight = rs.getWeight();
    }

    public BaseEntity getFromEntityObject() {
        return fromEntityObject;
    }

    public void setFromEntityObject(BaseEntity fromEntityObject) {
        this.fromEntityObject = fromEntityObject;
    }

    public BaseEntity getToEntityObject() {
        return toEntityObject;
    }

    public void setToEntityObject(BaseEntity toEntityObject) {
        this.toEntityObject = toEntityObject;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * This is used to reset a user back to the previous weight if they are requesting to be a friend or
     * in relationship.
     *
     * @return The previous weight, before the last save.
     */
    public Integer getPreviousWeight() {
        return previousWeight;
    }

    public void setPreviousWeight(Integer previousWeight) {
        this.previousWeight = previousWeight;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        Relationship that = (Relationship) o;

        if (weight != that.weight) return false;
        if (description != null ? !description.equals(that.description) : that.description != null) return false;
        if (fromEntity != null ? !fromEntity.equals(that.fromEntity) : that.fromEntity != null) return false;
        if (fromEntityObject != null ? !fromEntityObject.equals(that.fromEntityObject) : that.fromEntityObject != null)
            return false;
        if (toEntity != null ? !toEntity.equals(that.toEntity) : that.toEntity != null) return false;
        //noinspection RedundantIfStatement
        if (toEntityObject != null ? !toEntityObject.equals(that.toEntityObject) : that.toEntityObject != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (fromEntity != null ? fromEntity.hashCode() : 0);
        result = 31 * result + (toEntity != null ? toEntity.hashCode() : 0);
        result = 31 * result + (fromEntityObject != null ? fromEntityObject.hashCode() : 0);
        result = 31 * result + (toEntityObject != null ? toEntityObject.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + weight;
        return result;
    }

    public static Relationship createRelationship(BaseEntity fromEntity, BaseEntity toEntity) {
        Relationship r = new Relationship(fromEntity.toEntityReference(), toEntity.toEntityReference());
        r.fromEntityObject = fromEntity;
        r.toEntityObject = toEntity;
        applyGuid(r);
        return r;
    }

    public static Relationship createRelationship(BaseEntity fromEntity, BaseEntity toEntity, int weight) {
        Relationship r = new Relationship(fromEntity.toEntityReference(), toEntity.toEntityReference());
        r.fromEntityObject = fromEntity;
        r.toEntityObject = toEntity;
        r.weight = weight;
        applyGuid(r);
        return r;
    }

    public static Relationship createRelationship(EntityReference fromEntity, EntityReference toEntity, int weight) {
        Relationship r = new Relationship(fromEntity, toEntity);
        r.weight = weight;
        applyGuid(r);
        return r;
    }

    public static Relationship createRelationship(EntityReference fromEntity, EntityReference toEntity, RelationStatus rs) {
        Relationship r = new Relationship(fromEntity, toEntity);
        if (rs != null) r.weight = rs.getWeight();
        applyGuid(r);
        return r;
    }

    public static Relationship createRelationship(BaseEntity fromEntity, BaseEntity toEntity, RelationStatus rs) {
        Relationship r = new Relationship(fromEntity.toEntityReference(), toEntity.toEntityReference());
        r.fromEntityObject = fromEntity;
        r.toEntityObject = toEntity;
        if (rs != null) r.weight = rs.getWeight();
        applyGuid(r);
        return r;
    }


}
