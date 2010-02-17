package com.blackbox.server.occasion.listener;

import com.blackbox.foundation.occasion.Attendee;
import com.blackbox.foundation.occasion.Occasion;
import com.blackbox.foundation.occasion.OccasionType;
import com.blackbox.server.occasion.event.CreateOccasionEvent;
import com.blackbox.server.occasion.event.OccasionEvent;
import com.blackbox.server.occasion.event.UpdateOccasionEvent;
import com.blackbox.server.system.email.EmailDefinition;
import com.blackbox.server.system.email.SimpleEmailDelivery;
import org.yestech.event.ResultReference;
import org.yestech.event.annotation.AsyncListener;
import org.yestech.event.annotation.ListenedEvents;

import javax.annotation.Resource;
import java.util.List;

@ListenedEvents({CreateOccasionEvent.class, UpdateOccasionEvent.class})
@AsyncListener
public class OccasionSendInvEmailListener extends OccasionAbstractEmailListener {

    @Resource
    SimpleEmailDelivery emailDelivery;


    private String password = "You may need a password to open or Rsvp this event page.";

    @Override
    public void handle(OccasionEvent event, ResultReference<Occasion> reference) {

        final Occasion occasion = event.getOccasion();
        List<Attendee> attendees = occasion.getAttendees();
        for (final Attendee attendee : attendees) {
            String passwordPhr = "";
            if (occasion.getOccasionType() != OccasionType.OPEN) {
                passwordPhr = password + attendee.getPassword();
            }

            final String passwordPhr1 = passwordPhr;
            emailDelivery.send(new EmailDefinition() {
                {
                    withRecipient(attendee.getEmail(), attendee.getEmail());
                    withSubject("You are invited to a blackbox event");
                    withTemplateVariable("occasion_name", occasion.getName());
                    withTemplateVariable("occasion_url", getOccasionUrl(occasion));
                    withTemplateVariable("password_phrase", passwordPhr1);
                    withTextTemplate("/velocity/occasionInvitation-text.vm");
                    withHtmlTemplate("/velocity/occasionInvitation-html.vm");
                }
            });
        }
    }

}