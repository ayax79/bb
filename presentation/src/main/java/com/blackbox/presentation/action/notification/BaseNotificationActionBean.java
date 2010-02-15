package com.blackbox.presentation.action.notification;

import com.blackbox.bookmark.Bookmark;
import com.blackbox.notification.Notification;
import com.blackbox.notification.NotificationGroup;
import com.blackbox.notification.Notifications;
import com.blackbox.presentation.action.persona.BasePersonaActionBean;
import com.blackbox.presentation.action.util.CommaStringConverter;
import com.blackbox.social.Relationship;
import com.blackbox.social.Vouch;
import com.blackbox.user.IUserManager;
import com.blackbox.user.User;
import com.blackbox.media.IMediaManager;
import com.blackbox.gifting.IGiftingManager;
import com.blackbox.occasion.OccasionRequest;
import com.blackbox.occasion.IOccasionManager;
import com.blackbox.occasion.AttendingStatus;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.integration.spring.SpringBean;
import net.sourceforge.stripes.validation.Validate;

import javax.naming.AuthenticationException;

/**
 *
 *
 */
public class BaseNotificationActionBean extends BasePersonaActionBean {

    public static enum ViewAllType {
        friend,
        follow,
        wish,
        gift,
        vouch,
        occasion,
        occasions
    }

    @SpringBean("userManager")
    IUserManager userManager;

    @SpringBean("mediaManager")
    IMediaManager mediaManager;

    @SpringBean("giftingManager")
    IGiftingManager giftingManager;

    @SpringBean("occasionManager")
    private IOccasionManager occasionManager;

    protected Notifications notifications;

    protected Notification notification;

    protected NotificationGroup group;

    @Validate(converter = CommaStringConverter.class)
    protected String[] ids;

    @Validate(converter = CommaStringConverter.class)
    protected String[] entityIds;

	protected ViewAllType viewAllType;

    public NotificationGroup getGroup() {
        return group;
    }

    public void setGroup(NotificationGroup group) {
        this.group = group;
    }

    public Notification getNotification() {
        return notification;
    }

    public void setNotification(Notification notification) {
        this.notification = notification;
    }

    public Notifications getNotifications() {
        return notifications;
    }

    public String[] getIds() {
        return ids;
    }

    public void setIds(String[] ids) {
        this.ids = ids;
    }

    public String[] getEntityIds() {
        return entityIds;
    }

    public void setEntityIds(String[] entityIds) {
        this.entityIds = entityIds;
    }

    public Resolution viewAll() {
        //load notifications....
        notifications = socialManager.loadNotifications(getCurrentUser().getGuid());
        return new ForwardResolution("/notifications.jsp");
    }

    protected void processFollowingAcknowledge() {
        for (String entityGuid : entityIds) {
            User targetUser = loadUser(entityGuid);
            final Relationship existingRelationship = socialManager.loadRelationshipByEntities(targetUser.getGuid(), getCurrentUser().getGuid());
            existingRelationship.setAcknowledged(true);
            socialManager.relate(existingRelationship);
        }
        group = socialManager.loadNewestNotificationWithLimit(getCurrentUser().getGuid(), Notification.Type.Follow, 2);
    }

    protected void processWishAcknowledgement() {
        for (String guid : ids) {
            final Bookmark bookmark = bookmarkManager.loadBookmark(guid);
            bookmark.setAcknowledged(true);
            bookmarkManager.createBookmark(bookmark);
        }
        group = socialManager.loadNewestNotificationWithLimit(getCurrentUser().getGuid(), Notification.Type.Wish, 2);
    }

    protected void processWishReject() {
        for (String id : ids) {
            Bookmark bookmark = bookmarkManager.loadBookmark(id);
            if (bookmark != null && bookmark.getTarget().getGuid().equals(getCurrentUser().getGuid())) {
                bookmarkManager.deleteBookmark(id);
            }
        }
    }

    protected void processOccasionRequest(AttendingStatus status) {
        for (int i = 0; i < ids.length; i++) {
            String occasionGuid = ids[i];
            String attendeeGuid = entityIds[i];
            OccasionRequest request = new OccasionRequest();
            request.setAttendeeUserGuid(getCurrentUser().getGuid());
            request.setAttendingStatus(status);
            request.setOccasionGuid(occasionGuid);
            occasionManager.updateAttendence(request);
        }
        group = socialManager.loadNewestNotificationWithLimit(getCurrentUser().getGuid(), Notification.Type.Occasion, 2);
    }

    protected void processAcceptInvitationRequest() {
        for (int i = 0; i < ids.length; i++) {
            String occasionGuid = ids[i];
            String attendeeGuid = entityIds[i];
            OccasionRequest request = new OccasionRequest();
            request.setAttendeeUserGuid(attendeeGuid);
            request.setOccasionGuid(occasionGuid);
            request.setOwnerGuid(getCurrentUser().getGuid());
            occasionManager.acceptInvitationRequest(request);
        }
        group = socialManager.loadNewestNotificationWithLimit(getCurrentUser().getGuid(), Notification.Type.Occasion, 2);
    }

    protected void processRejectInvitationRequest() {
        for (int i = 0; i < ids.length; i++) {
            String occasionGuid = ids[i];
            String attendeeGuid = entityIds[i];
            OccasionRequest request = new OccasionRequest();
            request.setAttendeeUserGuid(attendeeGuid);
            request.setOccasionGuid(occasionGuid);
            request.setOwnerGuid(getCurrentUser().getGuid());
            occasionManager.updateAttendence(request);
        }
        group = socialManager.loadNewestNotificationWithLimit(getCurrentUser().getGuid(), Notification.Type.Occasion, 2);
    }

    protected void processVirtualGiftAccept() {
        for (String id : ids) {
            giftingManager.acceptVirtualGift(id);
        }
        group = socialManager.loadNewestNotificationWithLimit(getCurrentUser().getGuid(), Notification.Type.Gift, 2);
    }

    protected void processVirtualGiftReject() {
        for (String id : ids) {
            giftingManager.rejectVirtualGift(id);
        }
        group = socialManager.loadNewestNotificationWithLimit(getCurrentUser().getGuid(), Notification.Type.Gift, 2);
    }

    protected void processVouchAccept() throws AuthenticationException {
        for (String id : ids) {
            Vouch vouch = socialManager.loadVouchByGuid(id);
            if (vouch.getTarget().getGuid().equals(getCurrentUser().getGuid())) {
                vouch.setAccepted(true);
                socialManager.saveVouch(vouch);
                flushPersonaCache();
            }
        }
        group = socialManager.loadNewestNotificationWithLimit(getCurrentUser().getGuid(), Notification.Type.Vouch, 2);
    }

    protected void processVouchReject() throws AuthenticationException {
        for (String id : ids) {
            Vouch vouch = socialManager.loadVouchByGuid(id);
            if (vouch.getTarget().getGuid().equals(getCurrentUser().getGuid())) {
                socialManager.deleteVouch(vouch.getGuid());
                flushPersonaCache();
            }
        }
        group = socialManager.loadNewestNotificationWithLimit(getCurrentUser().getGuid(), Notification.Type.Vouch, 2);
    }


    protected User loadUser(String guid) {
        return userManager.loadUserByGuid(guid);
    }

	public ViewAllType getViewAllType() {
		return viewAllType;
	}

	public void setViewAllType(ViewAllType viewAllType) {
		this.viewAllType = viewAllType;
	}

    @Override
    public MenuLocation getMenuLocation() {
        return MenuLocation.notifications;
    }

}
