package com.blackbox.foundation.security;

import org.joda.time.DateTime;

import java.io.Serializable;

/**
 * Resents any type of assest that need to have security wrapped on it.
 */
public interface IAsset extends Serializable {
    public DateTime getCreated();
}
