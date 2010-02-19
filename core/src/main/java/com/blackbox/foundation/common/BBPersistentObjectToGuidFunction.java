package com.blackbox.foundation.common;

import com.blackbox.foundation.BBPersistentObject;
import com.google.common.base.Function;

/**
 * @author colin@blackboxrepublic.com
 */
public class BBPersistentObjectToGuidFunction<T extends BBPersistentObject> implements Function<T, String> {

    @Override
    public String apply(T from) {
        return from.getGuid();
    }
}