/*
 *
 * Author:  Artie Copeland
 * Last Modified Date: $DateTime: $
 */
package com.blackbox.exception;

/**
 * Root for all BlackBox specific exceptions
 *
 * @author Artie Copeland
 * @version $Revision: $
 */
public class BlackBoxException extends RuntimeException {

    public BlackBoxException() {
    }

    public BlackBoxException(String message) {
        super(message);
    }

    public BlackBoxException(String message, Throwable cause) {
        super(message, cause);
    }

    public BlackBoxException(Throwable cause) {
        super(cause);
    }
}
