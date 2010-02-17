package com.blackbox.foundation.activity;

import com.blackbox.foundation.BBPersistentObject;
import com.blackbox.foundation.EntityReference;
import com.blackbox.foundation.EntityType;
import com.blackbox.foundation.IEntity;
import com.blackbox.foundation.occasion.ActivityOccasion;
import com.blackbox.foundation.social.NetworkTypeEnum;
import com.blackbox.foundation.user.ExternalCredentials;
import org.compass.annotations.Searchable;
import org.compass.annotations.SearchableComponent;
import org.compass.annotations.SearchableProperty;
import org.compass.annotations.TermVector;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.ISODateTimeFormat;
import org.yestech.lib.util.Pair;
import org.yestech.publish.objectmodel.ArtifactType;
import org.yestech.publish.objectmodel.IArtifact;
import org.yestech.publish.objectmodel.IArtifactMetaData;
import org.yestech.publish.objectmodel.episodic.IEpisodicArtifact;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.List;

/**
 *
 *
 */
@Searchable
@XmlRootElement(name = "activity")
public class Activity extends BBPersistentObject implements IArtifact<Activity, String>, IArtifactMetaData<EntityReference, String>,
        IActivity, IEntity, IEpisodicArtifact, IVirtualGiftable {
    private static final long serialVersionUID = -5624572568426462428L;
    @SearchableProperty
    protected NetworkTypeEnum recipientDepth;
    @SearchableProperty(termVector = TermVector.YES)
    protected String comment;
    @SearchableComponent(prefix = "owner_")
    protected EntityReference owner;
    protected String fileName;
    protected String mimeType;
    protected long size;
    protected String location;
    protected String thumbnailLocation;
    protected ArtifactType artifactType;
    protected ActivityReference parentActivity;
    @SearchableProperty
    protected DateTime postDate;
    @SearchableProperty
    protected boolean avatar;
    @SearchableProperty
    protected boolean loosePic;  // it is not so nice that have 3 flags here, we can fix that later
    @SearchableProperty
    protected boolean passwordProtect; // if this media is passowrd protectedm
    protected Pair<String, String> uniqueNames;
    protected ActivityProfile senderProfile;
    protected String episodeId;
    protected String assetId;
    protected double aspect = 1.0;
    protected boolean virtualGift = false;
    protected boolean acknowledged = true;
    protected EntityType ownerType;
    protected ActivityTypeEnum activityType;
    @SearchableComponent(prefix = "occasion_")
    protected ActivityOccasion occasion;
    protected boolean publishToTwitter;
    protected ExternalCredentials externalCredentials;

    public Activity() {
        senderProfile = new ActivityProfile();
        owner = EntityReference.createEntityReference();
        occasion = new ActivityOccasion();
    }

    @Override
    @XmlTransient
    public String getArtifactIdentifier() {
        return getGuid();
    }

    @Override
    public void setArtifactIdentifier(String identifier) {
        setGuid(identifier);
    }

    @Override
    @XmlTransient
    public Activity getArtifactMetaData() {
        return this;
    }

    @Override
    public void setArtifactMetaData(Activity artifactMetaData) {
    }

    public ActivityOccasion getOccasion() {
        return occasion;
    }

    public void setOccasion(ActivityOccasion occasion) {
        this.occasion = occasion;
    }

    public void setParentGuid(String parentGuid) {
        parentActivity = new ActivityReference();
        parentActivity.setGuid(parentGuid);
    }

    public void setOwnerGuid(String ownerGuid) {
        owner.setGuid(ownerGuid);
    }

    public void setDepth(int depth) {
        this.recipientDepth = NetworkTypeEnum.getByDatabaseIdentifier(depth);
    }

    public void setOwnerType(int ownerType) {
        owner.setOwnerType(EntityType.getByDatabaseIdentifier(ownerType));
    }

    @Override
    public ActivityProfile getSenderProfile() {
        return senderProfile;
    }

    public void setSenderProfile(ActivityProfile senderProfile) {
        this.senderProfile = senderProfile;
    }

    public Pair<String, String> getUniqueNames() {
        return uniqueNames;
    }

    public void setUniqueNames(Pair<String, String> uniqueNames) {
        this.uniqueNames = uniqueNames;
    }

    @Override
    public EntityReference getSender() {
        return getArtifactOwner();
    }

    @Override
    public EntityType getOwnerType() {
        return ownerType;
    }

    public void setOwnerType(EntityType ownerType) {
        this.ownerType = ownerType;
    }

    @Override
    public EntityReference toEntityReference() {
        return new EntityReference(ownerType, getGuid());
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
    @XmlJavaTypeAdapter(com.blackbox.foundation.util.JodaDateTimeXmlAdapter.class)
    public DateTime getPostDate() {
        return postDate;
    }

    public void setPostDate(DateTime postDate) {
        this.postDate = postDate;
    }

    @Override
    public ActivityTypeEnum getActivityType() {
        return ActivityTypeEnum.MEDIA;
    }

    public void setActivityType(ActivityTypeEnum activityType) {
        this.activityType = activityType;
    }

    @XmlTransient
    public String getBody() {
        return getComment();
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @XmlTransient
    public List<IRecipient> getRecipients() {
        return null;
    }

    public void setRecipients(List<IRecipient> recipients) {
    }

    @Override
    public String getArtifactMetaDataIdentifier() {
        return getGuid();
    }

    @Override
    public void setArtifactMetaDataIdentifier(String metaDataIdentifier) {
        setGuid(metaDataIdentifier);
    }

    public String getThumbnailLocation() {
        return thumbnailLocation;
    }

    public void setThumbnailLocation(String thumbnailLocation) {
        this.thumbnailLocation = thumbnailLocation;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public EntityReference getArtifactOwner() {
        return owner;
    }

    @Override
    public void setArtifactOwner(EntityReference owner) {
        this.owner = owner;
    }

    @Override
    public ArtifactType getArtifactType() {
        return artifactType;
    }

    @Override
    public void setArtifactType(ArtifactType artifactType) {
        this.artifactType = artifactType;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public NetworkTypeEnum getRecipientDepth() {
        return recipientDepth;
    }

    public void setRecipientDepth(NetworkTypeEnum recipientDepth) {
        this.recipientDepth = recipientDepth;
    }

    public void setExternalCredentials(ExternalCredentials externalCredentials) {
        this.externalCredentials = externalCredentials;
    }

    public ExternalCredentials getExternalCredentials() {
        return externalCredentials;
    }

    public ActivityReference getParentActivity() {
        return parentActivity;
    }

    public void setParentActivity(ActivityReference parentActivity) {
        this.parentActivity = parentActivity;
    }

    public boolean isPasswordProtect() {
        return passwordProtect;
    }

    public void setPasswordProtect(boolean passwordProtect) {
        this.passwordProtect = passwordProtect;
    }

    public String getEpisodeId() {
        return episodeId;
    }

    public void setEpisodeId(String episodeId) {
        this.episodeId = episodeId;
    }

    public String getAssetId() {
        return assetId;
    }

    public void setAssetId(String assetId) {
        this.assetId = assetId;
    }

    public boolean isAvatar() {
        return avatar;
    }

    public void setAvatar(boolean avatar) {
        this.avatar = avatar;
    }

    public double getAspect() {
        return aspect;
    }

    public void setAspect(double aspect) {
        this.aspect = aspect;
    }

    public boolean isLoosePic() {
        return loosePic;
    }

    public void setLoosePic(boolean loosePic) {
        this.loosePic = loosePic;
    }

    public boolean isVirtualGift() {
        return virtualGift;
    }

    public void setVirtualGift(boolean virtualGift) {
        this.virtualGift = virtualGift;
    }

    public boolean isAcknowledged() {
        return acknowledged;
    }

    public void setAcknowledged(boolean acknowledged) {
        this.acknowledged = acknowledged;
    }

    public void setPublishToTwitter(boolean publishToTwitter) {
        this.publishToTwitter = publishToTwitter;
    }

    public boolean isPublishToTwitter() {
        return publishToTwitter;
    }
}
