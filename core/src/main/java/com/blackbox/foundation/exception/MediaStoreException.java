/*
 * Copyright (c) 2009. Blackbox, LLC
 * All rights reserved.
 */

package com.blackbox.foundation.exception;

/**
 * Thrown if there is problem storing media.
 *
 * @author A.J. Wright
 */
public class MediaStoreException extends BlackBoxException {

    public MediaStoreException() {
    }

    public MediaStoreException(Throwable cause) {
        super(cause);
    }

    public MediaStoreException(String message) {
        super(message);
    }

    public MediaStoreException(String message, Throwable cause) {
        super(message, cause);
    }
}
