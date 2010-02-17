package com.blackbox.foundation.util;

import com.blackbox.foundation.exception.BlackBoxException;

public class AffirmationException extends BlackBoxException {

    private static final long serialVersionUID = 1L;

    public AffirmationException() {
        super();
    }

    public AffirmationException(String message) {
        super(message);
    }

    public AffirmationException(Throwable cause) {
        super(cause);
    }

    public AffirmationException(String message, Throwable cause) {
        super(message, cause);
    }

}