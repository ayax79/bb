package com.blackbox.foundation.notification;

import static com.google.common.collect.Lists.newArrayList;

import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.Iterator;
import java.util.List;

/**
 *
 *
 */
@XmlRootElement
@XmlAccessorType
public class NotificationGroup<N extends Notification> implements Iterable<N>, Serializable {
    private List<N> items = newArrayList();
    private int total;
    private int totalNotifications;
    private Notification.Type type;

    public void setItems(List<N> items) {
        this.items = items;
    }

    public Notification.Type getType() {
        return type;
    }

    public void setType(Notification.Type type) {
        this.type = type;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getTotal() {
        return total;
    }

    public void addNotification(N notification) {
        items.add(notification);
    }

    public List<N> getItems() {
        return items;
    }

    public Iterator<N> iterator() {
        return items.iterator();
    }

    public int getTotalNotifications() {
        return totalNotifications;
    }

    public void setTotalNotifications(int totalNotifications) {
        this.totalNotifications = totalNotifications;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        NotificationGroup that = (NotificationGroup) o;

        if (total != that.total) return false;
        if (totalNotifications != that.totalNotifications) return false;
        if (items != null ? !items.equals(that.items) : that.items != null) return false;
        if (type != that.type) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = items != null ? items.hashCode() : 0;
        result = 31 * result + total;
        result = 31 * result + totalNotifications;
        result = 31 * result + (type != null ? type.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "NotificationGroup{" +
                "items=" + items +
                ", total=" + total +
                ", totalNotifications=" + totalNotifications +
                ", type=" + type +
                '}';
    }
}
