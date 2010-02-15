package com.blackbox.user;

import com.blackbox.BBPersistentObject;

import java.util.Collection;

/**
 * @author A.J. Wright
 */
public class AffiliateMapping extends BBPersistentObject {

    private User affiliate;
    private Collection<User> users;

    public User getAffiliate() {
        return affiliate;
    }

    public void setAffiliate(User affiliate) {
        this.affiliate = affiliate;
    }

    public Collection<User> getUsers() {
        return users;
    }

    public void setUsers(Collection<User> users) {
        this.users = users;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        AffiliateMapping that = (AffiliateMapping) o;

        if (affiliate != null ? !affiliate.equals(that.affiliate) : that.affiliate != null) return false;
        //noinspection RedundantIfStatement
        if (users != null ? !users.equals(that.users) : that.users != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (affiliate != null ? affiliate.hashCode() : 0);
        result = 31 * result + (users != null ? users.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "AffliateMapping{" +
                "affiliate=" + affiliate +
                ", users=" + users +
                '}';
    }
}
