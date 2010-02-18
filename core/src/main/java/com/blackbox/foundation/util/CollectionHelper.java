package com.blackbox.foundation.util;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;

import java.util.Collection;

/**
 * @author colin@blackboxrepublic.com
 */
public final class CollectionHelper {

    /**
     * @return the number of hits the predicate has on the parameter collection
     */
    public static <E> int countMatches(Collection<E> unfiltered, Predicate<? super E> predicate) {
        return Collections2.filter(unfiltered, predicate).size();
    }

    /**
     * @return true of the predicate hits on at least one collection item
     */
    public static <E> boolean exists(Collection<E> unfiltered, Predicate<? super E> predicate) {
        return countMatches(unfiltered, predicate) > 0;
    }

    /**
     * @return the only value in the parameter collection or throws IllegalArgumentException
     */
    public static <E> E guaranteeOne(Collection<E> values) {
        Affirm.isNotNull(values, "values", IllegalArgumentException.class);
        Affirm.that(values.size() == 1, "values size is not one: " + values.size(), IllegalArgumentException.class);
        return values.iterator().next();
    }

    /**
     * @param one a collection
     * @param two another collection
     * @return the size of either/both collection
     * @throws IllegalArgumentException should the parameters not be same size
     */
    public static <E> int sameSize(Collection<E> one, Collection<E> two) {
        Affirm.that(one.size() == two.size(), "Both collections must be same size", IllegalArgumentException.class);
        return one.size();
    }

}
