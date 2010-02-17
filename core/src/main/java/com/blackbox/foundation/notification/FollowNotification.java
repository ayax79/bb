package com.blackbox.foundation.notification;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 *
 *
 */
@XmlRootElement
public class FollowNotification extends Notification implements Serializable {
    private boolean following;

    public boolean isFollowing() {
        return following;
    }

    public void setFollowing(boolean following) {
        this.following = following;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        FollowNotification that = (FollowNotification) o;

        if (following != that.following) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (following ? 1 : 0);
        return result;
    }

    @Override
    public String toString() {
        return "FollowNotification{" + super.toString() +
                "following=" + following +
                '}';
    }
}
