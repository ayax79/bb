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
public final class DescendingActivityThreadComparator implements Comparator<IActivityThread>, Serializable {

    private static final DescendingActivityThreadComparator instance = new DescendingActivityThreadComparator();
    private static final long serialVersionUID = 4200125112815534315L;

    private DescendingActivityThreadComparator() {
    }

    @Override
    public int compare(IActivityThread o1, IActivityThread o2) {
        int equality = 0;
        if (o1 != null && o2 != null) {
            final DateTime time1 = o1.getParent().getPostDate();
            final DateTime time2 = o2.getParent().getPostDate();
            equality = time2.compareTo(time1);
        }
        return equality;
    }

    public static DescendingActivityThreadComparator getDescendingActivityThreadComparator() {
        return instance;
    }
}