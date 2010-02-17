package com.blackbox.foundation.user;

import com.blackbox.foundation.BBPersistentObject;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Simple class for catching emails for people who are register.
 * The emails should be deleted if they register.
 */
@XmlRootElement
public class EmailCapture extends BBPersistentObject {

    private String email;

    public EmailCapture() {
    }

    public EmailCapture(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        EmailCapture that = (EmailCapture) o;

        //noinspection RedundantIfStatement
        if (email != null ? !email.equals(that.email) : that.email != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (email != null ? email.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "EmailCapture{" +
                "email='" + email + '\'' +
                '}';
    }
}
