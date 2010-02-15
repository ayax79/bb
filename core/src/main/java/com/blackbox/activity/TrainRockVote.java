package com.blackbox.activity;

import com.blackbox.BBPersistentObject;
import com.blackbox.user.User;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class TrainRockVote extends BBPersistentObject {

    protected User user;
    protected boolean vote;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public boolean isVote() {
        return vote;
    }

    public void setVote(boolean vote) {
        this.vote = vote;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        TrainRockVote that = (TrainRockVote) o;

        if (vote != that.vote) return false;
        //noinspection RedundantIfStatement
        if (user != null ? !user.equals(that.user) : that.user != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (user != null ? user.hashCode() : 0);
        result = 31 * result + (vote ? 1 : 0);
        return result;
    }

    @Override
    public String toString() {
        return "TrainRockVote{" +
                "user=" + user +
                ", vote=" + vote +
                '}';
    }
}
