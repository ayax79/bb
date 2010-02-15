/*
 *
 * Author:  Artie Copeland
 * Last Modified Date: $DateTime: $
 */
package com.blackbox.activity;

import org.joda.time.DateTime;

import java.io.Serializable;
import java.util.Comparator;

/**
 * Provides a reverse sort so that is the second value is newer it is place at the top.
 *
 * @author Artie Copeland
 * @version $Revision: $
 */
public final class DescendingActivityAndGuidComparator implements Comparator<IActivity>, Serializable {

    private static final DescendingActivityAndGuidComparator instance = new DescendingActivityAndGuidComparator();
    private static final long serialVersionUID = -4916320623394386554L;

    private DescendingActivityAndGuidComparator() {

    }

    @Override
    public int compare(IActivity o1, IActivity o2) {
        int equality = 0;
        if (o1 != null && o2 != null) {
            final DateTime time1 = o1.getPostDate();
            final DateTime time2 = o2.getPostDate();
            equality = time2.compareTo(time1);
            if (equality == 0) {
                final String guid1 = o1.getGuid();
                final String guid2 = o2.getGuid();
                equality = guid2.compareTo(guid1);
            }
        }
        return equality;
    }

    public static DescendingActivityAndGuidComparator getDescendingActivityAndGuidComparator() {
        return instance;
    }
}