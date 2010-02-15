/*
 * Copyright (c) 2009. Blackbox, LLC
 * All rights reserved.
 */

package com.blackbox;

import com.blackbox.security.IAsset;
import org.compass.annotations.SearchableId;
import org.compass.annotations.SearchableProperty;
import org.joda.time.DateTime;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.io.Serializable;

/**
 * Base object that all blackbox objects that are persisted to hibernate most implement.
 */
public abstract class BBPersistentObject implements Serializable, IDated, IAsset {

    protected Long version;
    @SearchableProperty
    protected DateTime created;
    @SearchableProperty
    protected DateTime modified;

    @SearchableId(name = "guid")
    protected String guid;

    /**
     * Returns the generated unique identifier. All blackbox objects most have a completely unique guid.
     * <p/>
     * There are two ways this can be specified: <br />
     * <ul>
     * <li>By the caller when the object is created via a static factory method of that
     * object. This method allows the caller to know the identifier immediately.</li>
     * <li>Allows the system to generate the id itself</li>
     * </ul>
     *
     * @return the generated unique identifier.
     */
    public String getGuid() {
        return guid;
    }

    /**
     * Sets the generated unique identifier.
     *
     * @param guid the generated unique identifier.
     */
    public void setGuid(String guid) {
        this.guid = guid;
    }

    /**
     * Returns the current version of this object. This is primary used by hibernate for keeping track if the object is dirty or not.
     *
     * @return Gets the current version of this object.
     */
    public Long getVersion() {
        return version;
    }

    /**
     * Sets current version of this object.
     * <p/>
     * This method should only be called by hibernate.
     * This is primary used by hibernate for keeping track if the object is dirty or not.
     */
    public void setVersion(Long version) {
        this.version = version;
    }

    @XmlJavaTypeAdapter(com.blackbox.util.JodaDateTimeXmlAdapter.class)
    @Override
    public DateTime getCreated() {
        return created;
    }

    @Override
    public void setCreated(DateTime created) {
        this.created = created;
    }

    @XmlJavaTypeAdapter(com.blackbox.util.JodaDateTimeXmlAdapter.class)
    @Override
    public DateTime getModified() {
        return modified;
    }

    public void setModified(DateTime modified) {
        this.modified = modified;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BBPersistentObject)) return false;

        BBPersistentObject that = (BBPersistentObject) o;

        return !(guid != null ? !guid.equals(that.guid) : that.guid != null);
    }

    @Override
    public int hashCode() {
        return guid == null ? 0 : guid.hashCode();
    }

    @Override
    public String toString() {
        return "BBPersistentObject{" +
                "created=" + created +
                ", version=" + version +
                ", modified=" + modified +
                ", guid='" + guid + '\'' +
                '}';
    }
}
