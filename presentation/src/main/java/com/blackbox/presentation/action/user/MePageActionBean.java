package com.blackbox.presentation.action.user;

import com.blackbox.foundation.activity.*;
import com.blackbox.presentation.action.BaseBlackBoxActionBean;
import com.blackbox.presentation.action.util.JSONUtil;
import com.blackbox.foundation.social.ISocialManager;
import com.blackbox.foundation.social.Connection;
import com.blackbox.foundation.social.UserVouch;
import static com.blackbox.presentation.action.util.PresentationUtil.createResolutionWithJsonArray;
import static com.blackbox.presentation.action.BaseBlackBoxActionBean.ViewType.json;
import static com.blackbox.foundation.social.Connection.ConnectionType;
import com.blackbox.foundation.occasion.Occasion;
import com.blackbox.foundation.occasion.IOccasionManager;
import com.blackbox.foundation.bookmark.IBookmarkManager;
import com.blackbox.foundation.bookmark.UserWish;
import com.blackbox.foundation.user.PaginationResults;
import com.blackbox.foundation.user.IUserManager;
import com.blackbox.foundation.user.User;
import com.blackbox.foundation.gifting.IGiftingManager;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.integration.spring.SpringBean;

import java.util.*;

import org.json.JSONArray;
import org.json.JSONException;

/**
 *
 *
 */
public class MePageActionBean extends BaseBlackBoxActionBean {

    @SpringBean("socialManager")
    ISocialManager socialManager;
    @SpringBean("occasionManager")
    IOccasionManager occasionManager;
    @SpringBean("bookmarkManager")
    IBookmarkManager bookmarkManager;
    @SpringBean("activityManager")
    IActivityManager activityManager;
    @SpringBean("userManager")
    IUserManager userManager;                    
    @SpringBean("giftingManager")
    IGiftingManager giftingManager;

    private PaginationResults<Connection> connections;
    private PaginationResults<UserVouch> vouches;
    private PaginationResults<Occasion> occasions;
    private PaginationResults<UserWish> wishes;
    private PaginationResults<IActivity> gifts;
    private Collection<ActivityThread<IActivity>> threads;
    private int permanentVouchCount;
    private int maxPermanentVouches;
    private int startIndex = 0;
    private int maxResults = 10;
    private int wishCount;
    private int maxWishCount;
    private ConnectionType connectionType = ConnectionType.ALL;
    private AssociatedActivityFilterType activityFilterType = AssociatedActivityFilterType.ALL;

    @DefaultHandler
    public Resolution show() throws JSONException {
        permanentVouchCount = socialManager.monthlyVouchCount(getCurrentUser().getGuid(), true).getValue();
        User user = userManager.loadUserByGuid(getCurrentUser().getGuid());
        maxPermanentVouches = user.getMaxPermanentVouches();

        wishCount = bookmarkManager.monthlyWishCount(getCurrentUser().getGuid()).getValue();
        maxWishCount = user.getMaxWishes();

        connections = loadConnections(connectionType, 0, 4);
        vouches = loadVouches(0, 4);
        occasions = loadOccasions(0, 4);
        wishes = loadUserWishes(0, 4);
        threads = loadActivityThreads(0, 4);
        gifts = loadVirtualGifts(0, 4);
        return new ForwardResolution("/ajax/dash/mebox/me.jspf");
    }

    public Resolution activity() {
        threads = loadActivityThreads(startIndex, maxResults);
        return new ForwardResolution("/ajax/dash/mebox/activity.jspf");
    }

    public Resolution activity_list() throws JSONException {
        threads = loadActivityThreads(startIndex, maxResults);

        if (getView() == json) {
            return createResolutionWithJsonArray(getContext(), threadsToJson(threads));
        }

        return new ForwardResolution("/ajax/stream/activity_list.jspf");
    }

    public Resolution vouches() {
        vouches = loadVouches(startIndex, maxResults);
        return new ForwardResolution("/ajax/dash/mebox/vouches.jspf");
    }

    public Resolution vouches_list() {
        vouches = loadVouches(startIndex, maxResults);
        return new ForwardResolution("/ajax/dash/mebox/vouches_list.jspf");
    }

    public Resolution connections() {
        connections = loadConnections(connectionType, startIndex, maxResults);
        return new ForwardResolution("/ajax/dash/mebox/connections.jspf");
    }

