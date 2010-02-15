/*
 * Copyright (c) 2009. Blackbox, LLC
 * All rights reserved.
 */

package com.blackbox.occasion;

import com.blackbox.BaseEntity;
import com.blackbox.EntityReference;
import com.blackbox.EntityTypeAnnotation;
import com.blackbox.Status;
import com.blackbox.activity.*;
import com.blackbox.media.MediaMetaData;
import com.blackbox.security.IAsset;
import com.blackbox.social.Address;
import com.blackbox.social.NetworkTypeEnum;
import com.blackbox.user.User;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import org.compass.annotations.Searchable;
import org.compass.annotations.SearchableComponent;
import org.compass.annotations.SearchableProperty;
import org.compass.annotations.SearchableReference;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.ISODateTimeFormat;
import org.yestech.lib.i18n.CountryEnum;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.io.Serializable;
import java.util.List;

import static com.blackbox.EntityType.OCCASION;
import static com.google.common.collect.Lists.newArrayList;

/**
 * Represents events parties, etc.
 */
@Searchable
@EntityTypeAnnotation(OCCASION)
@XStreamAlias("occasion")
@XmlRootElement(name = "occasion")
public class Occasion extends BaseEntity implements Serializable, IAsset, IActivity, Comparable<IActivity> {
    private static final long serialVersionUID = 6298121577970818496L;

    @SearchableProperty
    private String description;
    private String location;
    @SearchableComponent(prefix = "address_")
    private Address address;
    @SearchableProperty
    private OccasionLevel occasionLevel;
    @SearchableProperty
    private DateTime postDate;
    @SearchableReference
    private User owner;
    private ActivityReference parentActivity;
    private ActivityProfile senderProfile;
    private List<Attendee> attendees = newArrayList();
    private OccasionLayout layout = new OccasionLayout();
    private String avatarLocation;
    @SearchableProperty
    private String hostBy;
    @SearchableProperty
    private DateTime eventTime;
    @SearchableProperty
    private DateTime eventEndTime;
    @SearchableProperty
    private OccasionType occasionType = OccasionType.OPEN;
    private boolean withGoogleMap;
    private OccasionNotifyOption occasionNotifyOption = new OccasionNotifyOption();

    private String eventUrl;
    private OccasionWebDetail occasionWebDetail = new OccasionWebDetail();

    private String phoneNumber;
    private String phoneExtension;
    private String email;
    private int totalAttendees = -1;
    private int totalEvents;
    private boolean publishToTwitter;
    private String twitterDescription;
    private boolean publishToFacebook;
    private String facebookDescription;
    private int facebookCategory = 1;
    private int facebookSubCategory = 19;

    public Occasion() {
        super(OCCASION);
        setStatus(Status.ENABLED);
        address = new Address();
    }

    public void setAddress1(String address1) {
        address.setAddress1(address1);
    }

    public void setAddress2(String address2) {
        address.setAddress2(address2);
    }

    public void setCity(String city) {
        address.setCity(city);
    }

    public void setCounty(String county) {
        address.setCounty(county);
    }

    public void setState(String state) {
        address.setState(state);
    }

    public void setCountry(String country) {
        address.setCountry(CountryEnum.valueOf(country));
    }

    public void setZipcode(String zipcode) {
        address.setZipCode(zipcode);
    }

    public void setOccasionTypeId(Integer occasionTypeDbId) {
        if (occasionTypeDbId != null) {
            this.occasionType = OccasionType.getByDatabaseIdentifier(occasionTypeDbId);
        }
    }

    public void setTotalEvents(Integer totalEvents) {
        if (totalEvents != null) {
            this.totalEvents = totalEvents;
        }
    }

    public int getTotalEvents() {
        return totalEvents;
    }

    public void setImageLocation(String imageLocation) {
        MediaMetaData data = MediaMetaData.createMediaMetaData();
        data.setLocation(imageLocation);
        layout.setTransiantImage(data);
    }

    public int getTotalAttendees() {
        if (totalAttendees < 0) {
            return attendees.size();
        }
        return totalAttendees;
    }

    public void setTotalAttendees(int totalAttendees) {
        this.totalAttendees = totalAttendees;
    }

    @Override
    public ActivityProfile getSenderProfile() {
        return senderProfile;
    }

    public void setSenderProfile(ActivityProfile senderProfile) {
        this.senderProfile = senderProfile;
    }


    public ActivityReference getParentActivity() {
        return parentActivity;
    }

    public void setParentActivity(ActivityReference parentActivity) {
        this.parentActivity = parentActivity;
    }

    @Override
    public EntityReference getSender() {
        return null;
    }

    @Override
    public NetworkTypeEnum getRecipientDepth() {
        return null;
    }

