package com.blackbox.foundation.user;

import com.blackbox.foundation.BBPersistentObject;


/**
 *
 *
 */
public class UserStats extends BBPersistentObject {

    private int profileViewCount;
    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getProfileViewCount() {
        return profileViewCount;
    }

    public void setProfileViewCount(int profileViewCount) {
        this.profileViewCount = profileViewCount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserStats)) return false;
        if (!super.equals(o)) return false;

        UserStats userStats = (UserStats) o;

        if (profileViewCount != userStats.profileViewCount) return false;
        //noinspection RedundantIfStatement
        if (user != null ? !user.equals(userStats.user) : userStats.user != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + profileViewCount;
        result = 31 * result + (user != null ? user.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "UserStats{" +
                "profileViewCount=" + profileViewCount +
                ", user=" + user +
                "} " + super.toString();
    }
}
