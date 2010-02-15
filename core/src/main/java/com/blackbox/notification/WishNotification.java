package com.blackbox.notification;

import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 *
 */
@XmlRootElement
public class WishNotification extends Notification {
    private boolean wishing;

    public boolean isWishing() {
        return wishing;
    }

    public void setWishing(boolean wishing) {
        this.wishing = wishing;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        WishNotification that = (WishNotification) o;

        if (wishing != that.wishing) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (wishing ? 1 : 0);
        return result;
    }

    @Override
    public String toString() {
        return "WishNotification{" + super.toString() +
                "wishing=" + wishing +
                '}';
    }
}