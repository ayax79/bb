package com.blackbox.foundation.security;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 *
 *
 */

@XmlRootElement(name = "privacy")
public class Privacy implements Serializable {
    private static final long serialVersionUID = -4272827406604068108L;
    private boolean nonFriendsCanPm;
    private boolean nonFriendsCanFollow;
    private boolean nonFriendsCanComment;
    private boolean nonFriendsCanSearch;

    private boolean limitedCanPm;
    private boolean limitedCanFollow;
    private boolean limitedCanComment;
    private boolean limitedCanSearch;
    private boolean limitedCanSeePictures;
    private boolean limitedCanSeeActivity;
    private boolean limitedCanSeeGifts;

    public Privacy() {
    }

    public boolean isNonFriendsCanPm() {
        return nonFriendsCanPm;
    }

    public void setNonFriendsCanPm(boolean nonFriendsCanPm) {
        this.nonFriendsCanPm = nonFriendsCanPm;
    }

    public boolean isNonFriendsCanFollow() {
        return nonFriendsCanFollow;
    }

    public void setNonFriendsCanFollow(boolean nonFriendsCanFollow) {
        this.nonFriendsCanFollow = nonFriendsCanFollow;
    }

    public boolean isNonFriendsCanComment() {
        return nonFriendsCanComment;
    }

    public void setNonFriendsCanComment(boolean nonFriendsCanComment) {
        this.nonFriendsCanComment = nonFriendsCanComment;
    }

    public boolean isNonFriendsCanSearch() {
        return nonFriendsCanSearch;
    }

    public void setNonFriendsCanSearch(boolean nonFriendsCanSearch) {
        this.nonFriendsCanSearch = nonFriendsCanSearch;
    }

    public boolean isLimitedCanPm() {
        return limitedCanPm;
    }

    public void setLimitedCanPm(boolean limitedCanPm) {
        this.limitedCanPm = limitedCanPm;
    }

    public boolean isLimitedCanFollow() {
        return limitedCanFollow;
    }

    public void setLimitedCanFollow(boolean limitedCanFollow) {
        this.limitedCanFollow = limitedCanFollow;
    }

    public boolean isLimitedCanComment() {
        return limitedCanComment;
    }

    public void setLimitedCanComment(boolean limitedCanComment) {
        this.limitedCanComment = limitedCanComment;
    }

    public boolean isLimitedCanSearch() {
        return limitedCanSearch;
    }

    public void setLimitedCanSearch(boolean limitedCanSearch) {
        this.limitedCanSearch = limitedCanSearch;
    }

    public boolean isLimitedCanSeePictures() {
        return limitedCanSeePictures;
    }

    public void setLimitedCanSeePictures(boolean limitedCanSeePictures) {
        this.limitedCanSeePictures = limitedCanSeePictures;
    }

    public boolean isLimitedCanSeeActivity() {
        return limitedCanSeeActivity;
    }

    public void setLimitedCanSeeActivity(boolean limitedCanSeeActivity) {
        this.limitedCanSeeActivity = limitedCanSeeActivity;
    }

    public boolean isLimitedCanSeeGifts() {
        return limitedCanSeeGifts;
    }

    public void setLimitedCanSeeGifts(boolean limitedCanSeeGifts) {
        this.limitedCanSeeGifts = limitedCanSeeGifts;
    }
}
