package com.blackbox.presentation.action.activity;

import com.blackbox.Status;
import com.blackbox.social.ISocialManager;
import com.blackbox.notification.Notifications;
import com.blackbox.occasion.IOccasionManager;
import com.blackbox.occasion.Occasion;
import com.blackbox.occasion.OccasionRequest;
import com.blackbox.util.Bounds;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.integration.spring.SpringBean;

import java.util.List;

/**
 * @author A.J. Wright
 */

/**
 *  NOTE FROM RHAYES:
 *  This *should* return private results if the viewer is the creator, or an invitee but it currently doesn't
 *
 */
public class SceneActivityActionBean extends ActivityActionBean {


    @SpringBean("socialManager")
    private ISocialManager socialManager;
	
    @SpringBean(value = "occasionManager")
    protected IOccasionManager occasionManager;
    protected List<Occasion> occasions;
	private Notifications notifications;

    @Override
    protected Resolution handle() {
		notifications = socialManager.loadNotifications(getCurrentUser().getGuid());
        OccasionRequest request = new OccasionRequest();
        request.setBounds(new Bounds(0, 5));
        request.setStatus(Status.ENABLED);
        occasions = occasionManager.loadOccasionsForScene(request);
        return new ForwardResolution("/ajax/dash/scene.jspf");
    }

    public void setOccasionManager(IOccasionManager occasionManager) {
        this.occasionManager = occasionManager;
    }

    public List<Occasion> getOccasions() {
        return occasions;
    }

	public Notifications getNotifications() {
		return notifications;
	}

	public void setNotifications(Notifications notifications) {
		this.notifications = notifications;
	}

	public void setSocialManager(ISocialManager socialManager) {
		this.socialManager = socialManager;
	}

	@Override
    public boolean isHasIntro() {
        return false;
    }
}
