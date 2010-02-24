package com.blackbox.presentation.action.notification;

import net.sourceforge.stripes.action.*;

import com.blackbox.foundation.social.Relationship;
import com.blackbox.foundation.notification.Notification;
import com.blackbox.foundation.user.User;
import com.blackbox.foundation.user.IUser;
import com.blackbox.foundation.activity.IActivity;
import com.blackbox.foundation.util.JaxbSafeCollectionWrapper;

import static com.blackbox.presentation.action.util.PresentationUtil.createResolutionWithText;

import com.blackbox.foundation.occasion.AttendingStatus;
import com.google.common.collect.Maps;
import com.google.common.base.Function;
import org.json.JSONException;

import javax.naming.AuthenticationException;
import java.util.Map;
import java.util.List;

public class AjaxNotificationActionBean extends BaseNotificationActionBean {

    private Map<String, IActivity> giftMap;

    public Resolution accept() {
        for (String guid : getIdsAsSet()) {
            final Relationship relationship = socialManager.loadRelationshipByGuid(guid);
            executeAccept(relationship.getFromEntity().getGuid());
        }
        group = socialManager.loadNewestNotificationWithLimit(getCurrentUser().getGuid(), Notification.Type.Friend, 2);
        return new ForwardResolution("/ajax/notification/notification.jspf");
    }

    public Resolution acceptRelationship() {
        for (String guid : getIdsAsSet()) {
            final Relationship relationship = socialManager.loadRelationshipByGuid(guid);
            executeAccept(relationship.getFromEntity().getGuid());
        }
        group = socialManager.loadNewestNotificationWithLimit(getCurrentUser().getGuid(), Notification.Type.Relationship, 2);
        return new ForwardResolution("/ajax/notification/notification.jspf");
    }

    public Resolution follow() throws JSONException {
        processFollowingAcknowledge();
        for (String entityGuid : entityIds) {
            User targetUser = loadUser(entityGuid);
            executeFollow(targetUser.toEntityReference());
        }
        return new ForwardResolution("/ajax/notification/notification.jspf");
    }

    public Resolution wish() throws JSONException {
        processWishAcknowledgement();
        for (String entityGuid : entityIds) {
            IUser user = userManager.loadUserByGuid(entityGuid);
            executeWish(user.toEntityReference());
        }
        return new ForwardResolution("/ajax/notification/notification.jspf");
    }

    public Resolution wishack() {
        processWishAcknowledgement();
        return new ForwardResolution("/ajax/notification/notification.jspf");
    }

    public Resolution rejectWish() {
        processWishReject();
        return new ForwardResolution("/ajax/notification/notification.jspf");
    }

    public Resolution acceptInvitationRequest() {
        processAcceptInvitationRequest();
        return new ForwardResolution("/ajax/notification/notification.jspf");
    }

    public Resolution rejectInvitationRequest() {
        processRejectInvitationRequest();
        return new ForwardResolution("/ajax/notification/notification.jspf");
    }

    public Resolution attendOccasionRequest() {
        processOccasionRequest(AttendingStatus.ATTENDING);
        return new ForwardResolution("/ajax/notification/notification.jspf");
    }

    public Resolution maybeOccasionRequest() {
        processOccasionRequest(AttendingStatus.TENATIVE);
        return new ForwardResolution("/ajax/notification/notification.jspf");
    }

    public Resolution cantOccasionRequest() {
        processOccasionRequest(AttendingStatus.NOT_ATTENDING);
        return new ForwardResolution("/ajax/notification/notification.jspf");
    }

    @HandlesEvent("ignored")
    public Resolution rejectFriendRequest() {
        for (String guid : getIdsAsSet()) {
            final Relationship relationship = socialManager.loadRelationshipByGuid(guid);
            socialManager.rejectRequest(getCurrentUser().getGuid(), relationship.getFromEntity().getGuid());
        }
        group = socialManager.loadNewestNotificationWithLimit(getCurrentUser().getGuid(), Notification.Type.Friend, 2);
        return new ForwardResolution("/ajax/notification/notification.jspf");
    }

    public Resolution rejectRelationshipRequest() {
        for (String guid : getIdsAsSet()) {
            final Relationship relationship = socialManager.loadRelationshipByGuid(guid);
            socialManager.rejectRequest(getCurrentUser().getGuid(), relationship.getFromEntity().getGuid());
        }
        group = socialManager.loadNewestNotificationWithLimit(getCurrentUser().getGuid(), Notification.Type.Relationship, 2);
        return new ForwardResolution("/ajax/notification/notification.jspf");
    }

    @DefaultHandler
    public Resolution viewAllList() {
        if (viewAllType == ViewAllType.gift) {
            JaxbSafeCollectionWrapper<List<IActivity>> jaxbSafeCollectionWrapper = giftingManager.loadVirtualGiftsReceived(getCurrentUser().getGuid(), false);
            if (jaxbSafeCollectionWrapper != null) {
                List<IActivity> gifts = jaxbSafeCollectionWrapper.getCollection();

                if (gifts != null && !gifts.isEmpty()) {
                    giftMap = Maps.uniqueIndex(gifts, new Function<IActivity, String>() {
                        @Override
                        public String apply(IActivity from) {
                            return from.getGuid() != null ? from.getGuid() : null;
                        }
                    });
                }
            }
        }

        //load notifications....
        notifications = socialManager.loadNotifications(getCurrentUser().getGuid());
        return new ForwardResolution("/ajax/notification/notifications-list.jspf");
    }

    public Resolution followack() {
        processFollowingAcknowledge();
        return new ForwardResolution("/ajax/notification/notification.jspf");
    }

    public Resolution acceptVirtualGift() {
        processVirtualGiftAccept();
        return new ForwardResolution("/ajax/notification/notification.jspf");
    }

    public Resolution rejectVirtualGift() {
        processVirtualGiftReject();
        return new ForwardResolution("/ajax/notification/notification.jspf");
    }

    @DontValidate
    public Resolution acceptVouch() throws AuthenticationException {
        processVouchAccept();
        return createResolutionWithText(getContext(), "success");
    }

    @DontValidate
    public Resolution rejectVouch() throws AuthenticationException {
        processVouchReject();
        return createResolutionWithText(getContext(), "success");
    }


    public Map<String, IActivity> getGiftMap() {
        return giftMap;
    }
}
