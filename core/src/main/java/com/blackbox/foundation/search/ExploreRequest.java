package com.blackbox.foundation.search;

import com.blackbox.foundation.geo.GeoPoint;
import com.blackbox.foundation.social.NetworkTypeEnum;
import com.blackbox.foundation.util.Range;
import org.joda.time.DateTime;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.io.Serializable;

/**
 * @author A.J. Wright
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class ExploreRequest implements Serializable {
    //TODO:  needs to be refactored as of now it is one huge bucket of crap...

    private static final long serialVersionUID = -3030439262185642883L;

    protected String name;
    protected Boolean inRelationship;
    protected Boolean single;
    protected Boolean vouched;
    protected com.blackbox.foundation.social.NetworkTypeEnum depth;
    @XmlJavaTypeAdapter(com.blackbox.foundation.util.JodaDateTimeXmlAdapter.class)
    protected DateTime lastOnline;
    protected String zipCode;
    @XmlJavaTypeAdapter(com.blackbox.foundation.util.JodaDateTimeXmlAdapter.class)
    protected DateTime registrationDate;
    protected Range orientation = new Range(LOWER_SLIDER_START, UPPER_SLIDER_START);
    protected Range polyStatus = new Range(LOWER_SLIDER_START, UPPER_SLIDER_START);
    protected Range relationshipStatus = new Range(LOWER_SLIDER_START, UPPER_SLIDER_START);
    protected Range interestLevel = new Range(LOWER_SLIDER_START, UPPER_SLIDER_START);
    protected Range energyLevel = new Range(LOWER_SLIDER_START, UPPER_SLIDER_START);
    protected String userGuid;
    protected int startIndex = 0;   // should not be included in equals and hashcode
    protected int maxResults = 10;  // should not be included in equals and hashcode

    protected Integer maxDistance = 25;
    // make these all false so the query 
    protected Boolean seekingFriends;
    protected Boolean seekingDating;
    protected Boolean seekingRelationships;
    protected Boolean seekingLove;
    protected Boolean seekingSnuggling;
    protected Boolean seekingHookups;

    protected GeoPoint center = null;
    protected GeoPoint neCorner = null;
    protected GeoPoint swCorner = null;

    private Boolean genderMale;
    private Boolean genderUnspecified;
    private Boolean genderFemale;

    private boolean promoter;
    private boolean member;

    private boolean publicEvent;
    private boolean inviteOnlyEvent;
    private SortEnum sortType = SortEnum.CHRONOLOGICAL_DESC;
    private boolean cacheResults;
    private static final int LOWER_SLIDER_START = -2;
    private static final int UPPER_SLIDER_START = -LOWER_SLIDER_START;

    public SortEnum getSortType() {
        return sortType;
    }

    public void setSortType(SortEnum sortType) {
        this.sortType = sortType;
    }

    public boolean isPromoter() {
        return promoter;
    }

    public void setPromoter(boolean promoter) {
        this.promoter = promoter;
    }

    public boolean isMember() {
        return member;
    }

    public void setMember(boolean member) {
        this.member = member;
    }

    public boolean isPublicEvent() {
        return publicEvent;
    }

    public void setPublicEvent(boolean publicEvent) {
        this.publicEvent = publicEvent;
    }

    public boolean isInviteOnlyEvent() {
        return inviteOnlyEvent;
    }

    public void setInviteOnlyEvent(boolean inviteOnlyEvent) {
        this.inviteOnlyEvent = inviteOnlyEvent;
    }

    public boolean isGenderMale() {
        if (genderMale == null) return false;
        return genderMale;
    }

    public void setGenderMale(boolean genderMale) {
        this.genderMale = genderMale;
    }

    public boolean isGenderUnspecified() {
        if (genderUnspecified == null) return false;
        return genderUnspecified;
    }

    public void setGenderUnspecified(boolean genderUnspecified) {
        this.genderUnspecified = genderUnspecified;
    }

    public boolean isGenderFemale() {
        if (genderFemale == null) return false;
        return genderFemale;
    }

    public void setGenderFemale(boolean genderFemale) {
        this.genderFemale = genderFemale;
    }

    public Boolean isInRelationship() {
        return inRelationship;
    }

    public void setInRelationship(Boolean inRelationship) {
        this.inRelationship = inRelationship;
    }

    public Boolean isVouched() {
        return vouched;
    }

    public void setVouched(Boolean vouched) {
        this.vouched = vouched;
    }

    public NetworkTypeEnum getDepth() {
        return depth;
    }

    public void setDepth(NetworkTypeEnum depth) {
        this.depth = depth;
    }

    public DateTime getLastOnline() {
        return lastOnline;
    }

    public void setLastOnline(DateTime lastOnline) {
        this.lastOnline = lastOnline;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public DateTime getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(DateTime registrationDate) {
        this.registrationDate = registrationDate;
    }

    public Range getOrientation() {
        return orientation;
    }

    public void setOrientation(Range orientation) {
        this.orientation = orientation;
    }

    public Range getPolyStatus() {
        return polyStatus;
    }

    public void setPolyStatus(Range polyStatus) {
        this.polyStatus = polyStatus;
    }

    public Range getRelationshipStatus() {
        return relationshipStatus;
    }

    public void setRelationshipStatus(Range relationshipStatus) {
        this.relationshipStatus = relationshipStatus;
    }

    public Range getInterestLevel() {
        return interestLevel;
    }

    public void setInterestLevel(Range interestLevel) {
        this.interestLevel = interestLevel;
    }

    public Range getEnergyLevel() {
        return energyLevel;
    }

    public void setEnergyLevel(Range energyLevel) {
        this.energyLevel = energyLevel;
    }

    public String getUserGuid() {
        return userGuid;
    }

    public void setUserGuid(String userGuid) {
        this.userGuid = userGuid;
    }

    public int getStartIndex() {
        return startIndex;
    }

    public void setStartIndex(int startIndex) {
        this.startIndex = startIndex;
    }

    public int getMaxResults() {
        return maxResults;
    }

    public void setMaxResults(int maxResults) {
        this.maxResults = maxResults;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean isSingle() {
        return single;
    }

    public void setSingle(Boolean single) {
        this.single = single;
    }

    public Integer getMaxDistance() {
        return maxDistance;
    }

    public void setMaxDistance(Integer maxDistance) {
        this.maxDistance = maxDistance;
    }

    public Boolean isSeekingFriends() {
        return seekingFriends;
    }

    public void setSeekingFriends(Boolean seekingFriends) {
        this.seekingFriends = seekingFriends;
    }

    public Boolean isSeekingDating() {
        return seekingDating;
    }

    public void setSeekingDating(Boolean seekingDating) {
        this.seekingDating = seekingDating;
    }

    public Boolean isSeekingRelationships() {
        return seekingRelationships;
    }

    public void setSeekingRelationships(Boolean seekingRelationships) {
        this.seekingRelationships = seekingRelationships;
    }

    public Boolean isSeekingLove() {
        return seekingLove;
    }

    public void setSeekingLove(Boolean seekingLove) {
        this.seekingLove = seekingLove;
    }

    public Boolean isSeekingSnuggling() {
        return seekingSnuggling;
    }

    public void setSeekingSnuggling(Boolean seekingSnuggling) {
        this.seekingSnuggling = seekingSnuggling;
    }

    public Boolean isSeekingHookups() {
        return seekingHookups;
    }

    public void setSeekingHookups(Boolean seekingHookups) {
        this.seekingHookups = seekingHookups;
    }

    /**
     * Could be used instead of zip code, but we're using zip code for now
     *
     * @return
     */
    public GeoPoint getCenter() {
        return center;
    }

    public void setCenter(GeoPoint center) {
        this.center = center;
    }

    /**
     * Should be null from presentation. A hack for use by the query builder.
     *
     * @return a geopoint
     */
    public GeoPoint getNeCorner() {
        return neCorner;
    }

    public void setNeCorner(GeoPoint neCorner) {
        this.neCorner = neCorner;
    }

    /**
     * Should be null from presentation. A hack for use by the query builder.
     *
     * @return a geopoint
     */
    public GeoPoint getSwCorner() {
        return swCorner;
    }

    public void setSwCorner(GeoPoint swCorner) {
        this.swCorner = swCorner;
    }

    public void cacheResults(boolean cacheResults) {
        this.cacheResults = cacheResults;
    }

    public boolean isCacheResults() {
        return cacheResults;
    }

    public boolean hasAnySlidersSlided() {
        return sliderHasChanged(orientation) || sliderHasChanged(polyStatus) || sliderHasChanged(relationshipStatus)
                || sliderHasChanged(interestLevel) || sliderHasChanged(energyLevel);
    }

    public boolean sliderHasChanged(Range range) {
        return LOWER_SLIDER_START < range.getStartIndex() || UPPER_SLIDER_START > range.getLastIndex();
    }

    public boolean hasZipCode() {
        return zipCode != null;
    }

    public boolean isMappable() {
        return GeoPoint.isMappable(neCorner) && GeoPoint.isMappable(swCorner);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ExploreRequest that = (ExploreRequest) o;

        if (inviteOnlyEvent != that.inviteOnlyEvent) return false;
        if (maxResults != that.maxResults) return false;
        if (member != that.member) return false;
        if (promoter != that.promoter) return false;
        if (publicEvent != that.publicEvent) return false;
        if (startIndex != that.startIndex) return false;
        if (center != null ? !center.equals(that.center) : that.center != null) return false;
        if (depth != that.depth) return false;
        if (energyLevel != null ? !energyLevel.equals(that.energyLevel) : that.energyLevel != null) return false;
        if (genderFemale != null ? !genderFemale.equals(that.genderFemale) : that.genderFemale != null) return false;
        if (genderMale != null ? !genderMale.equals(that.genderMale) : that.genderMale != null) return false;
        if (genderUnspecified != null ? !genderUnspecified.equals(that.genderUnspecified) : that.genderUnspecified != null)
            return false;
        if (inRelationship != null ? !inRelationship.equals(that.inRelationship) : that.inRelationship != null)
            return false;
        if (interestLevel != null ? !interestLevel.equals(that.interestLevel) : that.interestLevel != null)
            return false;
        if (lastOnline != null ? !lastOnline.equals(that.lastOnline) : that.lastOnline != null) return false;
        if (maxDistance != null ? !maxDistance.equals(that.maxDistance) : that.maxDistance != null) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (neCorner != null ? !neCorner.equals(that.neCorner) : that.neCorner != null) return false;
        if (orientation != null ? !orientation.equals(that.orientation) : that.orientation != null) return false;
        if (polyStatus != null ? !polyStatus.equals(that.polyStatus) : that.polyStatus != null) return false;
        if (registrationDate != null ? !registrationDate.equals(that.registrationDate) : that.registrationDate != null)
            return false;
        if (relationshipStatus != null ? !relationshipStatus.equals(that.relationshipStatus) : that.relationshipStatus != null)
            return false;
        if (seekingDating != null ? !seekingDating.equals(that.seekingDating) : that.seekingDating != null)
            return false;
        if (seekingFriends != null ? !seekingFriends.equals(that.seekingFriends) : that.seekingFriends != null)
            return false;
        if (seekingHookups != null ? !seekingHookups.equals(that.seekingHookups) : that.seekingHookups != null)
            return false;
        if (seekingLove != null ? !seekingLove.equals(that.seekingLove) : that.seekingLove != null) return false;
        if (seekingRelationships != null ? !seekingRelationships.equals(that.seekingRelationships) : that.seekingRelationships != null)
            return false;
        if (seekingSnuggling != null ? !seekingSnuggling.equals(that.seekingSnuggling) : that.seekingSnuggling != null)
            return false;
        if (single != null ? !single.equals(that.single) : that.single != null) return false;
        if (sortType != that.sortType) return false;
        if (swCorner != null ? !swCorner.equals(that.swCorner) : that.swCorner != null) return false;
        if (userGuid != null ? !userGuid.equals(that.userGuid) : that.userGuid != null) return false;
        if (vouched != null ? !vouched.equals(that.vouched) : that.vouched != null) return false;
        if (zipCode != null ? !zipCode.equals(that.zipCode) : that.zipCode != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (inRelationship != null ? inRelationship.hashCode() : 0);
        result = 31 * result + (single != null ? single.hashCode() : 0);
        result = 31 * result + (vouched != null ? vouched.hashCode() : 0);
        result = 31 * result + (depth != null ? depth.hashCode() : 0);
        result = 31 * result + (lastOnline != null ? lastOnline.hashCode() : 0);
        result = 31 * result + (zipCode != null ? zipCode.hashCode() : 0);
        result = 31 * result + (registrationDate != null ? registrationDate.hashCode() : 0);
        result = 31 * result + (orientation != null ? orientation.hashCode() : 0);
        result = 31 * result + (polyStatus != null ? polyStatus.hashCode() : 0);
        result = 31 * result + (relationshipStatus != null ? relationshipStatus.hashCode() : 0);
        result = 31 * result + (interestLevel != null ? interestLevel.hashCode() : 0);
        result = 31 * result + (energyLevel != null ? energyLevel.hashCode() : 0);
        result = 31 * result + (userGuid != null ? userGuid.hashCode() : 0);
        result = 31 * result + startIndex;
        result = 31 * result + maxResults;
        result = 31 * result + (maxDistance != null ? maxDistance.hashCode() : 0);
        result = 31 * result + (seekingFriends != null ? seekingFriends.hashCode() : 0);
        result = 31 * result + (seekingDating != null ? seekingDating.hashCode() : 0);
        result = 31 * result + (seekingRelationships != null ? seekingRelationships.hashCode() : 0);
        result = 31 * result + (seekingLove != null ? seekingLove.hashCode() : 0);
        result = 31 * result + (seekingSnuggling != null ? seekingSnuggling.hashCode() : 0);
        result = 31 * result + (seekingHookups != null ? seekingHookups.hashCode() : 0);
        result = 31 * result + (center != null ? center.hashCode() : 0);
        result = 31 * result + (neCorner != null ? neCorner.hashCode() : 0);
        result = 31 * result + (swCorner != null ? swCorner.hashCode() : 0);
        result = 31 * result + (genderMale != null ? genderMale.hashCode() : 0);
        result = 31 * result + (genderUnspecified != null ? genderUnspecified.hashCode() : 0);
        result = 31 * result + (genderFemale != null ? genderFemale.hashCode() : 0);
        result = 31 * result + (promoter ? 1 : 0);
        result = 31 * result + (member ? 1 : 0);
        result = 31 * result + (publicEvent ? 1 : 0);
        result = 31 * result + (inviteOnlyEvent ? 1 : 0);
        result = 31 * result + (sortType != null ? sortType.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ExploreRequest{" +
                "name='" + name + '\'' +
                ", inRelationship=" + inRelationship +
                ", single=" + single +
                ", vouched=" + vouched +
                ", depth=" + depth +
                ", lastOnline=" + lastOnline +
                ", zipCode='" + zipCode + '\'' +
                ", registrationDate=" + registrationDate +
                ", orientation=" + orientation +
                ", polyStatus=" + polyStatus +
                ", relationshipStatus=" + relationshipStatus +
                ", interestLevel=" + interestLevel +
                ", energyLevel=" + energyLevel +
                ", userGuid='" + userGuid + '\'' +
                ", startIndex=" + startIndex +
                ", maxResults=" + maxResults +
                ", maxDistance=" + maxDistance +
                ", seekingFriends=" + seekingFriends +
                ", seekingDating=" + seekingDating +
                ", seekingRelationships=" + seekingRelationships +
                ", seekingLove=" + seekingLove +
                ", seekingSnuggling=" + seekingSnuggling +
                ", seekingHookups=" + seekingHookups +
                ", center=" + center +
                ", neCorner=" + neCorner +
                ", swCorner=" + swCorner +
                ", genderMale=" + genderMale +
                ", genderUnspecified=" + genderUnspecified +
                ", genderFemale=" + genderFemale +
                ", promoter=" + promoter +
                ", member=" + member +
                ", publicEvent=" + publicEvent +
                ", inviteOnlyEvent=" + inviteOnlyEvent +
                ", sortType=" + sortType +
                '}';
    }

}
