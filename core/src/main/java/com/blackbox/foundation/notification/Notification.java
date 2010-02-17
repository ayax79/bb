package com.blackbox.foundation.notification;

import com.blackbox.foundation.occasion.AttendingStatus;
import com.blackbox.foundation.occasion.Occasion;
import com.blackbox.foundation.user.User;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 *
 *
 */
@XmlRootElement
public class Notification implements Serializable {
    public enum Type {
        Wish, Friend, Follow, Relationship, Gift, Vouch, Occasion
    }

    private String guid;
    private Type type;

    private String fromEntityGuid;
    private String displayName;
    private String image;
    private String avatarImage;
    private String username;
    private boolean following;
    private boolean wishing;
    private AttendingStatus attendingStatus;
    private Occasion occasion;
    private User user;
    private String attendeeGuid;

    public void setAttendeeGuid(String attendeeGuid) {
        this.attendeeGuid = attendeeGuid;
    }

    public String getAttendeeGuid() {
        return attendeeGuid;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public void setOccasion(Occasion occasion) {
        this.occasion = occasion;
    }

    public AttendingStatus getAttendingStatus() {
        return attendingStatus;
    }

    public Occasion getOccasion() {
        return occasion;
    }

    public void setAttendingStatus(AttendingStatus attendingStatus) {
        this.attendingStatus = attendingStatus;
    }

    public boolean isFollowing() {
        return following;
    }

    public void setFollowing(boolean following) {
        this.following = following;
    }

    public boolean isWishing() {
        return wishing;
    }

    public void setWishing(boolean wishing) {
        this.wishing = wishing;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public String getAvatarImage() {
        return avatarImage;
    }

    public String getImage() {
        return image;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setAvatarImage(String avatarImage) {
        this.avatarImage = avatarImage;
    }

    public void setFromEntityGuid(String fromEntityGuid) {
        this.fromEntityGuid = fromEntityGuid;
    }

    public String getFromEntityGuid() {
        return fromEntityGuid;
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Type getType() {
        return type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Notification that = (Notification) o;

        if (following != that.following) return false;
        if (wishing != that.wishing) return false;
        if (avatarImage != null ? !avatarImage.equals(that.avatarImage) : that.avatarImage != null) return false;
        if (displayName != null ? !displayName.equals(that.displayName) : that.displayName != null) return false;
        if (fromEntityGuid != null ? !fromEntityGuid.equals(that.fromEntityGuid) : that.fromEntityGuid != null)
            return false;
        if (guid != null ? !guid.equals(that.guid) : that.guid != null) return false;
        if (image != null ? !image.equals(that.image) : that.image != null) return false;
        if (attendingStatus != that.attendingStatus) return false;
        if (type != that.type) return false;
        if (username != null ? !username.equals(that.username) : that.username != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = guid != null ? guid.hashCode() : 0;
        result = 31 * result + (type != null ? type.hashCode() : 0);
        result = 31 * result + (fromEntityGuid != null ? fromEntityGuid.hashCode() : 0);
        result = 31 * result + (displayName != null ? displayName.hashCode() : 0);
        result = 31 * result + (image != null ? image.hashCode() : 0);
        result = 31 * result + (avatarImage != null ? avatarImage.hashCode() : 0);
        result = 31 * result + (username != null ? username.hashCode() : 0);
        result = 31 * result + (following ? 1 : 0);
        result = 31 * result + (wishing ? 1 : 0);
        result = 31 * result + (attendingStatus != null ? attendingStatus.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Notification{" +
                "guid='" + guid + '\'' +
                ", type=" + type +
                ", fromEntityGuid='" + fromEntityGuid + '\'' +
                ", displayName='" + displayName + '\'' +
                ", image='" + image + '\'' +
                ", avatarImage='" + avatarImage + '\'' +
                ", username='" + username + '\'' +
                ", following=" + following +
                ", wishing=" + wishing +
                ", attendingStatus=" + attendingStatus +
                '}';
    }
}
