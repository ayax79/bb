package com.blackbox.user;

import com.blackbox.bookmark.WishStatus;
import com.blackbox.social.Relationship;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.Collection;

/**
 * @author A.J. Wright
 */
@XmlRootElement
public class UserPersona implements Serializable {

    private String profileImageUrl;
    private Collection<Relationship> relationships;
    private Collection<Relationship> friends;
    private Collection<Relationship> following;
    private Collection<Relationship> followedBy;
    private Collection<Relationship> vouched;
    private Collection<Relationship> vouchedBy;
    private Collection<ViewedBy> viewedBy;
    private WishStatus wishStatus;
    private static final long serialVersionUID = -1093518806140515013L;

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public Collection<Relationship> getRelationships() {
        return relationships;
    }

    public void setRelationships(Collection<Relationship> relationships) {
        this.relationships = relationships;
    }

    public Collection<Relationship> getFriends() {
        return friends;
    }

    public void setFriends(Collection<Relationship> friends) {
        this.friends = friends;
    }

    public Collection<Relationship> getFollowing() {
        return following;
    }

    public void setFollowing(Collection<Relationship> following) {
        this.following = following;
    }

    public Collection<ViewedBy> getViewedBy() {
        return viewedBy;
    }

    public void setViewedBy(Collection<ViewedBy> viewedBy) {
        this.viewedBy = viewedBy;
    }

    public Collection<Relationship> getFollowedBy() {
        return followedBy;
    }

    public void setFollowedBy(Collection<Relationship> followedBy) {
        this.followedBy = followedBy;
    }

    public WishStatus getWishStatus() {
        return wishStatus;
    }

    public void setWishStatus(WishStatus wishStatus) {
        this.wishStatus = wishStatus;
    }

    public Collection<Relationship> getVouched() {
        return vouched;
    }

    public void setVouched(Collection<Relationship> vouched) {
        this.vouched = vouched;
    }

    public Collection<Relationship> getVouchedBy() {
        return vouchedBy;
    }

    public void setVouchedBy(Collection<Relationship> vouchedBy) {
        this.vouchedBy = vouchedBy;
    }
}
