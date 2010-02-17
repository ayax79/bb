package com.blackbox.foundation.social;

import com.blackbox.foundation.BBPersistentObject;
import com.blackbox.foundation.EntityReference;
import com.blackbox.foundation.Utils;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Represents that one entity has ignored another entity in the system.
 */
@XmlRootElement
public class Ignore extends BBPersistentObject {

    /**
     * Different types of ignoring supports, see the actual
     * enum values to see
     */
    public static enum IgnoreType {
        /**
         * Soft ignores are for just ignoring messages in the activity from another user. This could be someone you have
         * friended, but find them ignoring.
         */
        SOFT,
        /**
         * This actually blocks everything from the user, makes the user completey ignored, they can't contact you, etc.
         * This is the "Donkey kick" functionality.
         */
        HARD
    }

    private EntityReference ignorer;
    private EntityReference target;
    private IgnoreType type = IgnoreType.SOFT;

    public Ignore() {
    }

    public Ignore(EntityReference ignorer, EntityReference target) {
        this.ignorer = ignorer;
        this.target = target;
    }

    public Ignore(EntityReference ignorer, EntityReference target, IgnoreType type) {
        this.ignorer = ignorer;
        this.target = target;
        this.type = type;
    }

    /**
     * Returns the type ignore.
     *
     * @return the type ignore.
     */
    public IgnoreType getType() {
        return type;
    }

    /**
     * Sets the type ignore.
     *
     * @param type the type ignore.
     */
    public void setType(IgnoreType type) {
        this.type = type;
    }

    /**
     * Returns the entity who is being ignored for.
     *
     * @return the entity who is being ignored for.
     */
    public EntityReference getTarget() {
        return target;
    }

    /**
     * Sets the entity who is being ignored for.
     *
     * @param target the entity who is being ignored for.
     */
    public void setTarget(EntityReference target) {
        this.target = target;
    }

    /**
     * Returns the entity who is ignoreing for someone
     *
     * @return the entity who is ignoreing for someone
     */
    public EntityReference getIgnorer() {
        return ignorer;
    }

    /**
     * Sets the entity who is ignoreing for someone
     *
     * @param ignorer the entity who is ignoreing for someone
     */
    public void setIgnorer(EntityReference ignorer) {
        this.ignorer = ignorer;
    }

    /**
     * Creates a new instance of Ignore with the guid populated.
     *
     * @return a new instance of Ignore
     */
    public static Ignore createIgnore() {
        Ignore ignore = new Ignore();
        Utils.applyGuid(ignore);
        return ignore;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Ignore)) return false;
        if (!super.equals(o)) return false;

        Ignore ignore = (Ignore) o;

        if (ignorer != null ? !ignorer.equals(ignore.ignorer) : ignore.ignorer != null) return false;
        if (target != null ? !target.equals(ignore.target) : ignore.target != null) return false;
        //noinspection RedundantIfStatement
        if (type != ignore.type) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (ignorer != null ? ignorer.hashCode() : 0);
        result = 31 * result + (target != null ? target.hashCode() : 0);
        result = 31 * result + (type != null ? type.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Ignore{" +
                "ignorer=" + ignorer +
                ", target=" + target +
                ", type=" + type +
                "} " + super.toString();
    }

}