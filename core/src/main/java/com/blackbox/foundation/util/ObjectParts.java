package com.blackbox.foundation.util;

import org.apache.commons.lang.StringUtils;

/**
 * A way to keep around some parts for something to later, possibly get them all used up somewhere.
 */
public class ObjectParts {

    private static final Object[] EMPTY_ARRAY = new Object[0];

    private Object[] parts;

    public ObjectParts(Object... parts) {
        this.parts = parts;
    }

    public static Object[] getParts(ObjectParts objectParts) {
        return objectParts == null ? EMPTY_ARRAY : objectParts.getParts();
    }

    @Override
    public String toString() {
        return StringUtils.join(parts);
    }

    public Object[] getParts() {
        return parts;
    }
}