package com.blackbox.server.util;

import com.blackbox.foundation.occasion.Occasion;
import com.blackbox.foundation.occasion.Attendee;

import java.util.List;

/**
 *
 *
 */
public class OccasionUtil {
    public static void cleanAttendee(Occasion occasion) {
        List<Attendee> attendees = occasion.getAttendees();
        if (attendees != null) {
            for (Attendee attendee : attendees) {
                attendee.setOccasions(null);
            }
        }
    }

    public static void cleanAttendeeOcassion(Occasion occasion) {
        if (occasion != null && occasion.getAttendees() != null) {
            for (Attendee attendee : occasion.getAttendees()) {
                attendee.setOccasions(null);
            }
        }
    }



}
