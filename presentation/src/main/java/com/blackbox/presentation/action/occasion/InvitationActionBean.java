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
public class InvitationActionBean extends BaseBlackBoxActionBean {
    @SpringBean("occasionManager")
    private IOccasionManager occasionManager;

    @Validate(required = true)
    private String guid;

    @Validate(required = true)
    private String attendeeGuid;

    public IOccasionManager getOccasionManager() {
        return occasionManager;
    }

    public void setOccasionManager(IOccasionManager occasionManager) {
        this.occasionManager = occasionManager;
    }

    public Resolution accept() {
        OccasionRequest request = new OccasionRequest();
        request.setAttendeeUserGuid(attendeeGuid);
        request.setOccasionGuid(guid);
        request.setOwnerGuid(getCurrentUser().getGuid());

        occasionManager.acceptInvitationRequest(request);
        return PresentationUtil.createResolutionWithText(getContext(), "success");
    }


    public Resolution reject() {
        OccasionRequest request = new OccasionRequest();
        request.setAttendeeUserGuid(attendeeGuid);
        request.setOccasionGuid(guid);
        request.setOwnerGuid(getCurrentUser().getGuid());
        occasionManager.updateAttendence(request);
        return PresentationUtil.createResolutionWithText(getContext(), "success");
    }


    public String getAttendeeGuid() {
        return attendeeGuid;
    }

    public void setAttendeeGuid(String attendeeGuid) {
        this.attendeeGuid = attendeeGuid;
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }
}