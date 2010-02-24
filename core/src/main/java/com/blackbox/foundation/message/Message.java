/*
 * Copyright (c) 2009. Blackbox, LLC
 * All rights reserved.
 */

package com.blackbox.foundation.message;

import com.blackbox.foundation.BaseEntity;
import com.blackbox.foundation.EntityReference;
import com.blackbox.foundation.EntityType;
import com.blackbox.foundation.IEntity;
import com.blackbox.foundation.activity.*;
import com.blackbox.foundation.exception.BlackBoxException;
import com.blackbox.foundation.social.NetworkTypeEnum;
import com.blackbox.foundation.user.ExternalCredentials;
import org.compass.annotations.Searchable;
import org.compass.annotations.SearchableComponent;
import org.compass.annotations.SearchableProperty;
import org.compass.annotations.TermVector;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.ISODateTimeFormat;
import org.yestech.publish.objectmodel.IArtifact;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.blackbox.foundation.EntityReference.createEntityReference;
import static org.apache.commons.beanutils.BeanUtils.cloneBean;

/**
 * Represents Text Based Message that is published in the system.  the message can be o any text type it can be associated with a parent
 * but is not necessary.
 */
@Searchable
@XmlRootElement(name = "message")
public class Message extends BaseEntity implements IArtifact<MessageMetaData, String>, IEntity, IActivity, IVirtualGiftable, Comparable<IActivity> {

    private static final long serialVersionUID = -8072265383393044065L;

    private NetworkTypeEnum recipientDepth;
    private ActivityReference parentActivity;
    private DateTime postDate;
    //    private AccessControlList accessControlList;

    @SearchableComponent(prefix = "artifactMetaData_")
    private MessageMetaData artifactMetaData;

    @SearchableProperty(termVector = TermVector.YES)
    private String subject;

    @SearchableProperty(termVector = TermVector.YES)
    private String body;

    private boolean published = true;
    private boolean virtualGift = false;
    private boolean acknowledged = true;
    private boolean publishToTwitter = false;
    private boolean publishToFacebook = false;
    private String shortBody;
    private Map<ExternalCredentials.CredentialType, ExternalCredentials> externalCredentials;

    public Message() {
        super(EntityType.MESSAGE);
        this.artifactMetaData = MessageMetaData.createMessageMetaData();
        this.externalCredentials = new HashMap<ExternalCredentials.CredentialType, ExternalCredentials>();
    }

    public Message(String guid, DateTime postDate, NetworkTypeEnum recipientDepth, String body,
                   String ownerGuid, EntityType ownerType, boolean virtualGift) {
        this();
        this.guid = guid;
        this.postDate = postDate;
        this.recipientDepth = recipientDepth;
        this.body = body;
        this.virtualGift = virtualGift;
        this.artifactMetaData.setArtifactOwner(createEntityReference(ownerGuid, ownerType));
    }

    public void setParentGuid(String parentGuid) {
        parentActivity = new ActivityReference();
        parentActivity.setGuid(parentGuid);
    }

    public void setOwnerGuid(String ownerGuid) {
        artifactMetaData.getArtifactOwner().setGuid(ownerGuid);
    }

    public void setDepth(int depth) {
        this.recipientDepth = NetworkTypeEnum.getByDatabaseIdentifier(depth);
    }

    public void setOwnerType(int ownerType) {
        artifactMetaData.getArtifactOwner().setOwnerType(EntityType.getByDatabaseIdentifier(ownerType));
    }

    @Override
    public ActivityProfile getSenderProfile() {
        return artifactMetaData.getSenderProfile();
    }

    @Override
    public void setSenderProfile(ActivityProfile senderProfile) {
        artifactMetaData.setSenderProfile(senderProfile);
    }

    public ActivityReference getParentActivity() {
        return parentActivity;
    }

    public void setParentActivity(ActivityReference parentActivity) {
        this.parentActivity = parentActivity;
    }

    @Override
    public EntityReference getSender() {
        return artifactMetaData.getArtifactOwner();
    }

    @Override
    public List<IRecipient> getRecipients() {
        return artifactMetaData.getRecipients();
    }

    @XmlTransient
    public void setRecipients(List<IRecipient> recipients) {
        artifactMetaData.setRecipients(recipients);
    }

    @Override
    public EntityType getOwnerType() {
        return EntityType.MESSAGE;
    }

    @Override
    public EntityReference toEntityReference() {
        return new EntityReference(EntityType.MESSAGE, getGuid());
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
        return ActivityTypeEnum.MESSAGE;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    @Override
    public MessageMetaData getArtifactMetaData() {
        return artifactMetaData;
    }

    @Override
    public void setArtifactMetaData(MessageMetaData artifactMetaData) {
        this.artifactMetaData = artifactMetaData;
    }

//    @Override
//    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER, optional = false)
//    @Cascade(org.hibernate.annotations.CascadeType.ALL)
//    public AccessControlList getAccessControlList() {
//        return accessControlList;
//    }
//
//    @Override
//    public void setAccessControlList(AccessControlList accessControlList) {
//        this.accessControlList = accessControlList;
//    }

    @Override
    public String getArtifactIdentifier() {
        return getGuid();
    }

    @Override
    public void setArtifactIdentifier(String identifier) {
        setGuid(guid);
    }

    public NetworkTypeEnum getRecipientDepth() {
        return recipientDepth;
    }

    public void setRecipientDepth(NetworkTypeEnum recipientDepth) {
        this.recipientDepth = recipientDepth;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }


    public boolean isPublished() {
        return published;
    }

    public void setPublished(boolean published) {
        this.published = published;
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

    public boolean isPublishToTwitter() {
        return publishToTwitter;
    }

    public void setPublishToTwitter(boolean publishToTwitter) {
        this.publishToTwitter = publishToTwitter;
    }

    public boolean isPublishToFacebook() {
        return publishToFacebook;
    }

    public void setPublishToFacebook(boolean publishToFacebook) {
        this.publishToFacebook = publishToFacebook;
    }

    public ExternalCredentials getExternalCredentials(ExternalCredentials.CredentialType type) {
        return externalCredentials.get(type);
    }

    public Collection<ExternalCredentials> getAllExternalCredentials() {
        return externalCredentials.values();
    }

    public void addExternalCredentials(ExternalCredentials externalCredentials) {
        if (this.externalCredentials.containsKey(externalCredentials.getType())) {
            this.externalCredentials.remove(externalCredentials.getType());
        }
        this.externalCredentials.put(externalCredentials.getType(), externalCredentials);
    }

    public String getShortBody() {
        return shortBody;
    }

    public void setShortBody(String shortBody) {
        this.shortBody = shortBody;
    }

    @Override
    public String toString() {
        return "Message{" +
                "recipientDepth=" + recipientDepth +
                ", parentActivity=" + parentActivity +
                ", postDate=" + postDate +
                ", artifactMetaData=" + artifactMetaData +
                ", subject='" + subject + '\'' +
                ", body='" + body + '\'' +
                ", published=" + published +
                ", virtualGift=" + virtualGift +
                ", awknowledged=" + acknowledged +
                '}';
    }

    public static Message createMessage() {
        return ActivityFactory.createMessage();
    }

    public static Message cloneMessage(Message m) {
        try {
            return (Message) cloneBean(m);
        } catch (Exception e) {
            throw new BlackBoxException(e);
        }
    }

    @Override
    public int compareTo(IActivity other) {
        return DescendingActivityComparator.getDescendingActivityComparator().compare(this, other);
    }
}
