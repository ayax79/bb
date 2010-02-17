package com.blackbox.server.occasion;

import com.blackbox.foundation.Status;
import com.blackbox.foundation.occasion.IOccasionManager;
import com.blackbox.foundation.occasion.Occasion;
import com.blackbox.foundation.occasion.OccasionRequest;
import com.blackbox.server.BaseIntegrationTest;
import com.blackbox.server.occasion.event.Occasion7DayReminderEvent;
import com.blackbox.foundation.util.Bounds;
import org.joda.time.DateMidnight;
import org.junit.Test;
import org.yestech.event.multicaster.IEventMulticaster;

import javax.annotation.Resource;
import java.util.List;

public class OccasionCronjobIntegrationTest extends BaseIntegrationTest {

    @Resource
    protected IOccasionManager occasionManager;

   @Resource
   private IEventMulticaster eventMulticaster;

    @Test
    public void test7daysNotification() {

        DateMidnight cday6 = new DateMidnight();
        cday6.plusDays(6);

        DateMidnight cday7 = new DateMidnight();
        cday7.plusDays(7);

        OccasionRequest request = new OccasionRequest();
        request.setBounds(new Bounds(cday6.toDateTime(), cday7.toDateTime()));
        request.setStatus(Status.ENABLED);


        List<Occasion> occasions = occasionManager.loadOccasions(request);
        for (Occasion occasion : occasions) {
            eventMulticaster.process(new Occasion7DayReminderEvent(occasion));
        }
    }
}
