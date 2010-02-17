package com.blackbox.foundation.occasion;

import java.util.Comparator;

/**
 *
 *
 */
public abstract class OccasionComparator implements Comparator<Occasion> {
    private static final OccasionComparator EVENT_TIME_DESC = new OccasionComparator() {
        @Override
        public int compare(Occasion o1, Occasion o2) {
            return o2.getEventTime().compareTo(o1.getEventTime());
        }
    };

    public static OccasionComparator eventTimeDescending() {
        return EVENT_TIME_DESC;
    }
}
