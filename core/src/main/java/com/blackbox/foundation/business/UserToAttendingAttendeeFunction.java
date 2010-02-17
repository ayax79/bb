package com.blackbox.foundation.business;

import com.blackbox.foundation.occasion.Attendee;
import com.blackbox.foundation.occasion.AttendeeSource;
import com.blackbox.foundation.occasion.AttendingStatus;
import com.blackbox.foundation.user.User;
import com.google.common.base.Function;
import org.joda.time.DateTime;

/**
 * @author colin@blackboxrepublic.com
 */
public class UserToAttendingAttendeeFunction implements Function<User, Attendee> {

    private DateTime created;
    private String password;

    public UserToAttendingAttendeeFunction(DateTime created, String password) {
        this.created = created;
        this.password = password;
    }

    @Override
    public Attendee apply(User user) {
        Attendee attendee = new Attendee();
        attendee.setEmail(user.getEmail());
        attendee.setBbxUserName(user.getUsername());
        attendee.setBbxUserGuid(user.getGuid());
        attendee.setAttendeeSource(AttendeeSource.BBOX_NETWORK);
        attendee.setCreated(created);
        attendee.setPassword(password);
        attendee.setAttendingStatus(AttendingStatus.ATTENDING);
        attendee.setAcknowledged(true);
        return attendee;
    }
}
