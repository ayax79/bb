package com.blackbox.presentation.action.occasion;

import com.blackbox.occasion.AttendingStatus;
import com.blackbox.occasion.IOccasionManager;
import com.blackbox.occasion.OccasionRequest;
import com.blackbox.presentation.action.BaseBlackBoxActionBean;
import com.blackbox.presentation.action.util.PresentationUtil;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.integration.spring.SpringBean;
import net.sourceforge.stripes.validation.Validate;

/**
 *
 *
 */
public class RsvpActionBean extends BaseBlackBoxActionBean {
    @SpringBean("occasionManager")
    private IOccasionManager occasionManager;

    @Validate(required = true, on = {"attend"})
    private AttendingStatus status;

    @Validate(required = true)
    private String guid;

    public IOccasionManager getOccasionManager() {
        return occasionManager;
    }

    public void setOccasionManager(IOccasionManager occasionManager) {
        this.occasionManager = occasionManager;
    }

    public Resolution attend() {
        OccasionRequest request = new OccasionRequest();
        request.setAttendeeUserGuid(getCurrentUser().getGuid());
        request.setAttendingStatus(status);        
        request.setOccasionGuid(guid);
        occasionManager.updateAttendence(request);
        return PresentationUtil.createResolutionWithText(getContext(), "success");
    }


    public AttendingStatus getStatus() {
        return status;
    }

    public void setStatus(AttendingStatus status) {
        this.status = status;
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }
}
