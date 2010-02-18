/*
 * Copyright (c) 2009. Blackbox, LLC
 * All rights reserved.
 */
package com.blackbox.presentation.action.activity;

import com.blackbox.foundation.activity.ActivityRequest;
import com.blackbox.foundation.activity.IActivityManager;
import com.blackbox.foundation.activity.IActivityThread;
import com.blackbox.foundation.common.TwoBounds;
import com.blackbox.foundation.message.PrePublicationUtil;
import com.blackbox.foundation.user.IUserManager;
import com.blackbox.foundation.user.User;
import com.blackbox.foundation.util.Bounds;
import com.blackbox.presentation.action.BaseBlackBoxActionBean;
import com.blackbox.presentation.action.util.FilterType;
import com.blackbox.presentation.action.util.JSONUtil;
import net.sourceforge.stripes.action.*;
import net.sourceforge.stripes.integration.spring.SpringBean;
import org.joda.time.DateMidnight;
import org.json.JSONArray;
import org.json.JSONException;
import org.yestech.cache.ICacheManager;

import java.util.Collection;

import static com.blackbox.foundation.social.NetworkTypeEnum.*;
import static com.blackbox.presentation.action.BaseBlackBoxActionBean.ViewType.json;
import static com.blackbox.presentation.action.activity.ActivityActionBean.Scope.Today;
import static com.blackbox.presentation.action.activity.ActivityActionBean.Scope.Yesterday;
import static com.blackbox.presentation.action.util.PresentationUtil.createResolutionWithJsonArray;
import static com.google.common.collect.Lists.newArrayList;

/**
 *
 *
 */
public abstract class ActivityActionBean extends BaseBlackBoxActionBean {

    public static enum Scope {
        Yesterday,
        Today
    }

    @SpringBean("userManager")
    protected IUserManager userManager;

    @SpringBean(value = "activityManager")
    protected IActivityManager activityManager;

    @SpringBean("prePublishedMessageCache")
    ICacheManager<String, Collection<com.blackbox.foundation.message.Message>> prePublishedMessageCache;

    private Scope scope;
    private Collection<IActivityThread> activities;
    private String ownerGuid;

    private FilterType filter;
    private User user;
    private TwoBounds twoBounds = new TwoBounds(new Bounds(), new Bounds());

    @Before
    public void prepare() {
        if (this.ownerGuid != null && this.ownerGuid.equals(getCurrentUser().getGuid())) {
            user = userManager.loadUserByGuid(this.ownerGuid);
        } else {
            user = getCurrentUser();
        }

        if (filter == null) {
            filter = FilterType.all;
        }

        if (Yesterday == scope) {
            DateMidnight today = new DateMidnight();
            twoBounds.setStartDate(today.toDateTime());
            DateMidnight yesterday = today.minusDays(1);
            twoBounds.setEndDate(yesterday.toDateTime());
        } else if (Today == scope) {
            DateMidnight today = new DateMidnight();
            twoBounds.setStartDate(today.toDateTime());
            DateMidnight tomorrow = today.plusDays(1);
            twoBounds.setEndDate(tomorrow.toDateTime());
        }
    }

    @DontValidate
    @DefaultHandler
    public Resolution load() throws JSONException {
        activities = PrePublicationUtil.applyPrePublishedMessages(user, loadThreads(user, filter, twoBounds), prePublishedMessageCache);

        if (getView() == json) {
            return createResolutionWithJsonArray(getContext(), threadsToJson(activities));
        }
        return handle();
    }

    @DontValidate
    public Resolution loadPartial() throws JSONException {
        activities = PrePublicationUtil.applyPrePublishedMessages(user, loadThreads(user, filter, twoBounds), prePublishedMessageCache);

        if (getView() == json) {
            return createResolutionWithJsonArray(getContext(), threadsToJson(activities));
        }

        return new ForwardResolution("/ajax/dash/activity-stream.jspf");
    }

    protected abstract Resolution handle();

    protected JSONArray threadsToJson(Collection<IActivityThread> threads) throws JSONException {

        JSONArray array = new JSONArray();
        for (IActivityThread activity : threads) {
            array.put(JSONUtil.toJSON(activity));
        }
        return array;
    }

    protected Collection<IActivityThread> loadThreads(User user, FilterType filter, TwoBounds bounds) {
        if (filter == FilterType.friends_following) {
            return activityManager.loadActivityThreads(new ActivityRequest(user.toEntityReference(), newArrayList(FRIENDS, FOLLOWING), bounds));
        } else if (filter == FilterType.friends) {
            return activityManager.loadActivityThreads(new ActivityRequest(user.toEntityReference(), newArrayList(FRIENDS), bounds));
        } else if (filter == FilterType.following) {
            return activityManager.loadActivityThreads(new ActivityRequest(user.toEntityReference(), newArrayList(FOLLOWING), bounds));
        } else {
            return activityManager.loadActivityThreads(new ActivityRequest(user.toEntityReference(), newArrayList(FRIENDS, FOLLOWING, ALL_MEMBERS, WORLD), bounds));
        }
    }

    public Collection<IActivityThread> getActivities() {
        return activities;
    }

    public Scope getScope() {
        return scope;
    }

    public void setScope(Scope scope) {
        this.scope = scope;
    }

    public void setUserManager(IUserManager userManager) {
        this.userManager = userManager;
    }

    public void setOwnerGuid(String ownerGuid) {
        this.ownerGuid = ownerGuid;
    }

    public void setFilter(FilterType filter) {
        this.filter = filter;
    }

    public FilterType getFilter() {
        return filter;
    }

    public TwoBounds getBounds() {
        return twoBounds;
    }

    @Override
    public boolean isHasIntro() {
        return false;
    }
}
