/*
 *
 * Author:  Artie Copeland
 * Last Modified Date: $DateTime: $
 */
package com.blackbox.foundation.activity;

import org.joda.time.DateTime;
import org.yestech.lib.util.Pair;

import java.io.Serializable;
import java.util.Comparator;

/**
 * Provides a reverse sort so that is the second value is newer it is place at the top.
 *
 * @author Artie Copeland
 * @version $Revision: $
 */
public final class DescendingActivityPairComparator implements Comparator<Pair<String, DateTime>>, Serializable {

    private static final DescendingActivityPairComparator instance = new DescendingActivityPairComparator();
    private static final long serialVersionUID = 3317305293555337949L;

    private DescendingActivityPairComparator() {
    }

    @Override
    public int compare(Pair<String, DateTime> o1, Pair<String, DateTime> o2) {
        int equality = 0;
        if (o1 != null && o2 != null) {
            final DateTime time1 = o1.getSecond();
            final DateTime time2 = o2.getSecond();
            if (time1 != null && time2 != null) {
                equality = time2.compareTo(time1);
                if (equality == 0) {
                    equality = checkKey(o1, o2);
                }
            } else {
                //check keys to make sure same object
                equality = checkKey(o1, o2);
            }
        }
        return equality;
    }

    private int checkKey(Pair<String, DateTime> o1, Pair<String, DateTime> o2) {
        int equality;//check keys to make sure same object
        String key1 = o1.getFirst();
        String key2 = o2.getFirst();
        equality = key2.compareTo(key1);
        return equality;
    }

    public static DescendingActivityPairComparator getDescendingActivityPairComparator() {
        return instance;
    }
}