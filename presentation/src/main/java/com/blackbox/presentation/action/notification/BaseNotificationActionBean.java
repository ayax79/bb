package com.blackbox.presentation.action.notification;

import com.blackbox.foundation.bookmark.Bookmark;
import com.blackbox.foundation.gifting.IGiftingManager;
import com.blackbox.foundation.media.IMediaManager;
import com.blackbox.foundation.notification.Notification;
import com.blackbox.foundation.notification.NotificationGroup;
import com.blackbox.foundation.notification.Notifications;
import com.blackbox.foundation.occasion.AttendingStatus;
import com.blackbox.foundation.occasion.IOccasionManager;
import com.blackbox.foundation.occasion.OccasionRequest;
import com.blackbox.foundation.social.Relationship;
import com.blackbox.foundation.social.Vouch;
import com.blackbox.foundation.user.IUserManager;
import com.blackbox.foundation.user.User;
import com.blackbox.presentation.action.persona.BasePersonaActionBean;
import com.blackbox.presentation.action.util.CommaStringConverter;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.integration.spring.SpringBean;
import net.sourceforge.stripes.validation.Validate;

import javax.naming.AuthenticationException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

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
    private String[] ids;

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

    public Set<String> getIdsAsSet() {  // we were getting an attempt to delete the same bookmark more than once...this will guard against that...
        return ids == null ? Collections.<String>emptySet() : new HashSet<String>(Arrays.asList(ids));
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
        for (String guid : getIdsAsSet()) {
            final Bookmark bookmark = bookmarkManager.loadBookmark(guid);
            bookmark.setAcknowledged(true);
            bookmarkManager.createBookmark(bookmark);
        }
        group = socialManager.loadNewestNotificationWithLimit(getCurrentUser().getGuid(), Notification.Type.Wish, 2);
    }

    protected void processWishReject() {
        for (String id : getIdsAsSet()) {
            Bookmark bookmark = bookmarkManager.loadBookmark(id);
            if (bookmark != null && bookmark.getTarget().getGuid().equals(getCurrentUser().getGuid())) {
                bookmarkManager.deleteBookmark(id);
            }
        }
    }

    protected void processOccasionRequest(AttendingStatus status) {
        for (String occasionGuid : getIdsAsSet()) {
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
        for (String id : getIdsAsSet()) {
            giftingManager.acceptVirtualGift(id);
        }
        group = socialManager.loadNewestNotificationWithLimit(getCurrentUser().getGuid(), Notification.Type.Gift, 2);
    }

    protected void processVirtualGiftReject() {
        for (String id : getIdsAsSet()) {
            giftingManager.rejectVirtualGift(id);
        }
        group = socialManager.loadNewestNotificationWithLimit(getCurrentUser().getGuid(), Notification.Type.Gift, 2);
    }

    protected void processVouchAccept() throws AuthenticationException {
        for (String id : getIdsAsSet()) {
            Vouch vouch = socialManager.loadVouchByGuid(id);
            if (vouch.getTarget().getGuid().equals(getCurrentUser().getGuid())) {
                vouch.setAccepted(true);
                socialManager.saveVouch(vouch);
            }
        }
        group = socialManager.loadNewestNotificationWithLimit(getCurrentUser().getGuid(), Notification.Type.Vouch, 2);
    }

    protected void processVouchReject() throws AuthenticationException {
        for (String id : getIdsAsSet()) {
            Vouch vouch = socialManager.loadVouchByGuid(id);
            if (vouch.getTarget().getGuid().equals(getCurrentUser().getGuid())) {
                socialManager.deleteVouch(vouch.getGuid());
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
