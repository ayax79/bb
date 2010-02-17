package com.blackbox.foundation.message;

import com.blackbox.foundation.BBPersistentObject;
import com.blackbox.foundation.BaseEntity;
import com.blackbox.foundation.EntityReference;
import com.blackbox.foundation.activity.IRecipient;
import org.joda.time.DateTime;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 * @author A.J. Wright
 */
@XmlRootElement
public class MessageRecipient extends BBPersistentObject implements IRecipient {

    private EntityReference recipient;
    private MessageMetaData messageMetaData;
    private RecipientStatus status = RecipientStatus.INBOX;
    private String messageGuid;
    private BaseEntity recipientObject;
    private boolean read;
    private DateTime replyDate;
    private static final long serialVersionUID = -8039551682086603865L;


    public MessageRecipient() {
    }

    public MessageRecipient(EntityReference recipient) {
        this.recipient = recipient;
    }

    public MessageRecipient(EntityReference recipient, String messageGuid) {
        this.recipient = recipient;
        this.messageGuid = messageGuid;
    }

    public EntityReference getRecipient() {
        return recipient;
    }

    public void setRecipient(EntityReference recipient) {
        this.recipient = recipient;
    }

    /**
     * Note this field is not available on the client side to do cyclic dependencies issues.
     * Fuck you JAXB!
     */
    @XmlTransient
    public MessageMetaData getMessageMetaData() {
        return messageMetaData;
    }

    public void setMessageMetaData(MessageMetaData messageMetaData) {
        this.messageMetaData = messageMetaData;
    }

    public RecipientStatus getStatus() {
        return status;
    }

    public void setStatus(RecipientStatus status) {
        this.status = status;
    }

    public String getMessageGuid() {
        return messageGuid;
    }

    public void setMessageGuid(String messageGuid) {
        this.messageGuid = messageGuid;
    }

    public BaseEntity getRecipientObject() {
        return recipientObject;
    }

    public void setRecipientObject(BaseEntity recipientObject) {
        this.recipientObject = recipientObject;
    }

    public boolean isRead() {
        return read;
    }

    public void setRead(boolean read) {
        this.read = read;
    }

    @XmlJavaTypeAdapter(com.blackbox.foundation.util.JodaDateTimeXmlAdapter.class)
    public DateTime getReplyDate() {
        return replyDate;
    }

    public void setReplyDate(DateTime replyDate) {
        this.replyDate = replyDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        MessageRecipient that = (MessageRecipient) o;

        if (messageGuid != null ? !messageGuid.equals(that.messageGuid) : that.messageGuid != null) return false;
        if (messageMetaData != null ? !messageMetaData.equals(that.messageMetaData) : that.messageMetaData != null)
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
        result = 31 * result + (messageMetaData != null ? messageMetaData.hashCode() : 0);
        result = 31 * result + (status != null ? status.hashCode() : 0);
        result = 31 * result + (messageGuid != null ? messageGuid.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "MessageRecipient{" +
                "recipient=" + recipient +
                ", status=" + status +
                ", messageGuid='" + messageGuid + '\'' +
                '}';
    }
}
