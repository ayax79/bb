package com.blackbox.foundation.media;

import com.blackbox.foundation.BBPersistentObject;
import com.blackbox.foundation.BaseEntity;
import com.blackbox.foundation.EntityReference;
import com.blackbox.foundation.activity.IRecipient;
import static com.blackbox.foundation.activity.IRecipient.RecipientStatus.INBOX;
import org.joda.time.DateTime;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 * @author A.J. Wright
 */
@XmlRootElement
public class MediaRecipient extends BBPersistentObject implements IRecipient {

    private EntityReference recipient;
    private MediaMetaData mediaMetaData;
    private RecipientStatus status = INBOX;
    private static final long serialVersionUID = -5354353856008315184L;

    public MediaRecipient() {
    }

    public MediaRecipient(EntityReference recipient, MediaMetaData mmd) {
        this.recipient = recipient;
    }

    public EntityReference getRecipient() {
        return recipient;
    }

    public void setRecipient(EntityReference recipient) {
        this.recipient = recipient;
    }

    public MediaMetaData getMediaMetaData() {
        return mediaMetaData;
    }

    public void setMediaMetaData(MediaMetaData mediaMetaData) {
        this.mediaMetaData = mediaMetaData;
    }

    public RecipientStatus getStatus() {
        return status;
    }

    public void setStatus(RecipientStatus status) {
        this.status = status;
    }

    @XmlTransient
    @Override
    public BaseEntity getRecipientObject() {
        return null;
    }

    @Override
    public void setRecipientObject(BaseEntity baseEntity) {
    }

    @XmlTransient
    @Override
    public boolean isRead() {
        return false;
    }

    @Override
    public void setRead(boolean read) {
    }

    @XmlTransient
    @Override
    public DateTime getReplyDate() {
        return null;
    }

    @Override
    public void setReplyDate(DateTime replyDate) {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        MediaRecipient that = (MediaRecipient) o;

        if (mediaMetaData != null ? !mediaMetaData.equals(that.mediaMetaData) : that.mediaMetaData != null)
            return false;
        if (recipient != null ? !recipient.equals(that.recipient) : that.recipient != null) return false;
        //noinspection RedundantIfStatement
        if (status != that.status) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (recipient != null ? recipient.hashCode() : 0);
        result = 31 * result + (mediaMetaData != null ? mediaMetaData.hashCode() : 0);
        result = 31 * result + (status != null ? status.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "MediaRecipient{" +
                "recipient=" + recipient +
                ", messageMetaData=" + mediaMetaData +
                ", status=" + status +
                '}';
    }
}
