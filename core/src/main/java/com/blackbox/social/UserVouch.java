package com.blackbox.social;

import com.blackbox.user.User;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * @author A.J. Wright
 */
@XmlRootElement
public class UserVouch implements Serializable {
    private static final long serialVersionUID = 3202485942540910661L;

    public static enum VouchDirection {
        INCOMING, // they vouched you
        OUTGOING, // you vouched them
        MUTUAL
    }

    private User user;
    private User toUser;
    private User fromUser;
    private VouchDirection direction;
    private String toDescription;
    private String fromDescription;

    public UserVouch() {
    }

    public UserVouch(User user, VouchDirection direction) {
        this.user = user;
        this.direction = direction;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public VouchDirection getDirection() {
        return direction;
    }

    public void setDirection(VouchDirection direction) {
        this.direction = direction;
    }

    public User getToUser() {
        return toUser;
    }

    public void setToUser(User toUser) {
        this.toUser = toUser;
    }

    public User getFromUser() {
        return fromUser;
    }

    public void setFromUser(User fromUser) {
        this.fromUser = fromUser;
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

        UserVouch userVouch = (UserVouch) o;

        if (direction != userVouch.direction) return false;
        if (fromDescription != null ? !fromDescription.equals(userVouch.fromDescription) : userVouch.fromDescription != null)
            return false;
        if (toDescription != null ? !toDescription.equals(userVouch.toDescription) : userVouch.toDescription != null)
            return false;
        if (user != null ? !user.equals(userVouch.user) : userVouch.user != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = user != null ? user.hashCode() : 0;
        result = 31 * result + (direction != null ? direction.hashCode() : 0);
        result = 31 * result + (toDescription != null ? toDescription.hashCode() : 0);
        result = 31 * result + (fromDescription != null ? fromDescription.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "UserVouch{" +
                "user=" + user +
                ", direction=" + direction +
                ", toDescription='" + toDescription + '\'' +
                ", fromDescription='" + fromDescription + '\'' +
                '}';
    }
}
