package com.blackbox.server.user.event;

import com.blackbox.user.User;
import org.yestech.event.event.IEvent;

/**
 *
 *
 */
public class ForgotPasswordEvent implements IEvent {

    private final User user;
    private final String temporaryPassword;
    private static final long serialVersionUID = 2945801584024208823L;

    public ForgotPasswordEvent(User user, String temporaryPassword) {
        this.user = user;
        this.temporaryPassword = temporaryPassword;
    }

    @Override
    public String getEventName() {
        return "ForgotPassword";
    }

    public User getUser() {
        return user;
    }

    public String getTemporaryPassword() {
        return temporaryPassword;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ForgotPasswordEvent that = (ForgotPasswordEvent) o;

        if (temporaryPassword != null ? !temporaryPassword.equals(that.temporaryPassword) : that.temporaryPassword != null)
            return false;
        //noinspection RedundantIfStatement
        if (user != null ? !user.equals(that.user) : that.user != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = user != null ? user.hashCode() : 0;
        result = 31 * result + (temporaryPassword != null ? temporaryPassword.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ForgotPasswordEvent{" +
                "user=" + user +
                ", temporaryPassword='" + temporaryPassword + '\'' +
                '}';
    }
}
