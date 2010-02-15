package com.blackbox.exception;

/**
 *
 *
 */
public class UnknownUserException extends BlackBoxException {
    public UnknownUserException() {
    }

    public UnknownUserException(Throwable cause) {
        super(cause);
    }

    public UnknownUserException(String message) {
        super(message);
    }

    public UnknownUserException(String message, Throwable cause) {
        super(message, cause);
    }
}
