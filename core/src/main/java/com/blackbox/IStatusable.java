package com.blackbox;


/**
 * Represents that the implementer can have a status.
 *
 * @see com.blackbox.Status
 */
public interface IStatusable extends IDated {

    /**
     * Returns the status for the given object.
     *
     * @return the status for the given object.
     */
    Status getStatus();

    /**
     * Sets the status for the given object.
     *
     * @param status the status for the given object.
     */
    void setStatus(Status status);
}
