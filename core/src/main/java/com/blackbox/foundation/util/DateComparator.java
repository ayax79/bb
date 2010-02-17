package com.blackbox.foundation.util;

import org.joda.time.DateTime;

import java.util.Comparator;

/**
 *
 *
 */
public abstract class DateComparator implements Comparator<DateTime> {
    private static final DateComparator ASC = new DateComparator() {
        @Override
        public int compare(DateTime o1, DateTime o2) {
            return o1.compareTo(o2);
        }
    };
    private static final DateComparator DESC = new DateComparator() {
        @Override
        public int compare(DateTime o1, DateTime o2) {
            return o2.compareTo(o1);
        }
    };

    public static DateComparator ascending() {
        return ASC;
    }

    public static DateComparator decending() {
        return DESC;
    }
}
