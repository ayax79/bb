package com.blackbox.bookmark;

import com.blackbox.user.User;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * @author A.J. Wright
 */
@XmlRootElement
public class UserWish implements Serializable {

    private User user;
    private WishStatus status;
    private String toDescription;
    private String fromDescription;

    public UserWish() {
    }

    public UserWish(User user, WishStatus status) {
        this.user = user;
        this.status = status;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public WishStatus getStatus() {
        return status;
    }

    public void setStatus(WishStatus status) {
        this.status = status;
    }

    public String getToDescription() {
        return toDescription;
    }

    public void setToDescription(String toDescription) {
        this.toDescription = toDescription;
    }

    public String getFromDescription() {
        return fromDescription;
    }

    public void setFromDescription(String fromDescription) {
        this.fromDescription = fromDescription;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserWish userWish = (UserWish) o;

        if (fromDescription != null ? !fromDescription.equals(userWish.fromDescription) : userWish.fromDescription != null)
            return false;
        if (status != userWish.status) return false;
        if (toDescription != null ? !toDescription.equals(userWish.toDescription) : userWish.toDescription != null)
            return false;
        //noinspection RedundantIfStatement
        if (user != null ? !user.equals(userWish.user) : userWish.user != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = user != null ? user.hashCode() : 0;
        result = 31 * result + (status != null ? status.hashCode() : 0);
        result = 31 * result + (toDescription != null ? toDescription.hashCode() : 0);
        result = 31 * result + (fromDescription != null ? fromDescription.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "UserWish{" +
                "user=" + user +
                ", status=" + status +
                ", toDescription='" + toDescription + '\'' +
                ", fromDescription='" + fromDescription + '\'' +
                '}';
    }
}
