package com.blackbox.foundation.activity;

import com.blackbox.foundation.EntityReference;
import com.blackbox.foundation.social.NetworkTypeEnum;
import com.blackbox.foundation.util.Bounds;
import com.blackbox.foundation.common.TwoBounds;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

/**
 * @author A.J. Wright
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class ActivityRequest implements Serializable {

    protected EntityReference owner;
    protected Collection<NetworkTypeEnum> types;
    protected Bounds bounds;
    protected TwoBounds twoBounds;

    public ActivityRequest() {
        bounds = new Bounds();
        types = new ArrayList<NetworkTypeEnum>();
    }

    public ActivityRequest(EntityReference owner, Collection<NetworkTypeEnum> types, Bounds bounds) {
        this.owner = owner;
        this.types = types;
        this.bounds = bounds;
    }

    public ActivityRequest(EntityReference owner, Collection<NetworkTypeEnum> types, TwoBounds bounds) {
        this.owner = owner;
        this.types = types;
        this.twoBounds = bounds;
    }

    public ActivityRequest(Collection<NetworkTypeEnum> types, Bounds bounds) {
        this.types = types;
        this.bounds = bounds;
    }

    public EntityReference getOwner() {
        return owner;
    }

    public void setOwner(EntityReference owner) {
        this.owner = owner;
    }

    public Collection<NetworkTypeEnum> getTypes() {
        return types;
    }

    public void setTypes(Collection<NetworkTypeEnum> types) {
        this.types = types;
    }

    public Bounds getBounds() {
        return bounds;
    }

    public TwoBounds getTwoBounds() {
        return twoBounds;
    }

    public void setBounds(Bounds bounds) {
        this.bounds = bounds;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ActivityRequest that = (ActivityRequest) o;

        if (bounds != null ? !bounds.equals(that.bounds) : that.bounds != null) return false;
        if (owner != null ? !owner.equals(that.owner) : that.owner != null) return false;
        //noinspection RedundantIfStatement
        if (types != null ? !types.equals(that.types) : that.types != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = owner != null ? owner.hashCode() : 0;
        result = 31 * result + (types != null ? types.hashCode() : 0);
        result = 31 * result + (bounds != null ? bounds.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ActivityRequest{" +
                "owner=" + owner +
                ", types=" + types +
                ", bounds=" + bounds +
                '}';
    }
}
