package com.blackbox.activity;

import com.blackbox.EntityReference;
import com.blackbox.EntityType;
import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;

import java.io.Serializable;

/**
 * A lightweight reference to an {@link IActivity} object. This class stores the two minimal things it takes
 * to acquire any given {@link com.blackbox.IEntity} objects; its guid and its entity type. Plus
 * <p/>
 * {@link com.blackbox.BaseEntity} provides a helper method {@link com.blackbox.BaseEntity#toEntityReference()} to quickly create
 * EntityRefernce objects.
 */
public class ActivityReference extends EntityReference implements Serializable {

    private ActivityTypeEnum activityType;
    private DateTime postDate;
    private static final long serialVersionUID = 7703992138837220975L;
    private int lastCommentBucket = 0;
    private int bucket;

    public ActivityReference() {
    }

    public int getBucket() {
        return bucket;
    }

    public void setBucket(int bucket) {
        this.bucket = bucket;
    }

    public int getLastCommentBucket() {
        return lastCommentBucket;
    }

    public void setLastCommentBucket(int lastCommentBucket) {
        this.lastCommentBucket = lastCommentBucket;
    }

    public ActivityTypeEnum getActivityType() {
        return activityType;
    }

    public void setActivityType(ActivityTypeEnum activityType) {
        this.activityType = activityType;
    }

    public DateTime getPostDate() {
        return postDate;
    }

    public void setPostDate(DateTime postDate) {
        this.postDate = postDate;
    }

    /**
     * The type of Entity this reference is for. See {@link com.blackbox.EntityType} for possible types.
     *
     * @return type of Entity this reference is for.
     */
    @Override
    public EntityType getOwnerType() {
        return super.getOwnerType();
    }

    /**
     * Sets type of Entity this reference is for.
     *
     * @param ownerType type of Entity this reference is for.
     */
    @Override
    public void setOwnerType(EntityType ownerType) {
        super.setOwnerType(ownerType);
    }

    /**
     * Returns the generated unique identifier for the entity.
     *
     * @return the generated unique identifier for the entity.
     */
    @Override
    public String getGuid() {
        return super.getGuid();
    }

    /**
     * Sets the generated unique identifier for the entity.
     *
     * @param guid the generated unique identifier for the entity.
     */
    @Override
    public void setGuid(String guid) {
        super.setGuid(guid);
    }

    public static ActivityReference createActivityReference(IActivity activity) {
        ActivityReference activityReference = new ActivityReference();
        activityReference.setPostDate(activity.getPostDate());
        activityReference.setGuid(activity.getGuid());
        ActivityTypeEnum activityType = activity.getActivityType();
        activityReference.setActivityType(activityType);
        EntityType entityType = EntityType.MESSAGE;
        if (ActivityTypeEnum.MEDIA.equals(activityType)) {
            entityType = EntityType.MEDIA;
        } else if (ActivityTypeEnum.OCCASION.equals(activityType)) {
            entityType = EntityType.OCCASION;
        }
        activityReference.setOwnerType(entityType);
        return activityReference;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ActivityReference)) return false;
        if (!super.equals(o)) return false;

        ActivityReference that = (ActivityReference) o;

        if (activityType != that.activityType) return false;
        if (postDate != null ? !postDate.equals(that.postDate) : that.postDate != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (activityType != null ? activityType.hashCode() : 0);
        result = 31 * result + (postDate != null ? postDate.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ActivityReference{" +
                "activityType=" + activityType +
                ", postDate=" + postDate +
                ", super=" + super.toString() +
                '}';
    }

    public static ActivityReference createActivityReference(EntityReference parent) {
        ActivityReference activityReference = null;
        if (parent != null) {
            EntityType entityType = parent.getOwnerType();
            String guid = parent.getGuid();
            if (StringUtils.isNotBlank(guid) && entityType != null) {
                activityReference = new ActivityReference();
                activityReference.setGuid(guid);
                activityReference.setOwnerType(entityType);
                if (EntityType.MEDIA.equals(entityType)) {
                    activityReference.setActivityType(ActivityTypeEnum.MEDIA);
                } else if (EntityType.MESSAGE.equals(entityType)) {
                    activityReference.setActivityType(ActivityTypeEnum.MESSAGE);
                } else if (EntityType.OCCASION.equals(entityType)) {
                    activityReference.setActivityType(ActivityTypeEnum.OCCASION);
                } else {
                    activityReference = null;
                }
            }
        }
        return activityReference;
    }
}