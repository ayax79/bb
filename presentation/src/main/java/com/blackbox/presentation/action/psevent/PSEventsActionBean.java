/*
 * Copyright (c) 2009. Blackbox, LLC
 * All rights reserved.
 */
package com.blackbox.presentation.action.psevent;

import com.blackbox.foundation.Status;
import com.blackbox.foundation.util.Bounds;
import com.blackbox.foundation.occasion.IOccasionManager;
import com.blackbox.foundation.occasion.Occasion;
import com.blackbox.foundation.occasion.OccasionType;
import com.blackbox.foundation.occasion.OccasionRequest;
import com.blackbox.presentation.action.BaseBlackBoxActionBean;
import com.blackbox.foundation.search.SearchResult;
import com.blackbox.foundation.user.User;

import static com.google.common.collect.Lists.newArrayList;

import java.util.ArrayList;

import net.sourceforge.stripes.action.*;
import net.sourceforge.stripes.integration.spring.SpringBean;

import java.util.List;

import org.joda.time.DateMidnight;

public class PSEventsActionBean extends BaseBlackBoxActionBean {

    @SpringBean("occasionManager")
    private IOccasionManager occasionManager;
    private List<Occasion> occasions;
    private boolean owner;
    private String eventGuid;
    private String title;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getEventGuid() {
        return eventGuid;
    }

    public void setEventGuid(String eventGuid) {
        this.eventGuid = eventGuid;
    }

    public boolean getOwner() {
        return owner;
    }

    public void setOwner(boolean owner) {
        this.owner = owner;
    }

    private String searchStr;

    public String getSearchStr() {
        return searchStr;
    }

    public void setSearchStr(String searchStr) {
        this.searchStr = searchStr;
    }

    @DontValidate
    @HandlesEvent("my")
    public Resolution showMyEvents() {
        occasions = occasionManager.loadByOwnerGuid(getCurrentUser().getGuid());
        owner = true;
        title = "My events";
        return new ForwardResolution("/events.jsp");
    }

    @DontValidate
    @HandlesEvent("members")
    public Resolution showMembersEvent() {

        DateMidnight now = new DateMidnight();
        DateMidnight day30 = now.plusDays(30);

        OccasionRequest request = new OccasionRequest();
        request.setBounds(new Bounds(0, 10, now.toDateTime(), day30.toDateTime()));
        request.setOwnerTypes(newArrayList(User.UserType.NORMAL));
        request.setOccasionTypes(newArrayList(OccasionType.OPEN, OccasionType.INVITE_ONLY));
        request.setStatus(Status.ENABLED);
        occasions = occasionManager.loadOccasions(request);

        owner = false;
        title = "Member events";
        return new ForwardResolution("/events.jsp");
    }

    @DontValidate
    @HandlesEvent("delete")
    public Resolution deleteEvent() {
        //TODO:  add check to make sure owner is deleting....
        occasionManager.deleteOccasion(eventGuid);
        return new StreamingResolution("text", "success");
    }

    @DontValidate
    @DefaultHandler
    @HandlesEvent("search")
    public Resolution searchEvent() {
        if (searchStr != null) {
            List<SearchResult<Occasion>> result = occasionManager.searchOccasions(searchStr);
            if (result != null) {
                result.addAll(occasionManager.searchOccasions("", searchStr));
            } else {
                result = occasionManager.searchOccasions("", searchStr);
            }
            occasions = new ArrayList<Occasion>();
            for (SearchResult<Occasion> or : result) {
                occasions.add(or.getEntity());
            }
        }
        title = "Search results";
        return new ForwardResolution("/events.jsp");
    }

    @DontValidate
    @HandlesEvent("promoters")
    public Resolution showPromotersEvent() {

        DateMidnight now = new DateMidnight();
        DateMidnight day30 = now.plusDays(30);

        OccasionRequest request = new OccasionRequest();
        request.setBounds(new Bounds(0, 10, now.toDateTime(), day30.toDateTime()));
        request.setOwnerTypes(newArrayList(User.UserType.PROMOTER));
        request.setOccasionTypes(newArrayList(OccasionType.OPEN, OccasionType.INVITE_ONLY));
        request.setStatus(Status.ENABLED);
        occasions = occasionManager.loadOccasions(request);

        owner = false;
        title = "Promoters events";
        return new ForwardResolution("/events.jsp");
    }

    public void setOccasionManager(IOccasionManager occasionManager) {
        this.occasionManager = occasionManager;
    }

    public List<Occasion> getOccasions() {
        return occasions;
    }

    @Override
    public boolean isHasIntro() {
        return true;
    }
}
