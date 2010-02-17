package com.blackbox.foundation.util;

import com.blackbox.foundation.util.Affirm;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;

import java.util.Collection;

/**
 * @author colin@blackboxrepublic.com
 */
public final class CollectionUtils {

    public static <E> boolean exists(Collection<E> unfiltered, Predicate<? super E> predicate) {
        return !Collections2.filter(unfiltered, predicate).isEmpty();
    }

    public static <E> E guaranteeOne(Collection<E> values) {
        Affirm.isNotNull(values, "values", IllegalArgumentException.class);
        Affirm.that(values.size() == 1, "values size is not one: " + values.size(), IllegalArgumentException.class);
        return values.iterator().next();
    }
}
