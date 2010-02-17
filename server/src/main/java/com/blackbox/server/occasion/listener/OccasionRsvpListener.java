package com.blackbox.server.occasion.listener;

import com.blackbox.foundation.Utils;
import com.blackbox.foundation.occasion.*;
import com.blackbox.server.BaseBlackboxListener;
import com.blackbox.server.occasion.IOccasionDao;
import com.blackbox.server.occasion.event.OccasionRsvpEvent;
import com.blackbox.server.user.IUserDao;
import com.blackbox.server.util.PersistenceUtil;
import com.blackbox.foundation.user.User;
import static com.google.common.collect.Lists.newArrayList;
import org.yestech.event.ResultReference;
import org.yestech.event.annotation.ListenedEvents;
import org.yestech.event.annotation.AsyncListener;

import javax.annotation.Resource;
import java.util.List;

@ListenedEvents({OccasionRsvpEvent.class})
@AsyncListener
public class OccasionRsvpListener extends BaseBlackboxListener<OccasionRsvpEvent, Occasion> {

    @Resource(name = "occasionDao")
    private IOccasionDao occasionDao;

    @Resource(name = "userDao")
    private IUserDao userDao;

    public void setUserDao(IUserDao userDao) {
        this.userDao = userDao;
    }

    public void setOccasionDao(IOccasionDao occasionDao) {
        this.occasionDao = occasionDao;
    }

    /**
     * Called by the {@link org.yestech.event.multicaster.IEventMulticaster} when the Event is fired.
     *
     * @param occasionRsvpEvent Event registered
     * @param result            The result to return
     */
    @Override
    public void handle(OccasionRsvpEvent occasionRsvpEvent, ResultReference<Occasion> result) {
        OccasionRequest request = occasionRsvpEvent.getType();
        String occasionGuid = request.getOccasionGuid();
        String attendeeGuid = request.getAttendeeUserGuid();
        Attendee attendee = occasionDao.loadAttendeeByGuidAndOccasionGuid(occasionGuid, attendeeGuid);
        if (attendee != null) {
            attendee.setAttendingStatus(request.getAttendingStatus());
            attendee.setAcknowledged(true);
            occasionDao.update(attendee);
        } else {
            //check to see if occasion is open...
            Occasion occasion = occasionDao.loadByGuid(occasionGuid);
            if (OccasionType.OPEN.equals(occasion.getOccasionType())) {
                User user = userDao.loadUserByGuid(attendeeGuid);
                attendee = new Attendee();
                Utils.applyGuid(attendee);
                PersistenceUtil.setDates(attendee);
                attendee.setAttendingStatus(request.getAttendingStatus());
                attendee.setBbxUserGuid(attendeeGuid);
                attendee.setAttendeeSource(AttendeeSource.BBOX_NETWORK);
                attendee.setBbxUserName(user.getUsername());
                attendee.setEmail(user.getEmail());
                attendee.setAcknowledged(true);
                List<Occasion> occasions = attendee.getOccasions();
                if (occasions == null) {
                    occasions = newArrayList();
                    attendee.setOccasions(occasions);
                }
                occasions.add(occasion);
                List<Attendee> attendees = occasion.getAttendees();
                if (attendees == null) {
                    attendees = newArrayList();
                    occasion.setAttendees(attendees);
                }
                attendees.add(attendee);
                occasionDao.save(occasion);
            }
        }
    }
}