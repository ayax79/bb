package com.blackbox.user;

import com.blackbox.BBPersistentObject;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import org.joda.time.DateTime;

import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 *
 */
@XStreamAlias("viewedby")
@XmlRootElement(name = "viewedby")
public class ViewedBy extends BBPersistentObject {

    public static enum ViewedByType {
        PROFILE,
        EVENT
    }

    private User user;
    private String destGuid;
    private ViewedByType viewedByType;

    public ViewedBy() {
    }

    public ViewedBy(String userGuid, ViewedByType viewedByType, DateTime created) {
        user = new User(userGuid);
        this.viewedByType = viewedByType;
        this.created = created;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public ViewedByType getViewedByType() {
        return viewedByType;
    }

    public void setViewedByType(ViewedByType viewedByType) {
        this.viewedByType = viewedByType;
    }

    public String getDestGuid() {
        return destGuid;
    }

    public void setDestGuid(String destGuid) {
        this.destGuid = destGuid;
    }

    @Override
    public String toString() {
        return "ViewedBy {" +
                "viewedGuid =" + destGuid +
                ", viewedByType =" + viewedByType +
                ", user=" + user +
                "} " + super.toString();
    }
}