    @Override
    @XmlTransient
    public List<IRecipient> getRecipients() {
        return null;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public List<Attendee> getAttendees() {
        return attendees;
    }

    public void setAttendees(List<Attendee> attendees) {
        this.attendees = attendees;
    }

    @Override
    public String getFormattedPostDate() {
        if (postDate != null) {
            return postDate.withZone(DateTimeZone.UTC).toString(ISODateTimeFormat.dateTimeNoMillis());
        } else {
            return null;
        }
    }

    @Override
    @XmlJavaTypeAdapter(com.blackbox.util.JodaDateTimeXmlAdapter.class)
    public DateTime getPostDate() {
        return postDate;
    }

    public void setPostDate(DateTime postDate) {
        this.postDate = postDate;
    }

    @Override
    public ActivityTypeEnum getActivityType() {
        return ActivityTypeEnum.OCCASION;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public OccasionLevel getOccasionLevel() {
        return occasionLevel;
    }

    public void setOccasionLevel(OccasionLevel occasionLevel) {
        this.occasionLevel = occasionLevel;
    }

    public OccasionLayout getLayout() {
        return layout;
    }

    public void setLayout(OccasionLayout layout) {
        this.layout = layout;
    }

    public static Occasion createOccasion() {
        return ActivityFactory.createOccasion();
    }

    public String getHostBy() {
        return hostBy;
    }

    public void setHostBy(String hostBy) {
        this.hostBy = hostBy;
    }

    @XmlJavaTypeAdapter(com.blackbox.util.JodaDateTimeXmlAdapter.class)
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

    public boolean isWithGoogleMap() {
        return withGoogleMap;
    }

    public void setWithGoogleMap(boolean withGoogleMap) {
        this.withGoogleMap = withGoogleMap;
    }

    public OccasionNotifyOption getOccasionNotifyOption() {
        return occasionNotifyOption;
    }

    public void setOccasionNotifyOption(OccasionNotifyOption occasionNotifyOption) {
        this.occasionNotifyOption = occasionNotifyOption;
    }

    public String getEventUrl() {
        return eventUrl;
    }

    public void setEventUrl(String eventUrl) {
        this.eventUrl = eventUrl;
    }

    public OccasionWebDetail getOccasionWebDetail() {
        return occasionWebDetail;
    }

    public void setOccasionWebDetail(OccasionWebDetail occasionWebDetail) {
        this.occasionWebDetail = occasionWebDetail;
    }

    public String getAvatarLocation() {
        return avatarLocation;
    }

    public void setAvatarLocation(String avatarLocation) {
        this.avatarLocation = avatarLocation;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPhoneExtension() {
        return phoneExtension;
    }

    public void setPhoneExtension(String phoneExtension) {
        this.phoneExtension = phoneExtension;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPublishToTwitter(boolean publishToTwitter) {
        this.publishToTwitter = publishToTwitter;
    }

    public boolean isPublishToTwitter() {
        return publishToTwitter;
    }

    @XmlJavaTypeAdapter(com.blackbox.util.JodaDateTimeXmlAdapter.class)
    public DateTime getEventEndTime() {
        return eventEndTime;
    }

    public void setEventEndTime(DateTime eventEndTime) {
        this.eventEndTime = eventEndTime;
    }


    public void setTwitterDescription(String twitterDescription) {
        this.twitterDescription = twitterDescription;
    }

    public String getTwitterDescription() {
        return twitterDescription;
    }

    public void setFacebookDescription(String facebookDescription) {
        this.facebookDescription = facebookDescription;
    }

    public String getFacebookDescription() {
        return facebookDescription;
    }

    public void setPublishToFacebook(boolean publishToFacebook) {
        this.publishToFacebook = publishToFacebook;
    }

    public boolean isPublishToFacebook() {
        return publishToFacebook;
    }

    public int getFacebookSubCategory() {
        return facebookSubCategory;
    }

    public void setFacebookSubCategory(int facebookSubCategory) {
        this.facebookSubCategory = facebookSubCategory;
    }

    public int getFacebookCategory() {
        return facebookCategory;
    }

    public void setFacebookCategory(int facebookCategory) {
        this.facebookCategory = facebookCategory;
    }

    @Override
    public String toString() {
        return "Occasion{" +
                "description='" + description + '\'' +
                ", location='" + location + '\'' +
                ", address=" + address +
                ", occasionLevel=" + occasionLevel +
                ", postDate=" + postDate +
                ", owner=" + owner +
                ", parentActivity=" + parentActivity +
                ", senderProfile=" + senderProfile +
                ", avatarLocation='" + avatarLocation + '\'' +
                ", hostBy='" + hostBy + '\'' +
                ", eventTime=" + eventTime +
                ", eventEndTime=" + eventEndTime +
                ", occasionType=" + occasionType +
                ", withGoogleMap=" + withGoogleMap +
                ", occasionNotifyOption=" + occasionNotifyOption +
                ", eventUrl='" + eventUrl + '\'' +
                ", facebookDescription='" + facebookDescription + '\'' +
                ", publishToFacebook='" + publishToFacebook + '\'' +
                ", twitterDescription='" + twitterDescription + '\'' +
                ", publishToTwitter='" + publishToTwitter + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", phoneExtension='" + phoneExtension + '\'' +
                ", email='" + email + '\'' +
                ", facebookCategory=" + facebookCategory +
                ", facebookSubCategory=" + facebookSubCategory +
                ", totalAttendees=" + totalAttendees +
                ", totalEvents=" + totalEvents +
                '}';
    }

    @Override
    public int compareTo(IActivity other) {
        return DescendingActivityComparator.getDescendingActivityComparator().compare(this, other);
    }
}
