/*
 * Copyright (c) 2009. Blackbox, LLC
 * All rights reserved.
 */

package com.blackbox.foundation.occasion;

import com.blackbox.foundation.social.Address;
import org.compass.annotations.*;
import org.joda.time.DateTime;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.io.Serializable;

@Searchable(root = false)
public class ActivityOccasion implements Serializable {
    private static final long serialVersionUID = 6298121577970818496L;

    @SearchableId
    protected String guid;
    @SearchableProperty(termVector = TermVector.YES)
    private String description;
    @SearchableComponent(prefix = "address_")
    private Address address;
    private String avatarLocation;
    @SearchableProperty
    private String hostBy;
    @SearchableProperty
    private DateTime eventTime;
    @SearchableProperty
    private DateTime eventEndTime;
    @SearchableProperty
    private OccasionType occasionType = OccasionType.OPEN;
    private String eventUrl;

    public ActivityOccasion() {
        address = new Address();
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public String getHostBy() {
        return hostBy;
    }

    public void setHostBy(String hostBy) {
        this.hostBy = hostBy;
    }

    @XmlJavaTypeAdapter(com.blackbox.foundation.util.JodaDateTimeXmlAdapter.class)
    public DateTime getEventTime() {
        return eventTime;
    }

    public void setEventTime(DateTime eventTime) {
        this.eventTime = eventTime;
    }

    public OccasionType getOccasionType() {
        return occasionType;
    }

    public void setOccasionType(OccasionType occasionType) {
        this.occasionType = occasionType;
    }

    public String getEventUrl() {
        return eventUrl;
    }

    public void setEventUrl(String eventUrl) {
        this.eventUrl = eventUrl;
    }

    public String getAvatarLocation() {
        return avatarLocation;
    }

    public void setAvatarLocation(String avatarLocation) {
        this.avatarLocation = avatarLocation;
    }

    @XmlJavaTypeAdapter(com.blackbox.foundation.util.JodaDateTimeXmlAdapter.class)
    public DateTime getEventEndTime() {
        return eventEndTime;
    }

    public void setEventEndTime(DateTime eventEndTime) {
        this.eventEndTime = eventEndTime;
    }
}