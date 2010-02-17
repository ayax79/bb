package com.blackbox.presentation.action.psevent;

import com.blackbox.foundation.occasion.Attendee;
import com.blackbox.foundation.occasion.IOccasionManager;
import com.blackbox.foundation.occasion.Occasion;
import com.blackbox.presentation.action.BaseBlackBoxActionBean;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.integration.spring.SpringBean;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

public class PSEventSendEmailActionBean extends BaseBlackBoxActionBean {

    final private static Logger _logger = LoggerFactory.getLogger(PSEventSendEmailActionBean.class);
    @SpringBean("occasionManager")
    private IOccasionManager occasionManager;
    
    @DefaultHandler
    public Resolution begin() {
        _logger.info("calling begin");
        Occasion occasion = new Occasion();
        occasion.setName("a test");
        occasion.setEventUrl("http://localhost");
        Attendee at = new Attendee();
        at.setEmail("jack@redpillsystems.com");
        ArrayList<Attendee> atList = new ArrayList<Attendee>();
        atList.add(at);
        occasion.setAttendees(atList);
        occasion.setPostDate(new DateTime());
        occasion.setEventTime(occasion.getPostDate().plusMonths(1));
        
        occasionManager.createOccasion(occasion);
        return new ForwardResolution("/WEB-INF/jsp/include/ps_event/test_eventemail.jsp");
    }

    public void setOccasionManager(IOccasionManager occasionManager) {
        this.occasionManager = occasionManager;
    }

    @Override
    public boolean isHasIntro() {
        return false;
    }
}
