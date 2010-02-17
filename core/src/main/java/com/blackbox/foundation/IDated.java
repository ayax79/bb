/*
 * Copyright (c) 2009. Blackbox, LLC
 * All rights reserved.
 */

package com.blackbox.foundation;

import org.joda.time.DateTime;

/**
 * Defines whether the object that implements this object marks whether it has the date it was created and keeps track
 * of dates it was updated.
 */
public interface IDated {

    /**
     * Sets the DateTime that this object was created.
     * <p/>
     * This method should only be called by the system, it should not be manually set.
     *
     * @param created the DateTime that this object was created.
     */
    void setCreated(DateTime created);

    /**
     * Returns the date this obj
     *
     * @return
     */
    DateTime getCreated();

    /**
     * Sets the DateTime that this object was last modified.
     * <p/>
     * This method should only be called by the system, it should not be manually set.
     *
     * @param modified the DateTime that this object was last modified.
     */
    void setModified(DateTime modified);

    /**
     * Returns the DateTime that this object was last modified.
     *
     * @return the DateTime that this object was last modified.
     */
    DateTime getModified();


}
