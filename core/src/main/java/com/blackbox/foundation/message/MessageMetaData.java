/*
 *
 * Author:  Artie Copeland
 * Last Modified Date: $DateTime: $
 */
package com.blackbox.foundation.message;

import com.blackbox.foundation.BaseEntity;
import com.blackbox.foundation.Utils;
import com.blackbox.foundation.BBPersistentObject;
import com.blackbox.foundation.EntityReference;
import com.blackbox.foundation.activity.ActivityProfile;
import com.blackbox.foundation.activity.IRecipient;
import com.blackbox.foundation.exception.BlackBoxException;
import com.blackbox.foundation.occasion.Occasion;
import com.blackbox.foundation.user.User;
import static org.apache.commons.beanutils.BeanUtils.cloneBean;

import org.compass.annotations.SearchableComponent;
import org.compass.annotations.SearchableProperty;
import org.yestech.publish.objectmodel.ArtifactType;
import org.yestech.publish.objectmodel.IArtifactMetaData;
import org.compass.annotations.Searchable;

import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Artie Copeland
 * @version $Revision: $
 */
@Searchable(root = false)
@XmlSeeAlso({User.class, Occasion.class})
public class MessageMetaData extends BBPersistentObject implements IArtifactMetaData<EntityReference, String> {

    @SearchableComponent(prefix = "artifactOwner_")
    private EntityReference artifactOwner;
    private List<IRecipient> recipients;
    @SearchableProperty
    private ArtifactType artifactType;
    private MessageType messageType;
    private ActivityProfile senderProfile;
    private BaseEntity artifactOwnerObject;
    private static final long serialVersionUID = 7537539124240102687L;

    public MessageMetaData() {
        recipients = new ArrayList<IRecipient>();
        setArtifactType(ArtifactType.TEXT);
        senderProfile = new ActivityProfile();
        artifactOwner = EntityReference.createEntityReference();
    }

    public String getArtifactMetaDataIdentifier() {
        return getGuid();
    }

    public void setArtifactMetaDataIdentifier(String identifier) {
        setGuid(identifier);
    }

    public MessageType getMessageType() {
        return messageType;
    }

    public void setMessageType(MessageType messageType) {
        this.messageType = messageType;
    }

    @XmlJavaTypeAdapter(MessageRecipientXmlAdapter.class)
    public List<IRecipient> getRecipients() {
        return recipients;
    }

    public void setRecipients(List<IRecipient> recipients) {
        this.recipients = recipients;
    }

    public EntityReference getArtifactOwner() {
        return artifactOwner;
    }

    public void setArtifactOwner(EntityReference artifactOwner) {
        this.artifactOwner = artifactOwner;
    }

    public ActivityProfile getSenderProfile() {
        return senderProfile;
    }

    public void setSenderProfile(ActivityProfile senderProfile) {
        this.senderProfile = senderProfile;
    }

    public ArtifactType getArtifactType() {
        return artifactType;
    }

    public void setArtifactType(ArtifactType artifactType) {
        this.artifactType = artifactType;
    }

    public BaseEntity getArtifactOwnerObject() {
        return artifactOwnerObject;
    }

    public void setArtifactOwnerObject(BaseEntity artifactOwnerObject) {
        this.artifactOwnerObject = artifactOwnerObject;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        MessageMetaData that = (MessageMetaData) o;

        if (artifactOwnerObject != null ? !artifactOwnerObject.equals(that.artifactOwnerObject) : that.artifactOwnerObject != null)
            return false;
        if (artifactType != that.artifactType) return false;
        if (messageType != null ? !messageType.equals(that.messageType) : that.messageType != null) return false;
        if (recipients != null ? !recipients.equals(that.recipients) : that.recipients != null) return false;
        //noinspection RedundantIfStatement
        if (senderProfile != null ? !senderProfile.equals(that.senderProfile) : that.senderProfile != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (artifactOwner != null ? artifactOwner.hashCode() : 0);
        result = 31 * result + (recipients != null ? recipients.hashCode() : 0);
        result = 31 * result + (artifactType != null ? artifactType.hashCode() : 0);
        result = 31 * result + (messageType != null ? messageType.hashCode() : 0);
        result = 31 * result + (senderProfile != null ? senderProfile.hashCode() : 0);
        result = 31 * result + (artifactOwnerObject != null ? artifactOwnerObject.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "MessageMetaData{" +
                ", recipients=" + recipients +
                ", artifactType=" + artifactType +
                ", messageType=" + messageType +
                ", senderProfile=" + senderProfile +
                ", artifactOwnerObject=" + artifactOwnerObject +
                '}';
    }

    public static MessageMetaData createMessageMetaData() {
        MessageMetaData md = new MessageMetaData();
        Utils.applyGuid(md);
        return md;
    }

    public static MessageMetaData cloneMessageMetaData(MessageMetaData md) {
        try {
            return (MessageMetaData) cloneBean(md);
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
}
