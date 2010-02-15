/*
 * Copyright (c) 2009. Blackbox, LLC
 * All rights reserved.
 */

package com.blackbox.user;

import com.blackbox.BBPersistentObject;
import com.blackbox.Utils;
import com.blackbox.social.Address;
import org.compass.annotations.Searchable;
import org.compass.annotations.SearchableComponent;
import org.compass.annotations.SearchableProperty;
import org.joda.time.DateTime;
import org.terracotta.modules.annotations.InstrumentedClass;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 * Represents extra profile information information that is tied to a specific user.
 *
 * @author A.J. Wright
 * @see com.blackbox.user.User
 */
@XmlRootElement(name = "profile")
@InstrumentedClass
@Searchable(root = false)
public class Profile extends BBPersistentObject implements IProfile {
    private static final long serialVersionUID = 8627988150013811691L;

    private User user;

    @SearchableProperty
    private DateTime birthday;
    @SearchableProperty
    private SexEnum sex;
    @SearchableComponent
    private LookingFor lookingFor;
    @SearchableProperty
    private String lookingForExplain;
    private ReferFrom referFrom;

    // the next section can be removed
    @SearchableProperty
    private String politicalViews;
    @SearchableProperty
    private String religiousViews;
    @SearchableProperty
    private String website;
    @SearchableProperty
    private String bodyMods;
    @SearchableProperty
    private String mostly;

    private String profileImgUrl;
    private String avatarUrl;
    @SearchableComponent(prefix = "location_")
    private Address location;
    @SearchableComponent(prefix = "currentAddress_")
    private Address currentAddress;
    @SearchableComponent(prefix = "mood")
    private MoodThermometer mood;
    @SearchableProperty
    private String frequentCities1;
    @SearchableProperty
    private String frequentCities2;
    @SearchableProperty
    private String frequentCities3;
    @SearchableProperty
    private String timeZone;

    private boolean acceptsGifts;
    private boolean birthdayInVisible;
    private boolean visibleToPersona;
    private boolean visibleToTrialMember;
    private boolean visibleToPubStream;
    private boolean visibleToSearch;

    private boolean nonFriendsCanPm;
    private boolean nonFriendsCanFollow;
    private boolean nonFriendsCanComment;
    private boolean nonFriendsCanSearch;

    private boolean limitedCanPm;
    private boolean limitedCanFollow;
    private boolean limitedCanComment;
    private boolean limitedCanSearch;
    private boolean limitedCanSeePictures;
    private boolean limitedCanSeeActivity;
    private boolean limitedCanSeeGifts;

    @SearchableProperty
    private String phoneHome;
    @SearchableProperty
    private String phoneMobile;
    @SearchableProperty
    private String phoneOther;

    public String getPhoneHome() {
        return phoneHome;
    }

    public void setPhoneHome(String phoneHome) {
        this.phoneHome = phoneHome;
    }

    public String getPhoneMobile() {
        return phoneMobile;
    }

    public void setPhoneMobile(String phoneMobile) {
        this.phoneMobile = phoneMobile;
    }

    public String getPhoneOther() {
        return phoneOther;
    }

    public void setPhoneOther(String phoneOther) {
        this.phoneOther = phoneOther;
    }

    public Profile() {
//        accessControlList = new AccessControlList();
        lookingFor = new LookingFor();
        location = new Address();
        currentAddress = new Address();
        mood = new MoodThermometer();
    }

    /**
     * Returns Access rights for this profile object.
     *
     * @return the access rights for this profile object.
     */
//    @Override
//    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
//    @Cascade({org.hibernate.annotations.CascadeType.SAVE_UPDATE,
//            org.hibernate.annotations.CascadeType.DELETE_ORPHAN})
//    public AccessControlList getAccessControlList() {
//        return accessControlList;
//    }

    /**
     * Sets the acces rights for this profile object.
     *
     * @param accessControlList The access rights for this profile objects.
     */
//    @Override
//    public void setAccessControlList(AccessControlList accessControlList) {
//        this.accessControlList = accessControlList;
//    }

    /**
     * Returns the user this profile is tied too.
     *
     * @return the user that the profile is tied too.
     */
    public User getUser() {
        return user;
    }

    /**
     * Sets the user that the profile is tied too.
     *
     * @param user The user the profile is tied too.
     */
    public void setUser(User user) {
        this.user = user;
    }

    @XmlJavaTypeAdapter(com.blackbox.util.JodaDateTimeXmlAdapter.class)
    public DateTime getBirthday() {
        return birthday;
    }

    public void setBirthday(DateTime birthday) {
        this.birthday = birthday;
    }

    public Address getLocation() {
        return location;
    }

    public void setLocation(Address location) {
        this.location = location;
    }


    public Address getCurrentAddress() {
        return currentAddress;
    }

    public void setCurrentAddress(Address currentAddress) {
        this.currentAddress = currentAddress;
    }

    public SexEnum getSex() {
        return sex;
    }

    public void setSex(SexEnum sex) {
        this.sex = sex;
    }

    public boolean isAcceptsGifts() {
        return acceptsGifts;
    }

    public void setAcceptsGifts(boolean acceptsGifts) {
        this.acceptsGifts = acceptsGifts;
    }

    public LookingFor getLookingFor() {
        return lookingFor;
    }

    public void setLookingFor(LookingFor lookingFor) {
        this.lookingFor = lookingFor;
    }

    public String getPoliticalViews() {
        return politicalViews;
    }

