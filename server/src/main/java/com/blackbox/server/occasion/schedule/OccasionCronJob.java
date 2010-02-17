/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.blackbox.server.occasion.schedule;

import com.blackbox.foundation.occasion.IOccasionManager;
import com.blackbox.foundation.occasion.Occasion;
import com.blackbox.foundation.occasion.OccasionRequest;
import com.blackbox.server.occasion.event.Occasion1DayReminderEvent;
import com.blackbox.server.occasion.event.Occasion7DayReminderEvent;
import com.blackbox.server.occasion.event.OccasionRsvpListEvent;
import com.blackbox.foundation.util.Bounds;
import org.joda.time.DateMidnight;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.yestech.event.multicaster.IEventMulticaster;

import javax.annotation.Resource;
import java.util.List;

@Component("occasionCronJob")
public class OccasionCronJob {
    final private static Logger _logger = LoggerFactory.getLogger(OccasionCronJob.class);

    @Resource(name = "occasionManager")
    private IOccasionManager occasionManager;

    @Resource(name = "eventMulticaster")
    private IEventMulticaster eventMulticaster;

    public void doIt() {
        send7DaysNonResponseReminder();
        send1DayRsvpReminder();
    }

    @SuppressWarnings({"unchecked"})
    public void send7DaysNonResponseReminder() {
        _logger.info("calling send7DaysNonResponseReminder");
        
        DateMidnight cday6 = new DateMidnight().plusDays(6);
        DateMidnight cday7 = new DateMidnight().plusDays(7);

        OccasionRequest request = new OccasionRequest();
        request.setBounds(new Bounds(cday6.toDateTime(), cday7.toDateTime()));

        List<Occasion> occasions = occasionManager.loadOccasions(request);
        for (Occasion occasion : occasions) {
            getEventMulticaster().process(new Occasion7DayReminderEvent(occasion));
        }

    }

    @SuppressWarnings({"unchecked"})
    public void send1DayRsvpReminder() {

        DateMidnight ctom = new DateMidnight().plusDays(1);
        DateMidnight cnextDay = new DateMidnight().plusDays(2);

        OccasionRequest request = new OccasionRequest();
        request.setBounds(new Bounds(ctom.toDateTime(), cnextDay.toDateTime()));

        List<Occasion> occasions = occasionManager.loadOccasions(request);
        for (Occasion occasion : occasions) {
            getEventMulticaster().process(new Occasion1DayReminderEvent(occasion));
        }
    }

    @SuppressWarnings({"unchecked"})
    public void sendRsvpList() {
        
        DateMidnight ctom = new DateMidnight().plusDays(1);
        DateMidnight cnextDay = new DateMidnight().plusDays(5);
        OccasionRequest request = new OccasionRequest();
        request.setBounds(new Bounds(ctom.toDateTime(), cnextDay.toDateTime()));

        List<Occasion> occasions = occasionManager.loadOccasions(request);
        for (Occasion occasion : occasions) {
            getEventMulticaster().process(new OccasionRsvpListEvent(occasion));
        }
    }

    public void setOccasionManager(IOccasionManager occasionManager) {
        this.occasionManager = occasionManager;
    }

    public void setEventMulticaster(IEventMulticaster eventMulticaster) {
        this.eventMulticaster = eventMulticaster;
    }

    public IEventMulticaster getEventMulticaster() {
        return eventMulticaster;
    }

}
