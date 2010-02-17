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
public class AscendingActivityAndGuidComparator implements Comparator<IActivity>, Serializable {

    private static final AscendingActivityAndGuidComparator instance = new AscendingActivityAndGuidComparator();
    private static final long serialVersionUID = -7274140423338940466L;

    private AscendingActivityAndGuidComparator() {
    }

    @Override
    public int compare(IActivity o1, IActivity o2) {
        int equality = 0;
        if (o1 != null && o2 != null) {
            final DateTime time1 = o1.getPostDate();
            final DateTime time2 = o2.getPostDate();
            equality = time1.compareTo(time2);
            if (equality == 0) {
                final String guid1 = o1.getGuid();
                final String guid2 = o2.getGuid();
                equality = guid1.compareTo(guid2);
            }
        }
        return equality;
    }

    public static AscendingActivityAndGuidComparator getAscendingActivityComparator() {
        return instance;
    }
}