    public void setPoliticalViews(String politicalViews) {
        this.politicalViews = politicalViews;
    }

    public String getReligiousViews() {
        return religiousViews;
    }

    public void setReligiousViews(String religiousViews) {
        this.religiousViews = religiousViews;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getBodyMods() {
        return bodyMods;
    }

    public void setBodyMods(String bodyMods) {
        this.bodyMods = bodyMods;
    }

    public String getMostly() {
        return mostly;
    }

    public void setMostly(String mostly) {
        this.mostly = mostly;
    }

    public String getProfileImgUrl() {
        return profileImgUrl;
    }

    public void setProfileImgUrl(String profileImgUrl) {
        this.profileImgUrl = profileImgUrl;
    }

    public boolean isBirthdayInVisible() {
        return birthdayInVisible;
    }

    public void setBirthdayInVisible(boolean birthdayVisible) {
        this.birthdayInVisible = birthdayVisible;
    }

    public String getFrequentCities1() {
        return frequentCities1;
    }

    public void setFrequentCities1(String frequentCities1) {
        this.frequentCities1 = frequentCities1;
    }

    public String getFrequentCities2() {
        return frequentCities2;
    }

    public void setFrequentCities2(String frequentCities2) {
        this.frequentCities2 = frequentCities2;
    }

    public String getFrequentCities3() {
        return frequentCities3;
    }

    public void setFrequentCities3(String frequentCities3) {
        this.frequentCities3 = frequentCities3;
    }

    public String getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }

    public static Profile createProfile() {
        Profile p = new Profile();
        Utils.applyGuid(p);
        return p;
    }


    public MoodThermometer getMood() {
        return mood;
    }

    public void setMood(MoodThermometer mood) {
        this.mood = mood;
    }

    public boolean isNonFriendsCanPm() {
        return nonFriendsCanPm;
    }

    public void setNonFriendsCanPm(boolean nonFriendsCanPm) {
        this.nonFriendsCanPm = nonFriendsCanPm;
    }

    public boolean isNonFriendsCanFollow() {
        return nonFriendsCanFollow;
    }

    public void setNonFriendsCanFollow(boolean nonFriendsCanFollow) {
        this.nonFriendsCanFollow = nonFriendsCanFollow;
    }

    public boolean isNonFriendsCanComment() {
        return nonFriendsCanComment;
    }

    public void setNonFriendsCanComment(boolean nonFriendsCanComment) {
        this.nonFriendsCanComment = nonFriendsCanComment;
    }

    public boolean isNonFriendsCanSearch() {
        return nonFriendsCanSearch;
    }

    public void setNonFriendsCanSearch(boolean nonFriendsCanSearch) {
        this.nonFriendsCanSearch = nonFriendsCanSearch;
    }

    public boolean isVisibleToPersona() {
        return visibleToPersona;
    }

    public void setVisibleToPersona(boolean visibleToPersona) {
        this.visibleToPersona = visibleToPersona;
    }

    public boolean isVisibleToPubStream() {
        return visibleToPubStream;
    }

    public void setVisibleToPubStream(boolean visibleToPubStream) {
        this.visibleToPubStream = visibleToPubStream;
    }

    public boolean isVisibleToSearch() {
        return visibleToSearch;
    }

    public void setVisibleToSearch(boolean visibleToSearch) {
        this.visibleToSearch = visibleToSearch;
    }

    public boolean isVisibleToTrialMember() {
        return visibleToTrialMember;
    }

    public void setVisibleToTrialMember(boolean visibleToTrialMember) {
        this.visibleToTrialMember = visibleToTrialMember;
    }

    public boolean isLimitedCanPm() {
        return limitedCanPm;
    }

    public boolean isLimitedCanFollow() {
        return limitedCanFollow;
    }

    public void setLimitedCanFollow(boolean limitedCanFollow) {
        this.limitedCanFollow = limitedCanFollow;
    }

    public void setLimitedCanPm(boolean limitedCanPm) {
        this.limitedCanPm = limitedCanPm;
    }

    public boolean isLimitedCanComment() {
        return limitedCanComment;
    }

    public void setLimitedCanComment(boolean limitedCanComment) {
        this.limitedCanComment = limitedCanComment;
    }

    public boolean isLimitedCanSearch() {
        return limitedCanSearch;
    }

    public void setLimitedCanSearch(boolean limitedCanSearch) {
        this.limitedCanSearch = limitedCanSearch;
    }

    public boolean isLimitedCanSeePictures() {
        return limitedCanSeePictures;
    }

    public void setLimitedCanSeePictures(boolean limitedCanSeePictures) {
        this.limitedCanSeePictures = limitedCanSeePictures;
    }

    public boolean isLimitedCanSeeActivity() {
        return limitedCanSeeActivity;
    }

    public void setLimitedCanSeeActivity(boolean limitedCanSeeActivity) {
        this.limitedCanSeeActivity = limitedCanSeeActivity;
    }

    public boolean isLimitedCanSeeGifts() {
        return limitedCanSeeGifts;
    }

    public void setLimitedCanSeeGifts(boolean limitedCanSeeGifts) {
        this.limitedCanSeeGifts = limitedCanSeeGifts;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getLookingForExplain() {
        return lookingForExplain;
    }

    public void setLookingForExplain(String lookingForExplain) {
        this.lookingForExplain = lookingForExplain;
    }

    public ReferFrom getReferFrom() {
        return referFrom;
    }

    public void setReferFrom(ReferFrom referFrom) {
        this.referFrom = referFrom;
    }
}
