/*
 *
 * Author:  Artie Copeland
 * Last Modified Date: $DateTime: $
 */
package com.blackbox.foundation.activity;

import org.joda.time.DateTime;

import java.io.Serializable;
import java.util.Comparator;

/**
 * Provides a reverse sort so that is the second value is newer it is place at the top.
 *
 * @author Artie Copeland
 * @version $Revision: $
 */
public final class DescendingActivityComparator implements Comparator<IActivity>, Serializable {

    private static final DescendingActivityComparator instance = new DescendingActivityComparator();
    private static final long serialVersionUID = -4916320623394386554L;

    private DescendingActivityComparator() {

    }

    @Override
    public int compare(IActivity o1, IActivity o2) {
        int equality = 0;
        if (o1 != null && o2 != null) {
            final DateTime time1 = o1.getPostDate();
            final DateTime time2 = o2.getPostDate();
            equality = time2.compareTo(time1);
        }
        return equality;
    }

    public static DescendingActivityComparator getDescendingActivityComparator() {
        return instance;
    }
}
