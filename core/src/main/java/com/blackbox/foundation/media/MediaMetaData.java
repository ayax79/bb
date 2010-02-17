/*
 * Copyright (c) 2009. Blackbox, LLC
 * All rights reserved.
 */

package com.blackbox.foundation.media;

import com.blackbox.foundation.BBPersistentObject;
import com.blackbox.foundation.EntityReference;
import com.blackbox.foundation.EntityType;
import com.blackbox.foundation.IEntity;
import com.blackbox.foundation.activity.*;
import com.blackbox.foundation.exception.BlackBoxException;
import com.blackbox.foundation.social.NetworkTypeEnum;
import org.compass.annotations.Searchable;
import org.compass.annotations.SearchableComponent;
import org.compass.annotations.SearchableProperty;
import org.compass.annotations.TermVector;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.ISODateTimeFormat;
import org.yestech.lib.util.Pair;
import org.yestech.publish.objectmodel.ArtifactType;
import org.yestech.publish.objectmodel.IFileArtifactMetaData;
import org.yestech.publish.objectmodel.episodic.IEpisodicArtifact;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import static com.blackbox.foundation.EntityReference.createEntityReference;
import static org.apache.commons.beanutils.BeanUtils.cloneBean;

/**
 * @author A.J. Wright
 */
@Searchable
@XmlRootElement(name = "mediaMetaData")
public class MediaMetaData extends BBPersistentObject implements IFileArtifactMetaData<EntityReference, String>,
        IActivity, IEntity, IEpisodicArtifact, IVirtualGiftable, Comparable<IActivity> {
    private static final long serialVersionUID = 147275495168934928L;

    @SearchableProperty
    protected NetworkTypeEnum recipientDepth;
    protected List<IRecipient> recipients;
    @SearchableProperty(termVector = TermVector.YES)
    protected String comment;
    @SearchableComponent(prefix = "owner")
    protected EntityReference owner;
    protected String fileName;
    protected String mimeType;
    protected long size;
    protected String location;
    protected String thumbnailLocation;
    protected ArtifactType artifactType;
    //    protected AccessControlList accessControlList;
    protected ActivityReference parentActivity;
    @SearchableProperty
    protected DateTime postDate;
    protected boolean profile; // if this is a profile image
    protected boolean avatar;
    protected boolean loosePic;  // it is not so nice that have 3 flags here, we can fix that later
    protected boolean passwordProtect; // if this media is password protected
    protected Pair<String, String> uniqueNames;
    protected boolean library; // This option has essentially come to mean don't put in the stream.
    protected ActivityProfile senderProfile;
    protected MediaLibrary mediaLibrary;
    protected String episodeId;
    protected String assetId;
    protected double aspect = 1.0;
    protected boolean virtualGift = false;
    protected boolean acknowledged = true;


    public MediaMetaData() {
        recipients = new ArrayList<IRecipient>();
//        accessControlList = new AccessControlList();
        senderProfile = new ActivityProfile();
        owner = EntityReference.createEntityReference();

    }

    public MediaMetaData(String guid, DateTime postDate, NetworkTypeEnum recipientDepth,
                         String comment, String location, String mimeType, String thumbnailLocation,
                         String ownerGuid, EntityType ownerType, boolean virtualGift) {
        this();
        this.guid = guid;
        this.postDate = postDate;
        this.recipientDepth = recipientDepth;
        this.comment = comment;
        this.location = location;
        this.mimeType = mimeType;
        this.thumbnailLocation = thumbnailLocation;
        this.virtualGift = virtualGift;
        owner = createEntityReference(ownerGuid, ownerType);
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

    public MediaLibrary getMediaLibrary() {
        return mediaLibrary;
    }

    public void setMediaLibrary(MediaLibrary mediaLibrary) {
        this.mediaLibrary = mediaLibrary;
    }

    @Override
    public ActivityProfile getSenderProfile() {
        return senderProfile;
    }

    public void setSenderProfile(ActivityProfile senderProfile) {
        this.senderProfile = senderProfile;
    }

    @Override
    public Pair<String, String> getUniqueNames() {
        return uniqueNames;
    }

    @Override
    public void setUniqueNames(Pair<String, String> uniqueNames) {
        this.uniqueNames = uniqueNames;
    }

    @Override
    public EntityReference getSender() {
        return getArtifactOwner();
    }

    @Override
    public EntityType getOwnerType() {
        return EntityType.MEDIA;
    }

    @Override
    public EntityReference toEntityReference() {
        return new EntityReference(EntityType.MEDIA, getGuid());
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

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @XmlJavaTypeAdapter(MediaRecipientXmlAdapter.class)
    public List<IRecipient> getRecipients() {
        return recipients;
    }

    public void setRecipients(List<IRecipient> recipients) {
        this.recipients = recipients;
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

    @Override
    public String getLocation() {
        return location;
    }

    @Override
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

    @Override
    public String getMimeType() {
        return mimeType;
    }

    @Override
    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    @Override
    public String getFileName() {
        return fileName;
    }

    @Override
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public long getSize() {
        return size;
    }

    @Override
    public void setSize(long size) {
        this.size = size;
    }

    public NetworkTypeEnum getRecipientDepth() {
        return recipientDepth;
    }

    public void setRecipientDepth(NetworkTypeEnum recipientDepth) {
        this.recipientDepth = recipientDepth;
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

    public boolean isProfile() {
        return profile;
    }

    public void setProfile(boolean profile) {
        this.profile = profile;
    }

    public void setLibrary(boolean library) {
        this.library = library;
    }

    public boolean isLibrary() {
        return library;
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

    public static MediaMetaData createMediaMetaData() {
        return ActivityFactory.createMedia();
    }

    public static MediaMetaData cloneMediaMetaData(MediaMetaData mmd) {
//        MediaMetaData mmd2 = createMediaMetaData();
////        mmd2.accessControlList = AccessControlList.cloneAccessControlList(mmd2.accessControlList);
//        mmd2.guid = mmd.guid;
//        mmd2.recipients = mmd.recipients;
//        mmd2.recipientDepth = mmd.recipientDepth;
//        mmd2.comment = mmd.comment;
//        mmd2.owner = mmd.owner;
//        mmd2.fileName = mmd.fileName;
//        mmd2.mimeType = mmd.mimeType;
//        mmd2.size = mmd.size;
//        mmd2.location = mmd.location;
//        mmd2.artifactType = mmd.artifactType;
//        mmd2.parentActivity = mmd.parentActivity;
//        mmd2.postDate = mmd.postDate;
//        mmd2.avatar = mmd.avatar;
//        mmd2.profile = mmd.profile;
//        mmd2.passwordProtect = mmd.passwordProtect;
//        mmd2.uniqueNames = mmd.uniqueNames;
//        return mmd2;
        try {
            return (MediaMetaData) cloneBean(mmd);
        } catch (IllegalAccessException e) {
            throw new BlackBoxException(e);
        } catch (InstantiationException e) {
            throw new BlackBoxException(e);
        } catch (InvocationTargetException e) {
            throw new BlackBoxException(e);
        } catch (NoSuchMethodException e) {
            throw new BlackBoxException(e);
        }
    }

    @Override
    public int compareTo(IActivity other) {
        return DescendingActivityComparator.getDescendingActivityComparator().compare(this, other);
    }

}