    public Resolution connections_list() {
        connections = loadConnections(connectionType, startIndex, maxResults);
        return new ForwardResolution("/ajax/dash/mebox/connections_list.jspf");
    }

    public Resolution wishes() {
        wishes = loadUserWishes(startIndex, maxResults);
        return new ForwardResolution("/ajax/dash/mebox/wishes.jspf");
    }

    public Resolution wishes_list() {
        wishes = loadUserWishes(startIndex, maxResults);
        return new ForwardResolution("/ajax/dash/mebox/wishes_list.jspf");
    }

    public Resolution events() {
        occasions = loadOccasions(startIndex, maxResults);
        return new ForwardResolution("/ajax/dash/mebox/events.jspf");
    }

    public Resolution events_list() {
        occasions = loadOccasions(startIndex, maxResults);
        return new ForwardResolution("/ajax/dash/mebox/events_list.jspf");
    }

    public Resolution gifts() {
        gifts = loadVirtualGifts(startIndex, maxResults);
        return new ForwardResolution("/ajax/dash/mebox/gifts.jspf");
    }

    public Resolution gifts_list() {
        gifts = loadVirtualGifts(startIndex, maxResults);
        return new ForwardResolution("/ajax/dash/mebox/gifts_list.jspf");
    }

    protected JSONArray threadsToJson(Collection<ActivityThread<IActivity>> threads) throws JSONException {

        JSONArray array = new JSONArray();
        for (IActivityThread<IActivity> activity : threads) {
            array.put(JSONUtil.toJSON(activity));
        }
        return array;
    }

    protected PaginationResults<Connection> loadConnections(ConnectionType type, int startIndex, int maxResults) {
        return socialManager.loadConnections(getCurrentUser().getGuid(), type, startIndex, maxResults);
    }

    protected PaginationResults<UserVouch> loadVouches(int startIndex, int maxResults) {
        return socialManager.loadUserVouches(getCurrentUser().getGuid(), startIndex, maxResults);
    }

    protected PaginationResults<Occasion> loadOccasions(int startIndex, int maxResults) {
        return occasionManager.loadOccasionsByAttendee(getCurrentUser().getGuid(), startIndex, maxResults);
    }

    protected PaginationResults<UserWish> loadUserWishes(int startIndex, int maxResults) {
        return bookmarkManager.loadUserWishes(getCurrentUser().getGuid(), startIndex, maxResults);
    }

    protected Collection<ActivityThread<IActivity>> loadActivityThreads(int startIndex, int maxResults) {
        return activityManager.loadAssociatedActivity(getCurrentUser().getGuid(), AssociatedActivityFilterType.ALL, startIndex, maxResults);
    }

    protected PaginationResults<IActivity> loadVirtualGifts(int startIndex, int numResults) {
        return giftingManager.loadVirtualGiftsReceivedInBounds(getCurrentUser().getGuid(), startIndex, numResults);
    }

    public PaginationResults<Connection> getConnections() {
        return connections;
    }

    public PaginationResults<UserVouch> getVouches() {
        return vouches;
    }

    public PaginationResults<Occasion> getOccasions() {
        return occasions;
    }

    public PaginationResults<UserWish> getWishes() {
        return wishes;
    }

    public Collection<ActivityThread<IActivity>> getThreads() {
        return threads;
    }

    public int getMaxResults() {
        return maxResults;
    }

    public void setMaxResults(int maxResults) {
        this.maxResults = maxResults;
    }

    public int getStartIndex() {
        return startIndex;
    }

    public void setStartIndex(int startIndex) {
        this.startIndex = startIndex;
    }

    public long getPermanentVouchCount() {
        return permanentVouchCount;
    }

    public int getMaxPermanentVouches() {
        return maxPermanentVouches;
    }

    public int getWishCount() {
        return wishCount;
    }

    public int getMaxWishCount() {
        return maxWishCount;
    }

    public ConnectionType getConnectionType() {
        return connectionType;
    }

    public void setConnectionType(ConnectionType connectionType) {
        this.connectionType = connectionType;
    }

    public AssociatedActivityFilterType getActivityFilterType() {
        return activityFilterType;
    }

    public void setActivityFilterType(AssociatedActivityFilterType activityFilterType) {
        this.activityFilterType = activityFilterType;
    }

    public PaginationResults<IActivity> getGifts() {
        return gifts;
    }

    @Override
    public boolean isHasIntro() {
        return true;
    }
}
