package com.blackbox.notification;

import static com.google.common.collect.Maps.newHashMap;

import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.Iterator;
import java.util.Map;

/**
 *
 *
 */
@XmlRootElement(name = "notifications")
@XmlAccessorType
public class Notifications implements Iterable<NotificationGroup>, Serializable {
    private Map<Notification.Type, NotificationGroup> groups = newHashMap();

    public void setGroups(Map<Notification.Type, NotificationGroup> groups) {
        this.groups = groups;
    }

    public void addGroup(NotificationGroup notificationGroup) {
        if (notificationGroup != null) {
            groups.put(notificationGroup.getType(), notificationGroup);
        }
    }

    public Map<Notification.Type, NotificationGroup> getGroups() {
        return groups;
    }

    public Iterator<NotificationGroup> iterator() {
        return groups.values().iterator();
    }

    public NotificationGroup getGroup(Notification.Type type) {
        return groups.get(type);
    }

    public NotificationGroup getFollows() {
        return getGroup(Notification.Type.Follow);
    }

    public NotificationGroup getFriends() {
        return getGroup(Notification.Type.Friend);
    }

    public NotificationGroup getWishes() {
        return getGroup(Notification.Type.Wish);
    }

    public NotificationGroup getGifts() {
        return getGroup(Notification.Type.Gift);
    }

    public NotificationGroup getVouches() {
        return getGroup(Notification.Type.Vouch);
    }

    public NotificationGroup getOccasions() {
        return getGroup(Notification.Type.Occasion);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Notifications that = (Notifications) o;

        if (groups != null ? !groups.equals(that.groups) : that.groups != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return groups != null ? groups.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "Notifications{" +
                "groups=" + groups +
                '}';
    }
}
