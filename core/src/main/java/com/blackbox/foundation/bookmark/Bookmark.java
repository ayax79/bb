package com.blackbox.foundation.bookmark;

import com.blackbox.foundation.BBPersistentObject;
import com.blackbox.foundation.EntityReference;
import com.blackbox.foundation.user.User;
import static org.yestech.lib.crypto.MessageDigestUtils.sha1Hash;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.UUID;

/**
 *
 *
 */
@XmlRootElement
public class Bookmark extends BBPersistentObject {

    public static final int MAX_LIMITED_USER_WISHES = 10;
    public static final int MAX_UNLIMITED_USER_WISHES = Integer.MAX_VALUE;

    public static enum BookmarkType {
        // order matters, don't change it.
        NORMAL,
        WISH
    }

    private User owner;
    private EntityReference target;
    private BBPersistentObject targetObject;
    private BookmarkType type = BookmarkType.WISH;
    private boolean acknowledged;
    private String description;

    public boolean isAcknowledged() {
        return acknowledged;
    }

    public void setAcknowledged(boolean acknowledged) {
        this.acknowledged = acknowledged;
    }

    public BookmarkType getType() {
        return type;
    }

    public void setType(BookmarkType type) {
        this.type = type;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public EntityReference getTarget() {
        return target;
    }

    public void setTarget(EntityReference target) {
        this.target = target;
    }

    public BBPersistentObject getTargetObject() {
        return targetObject;
    }

    public void setTargetObject(BBPersistentObject targetObject) {
        this.targetObject = targetObject;
    }

    /**
     * This method should be used for creating a new instance of Bookmark. This class
     * will populate the guid value.
     *
     * @return Returns a new instance of bookmark.
     */
    public static Bookmark createBookmark() {
        Bookmark bookmark = new Bookmark();
        final String key = sha1Hash(UUID.randomUUID().toString());
        bookmark.setGuid(key);
        return bookmark;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        Bookmark bookmark = (Bookmark) o;

        if (acknowledged != bookmark.acknowledged) return false;
        if (description != null ? !description.equals(bookmark.description) : bookmark.description != null)
            return false;
        if (owner != null ? !owner.equals(bookmark.owner) : bookmark.owner != null) return false;
        if (target != null ? !target.equals(bookmark.target) : bookmark.target != null) return false;
        if (targetObject != null ? !targetObject.equals(bookmark.targetObject) : bookmark.targetObject != null)
            return false;
        //noinspection RedundantIfStatement
        if (type != bookmark.type) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (owner != null ? owner.hashCode() : 0);
        result = 31 * result + (target != null ? target.hashCode() : 0);
        result = 31 * result + (targetObject != null ? targetObject.hashCode() : 0);
        result = 31 * result + (type != null ? type.hashCode() : 0);
        result = 31 * result + (acknowledged ? 1 : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Bookmark{" +
                "owner=" + owner +
                ", target=" + target +
                ", targetObject=" + targetObject +
                ", type=" + type +
                ", acknowledged=" + acknowledged +
                ", description='" + description + '\'' +
                '}';
    }
}
