package com.blackbox.media;

import com.blackbox.EntityType;
import com.blackbox.bookmark.WishStatus;
import com.blackbox.user.User;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * @author A.J. Wright
 */
@XmlRootElement
public class AvatarImage implements Serializable {

    private String entityGuid;
    private EntityType entityType;
    private String name;
    private String imageLink;
    private WishStatus wishStatus;
    private User.UserType userType;

    public AvatarImage(String entityGuid, String name, User.UserType userType, String imageLink) {
        this.entityGuid = entityGuid;
        this.name = name;
        this.userType = userType;
        this.imageLink = imageLink;
    }

    public AvatarImage() {
    }

    public String getEntityGuid() {
        return entityGuid;
    }

    public void setEntityGuid(String entityGuid) {
        this.entityGuid = entityGuid;
    }

    public EntityType getEntityType() {
        return entityType;
    }

    public void setEntityType(EntityType entityType) {
        this.entityType = entityType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageLink() {
        return imageLink;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }

    public WishStatus getWishStatus() {
        return wishStatus;
    }

    public void setWishStatus(WishStatus wishStatus) {
        this.wishStatus = wishStatus;
    }

    public User.UserType getUserType() {
        return userType;
    }

    public void setUserType(User.UserType userType) {
        this.userType = userType;
    }

    public AvatarImage cloneAvatarImage() {
        return new AvatarImage(entityGuid, name, userType, imageLink);
    }
}
