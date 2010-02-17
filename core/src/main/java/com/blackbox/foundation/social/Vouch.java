package com.blackbox.foundation.social;

import com.blackbox.foundation.BBPersistentObject;
import com.blackbox.foundation.EntityReference;
import com.blackbox.foundation.Utils;
import org.joda.time.DateTime;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 * Represents that one entity has vouched for another entity in the system.
 */
@XmlRootElement
public class Vouch extends BBPersistentObject {

    public static final int MAX_UNLIMITED_USER_PERMANENT_VOUCHES = 20;
    public static final int MAX_LIMITED_USER_PERMANENT_VOUCHES = 20;
    public static final int MAX_UNLIMITED_USER_TEMPORARY_VOUCHES = 20;
    public static final int MAX_LIMITED_USER_TEMPORARY_VOUCHES = 20;

    public static final int TEMPORARY_DAYS = 90;
    public static final int PERMANENT_DAYS = Integer.MAX_VALUE;

    /**
     * How many vouches it takes before a limited converts to a permanent
     */
    public static final int TEMPORARY_PERMANENT_CONVERSION_COUNT = 3;

    /**
     * The time in days that a user is allowed to revouch someone before expiration
     */
    public static final int REVOUCH_WINDOW_BEFORE_EXPIRE = 30;

    private EntityReference voucher;
    private EntityReference target;
    private DateTime expirationDate;
    private boolean accepted;

    private String description;
    private int count = 1;

    public Vouch() {
    }

    public Vouch(EntityReference voucher, EntityReference target) {
        this.voucher = voucher;
        this.target = target;
    }

    /**
     * Returns the entity who is being vouched for.
     *
     * @return the entity who is being vouched for.
     */
    public EntityReference getTarget() {
        return target;
    }

    /**
     * Sets the entity who is being vouched for.
     *
     * @param target the entity who is being vouched for.
     */
    public void setTarget(EntityReference target) {
        this.target = target;
    }

    /**
     * Returns the entity who is vouching for someone
     *
     * @return the entity who is vouching for someone
     */
    public EntityReference getVoucher() {
        return voucher;
    }

    /**
     * Sets the entity who is vouching for someone
     *
     * @param voucher the entity who is vouching for someone
     */
    public void setVoucher(EntityReference voucher) {
        this.voucher = voucher;
    }

    @XmlJavaTypeAdapter(com.blackbox.foundation.util.JodaDateTimeXmlAdapter.class)
    public DateTime getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(DateTime expirationDate) {
        this.expirationDate = expirationDate;
    }

    public boolean isAccepted() {
        return accepted;
    }

    public void setAccepted(boolean accepted) {
        this.accepted = accepted;
    }

    /**
     * Creates a new instance of Vouch with the guid populated.
     *
     * @return a new instance of Vouch
     */
    public static Vouch createVouch() {
        Vouch vouch = new Vouch();
        Utils.applyGuid(vouch);
        return vouch;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        Vouch vouch = (Vouch) o;

        if (accepted != vouch.accepted) return false;
        if (count != vouch.count) return false;
        if (expirationDate != null ? !expirationDate.equals(vouch.expirationDate) : vouch.expirationDate != null)
            return false;
        if (target != null ? !target.equals(vouch.target) : vouch.target != null) return false;
        if (voucher != null ? !voucher.equals(vouch.voucher) : vouch.voucher != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (voucher != null ? voucher.hashCode() : 0);
        result = 31 * result + (target != null ? target.hashCode() : 0);
        result = 31 * result + (expirationDate != null ? expirationDate.hashCode() : 0);
        result = 31 * result + (accepted ? 1 : 0);
        result = 31 * result + count;
        return result;
    }

    @Override
    public String toString() {
        return "Vouch{" +
                "voucher=" + voucher +
                ", target=" + target +
                ", expirationDate=" + expirationDate +
                ", accepted=" + accepted +
                ", count=" + count +
                '}';
    }


    public static boolean isPermanent(Vouch v) {
        return v.expirationDate != null && v.expirationDate.isAfter(new DateTime().plusYears(50));
    }
}
