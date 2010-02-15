package com.blackbox.social;

/**
 * @author A.J. Wright
 */
public class VouchException extends RuntimeException {

    public VouchException() {
    }

    public VouchException(String message) {
        super(message);
    }

    public VouchException(String message, Throwable cause) {
        super(message, cause);
    }

    public VouchException(Throwable cause) {
        super(cause);
    }
}
